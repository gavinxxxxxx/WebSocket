package me.gavin.util;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import me.gavin.base.App;
import me.gavin.im.ws.R;

/**
 * 通知助手
 *
 * @author gavin.xiong 2018/3/6
 */
public final class NotificationHelper {

    public static void notify(Context context, String title, String content, String ticker, String avatar, PendingIntent intent) {
        NotificationManagerCompat.from(context)
                .notify(0x250, buildNotification(context, title, content, ticker, avatar, intent));
    }

    public static void cancel(Context context) {
        NotificationManagerCompat.from(context).cancel(0x250);
    }

    private static Notification buildNotification(Context cx, String tt, String cn, String tk, String av, PendingIntent i) {
        Notification.Builder builder = new Notification.Builder(cx)
                .setSmallIcon(R.drawable.vt_daily)
                // .setLargeIcon(getBitmap(cx, av))
                .setContentTitle(tt)
                .setContentText(cn)
                .setTicker(tk)
                .setPriority(Notification.PRIORITY_HIGH)
                .setShowWhen(true)
                .setAutoCancel(true)
                .setContentIntent(i)
                // .setActions()
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return builder.build();
    }

    /**
     * 获取网络图片
     *
     * @param url 图片网络地址
     * @return Bitmap 返回位图
     */
    private static Bitmap getBitmap(Context context, String url) {
        try {
            return Glide.with(App.get())
                    .load(url)
                    .asBitmap()
                    .into(60, 60)
                    .get(2000, TimeUnit.MILLISECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return BitmapFactory.decodeResource(App.get().getResources(), R.mipmap.ic_launcher);
        }
    }
}
