package com.sxonecard;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.sxonecard.base.CrashHandler;
import com.sxonecard.http.bean.FunctionData;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by HeQiang on 2017/4/22.
 */

public class CardApplication extends Application {
    public static int index = 0;
    public static List<List<Integer>> yAxisValues = new ArrayList<List<Integer>>();
    public static List<FunctionData> shiGuanData = new ArrayList<FunctionData>(16);

    private int timeCount;
    public static String status;
    /**
     * 荧光阈值.
     */
    public static int limitLineValue = 1000;

    public static  List<FunctionData> fd_list = new ArrayList<FunctionData>();

    public static String getStatus() {
        return status;
    }

    public static void setStatus(String status) {
        CardApplication.status = status;
    }

    public static float heSuanTemperature;
    private static CardApplication instance;
    public static CardApplication getInstance() {
        return instance;
    }

    private boolean isDeviceSuccess = false;

    //设备是否正常
    private AtomicBoolean isDeviceOn = new AtomicBoolean(false);
    @Override
    public void onCreate() {
        super.onCreate();
        initImageLoader();
        instance = this;
//        IMEI = DateUtil.getIMEI(this.getApplicationContext());
//        Toast.makeText(this.getApplicationContext(), "设备IMEI:" + IMEI,
//                Toast.LENGTH_SHORT).show();
    }

    public void setDevice(boolean status){
        isDeviceOn.set(status);
    }

    public boolean isDeviceOn(){
        boolean status = isDeviceOn.get();
        isDeviceOn.set(false);
        return status;
    }

    public void initImageLoader() {
        DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .considerExifParams(true)
                .cacheOnDisk(true)
                .displayer(new SimpleBitmapDisplayer())
//                .showImageOnFail(R.mipmap.img_null)
                .build();
        ImageLoaderConfiguration configuration = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .threadPoolSize(3)
                .defaultDisplayImageOptions(displayImageOptions)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.FIFO)
                .imageDownloader(new BaseImageDownloader(getApplicationContext(),
                        5 * 1000, 10 * 1000))
                .build();
        ImageLoader.getInstance().init(configuration);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

    public int getTimeCount() {
        return timeCount;
    }

    public void setTimeCount(int timeCount) {
        this.timeCount = timeCount;
    }

    public void incTimeCount(){
        timeCount++;
    }

    public boolean isDeviceSuccess() {
        return isDeviceSuccess;
    }

    public void setDeviceSuccess(boolean deviceSuccess) {
        isDeviceSuccess = deviceSuccess;
    }

}
