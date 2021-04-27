package com.vico.cases;

import com.vico.utils.CmmonMethod;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.ArrayList;
import java.util.List;

public class KongZhiYIKaiWangYe {
    public static void getAl() throws InterruptedException {
        {
            while (true) {
                System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
                ChromeOptions options = new ChromeOptions();
                options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
                WebDriver driver = new ChromeDriver(options);
                System.out.println(driver.getTitle());
//        System.exit(0);
                List<WebElement> inputs = new ArrayList<WebElement>();
                WebElement buttonClick;
                WebElement continueClick;
                boolean isClick = false;
                boolean isChoice = false;
                boolean isContinue = false;

                //如果是在选择页
                if (driver.getTitle().contains("Survey Question")) {
                    try {
                        inputs = driver.findElements(By.className("input-group"));
                        System.out.println("进来input,这里没问题");
                    } catch (Exception e) {
                        System.out.println("没找到选择项,可能网页出问题了");
                    }
                    System.out.println(driver.getTitle());
                    isClick = false;
                    isChoice = false;
                    for (int i = 0; i < inputs.size(); i++) {
                        isChoice = true;
                        if (inputs.get(i).getText().contains("Male")) {
                            inputs.get(i).click();
                            System.out.println("进来male");
                            isClick = true;
                        }
                    }
                    //如果是选择项且没被选上,则随机选一项
                    if (isChoice && !isClick) {
                        inputs.get(CmmonMethod.suijishu(inputs.size() - 1, 0)).click();
                    }
                    inputs.clear();
                    //拉到页面底部
                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight)");
                    Thread.sleep(1500);
                    //查看页面底部按钮,如果是确认,则点击确认,如果是continue则点继续
                    try {
                        buttonClick = driver.findElement(By.xpath("/html/body/div[1]/div[5]/div[1]/form/button"));
                        buttonClick.click();
                        Thread.sleep(4500);
                    } catch (Exception e) {
                        System.out.println("不是确认");
                    }
                }
                if (driver.getTitle().contains("Validating Answer")) {
                    while (!isContinue) {
                        System.out.println("进来继续页面");
                        //下拉500像素
                        String setscroll = "document.documentElement.scrollTop=" + "500";
                        JavascriptExecutor jse = (JavascriptExecutor) driver;
                        jse.executeScript(setscroll);
                        Thread.sleep(1500);
                        try {
                            continueClick = driver.findElement(By.xpath("//*[@id=\"continue-btn\"]/button"));
                            continueClick.click();
                            isContinue = true;
                            Thread.sleep(2500);
                        } catch (Exception e) {
                            System.out.println("不是继续");
                        }
                    }
                }
                buttonClick = null;
                continueClick = null;
                //如果网页断开,则刷新网页
                if (false) {
                    driver.navigate().refresh();
                }
                Thread.sleep(1000);
            }

        }
    }

    public static void main(String[] args) throws InterruptedException{
        System.setProperty("webdriver.chrome.driver", "C:\\Program Files (x86)\\Google\\Chrome\\Application\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("debuggerAddress", "127.0.0.1:9222");
        WebDriver driver = new ChromeDriver(options);
        System.out.println(driver.getTitle());
    }
}
