package com.sxonecard.ui;

/**
 * Created by pc on 2017-10-24.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.Window;
import android.view.WindowManager;

import com.sxonecard.R;

/**
 * Created by HUPENG on 2016/9/21.
 */
public class WelcomeActivity extends FragmentActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //隐藏标题栏以及状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        /**标题是属于View的，所以窗口所有的修饰部分被隐藏后标题依然有效,需要去掉标题**/
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.welcome);

        try {
            //使用Handler实现延时跳转
            new Handler(new Handler.Callback() {
                @Override
                public boolean handleMessage(Message msg) {
                    // 页面跳转
                    //startActivity(new Intent(getApplicationContext(), HeSuanActivity.class));
                    startActivity(new Intent(getApplicationContext(), PreparedActivity.class));
                    return false;
                }
            }).sendEmptyMessageDelayed(0, 3 * 1000);//表示任务延时三秒执行
        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }


}