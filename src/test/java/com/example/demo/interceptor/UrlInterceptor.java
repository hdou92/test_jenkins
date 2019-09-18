package com.example.demo.interceptor;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * FrontService设置token的拦截器
 */
public class UrlInterceptor implements Interceptor {
    private final String urlPrefix;

    public UrlInterceptor(String urlPrefix) {
        this.urlPrefix = urlPrefix == null || urlPrefix.isEmpty() ? urlPrefix
                : "/" + urlPrefix + "/" ;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        Request.Builder builder = request.newBuilder();

        if (urlPrefix != null && !urlPrefix.isEmpty()) {
//            builder.url(RetrofitUtils.combineUrlPrefix(urlPrefix, request.url()));
        }

        Request newRequest = builder.build();   // 新的请求
        return chain.proceed(newRequest);
    }
}
