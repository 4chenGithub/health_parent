package com.health.controller;

import com.health.Constant.MessageConstant;
import com.health.Entity.Result;
import com.health.Exception.*;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//使用自定义异常拦截器,拦截controller抛出的所有异常,代替controller处理异常则不用再写try..catch
//但是业务层和dao必须将错误抛出,controller才能将错误抓取
@RestControllerAdvice
public class MyExceptionInterceptor extends RuntimeException {
    //新增的菜单路径已经存在
    @ExceptionHandler(MenuIsExistException.class)
    public Result MenuIsExistExceptionInterceptor(MenuIsExistException e) {
        e.printStackTrace();
        return new Result(false, e.getMessage());
    }

    //删除的菜单id被引用
    @ExceptionHandler(MenuBeUseException.class)
    public Result MenuBeUseExceptionInterceptor(MenuBeUseException e) {
        e.printStackTrace();
        return new Result(false, e.getMessage());
    }


    //添加的菜单优先级已经被使用
    @ExceptionHandler(PriorityBeUseException.class)
    public Result PriorityBeUseExceptionInterceptor(PriorityBeUseException e) {
        e.printStackTrace();
        return new Result(false, e.getMessage());
    }

    //用户名已经存在
    @ExceptionHandler(UsernameBeUseException.class)
    public Result UsernameBeUseExceptionInterceptor(UsernameBeUseException e) {
        e.printStackTrace();
        return new Result(false, e.getMessage());
    }

    //手机号码已经存在
    @ExceptionHandler(TelephoneIsExistException.class)
    public Result TelephoneIsExistExceptionInterceptor(TelephoneIsExistException e) {
        e.printStackTrace();
        return new Result(false, e.getMessage());
    }


    //拦截controller抛出的其他异常
    @ExceptionHandler(Exception.class)
    public Result ExceptionInterceptor(Exception e) {
        e.printStackTrace();
        System.out.println(e.getMessage());
        return new Result(false, MessageConstant.SERVER_ERROR);
    }

}
