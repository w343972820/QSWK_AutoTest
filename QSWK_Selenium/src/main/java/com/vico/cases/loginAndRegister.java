package com.vico.cases;

import com.vico.config.InterFaceName;
import com.vico.config.TestConfig;
import com.vico.entity.RoleUser;
import com.vico.server.ConfigFile;

import com.vico.utils.CmmonMethod;
import com.vico.utils.DataBaseUtil;
import org.openqa.selenium.By;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class loginAndRegister {
    @BeforeClass
    public void loginBefore() throws IOException {
        TestConfig.sqlSession = DataBaseUtil.getSqlSession();
        TestConfig.login= ConfigFile.getUrl(InterFaceName.login);
        TestConfig.register= ConfigFile.getUrl(InterFaceName.register);
        ChromeOptions chromeOptions = new ChromeOptions();

       // System.setProperty("webdriver.chrome.driver", "E:\\seleuim\\chromedriver.exe");
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        //设置chrome浏览器的参数，使其不弹框提示（chrome正在受自动测试软件的控制）
        chromeOptions.addArguments("disable-infobars");
        //无界面参数
        //chromeOptions.addArguments("headless");
        //禁用沙盒 就是被这个参数搞了一天
         chromeOptions.addArguments("no-sandbox");
        // 开启开发者模式
        chromeOptions.setExperimentalOption("excludeSwitches", Collections.singletonList("enable-automation"));

        System.out.println("快去deriver了……");
        TestConfig.driver = new ChromeDriver(chromeOptions);
        TestConfig.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }
    @Test
    public void login(){
        //windows添加selenium驱动

        RoleUser sqlResult=TestConfig.sqlSession.selectOne("com.vico.dao.RoleUserDao.queryLastOne");
        String isRegister = sqlResult.getIsregister();
        String username="";
        String password="463006415wen";
        //如果是false则去注册，否则去登录
        if (isRegister.equals("false")){
            username=sqlResult.getPassword();
        }else{
            username=new BigDecimal(Double.toString(Double.valueOf(sqlResult.getPassword()))).add(new BigDecimal("1")).toPlainString();
        }

    }
    @Test
    public void register() throws InterruptedException {
        System.out.println("来的register..");
        RoleUser sqlResult=TestConfig.sqlSession.selectOne("com.vico.dao.RoleUserDao.queryLastOne");
        String username=new BigDecimal(Double.toString(Double.valueOf(sqlResult.getUsername()))).add(new BigDecimal("1")).toPlainString();
        System.out.println(username);
        String password="463006415wen";
        TestConfig.driver.get(TestConfig.register);
        System.out.println("已开启driver...");
        WebElement inputPhone = TestConfig.driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div/div/div[1]/div[2]/input"));
        WebElement inputPassword = TestConfig.driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div/div/div[1]/div[3]/input"));
        WebElement inputPasswordReset = TestConfig.driver.findElement(By.xpath("//*[@id=\"app\"]/div[1]/div/div/div[1]/div[4]/input"));
        Thread.sleep(100);
        inputPhone.sendKeys(username);
        Thread.sleep(1500);
        inputPassword.sendKeys(password);
        Thread.sleep(1200);
        inputPasswordReset.sendKeys(password);
        Thread.sleep(800);
        huaKuai();
    }
    public void huaKuai() throws InterruptedException {
        WebElement moveButton = TestConfig.driver.findElement(By.id("nc_1_n1z"));
        Actions actions = new Actions(TestConfig.driver);

        // 按下滑块
        new Actions(TestConfig.driver).clickAndHold(moveButton).perform();

        for (int i : getTrack(258)) {
            actions.moveByOffset(i, 0).perform();
            Thread.sleep(CmmonMethod.suijishu(30,2));
        }
        Thread.sleep(500);
        //释放
        actions.release(moveButton).perform();


       /* action.moveToElement(moveButton).clickAndHold(moveButton);
        Thread.sleep(200);
        action.dragAndDropBy(moveButton,(int)(Math.random()*200)+80, 0);
        Thread.sleep(800);
        action.dragAndDropBy(moveButton,(int)(Math.random()*200)+80, 0);
        Thread.sleep(700);
        action.dragAndDropBy(moveButton,(int)(Math.random()*200)+80, 0);
        Thread.sleep(900);
        action.dragAndDropBy(moveButton,(int)(Math.random()*200)+80, 0).perform();
        Thread.sleep(600);
        action.release();*/
        //确保每次拖动的像素不同，故而使用随机数
       /* action.clickAndHold(moveButton).moveByOffset((int)(Math.random()*200)+80, 0);
        Thread.sleep(2000);
        action.clickAndHold(moveButton).moveByOffset((int)(Math.random()*200)+80, 0);
        Thread.sleep(2000);
        action.clickAndHold(moveButton).moveByOffset((int)(Math.random()*200)+80, 0);
        Thread.sleep(2000);
        action.clickAndHold(moveButton).moveByOffset((int)(Math.random()*200)+80, 0);
        Thread.sleep(2000);
        //拖动完释放鼠标
        action.moveToElement(moveButton).release();
        //组织完这些一系列的步骤，然后开始真实执行操作
        Action actions = action.build();
        actions.perform();*/



    }

    // 获取运动轨迹
    public static Integer[] getTrack(int distance){
        List<Integer> integers = new ArrayList<Integer>();
//        distance += 20;
        int current = 0;
        double mid = (distance * 3) / 4;
        double t = 0.2;
        double v = 0.0;
        double v0 = 0;
        double move = 0.0;
        while (current < distance){
            int a = 0;
            if (current < mid){
                a = 2;
            }else {
                a = -3;
            }
            v0 = v;
            v = v0 + a * t;
            move = v0 * t + (0.5) * a * t * t;
            current += move;

            integers.add((int)Math.round(move));
        }
//        integers.addAll(Stream.of(-3, -3, -2, -2, -2, -2, -2, -1, -3, -4).collect(Collectors.toList()));
        return integers.toArray(new Integer[0]);
    }

    @AfterClass
    public void stopDriver() throws InterruptedException {
       // Thread.sleep(7000);
        //TestConfig.driver.quit();
       // TestConfig.driver=null;
    }

}
