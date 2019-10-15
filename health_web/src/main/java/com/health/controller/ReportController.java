package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.health.Constant.MessageConstant;
import com.health.Constant.RedisConstant;
import com.health.Entity.Result;
import com.health.interfaces.ReportService;
import com.sun.corba.se.spi.ior.ObjectKey;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiContext;
import org.jxls.util.JxlsHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {
    @Reference
    private ReportService reportService;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 查询从当前月份开始到过去的12个月的集合和每个月的会员数量集合
     *
     * @return
     */
    @GetMapping("/getMemberReport")
    public Result getMemberReport(@RequestParam Map<String, String> TimeMap) {
        //获取请求参数
        List<String> months = null;
        String TimeTile = null;
        //判断前端携带过来的数据是否为空,如果是则默认查询过去一年度的会员数量
        //先封装月份集合
        Calendar calendar = Calendar.getInstance();
        months = new ArrayList<>();
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
        String timeStart = null;
        String timeEnd = null;
        if (TimeMap != null) {
            timeStart = TimeMap.get("TimeStart");
            timeEnd = TimeMap.get("TimeEnd");
        }
        if (StringUtil.isEmpty(timeStart) || StringUtil.isEmpty(timeEnd)) {
            //获取以当前月份开始的过去12个月的开始月份,月份从0开始
            calendar.add(Calendar.MONTH, -12);
            for (int i = 0; i < 12; i++) {
                calendar.add(Calendar.MONTH, 1);
                String format = dateFormat1.format(calendar.getTime());
                months.add(format);
            }
            TimeTile = "上一年度会员数量";
        } else {
            //如果不是,说明有数据
            if (StringUtil.isNotEmpty(timeStart) && StringUtil.isNotEmpty(timeEnd)) {
                TimeTile = timeStart + "到" + timeEnd + "的会员数量";
                try {
                    Date endDate = dateFormat1.parse(timeEnd);
                    Date startDate = dateFormat1.parse(timeStart);
                    int month = 0;
                    if (endDate != null && startDate != null) {
                        month = (endDate.getYear() - startDate.getYear()) * 12 + (endDate.getMonth() - startDate.getMonth());
                    } else {
                        return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
                    }
                    calendar.setTime(startDate);
                    for (int i = 1; i <= month; i++) {
                        calendar.add(Calendar.MONTH, 1);
                        String format = dateFormat1.format(calendar.getTime());
                        months.add(format);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                    return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
                }
            } else {
                return new Result(false, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL);
            }

        }
        //创建最终要返回的map集合
        Map<String, Object> map = new HashMap<>();
        map.put("months", months);
        map.put("TimeTile", TimeTile);

        //调用业务服务查询当前月份集合每个月份对应的会员数量
        List<Integer> memberCount = reportService.getMemberCount(months);
        map.put("memberCount", memberCount);
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS, map);
    }


    /**
     * 查询套餐及其预约人数
     *
     * @return
     */
    @RequestMapping("/getPackageReport")
    public Result getPackageReport() {
        Map<String, Object> map = reportService.getPackageReport();
        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS, map);
    }

    /**
     * 统计当月运营
     * 使用redis提前统计运营数据，并存到redis数据库中，下次需要则直接从redis中获取，能提高效率
     * @return
     */
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        //先从redis中数据库中
        Jedis jedis = jedisPool.getResource();
        Gson gson = new Gson();
        String BusinessReportDataJson = jedis.get(RedisConstant.BUSINESS_DATA_RESOURCES);
        Map<String, Object> map = gson.fromJson(BusinessReportDataJson, new TypeToken<Map<String, Object>>() {
        }.getType());

        //判断从redis中获取的是否为空，为空，说明第一次获取，则从MySQL数据库中查询到，并存到redis中
        if (map == null) {
            map = reportService.getBusinessReportData();
            String json = gson.toJson(map);
            jedis.set(RedisConstant.BUSINESS_DATA_RESOURCES, json);
        }
        //关闭资源
        jedis.close();
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS, map);
    }

    /**
     * 导出Excel文件
     */
    @RequestMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //调用业务服务获取运营数据
        Map<String, Object> data = reportService.getBusinessReportData();
        //解析map集合中的所有数据
        String reportDate = (String) data.get("reportDate");
        Integer todayNewMember = (Integer) data.get("todayNewMember");
        Integer totalMember = (Integer) data.get("totalMember");
        Integer thisWeekNewMember = (Integer) data.get("thisWeekNewMember");
        Integer thisMonthNewMember = (Integer) data.get("thisMonthNewMember");
        //预约
        Integer todayOrderNumber = (Integer) data.get("todayOrderNumber");
        Integer todayVisitsNumber = (Integer) data.get("todayVisitsNumber");
        Integer thisWeekOrderNumber = (Integer) data.get("thisWeekOrderNumber");
        Integer thisWeekVisitsNumber = (Integer) data.get("thisWeekVisitsNumber");
        Integer thisMonthOrderNumber = (Integer) data.get("thisMonthOrderNumber");
        Integer thisMonthVisitsNumber = (Integer) data.get("thisMonthVisitsNumber");

        ////把信息写入到Excel文件中
        //获取模板文件的绝对路径

        String filePath = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
        System.out.println(filePath);
        //创建工作簿对象
        XSSFWorkbook workbook = new XSSFWorkbook(filePath);
        //获取工作表
        XSSFSheet sheet = workbook.getSheetAt(0);

        //获取第二行
        XSSFRow row = sheet.getRow(2);
        //设置第二行第5列的日期
        row.getCell(5).setCellValue(reportDate);

        //会员数据设置
        //获取第四行
        XSSFRow row4 = sheet.getRow(4);
        //设置5列的今日新增会员数
        row4.getCell(5).setCellValue(todayNewMember);
        //设置7列的总会员数
        row4.getCell(7).setCellValue(totalMember);
        //获取第五行
        XSSFRow row5 = sheet.getRow(5);
        //设置5列的本周新增会员数
        row5.getCell(5).setCellValue(thisWeekNewMember);
        //设置7列的本月新增会员数
        row5.getCell(7).setCellValue(thisMonthNewMember);

        //预约到诊数据设置
        //获取第7行
        XSSFRow row7 = sheet.getRow(7);
        //设置5列的今日预约数
        row7.getCell(5).setCellValue(todayOrderNumber);
        //设置7列的今日到诊数
        row7.getCell(7).setCellValue(todayVisitsNumber);
        //获取第8行
        XSSFRow row8 = sheet.getRow(8);
        //设置5列的本周的预约数
        row8.getCell(5).setCellValue(thisWeekOrderNumber);
        //设置7列的本周到诊数
        row8.getCell(7).setCellValue(thisWeekVisitsNumber);
        //获取第9行
        XSSFRow row9 = sheet.getRow(9);
        //设置5列的本月预约数
        row9.getCell(5).setCellValue(thisMonthOrderNumber);
        //设置7列的本月到诊数
        row9.getCell(7).setCellValue(thisMonthVisitsNumber);

        //套餐
        List<Map<String, Object>> hotPackage = (List<Map<String, Object>>) data.get("hotPackage");
        if (hotPackage != null && hotPackage.size() > 0) {
            //遍历套餐集合信息
            int column = 12;
            for (Map<String, Object> map : hotPackage) {
                System.out.println(map);
                String name = (String) map.get("name");
                //COUNT(*)得到是long类型
                Long count = (Long) map.get("count");
                //除不尽时,得到BigDecimal类型的小数,表示无限小数
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                String remark = (String) map.get("remark");
                //获取行
                System.out.println(column);
                XSSFRow rowPackage = sheet.getRow(column);


                rowPackage.getCell(4).setCellValue(name);
                rowPackage.getCell(5).setCellValue(count);
                //proportion.doubleValue()将BigDecimal转换成double类型
                rowPackage.getCell(6).setCellValue(proportion.doubleValue());
                rowPackage.getCell(7).setCellValue(remark);
                column++;
            }
        }

        //将数据响应到客户端并下载
        ServletOutputStream os = response.getOutputStream();
        //解决响应乱码
        response.setContentType("application/vnd.ms-excel");
        //通知浏览器弹框
        //生成文件唯一标识符
        String preFile = UUID.randomUUID().toString();
        String endFile = "运营统计数据";
        //endFile = Base64.getUrlEncoder().encodeToString(endFile.getBytes());
