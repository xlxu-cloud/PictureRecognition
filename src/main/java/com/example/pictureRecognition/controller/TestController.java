package com.example.pictureRecognition.controller;

import com.alibaba.fastjson.JSONObject;
import com.example.pictureRecognition.util.AliPictureRecognition;
import com.example.pictureRecognition.util.BaiDuPictureRecognition;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.ParseException;
import java.util.Base64;

//import static com.alibaba.cloudapi.pun.HighJing.getImgStr;

@RestController
public class TestController {

    @RequestMapping("")
    public String index() throws ParseException, IOException {
        String host = "http://dm-51.data.aliyun.com";//阿里提供的身份证识别的host
        String path = "/rest/160601/ocr/ocr_idcard.json";//阿里提供的path
//        String host = "https://dm-58.data.aliyun.com";//阿里提供的营业执照识别的host
        String appcode = "93e6168872ee479589f43c00287505b6";//购买的阿里appcode
        String idzFile = "C:\\Users\\sally\\Desktop\\z.jpg";
        String idfFile = "C:\\Users\\sally\\Desktop\\f.jpg";
        String yyzzFile = "C:\\Users\\sally\\Desktop\\yyzz.png";

        //识别营业执照传body
        JSONObject jsbody = new JSONObject();
        byte[] b = Files.readAllBytes(Paths.get(yyzzFile));
        String img = Base64.getEncoder().encodeToString(b);
        jsbody.put("image", img);

        JSONObject jsonStr = new JSONObject();
        jsonStr = AliPictureRecognition.IdentificationId(path, appcode, idzFile, "POST", "face");
        String idz = "姓名：" + jsonStr.get("name") + " \t性别：" + jsonStr.get("sex") +
                " \t名族：" + jsonStr.get("nationality") + " \t出生年月：" + jsonStr.get("birth") +
                " \t出生：" + jsonStr.get("birth") + "  \t身份证号码：" + jsonStr.get("num") +
                " \t住址：" + jsonStr.get("address") + "\n";

        jsonStr = AliPictureRecognition.IdentificationId(path, appcode, idfFile, "POST", "back");
        String idf = "签发机关：" + jsonStr.get("issue") + " \t有效期限：" + jsonStr.get("start_date") + "-" + jsonStr.get("end_date");

        jsonStr = AliPictureRecognition.businessLicense( appcode, jsbody.toJSONString(), "POST");
        String yyzz = "统一社会信用代码：" + jsonStr.get("reg_num") +
                " \t名称：" + jsonStr.get("name") + " \t类型： " + jsonStr.get("type") +
                " \t负责人：" + jsonStr.get("person") + " \t注册日期： " + jsonStr.get("establish_date") +
                //营业期限为长期和未识别到营业期限则默认为长期 即29991231
                " \t营业期限：" + jsonStr.get("valid_period") + " \t营业场所： " + jsonStr.get("address") +
                " \t注册资本：" + jsonStr.get("capital") + " \t经营范围：" + jsonStr.get("business");

        BaiDuPictureRecognition.idcard(idzFile);
        BaiDuPictureRecognition.idcard(idfFile);
        BaiDuPictureRecognition.businessLicense(yyzzFile);
        return "<pre>" + idz + "</pre>" + "<pre>" + idf + "</pre>" + "<pre>" + yyzz + "</pre>";
    }
}
