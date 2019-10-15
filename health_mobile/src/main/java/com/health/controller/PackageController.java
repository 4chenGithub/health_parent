package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.github.pagehelper.StringUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.health.Constant.MessageConstant;
import com.health.Constant.RedisConstant;
import com.health.Entity.Result;
import com.health.Uitls.QiNiuUtil;
import com.health.interfaces.PackageService;
import com.health.pojo.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

@RestController
@RequestMapping("/package")
public class PackageController {

    @Reference
    private PackageService packageService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 获取所有套餐列表
     *
     * @return
     */
    @GetMapping("/getPackage")
    public Result findAllPackage() {
        //每次访问移动端首页先从redis中获取，为空时再从MySQL中获取，并存到redis中
        Jedis jedis = jedisPool.getResource();
        String packageJson = jedis.get(RedisConstant.MOBILE_PACKAGE_DATA_RESOURCES);
        Gson gson = new Gson();
        List<Package> packageList = gson.fromJson(packageJson, new TypeToken<List<Package>>() {
        }.getType());

        //如果redis中查询的数据为空，则从MySQL中获取，并将获取到的数据库转换成json存放redis中
        if (packageList == null) {
            packageList = packageService.findAllPackage();
            String Json = gson.toJson(packageList);
            jedis.set(RedisConstant.MOBILE_PACKAGE_DATA_RESOURCES, Json);
        }
        //归还连接
        jedis.close();
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

    /**
     * 根据id获取套餐详情
     * 这里使用redis做缓存
     * @param id
     * @return
     */
    @GetMapping("/findById")
    public Result findById(int id) {
        //每次访问移动端首页先从redis中获取，为空时再从MySQL中获取，并存到redis中
        Jedis jedis = jedisPool.getResource();
        String packageJson = jedis.get(RedisConstant.MOBILE_PACKAGE_DETAIL_RESOURCES);
        Gson gson = new Gson();
        //将json转换成对象
        Package pkg = gson.fromJson(packageJson, new TypeToken<Package>() {
        }.getType());
        //如果redis中查询的数据为空，则从MySQL中获取，并将获取到的数据库转换成json存放redis中
        if (pkg == null) {
            pkg = packageService.findById(id);
            String Json = gson.toJson(pkg);
            jedis.set(RedisConstant.MOBILE_PACKAGE_DETAIL_RESOURCES, Json);
        }
        //归还连接
        jedis.close();
        //调用业务服务处理请求
        if (pkg != null) {
            pkg.setImg("http://" + QiNiuUtil.DOMAIN + "/" + pkg.getImg());
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, pkg);
        }
        return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
    }


    /**
     * 根据id查询套餐信息
     *
     * @param id
     * @return
     */
    @GetMapping("/findByPackageId")
    public Result findByPackageId(int id) {
        //调用业务服务处理请求
        Package pkg = packageService.findByPackageId(id);
        if (pkg != null) {
            pkg.setImg("http://" + QiNiuUtil.DOMAIN + "/" + pkg.getImg());
            return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, pkg);
        }
        return new Result(false, MessageConstant.QUERY_SETMEAL_FAIL);
    }

}