//        endFile= URLEncoder.encode(endFile,"ISO-8859-1");
        // 解决乱码?
        endFile = new String(endFile.getBytes(), "ISO-8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + preFile + endFile + ".xlsx");
        workbook.write(os);
        //关闭资源
        os.flush();
        workbook.close();
        os.close();
    }


    @RequestMapping("/exportBusinessReport2")
    public void exportBusinessReport2(HttpServletRequest req, HttpServletResponse res) {
        String template = req.getSession().getServletContext().getRealPath("template") + File.separator + "report_template2.xlsx";
        // 数据模型
        Context context = new PoiContext();
        context.putVar("obj", reportService.getBusinessReportData());
        try {
            res.setContentType("application/vnd.ms-excel");
            res.setHeader("content-Disposition", "attachment;filename=report2.xlsx");
            // 把数据模型中的数据填充到文件中
            JxlsHelper.getInstance().processTemplate(new FileInputStream(template), res.getOutputStream(), context);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 男女占比
     */
    @GetMapping("/getSex")
    public Result getSex() {
        //查询男女占比
        //Map<String,Object>
        //{value:7, name:'男'}  value:数量  name:性别男
        //{value:8, name:'女'}  value:数量  name:性别女
        List<Map<String, Object>> sexCount = reportService.findSexCount();
        //组装套餐列表数据
        List<String> sexNames = new ArrayList<>();
        if (null != sexCount) {
            for (Map<String, Object> map : sexCount) {
                sexNames.add((String) map.get("name"));
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("sexNames", sexNames);
        result.put("sexCount", sexCount);
        return new Result(true, MessageConstant.GET_SEX_SUCCESS, result);

    }

    /**
     * 年龄段占比
     */
    @GetMapping("/getMemberAge")
    public Result getMemberAge() {
        //查询男女占比
        //Map<String,Object>
        //{value:7, name:'男'}  value:数量  name:性别男
        //{value:8, name:'女'}  value:数量  name:性别女
        List<Map<String, Object>> ageCount = reportService.findAgeCount();
        //组装套餐列表数据
        List<String> memberAges = new ArrayList<>();
        if (null != ageCount) {
            for (Map<String, Object> map : ageCount) {
                memberAges.add((String) map.get("name"));
            }
        }

        Map<String, Object> result = new HashMap<>();
        result.put("memberAges", memberAges);
        result.put("ageCount", ageCount);
        return new Result(true, MessageConstant.GET_SEX_SUCCESS, result);

    }
}
