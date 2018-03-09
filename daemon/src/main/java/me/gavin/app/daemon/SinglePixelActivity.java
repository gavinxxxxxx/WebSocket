package me.gavin.app.daemon;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import static me.gavin.app.daemon.BuildConfig.TAG;

/**
 * 1 像素保活
 *
 * @author gavin.xiong 2018/3/9
 */
public class SinglePixelActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        L.d(TAG, "onCreate - 启动 1 像素保活");

        // 获得 Window 对象，设置其属性
        Window mWindow = getWindow();
        mWindow.setGravity(Gravity.START | Gravity.TOP);
        WindowManager.LayoutParams
                attrParams = mWindow.getAttributes();
        attrParams.x = 0;
        attrParams.y = 0;
        attrParams.height = 1;
        attrParams.width = 1;
        mWindow.setAttributes(attrParams);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        L.d(TAG, "onDestroy - 终止 1 像素保活");
        // TODO: 2018/3/9 此处需不需要重启应用 {@link https://mp.weixin.qq.com/s/d3scy-dC46NW9sz7wc3YLQ}
//        if(!SystemUtils.isAppAlive(this,Contants.PACKAGE_NAME)){
//            Intent intentAlive = new Intent(this, SportsActivity.class);
//            intentAlive.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            startActivity(intentAlive);
//            L.i(TAG,"SinglePixelActivity---->APP被干掉了，我要重启它");
//        }
    }
}
