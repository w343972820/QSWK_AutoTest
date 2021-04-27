package com.vico.cases;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vico.config.InterFaceName;
import com.vico.config.TestConfig;
import com.vico.service.ConfigFile;
import com.vico.service.GetHouTaiToken;
import com.vico.service.PostHttpClient;
import com.vico.utils.GoogleYanZheng;
import org.apache.http.impl.client.DefaultHttpClient;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;

public class HouTaiShengHe {
    private PostHttpClient client;
    private ArrayList<String> paynos;
    @BeforeClass
    public void beforeTest() throws IOException {
        TestConfig.houtaiselectCloudOrderPayList=ConfigFile.getUrl(InterFaceName.houtaiselectCloudOrderPayList);
        TestConfig.houtaicheckCloudOrderPay=ConfigFile.getUrl(InterFaceName.houtaicheckCloudOrderPay);
        TestConfig.client=new DefaultHttpClient();
        TestConfig.houTaiToken=GetHouTaiToken.getToken();
        client = new PostHttpClient();
    }
    @Test(groups = {"shenHeLieBiao"},description = "后台待审核列表")
    public void daiShenHeDingDan() throws IOException {
        paynos=new ArrayList<String>();
        JSONObject param=new JSONObject();
        param.put("page","1");
        param.put("pageSize","100");
        param.put("type","2");
        param.put("change","");
        param.put("payno","");
        param.put("starttime","");
        param.put("endtime","");
        param.put("token",TestConfig.houTaiToken);
        param.put("loginnum",TestConfig.houTaiLoginnum);
        String result = client.getResult(param, TestConfig.houtaiselectCloudOrderPayList);
        //拿到订单列表
        JSONObject resultsJson=JSONObject.parseObject(result);
        String code=resultsJson.getString("code");
        if (!code.equals("1000")){
            System.out.println("待审核订单请求返回错误:"+result);
        }
        Assert.assertEquals("1000",code);
        JSONObject dataJson=resultsJson.getJSONObject("data");
        JSONArray listArray=dataJson.getJSONArray("list");
        for (int i = 0; i <listArray.size() ; i++) {
            JSONObject siginJson=JSONObject.parseObject(listArray.get(i).toString());           paynos.add(siginJson.getString("payno"));
        }
    }
    @Test(dependsOnGroups = "shenHeLieBiao",description = "去审核订单")
    public void shengHeDingDan() throws IOException {
        String payno;
        GoogleYanZheng yanzhengmae = new GoogleYanZheng();
        for (int i = 0; i <paynos.size() ; i++) {
            payno=paynos.get(i);
            JSONObject param=new JSONObject();
            param.put("payno",payno);
            param.put("checktype","1");
            param.put("code",yanzhengmae.getGoogleYanZheng(TestConfig.userGoogleSeceret));
            param.put("checktotal","vicoTest");
            param.put("token",TestConfig.houTaiToken);
            param.put("loginnum",TestConfig.houTaiLoginnum);
            String result = client.getResult(param, TestConfig.houtaicheckCloudOrderPay);
            JSONObject resultsJson=JSONObject.parseObject(result);
            String code=resultsJson.getString("code");
            if (!code.equals("1000")){
                System.out.println("审核订单返回错误:"+result);
            }
        }
    }

}
