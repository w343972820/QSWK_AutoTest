package com.vico.service;

import com.vico.config.InterFaceName;
import com.vico.config.TestConfig;
import com.vico.utils.DataBaseUtil;

import java.io.IOException;
import java.util.ResourceBundle;

public class ConfigFile {
    private static ResourceBundle bundle=ResourceBundle.getBundle("application");
    static {
        try {
            TestConfig.sqlSession = DataBaseUtil.getSqlSession();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
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
        return address+uri;
    }

}
