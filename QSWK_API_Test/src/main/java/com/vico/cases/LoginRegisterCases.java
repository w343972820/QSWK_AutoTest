package com.vico.cases;

import com.alibaba.fastjson.JSONObject;
import com.vico.config.InterFaceName;
import com.vico.config.TestConfig;
import com.vico.service.ConfigFile;
import com.vico.service.PostHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

public class LoginRegisterCases {
    private PostHttpClient client;
    @BeforeClass
    public void beforeTest(){
        TestConfig.LoginUrl= ConfigFile.getUrl(InterFaceName.LoginUrl);
        TestConfig.Register=ConfigFile.getUrl(InterFaceName.Register);
        //给httpClient赋值
        TestConfig.client=new DefaultHttpClient();
        client = new PostHttpClient();
    }
    @Test
    public void login() throws IOException {
        String resultCode = getLogin();
        JSONObject resultJson=JSONObject.parseObject(resultCode);
        String code = resultJson.getString("code");
        Reporter.log("登录请求,只测接口是否响应,不做滑块处理,.");
        Assert.assertEquals("2050",code);
    }
    @Test
    public void register() throws IOException {
        String resultCode = getRegister();
        JSONObject resultJson=JSONObject.parseObject(resultCode);
        String code = resultJson.getString("code");
        Reporter.log("注册请求,只测接口是否响应,不做滑块处理,.");
        Assert.assertEquals("2050",code);
    }

    public String getRegister() throws IOException {
        JSONObject param=new JSONObject();
        param.put("phoneormail","15818655277");
        param.put("password","bebad2e16d3896509954ab10e111a6c3");
        param.put("sessionId","dddd");
        param.put("sign","dddd");
        param.put("validatetoken","dddd");
        param.put("scene","pc_register");
        param.put("userid",null);
        param.put("languagetype","1");
        param.put("token",null);
        param.put("source","1");

        String result = client.getResult(param, TestConfig.Register);
        return result;


    }


    public String getLogin() throws IOException {
        JSONObject param=new JSONObject();
        param.put("phoneormail","15818655277");
        param.put("password","bebad2e16d3896509954ab10e111a6c3");
        param.put("sessionId","dddd");
        param.put("sign","dddd");
        param.put("validatetoken","dddd");
        param.put("scene","dddd");
        param.put("userid",null);
        param.put("languagetype","1");
        param.put("token",null);
        param.put("source","1");

        String result = client.getResult(param, TestConfig.LoginUrl);
        return result;

    }


}
