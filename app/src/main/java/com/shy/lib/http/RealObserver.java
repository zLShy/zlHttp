package com.shy.lib.http;

import org.json.JSONObject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import okhttp3.ResponseBody;
import retrofit2.Response;

public abstract class RealObserver implements Observer<Response<ResponseBody>> {


    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(Response<ResponseBody> value) {

       int code =  value.code();
       if (code == 200) {
           try {
               JSONObject jsonObject = new JSONObject(value.body().string());
                int responseCode = jsonObject.getInt("code");
                if (responseCode == 1000) {
                    onSuccess(jsonObject.getString("data"));
                }else {
                    String msg = "";
                    switch (responseCode) {
                        case 1001:
                            msg = "失败";
                            break;
                        case 1002:
                            msg = "token过期";
                            break;
                        case 1003:
                            msg = "禁止访问";
                            break;
                            default:
                                msg = "未知错误";
                                break;
                    }
                    onFailer(responseCode,msg);
                }
           } catch (Exception e) {
               onFailer(code,e.getMessage());
           }

       }else {
//           try {
//               Log.e("TAG",value.errorBody().string());
//           } catch (IOException e) {
//               e.printStackTrace();
//           }
           onFailer(code,value.message());
       }

    }

    @Override
    public void onError(Throwable e) {

        onFailer(1001,e.getMessage());
    }

    @Override
    public void onComplete() {

    }

    protected abstract void onSuccess(String value);

    protected abstract void onFailer(int code,String msg);
}
