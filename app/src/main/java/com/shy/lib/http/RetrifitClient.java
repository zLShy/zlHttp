package com.shy.lib.http;

import android.content.Context;
import android.os.Build;

import java.util.concurrent.TimeUnit;

import androidx.annotation.RequiresApi;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class RetrifitClient {

    private static RetrifitClient mInstance;
    private Context mContext;

    private RetrifitClient() {

    }

    public static RetrifitClient getInstance() {

        if (mInstance == null) {
            synchronized (RetrifitClient.class) {
                if (mInstance == null) {
                    mInstance = new RetrifitClient();
                }
            }
        }
        return mInstance;
    }

    public void init(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public ApiService initApiService() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(" http://117.51.149.167:9000/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(getOkHttpClient())
                .build();

        return retrofit.create(ApiService.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private OkHttpClient getOkHttpClient() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(10000L, TimeUnit.SECONDS)
                .writeTimeout(10000L, TimeUnit.SECONDS)
                .addInterceptor(new RequestToJsonInterceptor(mContext))
                .addInterceptor(new CacheRequestInterceptor(mContext))
                .addNetworkInterceptor(new CacheResponseInterceptor())
                .build();
        return okHttpClient;
    }
}
