package com.vico.cases;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vico.config.InterFaceName;
import com.vico.config.TestConfig;
import com.vico.service.ConfigFile;
import com.vico.service.PostHttpClient;
import com.vico.utils.CmmonMethod;
import com.vico.utils.DataBaseUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.http.impl.client.DefaultHttpClient;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
@Log4j2
public class PersonPowerListCases {
    private PostHttpClient client;
    @BeforeClass
    public void personPowerBefore() throws IOException {
        if (TestConfig.sqlSession==null){
            TestConfig.sqlSession = DataBaseUtil.getSqlSession();
        }
        TestConfig.selectCloudOrderList= ConfigFile.getUrl(InterFaceName.selectCloudOrderList);
        TestConfig.orderpayafter=ConfigFile.getUrl(InterFaceName.orderpayafter);
        TestConfig.userAlreadyPay=ConfigFile.getUrl(InterFaceName.userAlreadyPay);
        TestConfig.client=new DefaultHttpClient();
        client = new PostHttpClient();
    }
    @Test(enabled = true,description = "续费订单")
    public void personPowerList() throws IOException, InterruptedException {
        //拿到用例数据后，访问接口，去拿接口返回结果
        String resultCode = getPersonPowerList();
        JSONObject resultJson=JSONObject.parseObject(resultCode);
        String code = resultJson.getString("code");
        if (!code.equals("1000")){
            Reporter.log("我的算力请求出错返回:"+resultCode);
            System.out.println("我的算力请求请求出错返回:"+resultCode);
        }
        Reporter.log("我的算力请求,响应 1000 即测试成功,否则测试失败.");
        Assert.assertEquals("1000",code);
        //拿到数据后,看有没需要续费的，进行续费
        String data = resultJson.getString("data");
        JSONObject dataJson=JSONObject.parseObject(data);
        //算力个数：
        String count=dataJson.getString("count");
        JSONArray listJsonArray = dataJson.getJSONArray("list");
        //分期套餐首次购买按一个月算，购买时天数字段随便弄什么数字都是一样的，续费是按期数续费，没用到天数,所以续费必需判断是什么套餐
        for(int i=0;i<listJsonArray.size();i++) {
            JSONObject orderListJson = JSONObject.parseObject(listJsonArray.get(i).toString());
            //当前期数
            String stagenum = orderListJson.getString("stagenum");
            //剩余算力天数
            String surday = orderListJson.getString("surday");
            //剩余电费天数
            String elecday = orderListJson.getString("elecday");
            //套餐总期数
            String mentnum = orderListJson.getString("mentnum");
            //套餐标识
            String pointname = orderListJson.getString("pointname");
            //套餐周期
            String cycleday = orderListJson.getString("cycleday");
            //订单号
            String orderno = orderListJson.getString("orderno");
            //判断是分期套餐或是不定期套餐,分期标识为:ci5，不定期为：cix
            if (pointname.contains("ci")) {
                //判断是否可以续费，如果可以，则去续费
                if (Integer.valueOf(stagenum) < Integer.valueOf(mentnum)) {
                    //去续费,续一期,bumyent为购买期数，paystages为分别是哪几期，如2，3，4
                    goRenew(orderno, "1",String.valueOf(Integer.valueOf(stagenum) + 1));
                }
            }
            //如果是加速回本套餐则续电费，续电费10天起
            if (pointname.contains("js") || pointname.contains("fs")){
                //如果电费天数小于剩余天数，则表示能续电费
                int bigDay=Integer.valueOf(surday)-Integer.valueOf(elecday);
                if (Integer.valueOf(elecday)<Integer.valueOf(surday)){
                    //随机续电费天数
                    goRenew(orderno, String.valueOf(CmmonMethod.suijishu(bigDay,1)),"1");
                }
            }
            Thread.sleep(50);
        }
    }
    public void goRenew(String orderno,String buyment,String paystages) throws IOException {
        JSONObject param=new JSONObject();
        param.put("orderno",orderno);
        param.put("buyment",buyment);
        param.put("cloudpayid",TestConfig.cloudpayid);
        param.put("paystages",paystages);
        param.put("userid",TestConfig.userid);
        param.put("languagetype","1");
        param.put("token",TestConfig.token);
        param.put("source","1");
        String result = client.getResult(param, TestConfig.orderpayafter);
        //解析续费订单
        JSONObject renewOrderJson=JSONObject.parseObject(result);
        //System.out.println(renewOrderJson.toString());
        String code=renewOrderJson.getString("code");
        if (!code.equals("1000")){
            Reporter.log("续费订单请求出错返回:"+result);
            System.out.println("续费订单请求请求出错返回:"+result);
        }
        Reporter.log("续费订单请求,响应 1000 即测试成功,否则测试失败.");
        Assert.assertEquals("1000",code);
        //找到确认订单号
        JSONObject dataJson = renewOrderJson.getJSONObject("data");
        String payno = dataJson.getString("payno");
        //如果下单成功还得去确定购买
        String resureOrder = client.goSure(payno, TestConfig.userAlreadyPay);
        code=JSONObject.parseObject(resureOrder).getString("code");
        if (!code.equals("1000")){
            Reporter.log("确认订单请求出错返回:"+resureOrder);
            System.out.println("确认订单请求请求出错返回:"+resureOrder);
        }
        Reporter.log("确认订单请求,响应 1000 即测试成功,否则测试失败.");
        Assert.assertEquals("1000",code);
    }


    private String getPersonPowerList() throws IOException {
        JSONObject param=new JSONObject();
        param.put("ordertype","");
        param.put("page","1");
        param.put("pageSize","100");
        param.put("userid",TestConfig.userid);
        param.put("languagetype","1");
        param.put("token",TestConfig.token);
        param.put("source","1");
        String result = client.getResult(param, TestConfig.selectCloudOrderList);
        return result;

    }


}
