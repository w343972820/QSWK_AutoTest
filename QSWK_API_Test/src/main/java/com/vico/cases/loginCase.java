package com.vico.cases;



import com.vico.config.TestConfig;
import com.vico.entity.Logincase;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.IOException;

public class loginCase {

    @BeforeClass
    public void beforeTest() throws IOException {
        System.out.println("ggggggggggg");
    }
    @Test
    public void test1() throws IOException {
        System.out.println("测试中...");
        Logincase oneData = TestConfig.sqlSession.selectOne("com.vico.dao.LogincaseDao.queryById", 1);
        System.out.println(oneData.getUsername());

    }
}
