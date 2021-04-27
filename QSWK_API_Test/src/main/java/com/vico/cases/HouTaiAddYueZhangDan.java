package com.vico.cases;

import com.alibaba.fastjson.JSONObject;
import com.vico.config.InterFaceName;
import com.vico.config.TestConfig;
import com.vico.service.ConfigFile;
import com.vico.service.GetHouTaiToken;
import com.vico.service.PostHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

public class HouTaiAddYueZhangDan {
    private PostHttpClient client;
    private String orderno;
    private String coins;
    @BeforeClass
    public void addYueZhangDanBefore() throws IOException, InterruptedException {
        TestConfig.client = new DefaultHttpClient();
        TestConfig.houTaiToken = GetHouTaiToken.getToken();
        TestConfig.getCloudBulkMouthData = ConfigFile.getUrl(InterFaceName.getCloudBulkMouthData);
        TestConfig.addCloudBulkMouth = ConfigFile.getUrl(InterFaceName.addCloudBulkMouth);

        while (TestConfig.houTaiToken.equals("")){
            System.out.println("后台token为空....");
            Thread.sleep(1000);
            TestConfig.houTaiToken = GetHouTaiToken.getToken();
        }
        //后台操作,先登录后台拿到登录token
        this.orderno = "bu2820112310004059";
        this.coins = "btc";
        client = new PostHttpClient();
    }
    @Test(groups = {"yueZhangDan"},description = "添加月账单")
    public void addYueZhangDan() throws IOException {
        String year="2020";
        String mouth="10";
        JSONObject param=new JSONObject();
        param.put("userid",TestConfig.userid);
        param.put("orderno",this.orderno);
        param.put("yearMouth",year+"-"+mouth);
        param.put("mouthnum","");
        param.put("buynum","");
        param.put("buyprice","");
        param.put("elecnum","");
        param.put("year",year);
        param.put("mouth",mouth);
        param.put("token",TestConfig.houTaiToken);
        param.put("loginnum",TestConfig.houTaiLoginnum);
        String result = client.getResult(param, TestConfig.getCloudBulkMouthData);
        System.out.println(result);
        JSONObject resultsJson=JSONObject.parseObject(result);
        String code=resultsJson.getString("code");
        if (!code.equals("1000")){
            System.out.println("月账单获取当月挖矿数据有问题:"+result);
        }
        Assert.assertEquals("1000",code);
        JSONObject dataJson = resultsJson.getJSONObject("data");
        param.replace("mouthnum",dataJson.getString("mouthnum"));
        param.replace("buynum",dataJson.getString("mouthnum"));
        param.replace("buyprice","100000");
        param.replace("elecnum",dataJson.getString("elecnum"));
        result = client.getResult(param, TestConfig.addCloudBulkMouth);
        resultsJson=JSONObject.parseObject(result);
        code=resultsJson.getString("code");
        if (!code.equals("1000")){
            System.out.println("月账添加有问题:"+result);
        }
    }
    @Test(dependsOnGroups = "yueZhangDan",description = "修改及审核月账单")
    public void shengHeDingDan() throws IOException {

    }




}
