package com.shy.lib.http;

import android.content.Context;

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
        return chain.proceed(request);
    }
}
