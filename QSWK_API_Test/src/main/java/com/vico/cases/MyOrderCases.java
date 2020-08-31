package com.vico.cases;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vico.config.InterFaceName;
import com.vico.config.TestConfig;
import com.vico.service.ConfigFile;
import com.vico.service.PostHttpClient;
import lombok.extern.log4j.Log4j2;
import org.apache.http.impl.client.DefaultHttpClient;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;

@Log4j2
public class MyOrderCases {
    private PostHttpClient client;
    private ArrayList<String> orderno;
    private ArrayList<String> payno;
    private ArrayList<String> typePayno;

    @BeforeClass
    public void myOrderBeforeClass(){
        TestConfig.selectCloudOrderPayList= ConfigFile.getUrl(InterFaceName.selectCloudOrderPayList);
        TestConfig.userAlreadyPay=ConfigFile.getUrl(InterFaceName.userAlreadyPay);
        TestConfig.selectCloudOrderPayDetail=ConfigFile.getUrl(InterFaceName.selectCloudOrderPayDetail);
        TestConfig.selectCloudOrderDetail=ConfigFile.getUrl(InterFaceName.selectCloudOrderDetail);
        TestConfig.selectCloudProfitList=ConfigFile.getUrl(InterFaceName.selectCloudProfitList);
        TestConfig.selectCloudOrderPayFenList=ConfigFile.getUrl(InterFaceName.selectCloudOrderPayFenList);
        TestConfig.client=new DefaultHttpClient();
        client = new PostHttpClient();
        orderno=new ArrayList<String>();
        payno=new ArrayList<String>();
        typePayno = new ArrayList<String>();
    }
    @Test(groups = {"order"},description = "算力套餐订单数据用例")
    public void myOrderTest() throws IOException, InterruptedException {
        String result = getMyOrder("");
        JSONObject resultJson=JSONObject.parseObject(result);
        String code=resultJson.getString("code");
        Reporter.log("算力套餐数据请求,响应 1000 即测试成功,否则测试失败.");
        Assert.assertEquals("1000",code);
        //拿出list内的orderno与pyno用于查看订单详情及算力执行情况
        jiexiJson(resultJson);
    }
    @Test(groups = {"order"},description = "电费套餐订单数据用例")
    public void myOrderTest2() throws IOException, InterruptedException {
        String result = getMyOrder("9");
        JSONObject resultJson=JSONObject.parseObject(result);
        String code=resultJson.getString("code");
        Reporter.log("电费套餐数据请求,响应 1000 即测试成功,否则测试失败.");
        Assert.assertEquals("1000",code);
        //拿出list内的orderno与pyno用于查看订单详情及算力执行情况
        jiexiJson(resultJson);
    }
    @Test(dependsOnGroups = "order",description = "订单详情")
    public void orderInfo() throws IOException, InterruptedException {
        for(int i=0;i<payno.size();i++){
            JSONObject param=new JSONObject();
            param.put("payno",payno.get(i));
            param.put("userid",TestConfig.userid);
            param.put("languagetype","1");
            param.put("token",TestConfig.token);
            param.put("source","1");
            String result = client.getResult(param, TestConfig.selectCloudOrderPayDetail);
            log.info("请求订单详情："+result);
            JSONObject resultJson=JSONObject.parseObject(result);
            String code=resultJson.getString("code");
            Reporter.log("电费套餐数据请求,响应 1000 即测试成功,否则测试失败.");
            Assert.assertEquals("1000",code);
            Thread.sleep(50);
        }
    }

    @Test(dependsOnGroups = "order",description = "算力管理")
    public void suanLiGuanLi() throws IOException, InterruptedException {
        for(int i=0;i<orderno.size();i++){
            JSONObject param=new JSONObject();
            param.put("orderno",orderno.get(i));
            param.put("userid",TestConfig.userid);
            param.put("languagetype","1");
            param.put("token",TestConfig.token);
            param.put("source","1");
            String result = client.getResult(param, TestConfig.selectCloudOrderDetail);
            log.info("请求算力管理："+result);
            JSONObject resultJson=JSONObject.parseObject(result);
            String code=resultJson.getString("code");
            Reporter.log("算力管理数据请求,响应 1000 即测试成功,否则测试失败.");
            Assert.assertEquals("1000",code);
            Thread.sleep(50);
        }
    }


    @Test(dependsOnGroups = "order",description = "产出记录/付费记录")
    public void chanChuAndFuFeiJiLu() throws IOException, InterruptedException {
        for(int i=0;i<orderno.size();i++){
            JSONObject param=new JSONObject();
            param.put("orderno",orderno.get(i));
            param.put("page","1");
            param.put("pageSize","100");
            param.put("userid",TestConfig.userid);
            param.put("languagetype","1");
            param.put("token",TestConfig.token);
            param.put("source","1");
            String result = client.getResult(param, TestConfig.selectCloudProfitList);
            String resultFufeiJiLu = client.getResult(param, TestConfig.selectCloudOrderPayFenList);
            log.info("付费记录："+resultFufeiJiLu);
            log.info("产出记录："+result);
            JSONObject resultJson=JSONObject.parseObject(result);
            String code=resultJson.getString("code");
            Reporter.log("产出记录数据请求,响应 1000 即测试成功,否则测试失败.");
            Assert.assertEquals("1000",code);
            JSONObject resultFuFeiJson=JSONObject.parseObject(resultFufeiJiLu);
            String codeFuFei=resultFuFeiJson.getString("code");
            Reporter.log("付费记录数据请求,响应 1000 即测试成功,否则测试失败.");
            Assert.assertEquals("1000",codeFuFei);
            Thread.sleep(50);
        }
    }


    @Test(dependsOnGroups = "order",description = "如果有末确认订单，去确认")
    public void sureOrder() throws IOException, InterruptedException {
        for(int i=0;i<typePayno.size();i++){
            String alreadPayResult = client.goSure(typePayno.get(i),TestConfig.userAlreadyPay);
            JSONObject resultJson=JSONObject.parseObject(alreadPayResult);
            String code=resultJson.getString("code");
            log.info("确认订单："+alreadPayResult);
            Reporter.log("有订单确认数据请求,响应 1000 即测试成功,否则测试失败.");
            Assert.assertEquals("1000",code);
            Thread.sleep(50);
        }
    }


    public void jiexiJson(JSONObject resultJson) throws InterruptedException {
        JSONObject dataJson = resultJson.getJSONObject("data");
        JSONArray listArray = dataJson.getJSONArray("list");
        for(int i=0;i<listArray.size();i++){
            int type=JSONObject.parseObject(listArray.get(i).toString()).getIntValue("type");
            if (type==1){
                typePayno.add(JSONObject.parseObject(listArray.get(i).toString()).getString("payno"));
            }else{
                orderno.add(JSONObject.parseObject(listArray.get(i).toString()).getString("orderno"));
                payno.add(JSONObject.parseObject(listArray.get(i).toString()).getString("payno"));
            }
            Thread.sleep(50);
        }
    }
    private String getMyOrder(String payordertype) throws IOException {
        JSONObject param=new JSONObject();
        param.put("page","1");
        param.put("pageSize","100");
        param.put("type","");
        param.put("payordertype",payordertype);
        param.put("userid",TestConfig.userid);
        param.put("languagetype","1");
        param.put("token",TestConfig.token);
        param.put("source","1");
        String result = client.getResult(param, TestConfig.selectCloudOrderPayList);
        log.info("请求算力套餐："+result);
        return result;
    }


}
