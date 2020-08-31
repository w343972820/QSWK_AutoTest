package com.vico.service;

import com.vico.config.InterFaceName;
import com.vico.config.TestConfig;
import com.vico.utils.DataBaseUtil;

import java.io.IOException;
import java.util.ResourceBundle;

public class ConfigFile {
    private static ResourceBundle bundle=ResourceBundle.getBundle("application");
    /*
    * 传入接口URI,返回接链接
    * */
    public static String getUrl(InterFaceName interFaceName){
        String address=bundle.getString("test.url");
        String uri="";
        if (interFaceName==InterFaceName.BannerList){
            uri=bundle.getString("test.BannerList.uri");
        }
        if (interFaceName==InterFaceName.NoticeManageList){
            uri=bundle.getString("test.NoticeManageList.uri");
        }
        if (interFaceName==InterFaceName.LoginUrl){
            uri=bundle.getString("test.Login.uri");
        }
        if (interFaceName==InterFaceName.Register){
            uri=bundle.getString("test.Register.uri");
        }
        if (interFaceName==InterFaceName.getCloudPackagelist){
            uri=bundle.getString("test.getCloudPackagelist.uri");
        }
        if (interFaceName==InterFaceName.selectCloudMinerParam){
            uri=bundle.getString("test.selectCloudMinerParam.uri");
        }
        if (interFaceName==InterFaceName.selectCloudShopDetail){
            uri=bundle.getString("test.selectCloudShopDetail.uri");
        }
        if (interFaceName==InterFaceName.buyCloudOrder){
            uri=bundle.getString("test.buyCloudOrder.uri");
        }
        if (interFaceName==InterFaceName.userAlreadyPay){
            uri=bundle.getString("test.userAlreadyPay.uri");
        }
        if (interFaceName==InterFaceName.selectCloudOrderList){
            uri=bundle.getString("test.selectCloudOrderList.uri");
        }
        if (interFaceName==InterFaceName.orderpayafter){
            uri=bundle.getString("test.orderpayafter.uri");
        }
        if (interFaceName==InterFaceName.selectCloudFrontData){
            uri=bundle.getString("test.selectCloudFrontData.uri");
        }
        if (interFaceName==InterFaceName.selectCloudProfitByCoin){
            uri=bundle.getString("test.selectCloudProfitByCoin.uri");
        }
        if (interFaceName==InterFaceName.selectCloudOrderPayList){
            uri=bundle.getString("test.selectCloudOrderPayList.uri");
        }
        if (interFaceName==InterFaceName.selectCloudOrderPayDetail){
            uri=bundle.getString("test.selectCloudOrderPayDetail.uri");
        }
        if (interFaceName==InterFaceName.selectCloudOrderDetail){
            uri=bundle.getString("test.selectCloudOrderDetail.uri");
        }
        if (interFaceName==InterFaceName.selectCloudProfitList){
            uri=bundle.getString("test.selectCloudProfitList.uri");
        }
        if (interFaceName==InterFaceName.selectCloudOrderPayFenList){
            uri=bundle.getString("test.selectCloudOrderPayFenList.uri");
        }
        return address+uri;
    }

}
