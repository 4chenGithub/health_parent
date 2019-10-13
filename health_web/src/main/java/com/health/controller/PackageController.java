package com.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.health.Constant.MessageConstant;
import com.health.Constant.RedisConstant;
import com.health.Entity.PageResult;
import com.health.Entity.QueryPageBean;
import com.health.Entity.Result;
import com.health.Uitls.QiNiuUtil;
import com.health.interfaces.PackageService;
import com.health.pojo.Package;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/package")
public class PackageController {
    @Reference
    private PackageService packageService;
    @Autowired
    private JedisPool jedisPool;

    /**
     * 上传图片
     * 为了防止上传的文件相同被覆盖,则要产生唯一的字符串作为文件
     *
     * @param imgFile
     * @return
     */
    @PostMapping("/upload")
    public Result uploadPhoto(@RequestParam("imgFile") MultipartFile imgFile) {
        //获取前端上传过来的文件名
        String filename = imgFile.getOriginalFilename();
        //使用UUID创建唯一标识
        filename = UUID.randomUUID().toString() + filename.substring(filename.lastIndexOf("."));
        //使用工具类上传图片
        try {
            //上传图片到七牛云
            QiNiuUtil.uploadViaByte(imgFile.getBytes(), filename);
            //上传成功
            //将所有的文件名存到redis
            jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_RESOURCES,filename);
            //要封装路径到前端显示图片和文件名到前端赋值到formData中
            //拼接显示图片路径
            String ImgUrl = "http://" + QiNiuUtil.DOMAIN + "/" + filename;
            //将结果封装到map集合中
            Map<String, String> map = new HashMap<>();
            map.put("imgUrl", ImgUrl);
            map.put("fileName", filename);
            return new Result(true, MessageConstant.UPLOAD_SUCCESS, map);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        }
    }


    /**
     * 新增检查套餐
     *
     * @param pkg
     * @param checkgroupIds
     * @return
     */
    @PostMapping("/addPackage")
    public Result addPackage(@RequestBody Package pkg, @RequestParam("checkgroupIds") Integer[] checkgroupIds) {
        //调用业务服务处理请求
        packageService.addPackage(pkg, checkgroupIds);
        //新增完套餐后,说明上传的图片是存到数据库的,把当前成功的图片名称存到redis中
        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,pkg.getImg());
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    @PostMapping("/findAllPage")
    public Result findAllPage(@RequestBody QueryPageBean queryPageBean) {
        //调用业务服务处理请求
        PageResult<Package> pageResult = packageService.findAllPage(queryPageBean);
        return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,pageResult);
    }
}
