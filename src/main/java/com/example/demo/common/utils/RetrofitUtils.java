package com.example.demo.common.utils;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.java8.Java8CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RetrofitUtils {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetrofitUtils.class);

    private static final int BEGIN = "https://".length();

    public static String getHttp(String url) {
        return url.substring(0, url.indexOf("/", BEGIN));
    }

    public static String getHttpDomain(String url) {
        return url.substring(0, url.indexOf("/", BEGIN));
    }

    public static Retrofit getRetrofit(String baseUrl) {
        return getRetrofit(baseUrl, 60, JacksonConverterFactory.create(), null);
    }

    public static Retrofit getStringConverterRetrofit(String baseUrl) {
        return getRetrofit(baseUrl, ScalarsConverterFactory.create());
    }

    public static Retrofit getRetrofit(String baseUrl, retrofit2.Converter.Factory converterFactory) {
        return getRetrofit(baseUrl, 60, converterFactory, null);
    }

    public static Retrofit getRetrofit(String baseUrl, int timeOut) {
        return getRetrofit(baseUrl, timeOut, JacksonConverterFactory.create(), null);
    }

    public static Retrofit getRetrofit(String baseUrl, List<Interceptor> interceptors) {
        return getRetrofit(baseUrl, 60, JacksonConverterFactory.create(), interceptors);
    }

    public static Retrofit getStringConverterRetrofit(String baseUrl, List<Interceptor> interceptors) {
        return getRetrofit(baseUrl, ScalarsConverterFactory.create(), interceptors);
    }

    public static Retrofit getRetrofit(String baseUrl, retrofit2.Converter.Factory converterFactory, List<Interceptor> interceptors) {
        return getRetrofit(baseUrl, 60, converterFactory, interceptors);
    }

    public static Retrofit getRetrofit(String baseUrl, int timeOut, List<Interceptor> interceptors) {
        return getRetrofit(baseUrl, timeOut, JacksonConverterFactory.create(), interceptors);
    }

    public static Retrofit getRetrofit(String baseUrl, int timeOut, retrofit2.Converter.Factory converterFactory, List<Interceptor> interceptors) {
        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder().
                connectTimeout(timeOut, TimeUnit.SECONDS).
                readTimeout(timeOut, TimeUnit.SECONDS).
                writeTimeout(timeOut, TimeUnit.SECONDS);

        if (interceptors!= null && !interceptors.isEmpty()) {
            okHttpClientBuilder.interceptors().addAll(interceptors);
        }

        OkHttpClient client = okHttpClientBuilder.build();
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(baseUrl) // 设置网络请求的Url地址
                .client(client)
                // 支持java8平台
                .addCallAdapterFactory(Java8CallAdapterFactory.create())
                // 设置json数据解析器
                .addConverterFactory(converterFactory);
        return builder.build();
    }

    public static <T> T checkAndGetData(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if (!response.isSuccessful()) {
                ResponseBody errorBody = response.errorBody();
                if (errorBody != null) {
                    throw new RuntimeException("http call failed, response: " + response.toString() + ", errorBody: " + errorBody.string());
                } else {
                    throw new RuntimeException("http call failed, response: " + response.toString());
                }
            }
            return response.body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T checkAndGetDataFromRest(Call<RestResult<T>> call) {
        try {
            Response<RestResult<T>> response = call.execute();
            if (!response.isSuccessful()) {
                ResponseBody errorBody = response.errorBody();
                if (errorBody != null) {
                    throw new RuntimeException("http call failed, response: " + response.toString() + ", errorBody: " + errorBody.string());
                } else {
                    throw new RuntimeException("http call failed, response: " + response.toString());
                }
            }
            return response.body().getData();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getData(Call<T> call) {
        Response<T> response;
        try {
            response = call.execute();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return response.body();
    }


    public static <T> T checkDataFromRest(Call<RestResult<T>> call) {
        Response<RestResult<T>> response;
        try {
            response = call.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!response.isSuccessful()) {
            throw new RuntimeException("http call failed, response: " + response);
        }

        RestResult<T> rsl = response.body();
        if (rsl != null && RestResult.RESULT_SUCCESS.equals(rsl.getCode())) {
            return rsl.getData();
        }

        throw new RuntimeException("checkDataFromRest execute failed, response: " + response + ", result: " + rsl);
    }

    public static <T> T getDataFromRest(Call<RestResult<T>> call) {
        Response<RestResult<T>> response;
        try {
            response = call.execute();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (!response.isSuccessful()) {
            LOGGER.warn("http call failed, response: " + response);
            return null;
        }
        RestResult<T> rsl = response.body();
        if (rsl != null && RestResult.RESULT_SUCCESS.equals(rsl.getCode())) {
            return rsl.getData();
        }

        LOGGER.warn("getDataFromRest execute failed, response: " + response + ", result: " + rsl);

        return null;
    }



}
