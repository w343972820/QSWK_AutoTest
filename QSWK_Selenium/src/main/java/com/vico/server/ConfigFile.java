package com.vico.server;

import com.vico.config.InterFaceName;

import java.util.ResourceBundle;

public class ConfigFile {
    private static ResourceBundle bundle=ResourceBundle.getBundle("application");
    public static String getUrl(InterFaceName interFaceName) {
        String address = bundle.getString("test.url");
        String uri = "";
        if (interFaceName==InterFaceName.login){
            uri=bundle.getString("test.login.uri");
        }
        if (interFaceName==InterFaceName.register){
            uri=bundle.getString("test.register.uri");
        }
        return address+uri;
    }
}
