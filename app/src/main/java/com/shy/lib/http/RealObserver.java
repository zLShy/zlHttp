package com.shy.lib.http;

import android.util.Log;

import org.json.JSONObject;

import java.nio.charset.Charset;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import retrofit2.Response;

public abstract class RealObserver implements Observer<Response<ResponseBody>> {


    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Response<ResponseBody> value) {

        int code = value.code();
        if (code == 200) {
            try {
                BufferedSource source = value.body().source();
                source.request(Long.MAX_VALUE); // Buffer the entire body.
                Buffer buffer = source.buffer();
                Charset UTF8 = Charset.forName("UTF-8");
                Log.d("REQUEST_JSON", buffer.clone().readString(UTF8));
                JSONObject jsonObject = new JSONObject(value.body().string());
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    onSuccess(jsonObject.getString("data"));
                } else {
                    onFailer(1001, jsonObject.getString("msg"));
                }

            } catch (Exception e) {
                onFailer(code, e.getMessage());
            }

        } else {
//           try {
//               Log.e("TAG",value.errorBody().string());
//           } catch (IOException e) {
//               e.printStackTrace();
//           }
            onFailer(code, value.message());
        }

    }

    @Override
    public void onError(Throwable e) {

        onFailer(1001, e.getMessage());
    }

    @Override
    public void onComplete() {

    }

    protected abstract void onSuccess(String value);

    protected abstract void onFailer(int code, String msg);
}
