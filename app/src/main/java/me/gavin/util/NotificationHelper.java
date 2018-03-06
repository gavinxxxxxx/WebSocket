package me.gavin.util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import me.gavin.im.ws.R;

/**
 * 通知助手
 *
 * @author gavin.xiong 2018/3/6
 */
public final class NotificationHelper {

    public static void notify(Context context, String title, String content, String ticker, PendingIntent intent) {
        NotificationManagerCompat.from(context).notify(1, newNotificationBuilder(context)
                .setSmallIcon(R.mipmap.ic_launcher, 100)
                .setContentTitle(title)
                .setContentText(content)
                .setTicker(ticker)
                .setAutoCancel(true)
                .setContentIntent(intent)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .build());
    }

    private static Notification.Builder newNotificationBuilder(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return new Notification.Builder(context);
        } else {
            createNotificationChannelIfNotExist(context);
            return new Notification.Builder(context, "default");
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static void createNotificationChannelIfNotExist(Context context) {
        NotificationManager notificationManager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
        if (notificationManager != null && notificationManager.getNotificationChannel("default") == null) {
            NotificationChannel channel = new NotificationChannel("default", "默认", NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); // 是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); // 小红点颜色
            channel.setShowBadge(true); // 是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(channel);
        }
    }
}
