package me.gavin.util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import me.gavin.app.main.MainActivity;
import me.gavin.base.App;
import me.gavin.base.RequestCode;
import me.gavin.im.ws.R;

/**
 * 通知助手
 *
 * @author gavin.xiong 2018/3/6
 */
public final class NotificationHelper {

    public static final String CHANNEL_RUNNING = "running";
    public static final String CHANNEL_CHAT_SINGLE = "chatSingle";
    public static final String CHANNEL_CHAT_GROUP = "chatGroup";
    public static final String CHANNEL_CHAT_OFFICIAL = "chatOfficial";
    public static final String CHANNEL_CHAT_SYSTEM = "chatSystem";
    public static final String CHANNEL_FRIEND_REQUEST = "friendRequest";

    public static Notification.Builder newNotificationBuilder(Context context, String channelId) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return new Notification.Builder(context);
        } else {
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null && manager.getNotificationChannel(channelId) == null) {
                createChannel(context, channelId);
            }
            return new Notification.Builder(context, channelId);
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private static void createChannel(Context context, String channelId) {
        NotificationChannel channel = null;
        switch (channelId) {
            case CHANNEL_RUNNING:
                channel = new NotificationChannel(channelId, "运行中", NotificationManager.IMPORTANCE_MIN);
                channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);//设置在锁屏界面上显示这条通知
                channel.enableLights(false);
                channel.setShowBadge(false);
                channel.enableVibration(false);
                break;
            case CHANNEL_CHAT_SINGLE:
            case CHANNEL_CHAT_GROUP:
            case CHANNEL_CHAT_OFFICIAL:
            case CHANNEL_CHAT_SYSTEM:
            case CHANNEL_FRIEND_REQUEST:
                channel = new NotificationChannel(channelId, "聊天消息", NotificationManager.IMPORTANCE_HIGH);
                channel.setBypassDnd(false);
                channel.enableLights(true);
                channel.setLightColor(Color.GREEN);
                channel.setShowBadge(true);
                channel.enableVibration(true);
                break;
            default:
                break;
        }
        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null && channel != null) {
            manager.createNotificationChannel(channel);
        }
    }

    public static Notification newBackgroundNotification(Context cx) {
        return newNotificationBuilder(cx, CHANNEL_RUNNING)
                .setSmallIcon(R.drawable.vt_daily)
                .setContentTitle(String.format("「%s」正在运行", cx.getString(R.string.app_name)))
                .setContentText("屠龙宝刀 点击就送")
                .setContentIntent(PendingIntent.getActivity(cx, RequestCode.DONT_CARE,
                        new Intent(cx, MainActivity.class)
                                .addCategory(Intent.CATEGORY_LAUNCHER)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED), // 关键的一步，设置启动模式
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .build();
    }

    public static void notify(Context context, String title, String content, String ticker, String avatar, PendingIntent intent) {
        NotificationManagerCompat.from(context)
                .notify(0x250, buildNotification(context, title, content, ticker, avatar, intent));
//        buildNotification2(context, title, content, ticker, avatar, intent);
    }

    public static void cancel(Context context) {
        NotificationManagerCompat.from(context).cancel(0x250);
    }

    private static Notification buildNotification(Context cx, String tt, String cn, String tk, String av, PendingIntent i) {
        Notification.Builder builder = newNotificationBuilder(cx, CHANNEL_CHAT_SINGLE)
                .setSmallIcon(R.drawable.vt_daily)
                // .setLargeIcon(getBitmap(av))
                .setContentTitle(tt)
                .setContentText(cn)
                .setTicker(tk)
                .setNumber(2)
                .setPriority(Notification.PRIORITY_HIGH)
                .setShowWhen(true)
                .setAutoCancel(true)
                .setContentIntent(i)
                // .setActions()
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        return builder.build();
    }

    private static void buildNotification2(Context cx, String tt, String cn, String tk, String av, PendingIntent i) {
        Notification.Builder builder = newNotificationBuilder(cx, CHANNEL_CHAT_SINGLE)
                .setSmallIcon(R.drawable.vt_daily)
                .setContentTitle(tt)
                .setContentText(cn)
                .setTicker(tk)
                .setNumber(2)
                .setPriority(Notification.PRIORITY_HIGH)
                .setShowWhen(true)
                .setAutoCancel(true)
                .setContentIntent(i)
                // .setActions()
                .setDefaults(NotificationCompat.DEFAULT_ALL);
        getBitmap2(cx, null, 0x250, builder, av);
    }

    /**
     * 获取网络图片
     *
     * @param url 图片网络地址
     * @return Bitmap 返回位图
     */
    private static Bitmap getBitmap(String url) {
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

    /**
     * 获取网络图片
     *
     * @param url 图片网络地址
     * @return Bitmap 返回位图
     */
    private static void getBitmap2(Context cx, String tag, int id, Notification.Builder builder, String url) {
        Glide.with(cx.getApplicationContext())
                .load(url)
                .asBitmap()
                .override(60, 60)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        builder.setLargeIcon(resource);
                        NotificationManagerCompat.from(cx).notify(tag, id, builder.build());
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        builder.setLargeIcon(BitmapFactory.decodeResource(cx.getResources(), R.mipmap.ic_launcher));
                        NotificationManagerCompat.from(cx).notify(tag, id, builder.build());
                    }
                });
    }
}
