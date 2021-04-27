package com.vico.cases;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TianGanDiZhi {
    private  static String riTianGan="";
    private static String riDiZhi="";
    private static int ganZhiWeiZhi=0;
    private static int diZhiWeiZhi=0;

    private static String getYearTianGanDiZhi(int year) {
        String tianGanDiZhi = "";
        if (year >= 1800 && year <= 1899) {
            tianGanDiZhi = getYearGan(0, year);
        } else if (year >= 1900 && year <= 1999) {
            tianGanDiZhi = getYearGan(1, year);
        } else if (year >= 2000 && year <= 2099) {
            tianGanDiZhi = getYearGan(2, year);
        } else {
            System.out.println("年份输入错误，年份支侍1800至2099");
        }
        return tianGanDiZhi;
    }

    private static String getYearGan(int blag, int year) {
        int yuShu = 0;
        yuShu = year % 10;
        String nianTianGan = "";
        String nianDiZhi = "";
        // 推算天干：西元年末位數-3=年干(适用于任何年代)。
        if (yuShu < 3) {
            yuShu = yuShu + 10 - 3;
        } else {
            yuShu = yuShu - 3;
        }
        nianTianGan =  yiBanTianGan(yuShu);
        // 推算地支：西元年末二位數+9=年支(適用於十九世紀，即1800—1899 年)；
        // 西元年末二位數十1=年支(適用於二十世紀，即1900—1999 年)；
        // 西元年末二位數+5=年支(適用於廿一世紀，即2000—2099 年)；
        yuShu = year % 100;
        if (blag == 0) {
            yuShu = yuShu + 9;
            yuShu = yuShu % 12;
        } else if (blag == 1) {
            yuShu = yuShu + 1;
            yuShu = yuShu % 12;
        } else if (blag == 2) {
            yuShu = yuShu + 5;
            yuShu = yuShu % 12;
        }
        if (yuShu == 0) {
            yuShu = 12;
        }
        switch (yuShu) {
            case 1:
                nianDiZhi = "子";
                break;
            case 2:
                nianDiZhi = "醜";
                break;
            case 3:
                nianDiZhi = "寅";
                break;
            case 4:
                nianDiZhi = "卯";
                break;
            case 5:
                nianDiZhi = "辰";
                break;
            case 6:
                nianDiZhi = "巳";
                break;
            case 7:
                nianDiZhi = "午";
                break;
            case 8:
                nianDiZhi = "未";
                break;
            case 9:
                nianDiZhi = "申";
                break;
            case 10:
                nianDiZhi = "酉";
                break;
            case 11:
                nianDiZhi = "戌";
                break;
            case 12:
                nianDiZhi = "亥";
                break;
        }

        return nianTianGan + nianDiZhi;
    }

    private static String getMonthGanZhiDiZhi(int year, int month) {
        int yuShu = 0;
        yuShu = year % 10;
        String monthGan = "";
        String mountDi="";
        // 推算天干：西元年末位數-3=年干(适用于任何年代)。
        if (yuShu < 3) {
            yuShu = yuShu + 10 - 3;
        } else {
            yuShu = yuShu - 3;
        }
        if (yuShu == 0) {
            yuShu = 10;
        }
        //月天干＝年天干*2+月数 在对应天干表
        yuShu = yuShu * 2 + month;
        monthGan = yiBanTianGan(yuShu);
        mountDi=dizhiGongGong(month);
        return monthGan+mountDi;
    }

    private static String dizhiGongGong(int month) {
        String mountDi="";
        if (month==0){
            month=12;
        }
        switch (month) {
            case 1:
                mountDi = "寅";
                break;
            case 2:
                mountDi = "卯";
                break;
            case 3:
                mountDi = "辰";
                break;
            case 4:
                mountDi = "巳";
                break;
            case 5:
                mountDi = "午";
                break;
            case 6:
                mountDi = "未";
                break;
            case 7:
                mountDi = "申";
                break;
            case 8:
                mountDi = "酉";
                break;
            case 9:
                mountDi = "戌";
                break;
            case 10:
                mountDi = "亥";
                break;
            case 11:
                mountDi = "子";
                break;
            case 12:
                mountDi = "醜";
                break;
        }
        return mountDi;
    }

    private static String riTianGanDizhi(int year,int month,int day) throws ParseException {
        //只算20世纪跟21世纪
        int yushu=0;
        if (year>=1900 && year<=2000) {
            //润年元旦
            if (year%4==0) {
                yushu = year%100;
                yushu = yushu/4;
                getGongRitianDiZhi(yushu,year,month,day);
            }else{
                //先计算平年元旦,通过元旦按60为一轮推算其它日子干支
                int yunNianCha=0;
                int runnian=0;

                if ((year-1)%4==0){
                    //润年后一年为天克地冲
                    yunNianCha=1;
                    runnian= year-1;
                    getFeiRunNianRiGanDiZhi(yunNianCha,runnian,year,month,day);

                }else if((year-2)%4==0){
                    //润年后二年天顺地逆
                    yunNianCha=2;
                    runnian= year-2;
                    getFeiRunNianRiGanDiZhi(yunNianCha,runnian,year,month,day);
                }else if((year-3)%4==0){
                    //润年后三年天克地车
                    yunNianCha=3;
                    runnian= year-3;
                    getFeiRunNianRiGanDiZhi(yunNianCha,runnian,year,month,day);
                }

            }
        }else if (year>2000 && year<=2100) {
            //润年元旦
            if (year%4==0) {
                yushu = year%100;
                yushu = (yushu+100)/4;
                getGongRitianDiZhi(yushu,year,month,day);
            }else{
                //先计算平年元旦,通过元旦按60为一轮推算其它日子干支
                int yunNianCha=0;
                int runnian=0;

                if ((year-1)%4==0){
                    //润年后一年为天克地冲
                    yunNianCha=1;
                    runnian= year-1;
                    getFeiRunNianRiGanDiZhi(yunNianCha,runnian,year,month,day);

                }else if((year-2)%4==0){
                    //润年后二年天顺地逆
                    yunNianCha=2;
                    runnian= year-2;
                    getFeiRunNianRiGanDiZhi(yunNianCha,runnian,year,month,day);
                }else if((year-3)%4==0){
                    //润年后三年天克地车
                    yunNianCha=3;
                    runnian= year-3;
                    getFeiRunNianRiGanDiZhi(yunNianCha,runnian,year,month,day);
                }
            }
        }else {
            System.out.println("只支持1900年到2100年");
        }

        return riTianGan+riDiZhi;
    }
    private static void getFeiRunNianRiGanDiZhi(int runNianCha,int year,int runnian,int month,int day) throws ParseException {
        int yushu=0;
        //先计算润年元旦对应值
        if (runnian>=1900 && runnian<=2000) {
            yushu = year%100;
            yushu = yushu/4;
            //由此得出润年天干地支位置ganZhiWeiZhi,diZhiWeiZhi
            getGongRitianDiZhi(yushu,runnian,1,1);
            //润年差为1则天克：x=(x+6)%10 if x==0 x=10,地冲:x=(x+6)%12 if x==0x=12
            if (runNianCha==1){
                //算出元旦天支地支
                ganZhiWeiZhi = (ganZhiWeiZhi+6) %10;
                riTianGan = yiBanTianGan(ganZhiWeiZhi);
                diZhiWeiZhi = (diZhiWeiZhi+6) %12;
                riDiZhi = dizhiGongGong (diZhiWeiZhi);
                //如果是元旦则不往下运行，如果是非元旦数据，则往下执行
                if (month==1 && day==1){
                    return;
                }else{
                   //非元旦数据与其它方法一至
                    //通过元旦天支地支，来计算其它日子
                    //计算相差多少天
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = sdf.parse(runnian+"-1-1");
                    Date date2 = sdf.parse(runnian+"-"+month+"-"+day);
                    //计算相差多少天
                    int days=daysBetween(date1,date2);
                    days=days%60;
                    //天支计算
                    int tianzhi=0;
                    int dizhi=0;
                    tianzhi = (ganZhiWeiZhi+days)%10;
                    riTianGan=yiBanTianGan(tianzhi);
                    dizhi=diZhiWeiZhi+days;
                    dizhi=dizhi%12;
                    riDiZhi=dizhiGongGong(dizhi);
                    System.out.println("非元旦");
                }

            } else if (runNianCha==2){
                ganZhiWeiZhi = (ganZhiWeiZhi+1) %10;
                riTianGan = yiBanTianGan(ganZhiWeiZhi);
                diZhiWeiZhi = (diZhiWeiZhi-1) %12;
                riDiZhi = dizhiGongGong (diZhiWeiZhi);
                //如果是元旦则不往下运行，如果是非元旦数据，则往下执行
                if (month==1 && day==1){
                    return;
                }else{
                    //非元旦数据与其它方法一至
                    //通过元旦天支地支，来计算其它日子
                    //计算相差多少天
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = sdf.parse(runnian+"-1-1");
                    Date date2 = sdf.parse(runnian+"-"+month+"-"+day);
                    //计算相差多少天
                    int days=daysBetween(date1,date2);
                    days=days%60;
                    //天支计算
                    int tianzhi=0;
                    int dizhi=0;
                    tianzhi = (ganZhiWeiZhi+days)%10;
                    riTianGan=yiBanTianGan(tianzhi);
                    dizhi=diZhiWeiZhi+days;
                    dizhi=dizhi%12;
                    riDiZhi=dizhiGongGong(dizhi);
                    System.out.println("非元旦");
                }
            } else if (runNianCha==3){
                ganZhiWeiZhi = (ganZhiWeiZhi+6) %10;
                riTianGan = yiBanTianGan(ganZhiWeiZhi);
                diZhiWeiZhi = (diZhiWeiZhi+4) %12;
                riDiZhi = dizhiGongGong (diZhiWeiZhi);
                //如果是元旦则不往下运行，如果是非元旦数据，则往下执行
                if (month==1 && day==1){
                    return;
                }else{
                    //非元旦数据与其它方法一至
                    //通过元旦天支地支，来计算其它日子
                    //计算相差多少天
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    Date date1 = sdf.parse(runnian+"-1-1");
                    Date date2 = sdf.parse(runnian+"-"+month+"-"+day);
                    //计算相差多少天
                    int days=daysBetween(date1,date2);
                    days=days%60;
                    //天支计算
                    int tianzhi=0;
                    int dizhi=0;
                    tianzhi = (ganZhiWeiZhi+days)%10;
                    riTianGan=yiBanTianGan(tianzhi);
                    dizhi=diZhiWeiZhi+days;
                    dizhi=dizhi%12;
                    riDiZhi=dizhiGongGong(dizhi);
                    System.out.println("非元旦");
                }
            }

        }else if (runnian>2000 && runnian<=2100){
            yushu = year%100;
            yushu = (yushu+100)/4;
                //由此得出润年天干地支位置ganZhiWeiZhi,diZhiWeiZhi
                getGongRitianDiZhi(yushu,runnian,1,1);
                //润年差为1则天克：x=(x+6)%10 if x==0 x=10,地冲:x=(x+6)%12 if x==0x=12
                if (runNianCha==1){
                    //算出元旦天支地支
                    ganZhiWeiZhi = (ganZhiWeiZhi+6) %10;
                    riTianGan = yiBanTianGan(ganZhiWeiZhi);
                    diZhiWeiZhi = (diZhiWeiZhi+6) %12;
                    riDiZhi = dizhiGongGong (diZhiWeiZhi);
                    //如果是元旦则不往下运行，如果是非元旦数据，则往下执行
                    if (month==1 && day==1){
                        return;
                    }else{
                        //非元旦数据与其它方法一至
                        //通过元旦天支地支，来计算其它日子
                        //计算相差多少天
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1 = sdf.parse(runnian+"-1-1");
                        Date date2 = sdf.parse(runnian+"-"+month+"-"+day);
                        //计算相差多少天
                        int days=daysBetween(date1,date2);
                        days=days%60;
                        //天支计算
                        int tianzhi=0;
                        int dizhi=0;
                        tianzhi = (ganZhiWeiZhi+days)%10;
                        riTianGan=yiBanTianGan(tianzhi);
                        dizhi=diZhiWeiZhi+days;
                        dizhi=dizhi%12;
                        riDiZhi=dizhiGongGong(dizhi);
                        System.out.println("非元旦");
                    }

                } else if (runNianCha==2){
                    ganZhiWeiZhi = (ganZhiWeiZhi+1) %10;
                    riTianGan = yiBanTianGan(ganZhiWeiZhi);
                    diZhiWeiZhi = (diZhiWeiZhi-1) %12;
                    riDiZhi = dizhiGongGong (diZhiWeiZhi);
                    //如果是元旦则不往下运行，如果是非元旦数据，则往下执行
                    if (month==1 && day==1){
                        return;
                    }else{
                        //非元旦数据与其它方法一至
                        //通过元旦天支地支，来计算其它日子
                        //计算相差多少天
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1 = sdf.parse(runnian+"-1-1");
                        Date date2 = sdf.parse(runnian+"-"+month+"-"+day);
                        //计算相差多少天
                        int days=daysBetween(date1,date2);
                        days=days%60;
                        //天支计算
                        int tianzhi=0;
                        int dizhi=0;
                        tianzhi = (ganZhiWeiZhi+days)%10;
                        riTianGan=yiBanTianGan(tianzhi);
                        dizhi=diZhiWeiZhi+days;
                        dizhi=dizhi%12;
                        riDiZhi=dizhiGongGong(dizhi);
                        System.out.println("非元旦");
                    }
                } else if (runNianCha==3){
                    ganZhiWeiZhi = (ganZhiWeiZhi+6) %10;
                    riTianGan = yiBanTianGan(ganZhiWeiZhi);
                    diZhiWeiZhi = (diZhiWeiZhi+4) %12;
                    riDiZhi = dizhiGongGong (diZhiWeiZhi);
                    //如果是元旦则不往下运行，如果是非元旦数据，则往下执行
                    if (month==1 && day==1){
                        return;
                    }else{
                        //非元旦数据与其它方法一至
                        //通过元旦天支地支，来计算其它日子
                        //计算相差多少天
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        Date date1 = sdf.parse(runnian+"-1-1");
                        Date date2 = sdf.parse(runnian+"-"+month+"-"+day);
                        //计算相差多少天
                        int days=daysBetween(date1,date2);
                        days=days%60;
                        //天支计算
                        int tianzhi=0;
                        int dizhi=0;
                        tianzhi = (ganZhiWeiZhi+days)%10;
                        riTianGan=yiBanTianGan(tianzhi);
                        dizhi=diZhiWeiZhi+days;
                        dizhi=dizhi%12;
                        riDiZhi=dizhiGongGong(dizhi);
                        System.out.println("非元旦");
                    }


            }

        }else{
            System.out.println("年份有问题："+runnian);
        }
    }
    private static void getGongRitianDiZhi(int yushu,int year,int month,int day) throws ParseException {
        //元旦天干地支
        if (month==1 && day==1) {
            riTianGan=yiBanTianGan(yushu);
            riDiZhi = runNianRiDiZhi(yushu);
        }else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdf.parse(year+"-1-1");
            Date date2 = sdf.parse(year+"-"+month+"-"+day);
            //计算相差多少天
            int days=daysBetween(date1,date2);
            days=days%60;
            //天支计算
            int tianzhi=0;
            int dizhi=0;
            tianzhi = yushu+days;
            riTianGan=yiBanTianGan(tianzhi);
            //地支计算
            if (yushu == 0) {
                yushu = 10;
            }
            yushu=yushu%4;
            switch (yushu) {
                case 0:
                    yushu = 8;
                    break;
                case 1:
                    yushu = 5;
                    break;
                case 2:
                    yushu = 2;
                    break;
                case 3:
                    yushu = 11;
                    break;
            }
            dizhi=yushu+days;
            dizhi=dizhi%12;
            riDiZhi=dizhiGongGong(dizhi);
        }

    }
    private static String runNianRiDiZhi(int yushu) {
        String monthDi="";
        //yushu = yushu % 10;
        if (yushu == 0) {
            yushu = 10;
        }
        yushu=yushu%4;
        switch (yushu) {
            case 0:
                monthDi = "酉";
                diZhiWeiZhi=10;
                break;
            case 1:
                monthDi = "午";
                diZhiWeiZhi=7;
                break;
            case 2:
                monthDi = "卯";
                diZhiWeiZhi=4;
                break;
            case 3:
                monthDi = "子";
                diZhiWeiZhi=1;
                break;
        }
        return monthDi;

    }
    private static String yiBanTianGan(int yuShu) {
        String monthGan="";
        yuShu = yuShu % 10;
        if (yuShu == 0) {
            yuShu = 10;
        }
        ganZhiWeiZhi=yuShu;
        switch (yuShu) {
            case 1:
                monthGan = "甲";
                break;
            case 2:
                monthGan = "乙";
                break;
            case 3:
                monthGan = "丙";
                break;
            case 4:
                monthGan = "丁";
                break;
            case 5:
                monthGan = "戊";
                break;
            case 6:
                monthGan = "己";
                break;
            case 7:
                monthGan = "庚";
                break;
            case 8:
                monthGan = "辛";
                break;
            case 9:
                monthGan = "壬";
                break;
            case 10:
                monthGan = "癸";
                break;
        }
        return monthGan;
    }
    //两日期相差多少天
    public static int daysBetween(Date date1,Date date2){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date1);
        long time1 = cal.getTimeInMillis();
        cal.setTime(date2);
        long time2 = cal.getTimeInMillis();
        long between_days=(time2-time1)/(1000*3600*24);

        return Integer.parseInt(String.valueOf(between_days));
    }

    private static String getShiGanZhiDiZhi(int year,int month,int day,int xiaoshi) throws ParseException {
        //求出日干支
        riTianGanDizhi(year,month,day);
        xiaoshi =xiaoshi %12;
        if (xiaoshi==0){
            xiaoshi = 12;
        }
        String shiDiZhi="";
        int shiTianGanWeiZhi=0;
        //ganZhiWeiZhi*2+时支-2 就为是干支,地支固定
        String shiGanZhi="";
        shiTianGanWeiZhi=ganZhiWeiZhi*2 + xiaoshi-2;
        shiGanZhi=yiBanTianGan(shiTianGanWeiZhi);
        switch (xiaoshi) {
            case 1:
                shiDiZhi = "子";
                break;
            case 2:
                shiDiZhi = "丑";
                break;
            case 3:
                shiDiZhi = "寅";
                break;
            case 4:
                shiDiZhi = "卯";
                break;
            case 5:
                shiDiZhi = "辰";
                break;
            case 6:
                shiDiZhi = "巳";
                break;
            case 7:
                shiDiZhi = "午";
                break;
            case 8:
                shiDiZhi = "未";
                break;
            case 9:
                shiDiZhi = "申";
                break;
            case 10:
                shiDiZhi = "酉";
                break;
            case 11:
                shiDiZhi = "戌";
                break;
            case 12:
                shiDiZhi = "亥";
                break;
        }
        return shiGanZhi+shiDiZhi;



    }



    public static void main(String[] args) throws ParseException {
        int year=2020;
        int month=12;
        int day=17;
        int xiaoshi=9;
        System.out.println("年天干地支："+getYearTianGanDiZhi(year));
        System.out.println("月天干地支:" + getMonthGanZhiDiZhi(year, month));
        System.out.println("日天干："+riTianGanDizhi(year,month,day));
        System.out.println("时天地："+getShiGanZhiDiZhi(year,month,day,xiaoshi));
    }

}

