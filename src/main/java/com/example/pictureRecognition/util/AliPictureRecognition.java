package com.example.pictureRecognition.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AliPictureRecognition {

    public static String img_base64(String path) {
        /**
         *  对path进行判断，如果是本地文件就二进制读取并base64编码，如果是url,则返回
         */
        String imgBase64 = "";
        if (path.startsWith("http")) {
            imgBase64 = path;
        } else {
            try {
                File file = new File(path);
                byte[] content = new byte[(int) file.length()];
                FileInputStream finputstream = new FileInputStream(file);
                finputstream.read(content);
                finputstream.close();
                imgBase64 = new String(Base64.encodeBase64(content));
            } catch (IOException e) {
                e.printStackTrace();
                return imgBase64;
            }
        }

        return imgBase64;
    }

    public static JSONObject IdentificationId(String path, String appcode, String imgFile, String method, String side) {
        Map<String, String> headers = new HashMap<String, String>();
        String host = "http://dm-51.data.aliyun.com";
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/json; charset=UTF-8");

        Map<String, String> querys = new HashMap<String, String>();
        // 对图像进行base64编码
        String imgBase64 = img_base64(imgFile);
        //configure配置
        JSONObject configObj = new JSONObject();
        configObj.put("side", side);
        String config_str = configObj.toString();
        // 拼装请求body的json字符串
        JSONObject requestObj = new JSONObject();
        requestObj.put("image", imgBase64);
        if (configObj.size() > 0) {
            requestObj.put("configure", config_str);
        }
        String bodys = requestObj.toString();

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            int stat = response.getStatusLine().getStatusCode();
            if (stat != 200) {
                return null;
            }
            String res = EntityUtils.toString(response.getEntity());
            JSONObject res_obj = JSON.parseObject(res);
            return res_obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONObject businessLicense(String appcode, String bodys, String method) {
        String host = "https://dm-58.data.aliyun.com";
        String path = "/rest/160601/ocr/ocr_business_license.json";
        Map<String, String> headers = new HashMap<>();
        //最后在header中的格式(中间是英文空格)为Authorization:APPCODE
        headers.put("Authorization", "APPCODE " + appcode);
        //根据API的要求，定义相对应的Content-Type
        headers.put("Content-Type", "application/json; charset=UTF-8");
        Map<String, String> querys = new HashMap<>();

        try {
            HttpResponse response = HttpUtils.doPost(host, path, method, headers, querys, bodys);
            int stat = response.getStatusLine().getStatusCode();
            if (stat != 200) {
                return null;
            }
            String res = EntityUtils.toString(response.getEntity());
            JSONObject res_obj = JSON.parseObject(res);
            return res_obj;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}