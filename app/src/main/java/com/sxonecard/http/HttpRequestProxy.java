package com.sxonecard.http;

import com.sxonecard.http.bean.ShutDownBean;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * 获取Http数据代理
 */
public class HttpRequestProxy {


    private static final int DEFAULT_TIMEOUT = 5;

    private Retrofit retrofit;
    //http数据服务接口
    private HttpDataService dataService;

    //构造方法私有
    private HttpRequestProxy() {
        //手动创建一个OkHttpClient并设置超时时间
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        //添加header
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("User-Agent", "").build();
                return chain.proceed(request);
            }
        }).addInterceptor(logging)
                .connectTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(DEFAULT_TIMEOUT, TimeUnit.SECONDS);
//        GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(OrderInfo.class, new OrderInfoAdapter());
        retrofit = new Retrofit.Builder()
                .client(builder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .baseUrl(Constants.BASEURL)
                .build();

        dataService = retrofit.create(HttpDataService.class);
    }

    //获取单例
    public static HttpRequestProxy getInstance() {
        return SingletonHolder.INSTANCE;
    }

    private <T> void toSubscribe(Observable<T> o, Subscriber<T> s) {
        o.subscribeOn(Schedulers.newThread())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s);
    }
    //在访问HttpMethods时创建单例
    private static class SingletonHolder {
        private static final HttpRequestProxy INSTANCE = new HttpRequestProxy();
    }

//    public void userLogin(Subscriber<LoginResult> subscriber, Map<String, String> param) {
//        Observable observable = dataService.login(param)
//                .map(new HttpResultFunc<LoginResult>());
//        toSubscribe(observable, subscriber);
//    }
    public void shutDownDevice(Subscriber<ShutDownBean> subscriber, String imeiId) {
        Observable observable = dataService.shutDown(imeiId).map(new HttpResultFunc<ShutDownBean>());
        toSubscribe(observable, subscriber);
    }



    /**
     * 用来统一处理Http的resultCode,并将HttpResult的Data部分剥离出来返回给subscriber
     *
     * @param <T> Subscriber真正需要的数据类型，也就是Data部分的数据类型
     */
    private class HttpResultFunc<T> implements Func1<HttpResult<T>, T> {

        @Override
        public T call(HttpResult<T> httpResult) {
            try {
                String res = URLDecoder.decode(httpResult.getDes(), "UTF-8");
                httpResult.setDes(res);
            } catch (Exception e) {

            }
            if (httpResult.getRet() < 0) {
                throw new HttpDataException(httpResult.getRet(), httpResult.getDes());
            }
            return httpResult.getData();
        }
    }

    private class GetDataFunc<T> implements Func1<T, T> {

        @Override
        public T call(T t) {
            return t;
        }
    }
}
