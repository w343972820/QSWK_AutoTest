package com.vico.cases;

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
public class DataBtcEth {
    private PostHttpClient client;
    private ArrayList<String> coins;
    @BeforeClass
    public void getTaoCanBefore(){
        TestConfig.selectCloudFrontData= ConfigFile.getUrl(InterFaceName.selectCloudFrontData);
        TestConfig.selectCloudProfitByCoin= ConfigFile.getUrl(InterFaceName.selectCloudProfitByCoin);
        //给httpClient赋值
        TestConfig.client=new DefaultHttpClient();
        client = new PostHttpClient();
        coins=new ArrayList<String>();
        coins.add("BTC");
        coins.add("ETH");
    }
    @Test(description = "请求btc或eth套餐数据，可查看运行总算力及总产出数据")
    public void getTaoCan() throws IOException, InterruptedException {
        String result;
        for(int i=0;i<coins.size();i++){
            result = getTaoCanResult(coins.get(i));
            JSONObject resultJson=JSONObject.parseObject(result);
            String code=resultJson.getString("code");
            log.info(coins.get(i)+"套餐数据请求,响应 1000 即测试成功,否则测试失败.");
            Reporter.log(coins.get(i)+"套餐数据请求,响应 1000 即测试成功,否则测试失败.");
            Assert.assertEquals("1000",code);
            Thread.sleep(50);
        }
    }
    @Test(description = "每日算力产出日志")
    public void getEveryDaySuanLi() throws IOException, InterruptedException {
        String result;
        for(int i=0;i<coins.size();i++){
            result = getEveryDayLog(coins.get(i));
            JSONObject resultJson=JSONObject.parseObject(result);
            String code=resultJson.getString("code");
            log.info(coins.get(i)+"套餐数据请求,响应 1000 即测试成功,否则测试失败.");
            Reporter.log(coins.get(i)+"套餐数据请求,响应 1000 即测试成功,否则测试失败.");
            Assert.assertEquals("1000",code);
            Thread.sleep(50);
        }
    }

    private String getEveryDayLog(String coin) throws IOException {
        JSONObject param=new JSONObject();
        param.put("page","1");
        param.put("pageSize","100");
        param.put("cointypename",coin);
        param.put("userid",TestConfig.userid);
        param.put("languagetype","1");
        param.put("token",TestConfig.token);
        param.put("source","1");
        String result = client.getResult(param, TestConfig.selectCloudProfitByCoin);
        log.info("请求"+coin+"套餐："+result);
        return result;


    }

    private String getTaoCanResult(String coin) throws IOException {
        JSONObject param=new JSONObject();
        param.put("cointypename",coin);
        param.put("userid",TestConfig.userid);
        param.put("languagetype","1");
        param.put("token",TestConfig.token);
        param.put("source","1");
        String result = client.getResult(param, TestConfig.selectCloudFrontData);
        log.info("请求"+coin+"套餐："+result);
        return result;
    }


}
