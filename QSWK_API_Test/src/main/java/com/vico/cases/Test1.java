package com.vico.cases;



import lombok.extern.log4j.Log4j2;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.annotations.Test;

import java.io.IOException;
@Log4j2
public class Test1 {
    @Test
    public void test1(){

        String url=  "https://m.cngold.org/quote/gjs/yhzhj_jh9999.html";
        try {
            Document doc = Jsoup.connect(url).get();
            log.info(doc.body());
            System.out.println(doc.body());


        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

}
