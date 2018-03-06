package me.gavin.app.im;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import javax.inject.Inject;

import dagger.Lazy;
import io.reactivex.disposables.CompositeDisposable;
import me.gavin.app.message.Message;
import me.gavin.base.App;
import me.gavin.base.Config;
import me.gavin.base.RxBus;
import me.gavin.inject.component.ApplicationComponent;
import me.gavin.service.base.DataLayer;
import me.gavin.util.L;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * 消息服务 - 收发消息
 *
 * @author gavin.xiong 2018/2/28
 */
public class IMService extends Service {

    public static final String TAG = "webSocket";

    @Inject
    protected Lazy<OkHttpClient> mOkHttpClient;
    @Inject
    protected Lazy<Gson> mGson;
    @Inject
    protected Lazy<DataLayer> mDataLayer;

    private CompositeDisposable mCompositeDisposable;

    private WebSocket mWebSocket;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        L.e("onCreate - " + System.currentTimeMillis() + " - " + this);
        ApplicationComponent.Instance.get().inject(this);
        mCompositeDisposable = new CompositeDisposable();
        createWebSocket();
        subscribe();

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            startForeground(0x250, new Notification.Builder(this).build());
        } else {
            createChannel();
            startForeground(0x250, new Notification.Builder(this, "default").build());
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createChannel() {
        NotificationChannel channel = new NotificationChannel("default",
                "默认", NotificationManager.IMPORTANCE_DEFAULT);
        channel.enableLights(true); //是否在桌面icon右上角展示小红点
        channel.setLightColor(Color.GREEN); //小红点颜色
        channel.setShowBadge(true); //是否在久按桌面图标时显示此渠道的通知
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.e("onStartCommand - " + System.currentTimeMillis() + " - " + this);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.e("onDestroy - " + System.currentTimeMillis() + " - " + this);
        dispose();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        L.e("onTaskRemoved - " + System.currentTimeMillis() + " - " + this);
        dispose();
    }

    private void dispose() {
        mCompositeDisposable.dispose();
        if (mWebSocket != null) {
            mWebSocket.send("END");
            mWebSocket.close(1000, "close by me");
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            startService(new Intent(App.get(), IMService.class));
        } else {
            startForegroundService(new Intent(App.get(), IMService.class));
        }
    }

    private void subscribe() {
        RxBus.get().toObservable(SendMsgEvent.class)
                .map(event -> event.message)
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(msg -> {
                    String content = mGson.get().toJson(msg);
                    TTT ttt = new TTT();
                    ttt.setType("MSG");
                    ttt.setContent(content);
                    ttt.setTo(msg.getChatId());
                    ttt.setFrom(msg.getSender());
                    String json = mGson.get().toJson(ttt);
                    boolean result = mWebSocket.send(json);
                    L.d(TAG, "onSend - " + result + " - " + json);
                });
    }

    private void createWebSocket() {
        if (App.getUser() == null || !App.getUser().isLogged()) {
            return;
        }
        Request request = new Request.Builder()
                .url(Config.WS_URL)
                .build();
        mWebSocket = mOkHttpClient.get()
                .newWebSocket(request, new MyWebSocketListener());
    }

    private class MyWebSocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            L.e(TAG, "onOpen - " + response);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            L.d(TAG, "onMessage - " + text);
            TTT ttt = mGson.get().fromJson(text, TTT.class);
            switch (ttt.getType()) {
                case "MSG":
                    Message msg = mGson.get().fromJson(ttt.getContent(), Message.class);
                    if (msg.getChatType() == Message.CHAT_TYPE_SINGLE
                            && msg.getSender() == msg.getChatId()) { // 自己发的单聊消息不接收
                        return;
                    }
                    msg.setChatId(msg.getSender());
                    mDataLayer.get().getMessageService().insert(msg);
                    RxBus.get().post(new ReceiveMsgEvent(msg));
                    break;
                case "ADD_FRIEND":
                    me.gavin.app.contact.Request req = mGson.get().fromJson(ttt.getContent(), me.gavin.app.contact.Request.class);
                    req.setUid(ttt.getFrom());
                    mDataLayer.get().getContactService().insetRequest(req);

                    long time = System.currentTimeMillis();
                    Message reqMsg = new Message();
                    reqMsg.setId(ttt.getFrom() + "" + time);
                    reqMsg.setContent(String.format("%s 请求加为好友", req.getName()));
                    reqMsg.setTime(time);
                    reqMsg.setSender(ttt.getFrom());
                    reqMsg.setChatType(Message.CHAT_TYPE_SYSTEM);
                    reqMsg.setChatId(Message.SYSTEM_CONTACT_REQUEST);
                    mDataLayer.get().getMessageService().insert(reqMsg);
                    break;
            }
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            L.e(TAG, "onClosed - " + code + " - " + reason);
            createWebSocket();
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            L.e(TAG, "onFailure - " + t);
            createWebSocket();
        }
    }
}
