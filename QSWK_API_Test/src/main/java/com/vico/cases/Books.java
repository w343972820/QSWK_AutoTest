package com.vico.cases;

import com.alibaba.fastjson.JSONObject;
import com.vico.config.InterFaceName;
import com.vico.config.TestConfig;
import com.vico.entity.ShopGoods;
import com.vico.service.ConfigFile;
import com.vico.service.PostHttpClient;
import com.vico.utils.DataBaseUtil;
import lombok.extern.log4j.Log4j2;
import org.apache.http.impl.client.DefaultHttpClient;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;

import java.util.List;
@Log4j2
public class Books {
    private PostHttpClient client;
    @BeforeClass
    public void beforeTest() throws IOException {
        TestConfig.selectCloudShopDetailUrl= ConfigFile.getUrl(InterFaceName.selectCloudShopDetail);
        TestConfig.buyCloudOrder=ConfigFile.getUrl(InterFaceName.buyCloudOrder);
        TestConfig.userAlreadyPay=ConfigFile.getUrl(InterFaceName.userAlreadyPay);
        if (TestConfig.sqlSession==null){
            TestConfig.sqlSession = DataBaseUtil.getSqlSession();
        }
        //给httpClient赋值
        TestConfig.client=new DefaultHttpClient();
        client = new PostHttpClient();
    }
    @Test(description = "下单购买套餐")
    public void getBookdsInfor() throws IOException, InterruptedException {
        List<ShopGoods> shopGoods = TestConfig.sqlSession.selectList("com.vico.dao.ShopGoodsDao.vicoqueryAll");
        for (int number=0;number<shopGoods.size();number++){
            int type = shopGoods.get(number).getType();
            if (type==2){
                //如果type类型为2的话套餐已售罄
                continue;
            }
            String resultCode = getBookdsInfore(shopGoods.get(number).getShopno());
            //拿到订单信息后，查询个人钱包余额及支付方式是不是匹配,如果不匹配也去下单查看接口返回，先下个没钱单，在下个有钱单
            JSONObject infoJson=JSONObject.parseObject(resultCode);
            String code = infoJson.getString("code");
            if (!code.equals("1000")){
                Reporter.log("订单页数据请求出错返回:"+resultCode);
                System.out.println("订单页数据请求出错返回:"+resultCode);
            }
            Reporter.log("订单页数据请求,响应 1000 即测试成功,否则测试失败.");
            Assert.assertEquals("1000",code);
            //拿pointName来判断是什么套餐，根据不能套餐买不同天数电费
            JSONObject dataJson=infoJson.getJSONObject("data");
            String pointname = dataJson.getString("pointname");
            String qishu = dataJson.getString("mentnum");
            String bookDay=shopGoods.get(number).getCycleday();
            String buynum=shopGoods.get(number).getBuynum();
            //目前只有月付/加速回本,分期/不定期四个套餐防盗锁
            String buyday=String.valueOf(Integer.valueOf(bookDay)/Integer.valueOf(qishu)) ;
            //判断是否是加速回本套餐
            if (pointname.contains("js")){
                buyday="10";
            }
            //判断是否为月付套餐
            if (pointname.contains("fs")){
                buyday="30";
            }
            //直接下单
            String returnBookInfo = goBook(shopGoods.get(number).getShopno(), buynum, buyday);
            infoJson=JSONObject.parseObject(returnBookInfo);
            code = infoJson.getString("code");
            if (!code.equals("1000")){
                Reporter.log("下单出错返回:"+returnBookInfo);
                System.out.println("下单出错返回:"+returnBookInfo);
            }
            Assert.assertEquals("1000",code);
            //下完单得确认
            dataJson=infoJson.getJSONObject("data");
            String payno = dataJson.getString("payno");
            String alreadPayResult = client.goSure(payno,TestConfig.userAlreadyPay);
            infoJson=JSONObject.parseObject(alreadPayResult);
            code = infoJson.getString("code");
            if (!code.equals("1000")){
                Reporter.log("确认付费出错返回:"+alreadPayResult);
                System.out.println("确认付费出错返回:"+alreadPayResult);
            }
            Reporter.log("确认付费数据请求,响应 1000 即测试成功,否则测试失败.");
            Assert.assertEquals("1000",code);
            Thread.sleep(50);
        }

    }
    public String goBook(String shopno,String buynum,String buyday) throws IOException {
        JSONObject param=new JSONObject();
        param.put("shopno",shopno);
        param.put("buynum",buynum);
        param.put("cloudpayid",TestConfig.cloudpayid);
        param.put("buyday",buyday);
        param.put("userid",TestConfig.userid);
        param.put("languagetype","1");
        param.put("token",TestConfig.token);
        param.put("source","1");
        System.out.println(TestConfig.buyCloudOrder);
        log.info("下单参数："+param);
        String result = client.getResult(param, TestConfig.buyCloudOrder);
        log.info("下单返回："+result);
        return result;
    }

    private String getBookdsInfore(String shopno) throws IOException {
        JSONObject param=new JSONObject();
        param.put("shopno",shopno);
        param.put("userid",TestConfig.userid);
        param.put("languagetype","1");
        param.put("token",TestConfig.token);
        param.put("source","1");
        String result = client.getResult(param, TestConfig.selectCloudShopDetailUrl);
        log.info("下单页返回："+result);
        return result;


    }


}
