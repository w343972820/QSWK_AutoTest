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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HouTaiAddShouYi {
    private PostHttpClient client;
    private String orderno;
    private String coins;

    @BeforeClass
    public void addShouYiBefore() throws IOException, InterruptedException {
        TestConfig.houTaiaddBulkProfitSendOld = ConfigFile.getUrl(InterFaceName.houTaiaddBulkProfitSendOld);
        TestConfig.selectCloudProfitSendByOrderno = ConfigFile.getUrl(InterFaceName.selectCloudProfitSendByOrderno);
        TestConfig.checkProfitSendOld = ConfigFile.getUrl(InterFaceName.checkProfitSendOld);
        TestConfig.client = new DefaultHttpClient();
        TestConfig.houTaiToken = GetHouTaiToken.getToken();
        while (TestConfig.houTaiToken.equals("")){
            System.out.println("后台token为空....");
            Thread.sleep(1000);
            TestConfig.houTaiToken = GetHouTaiToken.getToken();

        }
        //后台操作,先登录后台拿到登录token
        this.orderno = "buhi21042510004627";
        this.coins = "btc";
        client = new PostHttpClient();
    }

    @Test(groups = {"tianJiaShouYi"}, description = "后台添加收益")
    public void addShouYi() throws IOException, ParseException, InterruptedException {
        //当前日期减一天
        Date date1 = getTimeZhuang(new Date(), -2);
        Date date = getTimeZhuang(new Date(), -1);
        int tianshu = 55;
        for (int i = 0; i < tianshu; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String days = sdf.format(date);
            // System.out.println(aad);
            //添加收益
            String returnBookInfo = goAddUserCoin(this.orderno, this.coins, days);
            JSONObject resultJson = JSONObject.parseObject(returnBookInfo);
            String code = resultJson.getString("code");
            if (!code.equals("1000")) {
                System.out.println("添加收益请求返回：" + resultJson);
                while (code.equals("1001")) {
                    returnBookInfo = goAddUserCoin(this.orderno, this.coins, days);
                    resultJson = JSONObject.parseObject(returnBookInfo);
                    code = resultJson.getString("code");
                    Thread.sleep(1000);
                    System.out.println("添加收益请求返回：" + resultJson);
                }
            }
            Assert.assertEquals("1000", code);
            //System.out.println(returnBookInfo);
            Thread.sleep(80);
            date = getTimeZhuang(date, -1);
        }


    }

    @Test(dependsOnGroups = "tianJiaShouYi", description = "去审核添加的收益")
    public void shengheShouYi() throws IOException, InterruptedException {
        //查看是否需要审核,如果有需要则去审核
        String returnBookInfo = goIfShenHe(this.orderno);
        JSONObject resultJson = JSONObject.parseObject(returnBookInfo);
        String code = resultJson.getString("code");
        if (!code.equals("1000")) {
            System.out.println("产出记录请求返回：" + resultJson);
        }
        Assert.assertEquals("1000", code);
        JSONObject dataJson = resultJson.getJSONObject("data");
        JSONArray listJsonArray = dataJson.getJSONArray("list");
        for (int i = 0; i < listJsonArray.size(); i++) {
            JSONObject oneJson = JSONObject.parseObject(listJsonArray.get(i).toString());
            System.out.println(oneJson);
            int blagShenHe = oneJson.getInteger("sendtype");
            if (blagShenHe == 1) {
                //去核审
                int ids = oneJson.getInteger("id");
                String shengHeResult = goShengHe(ids);
                JSONObject shengHeJson = JSONObject.parseObject(shengHeResult);
                String shengHeCode = shengHeJson.getString("code");
                if (!shengHeCode.equals("1000")) {
                    System.out.println("审核请求返回:" + shengHeJson);
                    while (shengHeCode.equals("1001")) {
                        shengHeResult = goShengHe(ids);
                        shengHeJson = JSONObject.parseObject(shengHeResult);
                        shengHeCode = shengHeJson.getString("code");
                        Thread.sleep(1000);
                        System.out.println("添加收益请求返回：" + shengHeResult);
                    }
                }
                Assert.assertEquals("1000", shengHeCode);
            }
            Thread.sleep(80);
        }

    }

    private String goShengHe(int ids) throws IOException {
        GoogleYanZheng yanzhengmae = new GoogleYanZheng();
        JSONObject param = new JSONObject();
        param.put("checktype", "1");
        param.put("code", yanzhengmae.getGoogleYanZheng(TestConfig.userGoogleSeceret));
        param.put("id", String.valueOf(ids));
        param.put("token", TestConfig.houTaiToken);
        param.put("loginnum", TestConfig.houTaiLoginnum);
        System.out.println("参数：" + param);
        String result = client.getResult(param, TestConfig.checkProfitSendOld);
        return result;
    }

    private String goIfShenHe(String orderno) throws IOException {
        JSONObject param = new JSONObject();
        param.put("page", "1");
        param.put("pageSize", "500");
        param.put("orderno", orderno);
        param.put("token", TestConfig.houTaiToken);
        param.put("loginnum", TestConfig.houTaiLoginnum);
        System.out.println("参数：" + param);
        String result = client.getResult(param, TestConfig.selectCloudProfitSendByOrderno);
        return result;

    }

    @Test(enabled = false)
    public void test1() throws ParseException {
        //当前日期减一天
        Date date1 = getTimeZhuang(new Date(), -1);
        Date date = getTimeZhuang(date1, -1);
        int tianshu = 5;
        for (int i = 0; i < tianshu; i++) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String aad = sdf.format(date);
            System.out.println(aad);
            date = getTimeZhuang(date, -1);
        }
    }

    private String goAddUserCoin(String orderno, String coin, String days) throws IOException {
        //币种收益
        String profitnum = "0.01857";
        //转换成usdt数
        String profitusdt = "1.3";
        //发放日期
        String profittime = "2020-09-03";
        //写好日期
        GoogleYanZheng yanzhengmae = new GoogleYanZheng();
        JSONObject param = new JSONObject();
        param.put("orderno", orderno);
        param.put("userid", TestConfig.userid);
        param.put("cointypename", coin);
        param.put("profitnum", profitnum);
        param.put("profitusdt", profitusdt);
        //添加时间：2020-09-13
        param.put("profittime", days);
        param.put("orepool", "vicoTest");
        //谷歌验证
        param.put("code", yanzhengmae.getGoogleYanZheng(TestConfig.userGoogleSeceret));
        param.put("token", TestConfig.houTaiToken);
        param.put("loginnum", TestConfig.houTaiLoginnum);
        System.out.println("参数：" + param);
        String result = client.getResult(param, TestConfig.houTaiaddBulkProfitSendOld);
        return result;
    }

    public Date getTimeZhuang(Date de, int deys) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d = sdf.parse(sdf.format(de));
        Calendar cal = Calendar.getInstance();
        cal.setTime(d);
        cal.add(Calendar.DAY_OF_YEAR, deys);
        return cal.getTime();
    }

}
