package com.cll.wenda.utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * @author chenliangliang
 * @date: 2017/11/25
 */
public class HttpUtil {

    private static final OkHttpClient client = new OkHttpClient();

    public static final String APPID="wx9f10d824ea5069ea";

    public static final String APPSECRET="62f33e49e33a3665b61856a76124e16a";


    public static String doGet(String url) throws IOException {

        String res = null;
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            res = response.body().string();
        }
        System.out.println(res);
        return res;
    }
}
