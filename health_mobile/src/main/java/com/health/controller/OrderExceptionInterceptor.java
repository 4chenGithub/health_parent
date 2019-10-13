package com.health.controller;

import com.health.Constant.MessageConstant;
import com.health.Entity.Result;
import com.health.Exception.OrderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//使用自定义异常拦截器,拦截controller抛出的所有异常,代替controller处理异常则不用再写try..catch
//但是业务层和dao必须将错误抛出,controller才能将错误抓取
@RestControllerAdvice
public class OrderExceptionInterceptor extends RuntimeException {

    //拦截自定义异常
    @ExceptionHandler(OrderException.class)
    public Result CheckGroupBeUseExceptionInterceptor(OrderException e){
        e.printStackTrace();
        return new Result(false, e.getMessage());
    }
    //拦截controller抛出的其他异常
    @ExceptionHandler(Exception.class)
    public Result ExceptionInterceptor(Exception e){
        e.printStackTrace();
        return new Result(false, MessageConstant.SERVER_ERROR);
    }

}
