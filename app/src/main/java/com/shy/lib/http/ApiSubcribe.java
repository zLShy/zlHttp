package com.shy.lib.http;


import android.os.Build;

import java.util.Map;

import androidx.annotation.RequiresApi;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;


public class ApiSubcribe {

    private static ApiSubcribe mInstance;

    private ApiSubcribe() {
    }

    public static ApiSubcribe getInstance() {
        if (mInstance == null) {
            synchronized (ApiSubcribe.class) {
                if (mInstance == null) {
                    mInstance = new ApiSubcribe();
                }
            }
        }

        return mInstance;
    }

    private void Subcribe(Observer<Response<ResponseBody>> mObserver, Observable<Response<ResponseBody>> mObserverable) {

        mObserverable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(mObserver);

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    public void GetMethod(String url, Map<String, Object> parems, RealObserver observer) {
        Subcribe(observer, RetrifitClient.getInstance().initApiService().GetMethod(url, parems));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void PostMethod(String url, Map<String, Object> parems, RealObserver observer) {
        Subcribe(observer, RetrifitClient.getInstance().initApiService().PostMethod(url, parems));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void PostMethod(String url, RealObserver observer) {
        Subcribe(observer, RetrifitClient.getInstance().initApiService().PostMethod(url));
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void UpLoadMethod(String url, Map<String, RequestBody> parems, RealObserver observer) {
        Subcribe(observer, RetrifitClient.getInstance().initApiService().upload(url, parems));
    }

}
