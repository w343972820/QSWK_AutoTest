package com.vico.service;

import com.vico.config.InterFaceName;
import com.vico.config.TestConfig;
import com.vico.utils.DataBaseUtil;

import java.io.IOException;
import java.util.ResourceBundle;

public class ConfigFile {
    private static ResourceBundle bundle = ResourceBundle.getBundle("application");

    /*
     * 传入接口URI,返回接链接
     * */
    public static String getUrl(InterFaceName interFaceName) {
        String address = bundle.getString("test.url");
        String uri = "";
        for (InterFaceName interFace : InterFaceName.values()) {
            if (interFaceName == interFace) {
                uri = bundle.getString("test." + interFace + ".uri");
            }
        }
        return address + uri;
    }

}
