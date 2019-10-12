package com.health.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.health.Uitls.DateUtils;
import com.health.dao.MemberDao;
import com.health.dao.OrderDao;
import com.health.dao.ReportDao;
import com.health.interfaces.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {
    @Autowired
    private ReportDao reportDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    /**
     * 查询当前月份集合每个月份对应的会员数量
     *
     * @param months
     * @return
     */
    @Override
    public List<Integer> getMemberCount(List<String> months) {
        //查询每个月对应的会员数量
        List<Integer> memberCount = new ArrayList<>();
        //遍历每个月
        for (String month : months) {
            String m = month.substring(0, month.lastIndexOf("-"));
            String endMonth = m + "-31";
            //调用dao层根据始末月份查询当前月份的会员数量集合
            int count = reportDao.FindMemberCount(endMonth);
            memberCount.add(count);
        }

        return memberCount;
    }

    /**
     * 查询套餐及其预约人数
     * map集合封装两个键值对
     * 1、packageNames：[]
     * 2、packageCount：{name:value...}
     *
     * @return
     */
    @Override
    public Map<String, Object> getPackageReport() {
        //查询套餐及其预约人数
        List<Map<String, Object>> list = reportDao.getPackageReport();
        List<String> packageNames = null;
        if (list != null && list.size() > 0) {
            //封装套餐名称集合
            packageNames = new ArrayList<>();
            for (Map<String, Object> map : list) {
                String name = (String) map.get("name");
                packageNames.add(name);
            }
        }
        //封装最终结果
        Map<String, Object> map = new HashMap<>();
        map.put("packageNames", packageNames);
        map.put("packageCount", list);
        return map;
    }

    /**
     * 获取运营统计数据
     *
     * @return
     */
    @Transactional
    @Override
    public Map<String, Object> getBusinessReportData() {
        Date date = DateUtils.getToday();
        String reportDate = null;
        Map<String, Object> map = null;
        try {
            //获取今日
            reportDate = DateUtils.date2String(date, DateUtils.DFM);
            //本月第一日
            String monthStart = DateUtils.date2String(DateUtils.getFirstDayOfThisMonth(), DateUtils.DFM);
            //本月最后一日
            String monthEnd = DateUtils.date2String(DateUtils.getLastDayOfMonth(date), DateUtils.DFM);
            //获取星期一的日期
            String Monday = DateUtils.date2String(DateUtils.getThisWeekMonday(date), DateUtils.DFM);
            //获取本日的当前星期天的日期
            String sunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());

            //会员统计
            //1、本日新增会员
            Integer todayNewMember = memberDao.getTodayMemberCount(reportDate);
            //2、本周新增会员:从本周开始到当前时间结束,因为新增会员的时间不可能是未来的时间
            Integer thisWeekNewMember = memberDao.getThisWeekOrMonthMemberCount(Monday, reportDate);
            //3、本月新增会员:同上
            Integer thisMonthNewMember = memberDao.getThisWeekOrMonthMemberCount(monthStart, reportDate);
            //总会员数
            Integer totalMember = memberDao.getTotalMemberCount();

            //预约到诊统计
            //1、今日预约数
            Integer todayOrderNumber = orderDao.getTodayOrderNumber(reportDate);
            //2、今日到诊数
            Integer todayVisitsNumber = orderDao.getTodayVisitsNumber(reportDate);
            //3、本周预约数:预约的可以是未来的时间
            Integer thisWeekOrderNumber = orderDao.getThisWeekOrMonthOrderNumber(Monday, sunday);
            //4、本周到诊数:到诊不能是未来时间,统计的是已经到诊的.我们不能去到未来体检
            Integer thisWeekVisitsNumber = orderDao.getThisWeekOrMonthVisitsNumber(Monday, reportDate);
            //5、本月预约数
            Integer thisMonthOrderNumber = orderDao.getThisWeekOrMonthOrderNumber(monthStart, monthEnd);
            //6、本月到诊数:同上
            Integer thisMonthVisitsNumber = orderDao.getThisWeekOrMonthVisitsNumber(monthStart, reportDate);

            //热门套餐：取出预约人数倒序排序前4个
            List<Map<String, Object>> hotPackage = orderDao.getHotPackage();

            //封装到最终结果
            map = new HashMap<>();
            map.put("reportDate", reportDate);
            map.put("todayNewMember", todayNewMember);
            map.put("thisWeekNewMember", thisWeekNewMember);
            map.put("thisMonthNewMember", thisMonthNewMember);
            map.put("totalMember", totalMember);
            map.put("todayOrderNumber", todayOrderNumber);
            map.put("todayVisitsNumber", todayVisitsNumber);
            map.put("thisWeekOrderNumber", thisWeekOrderNumber);
            map.put("thisWeekVisitsNumber", thisWeekVisitsNumber);
            map.put("thisMonthOrderNumber", thisMonthOrderNumber);
            map.put("thisMonthVisitsNumber", thisMonthVisitsNumber);
            map.put("hotPackage", hotPackage);

        } catch (Exception e) {
            e.printStackTrace();
        }


        return map;
    }

    public static void main(String[] args) throws Exception {
        Date date = DateUtils.getToday();
        String reportDate = null;
        //获取今日
        reportDate = DateUtils.date2String(date, DateUtils.DFM);
        System.out.println(reportDate);
        //本月第一日
        // String monthStart = reportDate.substring(0, reportDate.lastIndexOf("-")) + "-01";
        String monthStart = DateUtils.date2String(DateUtils.getFirstDayOfThisMonth(), DateUtils.DFM);
        System.out.println(monthStart);
        //本月最后一日
        // String monthEnd = reportDate.substring(0, reportDate.lastIndexOf("-")) + "-31";
        String monthEnd = DateUtils.date2String(DateUtils.getLastDayOfMonth(date), DateUtils.DFM);
        System.out.println(monthEnd);
        //获取星期一的日期
        String Monday = DateUtils.date2String(DateUtils.getThisWeekMonday(date), DateUtils.DFM);
        System.out.println(Monday);
        //获取本日的当前星期天的日期
        String sunday = DateUtils.parseDate2String(DateUtils.getSundayOfThisWeek());
        System.out.println(sunday);

    }

}
