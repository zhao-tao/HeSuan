package com.sxonecard.background;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.sxonecard.ui.HeSuanActivity;

/**
 * Created by HeQiang on 2017/5/13.
 */

public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String action_boot = "android.intent.action.BOOT_COMPLETED";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(action_boot)) {
            Intent ootStartIntent = new Intent(context, HeSuanActivity.class);
            ootStartIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(ootStartIntent);
        }
    }
}
