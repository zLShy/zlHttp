package com.shy.lib.http;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ZhangL on 2020-04-07. 拦截传输数据转换为JSON
 */
public class RequestToJsonInterceptor implements Interceptor {

    Context mContext;

    public RequestToJsonInterceptor(Context context) {
        this.mContext = context;
    }

    @NotNull
    @Override
    public Response intercept(@NotNull Chain chain) throws IOException {

        Request request = chain.request();

        Map<String,Object> params = new HashMap<>();

        if (request.body() instanceof FormBody) {
            // 构造新的请求表单
            FormBody.Builder builder = new FormBody.Builder();

            FormBody body = (FormBody) request.body();
            //将以前的参数添加
            for (int i = 0; i < body.size(); i++) {
                builder.add(body.encodedName(i), body.encodedValue(i));
                params.put(body.encodedName(i), body.encodedValue(i));
            }
            Gson gson = new Gson();
            String JSON = gson.toJson(params);
            Log.e("TAG","--->"+JSON);
            RequestBody newRequestBody = RequestBody.create(JSON,MediaType.parse("application/json; charset=utf-8"));

            request = request.newBuilder()
                    .post(newRequestBody)
                    .build();
//            return chain.proceed(request);
        }


        return chain.proceed(request);
    }
}
