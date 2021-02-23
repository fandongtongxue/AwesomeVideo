package com.lin.controller;

import com.google.gson.Gson;
import com.lin.utils.RedisOperator;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author lkmc2
 * @date 2018/9/30
 * @description 基础控制器
 */
@RestController
public class BaseController {

    // Redis操作工具
    @Autowired
    protected RedisOperator redis;

    // 用户Redis Session名
    protected static final String USER_REDIS_SESSION = "user-redis-session";

    // 静态资源所在路径
    protected static final String FILE_BASE = "F:/AwesomeVideoUpload";

    // ffmpeg所在路径
    protected static final String FFMPEG_EXE = "/usr/local/Cellar/ffmpeg/4.3.2";

    // 每页分页的记录数
    protected static final Integer PAGE_SIZE = 5;

    public String uploadToQiniu(String filePath, String key){
        // 七牛云上传
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        String accessKey = "JkFGFDvaJ2WDZlvPSnoiNDXUZEdXmbY8qnw7nMBh";
        String secretKey = "6U5lamt5ei2bgmar3FwDFacg9asRLKt7dNgF0WHJ";
        String bucket = "temp";
        Auth auth = Auth.create(accessKey, secretKey);
        String upToken = auth.uploadToken(bucket);
        try {
            Response response = uploadManager.put(filePath, key, upToken);
            //解析上传成功的结果
            DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
            System.out.println("上传之后"+putRet.key);
            return "http://temp.dl.xiaobingkj.com/"+key;
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
            return "";
        }
    }

}
