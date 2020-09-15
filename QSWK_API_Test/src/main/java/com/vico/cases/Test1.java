package com.vico.cases;

import com.vico.config.InterFaceName;
import com.vico.config.TestConfig;
import com.vico.service.ConfigFile;
import org.testng.annotations.Test;

public class Test1 {
    @Test
    public void test1(){
        TestConfig.selectBannerListUrl= ConfigFile.getUrl(InterFaceName.BannerList);
        System.out.println(TestConfig.selectBannerListUrl);
    }
}
