package com.sxonecard.http.heart;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;


/**
 * Created by Administrator on 2017-4-28.
 */
public class PhpHeart extends Thread {

    private Handler mChildHandler;

    private static PhpHeart instance;

    private Handler mMainHandler;

    private Context context;

    public void run() {
        Looper.prepare();
        setmChildHandler(new Handler() {
            @Override
            public void handleMessage(Message msg) {
                try {
                    switch (msg.what){
                        case 1: // 心跳
//                            HttpDataListener listener = new HttpDataListener(){
//                                @Override
//                                public void onNext(Object o) {
//                                    System.out.println("aaaaaaaaaa");
//                                }
//                                @Override
//                                public void onError(Context context, int code, String msg) {
//                                    System.out.println("bbbbbbbbbb");
//                                }
//                            };
//                            Map<String, String> params = new HashMap<String, String>();
//                            params.put("Act", "run");
//                            String imei = DateUtil.getIMEI(context);
//                            params.put("ImeiId", "021");
//                            params.put("Status", "0");
//                            params.put("Note", "");
//                            params.put("Time", DateUtil.getCurrentTime());
//                            HttpRequestProxy.getInstance().phphreat(new HttpDataSubscriber(listener,
//                                    context, false), params);
                            break;
                        case 2: // 。。
                            break;
                    }
                } catch (Exception e) {
                e.printStackTrace();
                }
            }
        });
        Looper.loop();
    }

    public static PhpHeart getInstance() {
        if(instance==null){
            instance=new PhpHeart();
        }
        return instance;
    }

    public static void setInstance(PhpHeart instance) {
        PhpHeart.instance = instance;
    }

    public Handler getmMainHandler() {
        return mMainHandler;
    }

    public void setmMainHandler(Handler mMainHandler) {
        this.mMainHandler = mMainHandler;
    }

    public Handler getmChildHandler() {
        return mChildHandler;
    }

    public void setmChildHandler(Handler mChildHandler) {
        this.mChildHandler = mChildHandler;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
