package com.vico.service;

import com.alibaba.fastjson.JSONObject;
import com.vico.config.InterFaceName;
import com.vico.config.TestConfig;
import com.vico.utils.GoogleYanZheng;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;

public class GetHouTaiToken {

    public static String getToken() throws IOException {
        TestConfig.adminLogin = ConfigFile.getUrl(InterFaceName.adminLogin);
        TestConfig.client = new DefaultHttpClient();
        PostHttpClient client = new PostHttpClient();
        GoogleYanZheng yanzhengmae = new GoogleYanZheng();
        JSONObject param = new JSONObject();
        param.put("loginnum", "www123456");
        param.put("password", "aaaa123456");
        param.put("code", yanzhengmae.getGoogleYanZheng(TestConfig.userGoogleSeceret));
        String result = client.getResult(param, TestConfig.adminLogin);
        if (result.contains("1000")) {
            JSONObject resultJson = JSONObject.parseObject(result);
            JSONObject dataJson = resultJson.getJSONObject("data");
            result = dataJson.getString("token");
        } else {
            result = "";
        }
        return result;
    }


}
