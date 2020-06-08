package com.shy.lib.http;

import android.content.Context;

import android.util.Log;

import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
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
            FormBody.Builder builder = new FormBody.Builder(Charset.forName("UTF-8"));

            FormBody body = (FormBody) request.body();
            //将以前的参数添加
            JSONObject jsonObject = new JSONObject();
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("name","123");
//                jsonObject.put("zl","---");
//                JSONObject jsonObject1 = new JSONObject();
//                jsonObject1.put("code","123");
//                jsonObject1.put("type",2);
//                jsonObject.put("obj",jsonObject1);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            for (int i = 0; i < body.size(); i++) {
                builder.add(body.encodedName(i), body.encodedValue(i));
                try {
                    if (URLDecoder.decode(body.encodedName(i),"UTF-8").equals("smsCodeInfo")) {
                        JSONObject smsCodeJson = new JSONObject();
                        JSONObject sourceJson = new JSONObject(URLDecoder.decode(body.encodedValue(i),"UTF-8"));
                        smsCodeJson.put("code",sourceJson.getString("code"));
                        smsCodeJson.put("phone",sourceJson.getString("phone"));
                        smsCodeJson.put("modular",sourceJson.getInt("modular"));
                        Log.e("TAG",sourceJson.getString("code")+"="+sourceJson.getInt("modular"));
                        jsonObject.put("smsCodeInfo",smsCodeJson);
                    }else {
                        jsonObject.put(URLDecoder.decode(body.encodedName(i),"UTF-8"),URLDecoder.decode(body.encodedValue(i),"UTF-8"));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
//
            }
            String JSON = jsonObject.toString();
            Log.e("TAG","--->"+JSON);
            RequestBody newRequestBody = RequestBody.create(JSON,MediaType.parse("application/json;charset=utf-8"));

            request = request.newBuilder()
                    .post(newRequestBody)
                    .build();
//            return chain.proceed(request);
        }


        return chain.proceed(request);
    }
}
