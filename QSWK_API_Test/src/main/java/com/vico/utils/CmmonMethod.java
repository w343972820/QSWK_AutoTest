package com.vico.utils;

import java.util.Random;

public class CmmonMethod {
    public static int suijishu(int max, int min) {
        Random rand = new Random();
        int randNumber = rand.nextInt(max - min + 1) + min;
        return randNumber;
    }
}
