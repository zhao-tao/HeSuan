package com.sxonecard.util.vender;


import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.sxonecard.R;


public class clsSound {

    public static int swipe_fail;
    public static int load_fail;
    public static int startok;
    public static int swipe_ok;
    private static SoundPool soundPool = null;
    private static int currentid = 0;

    private static String path = null;


    public static void setPath(String path) {
        clsSound.path = path + "/";
    }

    public static void LoadSound(Context c) {
        if (soundPool == null) {
            soundPool = new SoundPool(10, AudioManager.STREAM_SYSTEM, 5);

//			//buy_paycash=soundPool.load(c, R.r,1);
//			device_err=soundPool.load(c,R.raw.device_err,1);
//			fail_to_getqrcode=soundPool.load(c,R.raw.fail_to_getqrcode,1);
//			fail_to_transfor=soundPool.load(c,R.raw.fail_to_transfor,1);
//			//fetch_code_tips=soundPool.load(c,R.raw.fetch_code_tips,1);
//			getqrcode=soundPool.load(c,R.raw.getqrcode,1);
//			net_err=soundPool.load(c,R.raw.net_err,1);
//			payok=soundPool.load(c,R.raw.payok,1);
//			plsscanf=soundPool.load(c,R.raw.plsscanf,1);
//			transforing=soundPool.load(c,R.raw.transforing,1);
//			transforok=soundPool.load(c,R.raw.transforok,1);
//
//			plsswipe=soundPool.load(c,R.raw.plsswipe,1);
//			plsputcash=soundPool.load(c,R.raw.plsputcash,1);

            load_fail = soundPool.load(c, R.raw.load_fail, 1);

            swipe_fail = soundPool.load(c, R.raw.swipe_fail, 1);//欢迎使用 福口冠 现 磨全自动鲜米机,请选择您想购买的金额
            startok = soundPool.load(c, R.raw.startok, 1);
            swipe_ok = soundPool.load(c, R.raw.swipe_ok, 1);
        }
    }

    public static void LoadSound() {
        if (path == null) {
            swipe_fail = soundPool.load(path + "swipe_fail", 1);//欢迎使用 福口冠 现 磨全自动鲜米机,请选择您想购买的金额
            startok = soundPool.load(path + "startok", 1);
            swipe_ok = soundPool.load(path + "swipe_ok", 1);
        }
    }

    public static void makeSound(int id) {
        soundPool.stop(currentid);
        currentid = soundPool.play(id, 1, 1, 0, 0, 1);
    }

}
