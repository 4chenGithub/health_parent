package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.Constant.MessageConstant;
import com.health.Entity.Result;
import com.health.Uitls.QiNiuUtil;
import com.health.interfaces.PackageService;
import com.health.pojo.Package;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/package")
public class PackageController {

    @Reference
    private PackageService packageService;

    @GetMapping("/getPackage")
    public Result findAllPackage() {
        //调用业务服务查询所有套餐信息集合
        List<Package> packageList = packageService.findAllPackage();
        //由于数据库查询处出来的图片路径只是文件名称，
        // 所有我们需要对所有package对象中的Img属性进行手动拼接
        if (packageList != null && packageList.size() > 0) {
            //使用foreach遍历集合
            packageList.forEach(pack -> {
                //表示取出集合中的每一个元素并对每一个元素进行改造
                pack.setImg("http://" + QiNiuUtil.DOMAIN + "/" + pack.getImg());
            });
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, packageList);
        }
        return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
    }


    @GetMapping("/findById")
    public Result findById(int id) {
        //调用业务服务处理请求
        Package pkg = packageService.findById(id);
        if (pkg != null) {
            pkg.setImg("http://" + QiNiuUtil.DOMAIN + "/" + pkg.getImg());
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,pkg);
        }
        return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
    }


    /**
     * 根据id查询套餐信息
     * @param id
     * @return
     */
    @GetMapping("/findByPackageId")
    public Result findByPackageId(int id) {
        //调用业务服务处理请求
        Package pkg = packageService.findByPackageId(id);
        if (pkg != null) {
            pkg.setImg("http://" + QiNiuUtil.DOMAIN + "/" + pkg.getImg());
            return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,pkg);
        }
        return new Result(false,MessageConstant.QUERY_SETMEAL_FAIL);
    }

}
