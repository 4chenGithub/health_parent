package com.health.Uitls;

import com.google.gson.Gson;
import com.health.Constant.MessageConstant;
import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.BatchStatus;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;

import java.util.ArrayList;
import java.util.List;

//七牛云上传文件工具类
public class QiNiuUtil {
    //七牛云的一对秘钥
    private static final String ACCESSKEY = "DDZvZrStYcOrh8WT0QzfPNE5bhneq5zPVpLg4CUa";
    private static final String SECRETKEY = "MwUnFrZ7JjAI3fm6Vxsm4LW3B_FCD-SaGz3WrggU";
    //七牛云上创建的存储空间的名称
    private static final String BUCKET = "chen520";
    //创建的空间域名:用于给用户访问图片
    public static final String DOMAIN = "pybd7dwsq.bkt.clouddn.com";

    public static void main(String[] args) {
        uploadFile("D:\\JAVA\\develop\\ideaIU\\health_Model\\health_web\\src\\main\\webapp\\img\\photo1.png", "photo1.png");
        //removeFiles("20190529083159.jpg","20190529083241.jpg");
    }

    /**
     * 批量删除
     *
     * @param filenames 需要删除的文件名列表
     * @return 删除成功的文件名列表
     */
    public static List<String> removeFiles(String... filenames) {
        // 删除成功的文件名列表
        List<String> removeSuccessList = new ArrayList<String>();
        if (filenames.length > 0) {
            // 创建仓库管理器
            BucketManager bucketManager = getBucketManager();
            // 创建批处理器
            BucketManager.Batch batch = new BucketManager.Batch();
            // 批量删除多个文件
            batch.delete(BUCKET, filenames);
            try {
                // 获取服务器的响应
                Response res = bucketManager.batch(batch);
                // 获得批处理的状态
                BatchStatus[] batchStatuses = res.jsonToObject(BatchStatus[].class);
                for (int i = 0; i < filenames.length; i++) {
                    BatchStatus status = batchStatuses[i];
                    String key = filenames[i];
                    System.out.print(key + "\t");
                    if (status.code == 200) {
                        removeSuccessList.add(key);
                        System.out.println("delete success");
                    } else {
                        System.out.println("delete failure");
                    }
                }
            } catch (QiniuException e) {
                e.printStackTrace();
                throw new RuntimeException(MessageConstant.PIC_UPLOAD_FAIL);
            }
        }
        return removeSuccessList;
    }

    /**
     * 上传本地文件
     *
     * @param localFilePath //本地文件的全路径
     * @param savedFilename //保存到七牛云的文件名
     */
    public static void uploadFile(String localFilePath, String savedFilename) {
        UploadManager uploadManager = getUploadManager();
        String upToken = getToken();
        try {
            Response response = uploadManager.put(localFilePath, savedFilename, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(String.format("key=%s, hash=%s", putRet.key, putRet.hash));
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
            throw new RuntimeException(MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**     * 上传字节流的文件
     *
     * @param bytes         //要上传的文件的字节流
     * @param savedFilename //保存到七牛云的文件名
     */
    public static void uploadViaByte(byte[] bytes, String savedFilename) {
        UploadManager uploadManager = getUploadManager();
        String upToken = getToken();
        try {
            Response response = uploadManager.put(bytes, savedFilename, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println(putRet.key);
            System.out.println(putRet.hash);
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
            throw new RuntimeException(MessageConstant.PIC_UPLOAD_FAIL);
        }
    }

    /**
     * 创建授权
     * @return
     */
    private static String getToken() {
        // 创建授权
        Auth auth = Auth.create(ACCESSKEY, SECRETKEY);
        // 获得认证后的令牌
        String upToken = auth.uploadToken(BUCKET);
        return upToken;
    }

    /**
     * Zone为抽象类,里面封装了不同区域的region
     * Zone.zone0 表示是华东的服务器 对应的是存到我们创建的华东的存储空间
     * Zone.zone2 表示是华南的服务器 对应的是存到我们创建的华南的存储空间
     * @return
     */
    private static UploadManager getUploadManager() {
        //构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone0());
        //构建上传管理器
        return new UploadManager(cfg);
    }

    private static BucketManager getBucketManager() {
        // 创建授权信息
        Auth auth = Auth.create(ACCESSKEY, SECRETKEY);
        // 创建操作某个仓库的管理器
        return new BucketManager(auth, new Configuration(Zone.zone0()));
    }

}
