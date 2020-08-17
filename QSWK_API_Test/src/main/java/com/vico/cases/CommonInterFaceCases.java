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

public class CommonInterFaceCases {
    private PostHttpClient client;
    @BeforeClass
    public void beforeTest(){
        TestConfig.selectBannerListUrl=ConfigFile.getUrl(InterFaceName.BannerList);
        TestConfig.selectNoticeManageListUrl=ConfigFile.getUrl(InterFaceName.NoticeManageList);
        //给httpClient赋值
        TestConfig.client=new DefaultHttpClient();
        client = new PostHttpClient();
    }
    @Test
    public void bannerListTest() throws IOException {
        //拿到用例数据后，访问接口，去拿接口返回结果
        String resultCode = getBannerListResult();
        JSONObject resultJson=JSONObject.parseObject(resultCode);
        String code = resultJson.getString("code");
        Reporter.log("导航栏数据请求,响应 1000 即测试成功,否则测试失败.");
        Assert.assertEquals("1000",code);
    }
    @Test
    public void noticeManageList() throws IOException {
        String resultCode = getNoticeManageList();
        JSONObject resultJson=JSONObject.parseObject(resultCode);
        String code = resultJson.getString("code");
        Reporter.log("站内公告数据请求,响应 1000 即测试成功,否则测试失败.");
        Assert.assertEquals("1000",code);
    }

    public String getNoticeManageList() throws IOException {
        JSONObject param=new JSONObject();
        param.put("page","1");
        param.put("pageSize","10");
        param.put("userid",null);
        param.put("languagetype","1");
        param.put("token",null);
        param.put("source","1");
        String result = client.getResult(param, TestConfig.selectNoticeManageListUrl);
        return result;
    }
    public String getBannerListResult() throws IOException {
        JSONObject param=new JSONObject();
        param.put("type","1");
        param.put("userid",null);
        param.put("languagetype","1");
        param.put("token",null);
        param.put("source","1");
        String result = client.getResult(param, TestConfig.selectBannerListUrl);
        return result;
    }
}
