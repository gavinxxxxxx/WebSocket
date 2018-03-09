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
        NotificationManagerCompat.from(context)
                .notify(0x250, buildNotification(context, title, content, ticker, intent));
    }

    public static void cancel(Context context) {
        NotificationManagerCompat.from(context).cancel(0x250);
    }

    public static Notification.Builder newNotificationBuilder(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return new Notification.Builder(context);
        } else {
            createNotificationChannelIfNotExist(context);
            return new Notification.Builder(context, "default2");
        }
    }

    private static Notification buildNotification(Context cx, String tt, String cn, String tk, PendingIntent i) {
        Notification.Builder builder = newNotificationBuilder(cx)
                .setSmallIcon(R.drawable.vt_daily)
                .setContentTitle(tt)
                .setContentText(cn)
                .setTicker(tk)
                .setShowWhen(true)
                .setAutoCancel(true)
                .setContentIntent(i)
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setPriority(Notification.PRIORITY_HIGH);
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//            builder.setActions(new Notification.Action.Builder(null, "Action1", null)
//                    .build());
//        }
        return builder.build();
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static void createNotificationChannelIfNotExist(Context context) {
        NotificationManager notificationManager = ((NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE));
        if (notificationManager != null && notificationManager.getNotificationChannel("default2") == null) {
            NotificationChannel channel = new NotificationChannel("default2", "默认", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true); // 是否在桌面icon右上角展示小红点
            channel.setLightColor(Color.GREEN); // 小红点颜色
            channel.setShowBadge(true); // 是否在久按桌面图标时显示此渠道的通知
            notificationManager.createNotificationChannel(channel);
        }
    }
}
