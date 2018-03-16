package me.gavin.app.daemon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class ScreenBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.d("KeepAppAlive", "SreenLockReceiver-->监听到系统广播：" + action);
//        if (mStateReceiverListener == null) {
//            return;
//        }
//        if (Intent.ACTION_SCREEN_ON.equals(action)) {         // 开屏
//            mStateReceiverListener.onSreenOn();
//        } else if (Intent.ACTION_SCREEN_OFF.equals(action)) {  // 锁屏
//            mStateReceiverListener.onSreenOff();
//        } else if (Intent.ACTION_USER_PRESENT.equals(action)) { // 解锁
//            mStateReceiverListener.onUserPresent();
//        }
    }
}