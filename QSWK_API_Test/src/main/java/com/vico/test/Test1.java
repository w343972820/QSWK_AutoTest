package com.vico.test;

import org.testng.Assert;
import org.testng.annotations.Test;

public class Test1 {

    @Test
    public void test1(){
        System.out.println("test1....");
        test2("vico");
    }
    public void test2(String name){
        System.out.println("test2..."+name);
        Assert.assertEquals("vico",name);
    }
}
