package com.vico.cases;

import com.alibaba.fastjson.JSONArray;
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
import java.util.ArrayList;
@Log4j2
public class CommonInterFaceCases {
    private PostHttpClient client;
    @BeforeClass
    public void beforeTest() throws IOException {
        TestConfig.selectBannerListUrl=ConfigFile.getUrl(InterFaceName.BannerList);
        TestConfig.selectNoticeManageListUrl=ConfigFile.getUrl(InterFaceName.NoticeManageList);
        TestConfig.getCloudPackagelistUrl=ConfigFile.getUrl(InterFaceName.getCloudPackagelist);
        TestConfig.selectCloudMinerParamUrl=ConfigFile.getUrl(InterFaceName.selectCloudMinerParam);
        if (TestConfig.sqlSession==null){
            TestConfig.sqlSession = DataBaseUtil.getSqlSession();
        }
        //给httpClient赋值
        TestConfig.client=new DefaultHttpClient();
        client = new PostHttpClient();
    }
    @Test(enabled = true)
    public void bannerListTest() throws IOException {
        //拿到用例数据后，访问接口，去拿接口返回结果
        String resultCode = getBannerListResult();
        JSONObject resultJson=JSONObject.parseObject(resultCode);
        String code = resultJson.getString("code");
        Reporter.log("导航栏数据请求,响应 1000 即测试成功,否则测试失败.");
        Assert.assertEquals("1000",code);
    }
    @Test(enabled = true)
    public void noticeManageList() throws IOException {
        String resultCode = getNoticeManageList();
        JSONObject resultJson=JSONObject.parseObject(resultCode);
        String code = resultJson.getString("code");
        Reporter.log("站内公告数据请求,响应 1000 即测试成功,否则测试失败.");
        Assert.assertEquals("1000",code);
    }
    @Test(enabled = true)
    public void CloudPackagelist() throws IOException {
        //工具类中获取btc/eth套餐数据,用于预估收益
        String coin="ETH";
        String resultCode = getCloudPackagelist(coin);
        JSONObject resultJson=JSONObject.parseObject(resultCode);
        String code = resultJson.getString("code");
        Reporter.log("工具栏请求商品参数"+coin+",响应 1000 即测试成功,否则测试失败.");
        Assert.assertEquals("1000",code);
        JSONArray dataJsonArray = resultJson.getJSONArray("data");
        JSONObject calcListJson;
        JSONArray calcArray;
        for(int i=0;i<dataJsonArray.size();i++){
            calcListJson=JSONObject.parseObject(dataJsonArray.get(i).toString());
            calcArray = calcListJson.getJSONArray("calcList");
            if (calcArray.size()>0){
                for(int j=0;j<calcArray.size();j++){
                    System.out.println(calcArray.get(j));
                }
            }
        }
    }
    @Test
    public void selectCloudMinerParam() throws IOException {
        String coin="BTC";
        String resultCode = selectCloudMinerParamList(coin);
        JSONObject resultJson=JSONObject.parseObject(resultCode);
        String code = resultJson.getString("code");
        Reporter.log("算力商城请求商品参数 "+coin+",响应 1000 即测试成功,否则测试失败.");
        Assert.assertEquals("1000",code);
        JSONObject twoJson = resultJson.getJSONObject("data");
        JSONArray dataJsonArray = twoJson.getJSONArray("list");
        JSONObject calcListJson;
        JSONArray calcArray;
        JSONObject goodsJson;
        ShopGoods goods;
        ArrayList<ShopGoods> shopGoods=new ArrayList<ShopGoods>();
        //获取所有商品数据
        for(int i=0;i<dataJsonArray.size();i++){
            //拿到每个商品shopno,buynum(最低购买数),supnum(最大购买数量),cycleday(购买天数),cloundpayids(支持的支付方式),eachtnum(每T算力费),dayelec(每T每天电费)
            calcListJson = JSONObject.parseObject(dataJsonArray.get(i).toString());
            calcArray =calcListJson.getJSONArray("listshop");
            for(int j=0;j<calcArray.size();j++){
                goodsJson=JSONObject.parseObject(calcArray.get(j).toString());
                goods = new ShopGoods();
                goods.setShopno(goodsJson.getString("shopno"));
                goods.setSupnum(goodsJson.getString("supnum"));
                goods.setEachtnum(goodsJson.getString("eachtnum"));
                goods.setDayelec(goodsJson.getString("dayelec"));
                goods.setCloundpayids(goodsJson.getString("cloudpayids"));
                goods.setBuynum(goodsJson.getString("buynum"));
                goods.setCycleday(goodsJson.getString("cycleday"));
                goods.setPackagename(goodsJson.getString("packagename"));
                shopGoods.add(goods);
                goods=null;
            }
        }
        Reporter.log("商品数："+shopGoods.size());
        System.out.println("商品数："+shopGoods.size());
        //将所有商品存入数据库
        TestConfig.sqlSession.delete("com.vico.dao.ShopGoodsDao.deleteAll");
        TestConfig.sqlSession.commit();
        for(int i=0;i<shopGoods.size();i++){
            System.out.println(shopGoods.get(i));
            TestConfig.sqlSession.insert("com.vico.dao.ShopGoodsDao.insert",shopGoods.get(i));
            log.info("insert: "+shopGoods.get(i));
        }
        TestConfig.sqlSession.commit();
    }

    private String selectCloudMinerParamList(String coin) throws IOException {
        JSONObject param=new JSONObject();
        param.put("page","1");
        param.put("pageSize","10");
        param.put("cointypename",coin);
        param.put("userid",null);
        param.put("languagetype","1");
        param.put("token",null);
        param.put("source","1");
        String result = client.getResult(param, TestConfig.selectCloudMinerParamUrl);
        return result;


    }

    private String getCloudPackagelist(String coin) throws IOException {
        JSONObject param=new JSONObject();
        param.put("cointypename",coin);
        param.put("userid",null);
        param.put("languagetype","1");
        param.put("token",null);
        param.put("source","1");
        String result = client.getResult(param, TestConfig.getCloudPackagelistUrl);
        return result;

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
