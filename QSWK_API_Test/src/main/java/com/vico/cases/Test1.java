package com.vico.cases;

import com.vico.config.InterFaceName;
import com.vico.config.TestConfig;
import com.vico.service.ConfigFile;
import org.testng.annotations.Test;

public class Test1 {
    @Test
    public void test1(){
        TestConfig.houTaiaddBulkProfitSendOld=ConfigFile.getUrl(InterFaceName.houTaiaddBulkProfitSendOld);
        TestConfig.selectCloudProfitSendByOrderno=ConfigFile.getUrl(InterFaceName.selectCloudProfitSendByOrderno);
        TestConfig.checkProfitSendOld=ConfigFile.getUrl(InterFaceName.checkProfitSendOld);
        System.out.println(TestConfig.houTaiaddBulkProfitSendOld);
        System.out.println(TestConfig.selectCloudProfitSendByOrderno);
        System.out.println(TestConfig.checkProfitSendOld);
    }

}
