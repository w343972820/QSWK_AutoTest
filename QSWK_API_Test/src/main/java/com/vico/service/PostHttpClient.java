package com.vico.service;

import com.alibaba.fastjson.JSONObject;
import com.vico.config.TestConfig;
import lombok.extern.log4j.Log4j2;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
@Log4j2
public class PostHttpClient {
    public String getResult(JSONObject param,String url) throws IOException {
        HttpPost post = new HttpPost(url);
        //设置请求头信息 设置header
        post.setHeader("content-type","application/json");

        //将参数信息添加到参数中去
        StringEntity entity=new StringEntity(param.toString(),"utf-8");
        post.setEntity(entity);
        String result;
        //执行post请求
        HttpResponse response = TestConfig.client.execute(post);
        //获取响应结果
        result= EntityUtils.toString(response.getEntity(),"utf-8");
        //System.out.println(result);
        return result;
    }
    public String goSure(String payno,String url) throws IOException {
        JSONObject param=new JSONObject();
        param.put("image","");
        param.put("payno",payno);
        param.put("userid",TestConfig.userid);
        param.put("languagetype","1");
        param.put("token",TestConfig.token);
        param.put("source","1");
        String result = getResult(param, url);
        log.info("确认支付返回："+result);
        return result;
    }
}
