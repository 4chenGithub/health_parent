package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.Constant.MessageConstant;
import com.health.Entity.Result;
import com.health.Uitls.POIUtils;
import com.health.interfaces.OrderSettingService;
import com.health.pojo.OrderSetting;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/orderSetting")
public class OrderSettingController {
    @Reference
    private OrderSettingService orderSettingService;

    @PostMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile excelFile) {
        List<String[]> excel = null;
        try {
            //使用POI工具类解析文件
            excel = POIUtils.readExcel(excelFile);
            //对解析后的数据进行处理
            //1、判断是否为空
            if (excel != null && excel.size() > 0) {
                List<OrderSetting> list = new ArrayList<>();
                SimpleDateFormat format = new SimpleDateFormat(POIUtils.DATE_FORMAT);
                for (String[] strings : excel) {
                    OrderSetting orderSetting = new OrderSetting(format.parse(strings[0]), Integer.parseInt(strings[1]));
                    list.add(orderSetting);
                }
                //调用业务服务处理请求
                orderSettingService.UploadFile(list);
                return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
    }

    @GetMapping("/getAllDate")
    public Result getAllDate(String date) {
        List<OrderSetting> orderSettingList = orderSettingService.FindAllDate(date);
        //将集合类型转换成前端需要的[{ date: 1, number: 120, reservations: 1 },{ date: 1, number: 120, reservations: 1 }]
        if (orderSettingList != null && orderSettingList.size() > 0) {
            List<Map<String, Object>> list = new ArrayList<>();
            SimpleDateFormat format = new SimpleDateFormat("d");
            for (OrderSetting orderSetting : orderSettingList) {
                //遍历出每一个对象
                Map<String, Object> map = new HashMap<>();
                map.put("date", Integer.parseInt(format.format(orderSetting.getOrderDate())));
                map.put("number", orderSetting.getNumber());
                map.put("reservations", orderSetting.getReservations());
                list.add(map);
            }
            //处理成功
            return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS, list);
        } else {
            //异常
            return new Result(false, MessageConstant.GET_ORDERSETTING_FAIL);
        }
    }


    @RequestMapping("/update")
    public Result update(@RequestBody OrderSetting orderSetting){
        //调用业务服务处理请求
        orderSettingService.update(orderSetting);
        return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
    }

}
