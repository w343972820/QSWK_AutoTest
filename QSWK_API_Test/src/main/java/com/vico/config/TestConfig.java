package com.vico.config;

import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.ibatis.session.SqlSession;

public class TestConfig {
    public static String selectBannerListUrl;
    public static String selectNoticeManageListUrl;
    public static String LoginUrl;
    public static String Register;
    public static SqlSession sqlSession;
    public static HttpClient client;
    public static CookieStore store;

}
