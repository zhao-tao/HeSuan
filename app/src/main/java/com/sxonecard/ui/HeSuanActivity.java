package com.sxonecard.ui;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.sxonecard.CardApplication;
import com.sxonecard.R;
import com.sxonecard.base.BaseFragment;
import com.sxonecard.base.RxBus;

import java.lang.ref.WeakReference;

import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;


/**
 * Created by HeQiang on 2017/4/22.
 */

public class HeSuanActivity extends FragmentActivity {
    private static String TAG = "HeSuanActivity";
    final Handler navHandler = new NavigationHandler(this);

    private Observable<String> testConnObservable;
    private Observable<String> checkModuleObservable;
    private int last_action;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        ButterKnife.bind(this);
        initView();
        registerBus();
        initDevice();
    }

    //初始化设备，开启测试连接
    private void initDevice() {
        //发送测试链接.
//        SerialPortUtil.getInstance().testConn();
    }

    private void registerBus() {
        testConnObservable = RxBus.get().register("testConn", String.class);
        testConnObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String testConnStr) {
                if ("0".equals(testConnStr.trim())) {
                    //测试链接故障.
                    Message msg = new Message();
                    msg.what = 404;
                    msg.obj = getApplicationContext().getText(R.string.conn_err);
                    navHandler.sendMessage(msg);
                } else {
                    CardApplication.getInstance().setDeviceSuccess(true);
                    //navHandler.sendEmptyMessage(0);
//                    Intent intent = new Intent();
//                    intent.setClass(HeSuanActivity.this, LineFragment.class);
//                    HeSuanActivity.this.startActivity(intent);
                }
            }
        });

        checkModuleObservable = RxBus.get().register("checkModule", String.class);
        checkModuleObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String status) {
                if (!"1".equals(status.trim())) {
                    //硬件故障,跳转至维护页面.
                    Message msg = new Message();
                    msg.what = 405;
                    msg.obj = getApplicationContext().getText(R.string.device_err);
                    navHandler.sendMessage(msg);
                } else {
                    //硬件检测正常,跳转至检测画面.
                    navHandler.sendEmptyMessage(0);
                }
            }
        });
    }

    /*private boolean android_update(SetBean set) {
        try {
            PackageManager pm = getPackageManager();
            PackageInfo pi = pm.getPackageInfo(getPackageName(), 0);
            if (pi.versionCode < set.getAndroidVersion()) {
                Toast.makeText(this, "正在更新中...", Toast.LENGTH_LONG).show();
                DownLoadFile dlf = new DownLoadFile(this.getApplicationContext());
                dlf.downFiletoDecive(set.getAndroid());
                return true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }*/

    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
        RxBus.get().unregister("testConn", testConnObservable);
        RxBus.get().unregister("checkModule", checkModuleObservable);
    }

    public void initView() {
        LineFragment f = new LineFragment();
        f.setNavHandle(navHandler);
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_action, f);
        fragmentTransaction.commit();
    }

    /**
     * 切换页面状态（连接错误，设备错误等）
     * @param index
     * @param obj
     */
    public void changeAction(int index, Object obj) {
        //前后页面相同，不切换
        if (last_action == index)
            return;
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        BaseFragment fragment = null;
        last_action = index;
        switch (index) {
            case 0:
                fragment = new LineFragment();
                break;
            case 404:
                fragment = new ConnException();
                break;
            case 405:
                fragment = new DeviceException();
                break;
            default:
                fragment = new LineFragment();
        }
        fragment.setNavHandle(navHandler);
        if (obj != null) {
            Bundle bundle = new Bundle();
            bundle.putString("msg", String.valueOf(obj));
            fragment.setArguments(bundle);
        }
        fragmentTransaction.replace(R.id.fragment_action, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    //禁用返回键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return true;
    }

    private static class NavigationHandler extends Handler {
        private final WeakReference<HeSuanActivity> activityWeakReference;

        public NavigationHandler(HeSuanActivity activity) {
            this.activityWeakReference = new WeakReference<>(activity);
        }

        public void handleMessage(Message msg) {
            HeSuanActivity activity = activityWeakReference.get();
            if (activity != null) {
                activity.changeAction(msg.what, msg.obj);
            }
        }
    }
}
