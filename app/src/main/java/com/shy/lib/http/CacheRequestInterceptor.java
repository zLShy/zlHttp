package com.shy.lib.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class CacheRequestInterceptor implements Interceptor {

    private Context mContext;

    public CacheRequestInterceptor(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request request = chain.request();
        if (!LocalNetWorkUtils.isNetConnected(mContext)) {
            request = new Request.Builder()
                    .cacheControl(CacheControl.FORCE_CACHE)
                    .build();
        }
        SharedPreferences preferences = mContext.getSharedPreferences("share_data",Context.MODE_PRIVATE);
        Log.e("TAG","TOKEN"+preferences.getString("token",null));
        String token = preferences.getString("token",null);
        if (TextUtils.isEmpty(token)) {
            request = request.newBuilder()
                    .addHeader("Content-Type","application/json;charset=UTF-8")
                    .build();
        }else {
            request = request.newBuilder()
                    .addHeader("bearer",preferences.getString("token",null))
                    .addHeader("Content-Type","application/json;charset=utf-8")
                    .build();
        }
        
        return chain.proceed(request);
    }
}
