package com.example.demo.http;

import com.example.demo.common.utils.NormalClient;
import com.example.demo.common.utils.RetrofitUtils;
import com.example.demo.interceptor.UrlInterceptor;
import okhttp3.Interceptor;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Retrofit;

import java.util.ArrayList;
import java.util.List;

public class RedisController {

    private static final String BASE_URL = "http://127.0.0.1:8091/";
    private static Retrofit apis;

    @Before
    public void before() throws Exception {
        List<Interceptor> list = new ArrayList<>();
        list.add(new UrlInterceptor("/api"));
        apis = RetrofitUtils.getRetrofit(BASE_URL, list);
    }

    /**
     * 获取所有的配置文件
     */
    @Test
    public void getAllConfigs() {
        NormalClient client = apis.create(NormalClient.class);
        Call<Object> call = client.get("/testRedisSet");
        Object o = RetrofitUtils.checkAndGetData(call);
        System.out.println(o);
    }
}
