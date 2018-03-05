package me.gavin.app.im;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.Random;

import javax.inject.Inject;

import dagger.Lazy;
import io.reactivex.disposables.CompositeDisposable;
import me.gavin.app.message.Message;
import me.gavin.base.App;
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
    @Inject
    protected CompositeDisposable mCompositeDisposable;

    WebSocket mWebSocket;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationComponent.Instance.get().inject(this);
        createWebSocket();
        subscribe();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
        if (mWebSocket != null) {
            mWebSocket.send("END");
            mWebSocket.close(1000, "close by me");
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            startService(new Intent(this, IMService.class));
        } else {
            stopSelf();
        }
    }

    private void subscribe() {
        RxBus.get().toObservable(SendMsgEvent.class)
                .map(event -> event.message)
                // .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(msg -> {
                    L.d("onSend - " + msg.getContent());
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
        if (App.getUser() == null || TextUtils.isEmpty(App.getUser().getToken())) {
            return;
        }
        Request request = new Request.Builder()
                .url("ws://m.yy-happy.com/yy-app-web/websocket/socketServer.do")
                .header("A-SID", App.getUser().getToken())
                .build();
        mWebSocket = mOkHttpClient.get().newWebSocket(request, new MyWebSocketListener());
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

    private Message getRe(String content) {
        long time = System.currentTimeMillis();
        long sender = 10000L + new Random(time).nextInt(200);
        Message re = new Message();
        re.setId(sender + "" + time);
        re.setContent("他说" + content);
        re.setTime(time);
        re.setSender(sender);
        re.setChatId(sender);
        return re;
    }

    private String createMessageRe(String content) {
        long time = System.currentTimeMillis();
        long sender = App.getUser().getId();
//         long chatId = 10000L + new Random(time).nextInt(200);
        long chatId = sender;
        Message t = new Message();
        t.setId(sender + "" + time);
        t.setContent(content);
        t.setTime(time);
        t.setSender(sender);
        t.setChatId(chatId);
        String json = mGson.get().toJson(t);

        TTT ttt = new TTT();
        ttt.setType("MSG");
        ttt.setContent(json);
        ttt.setFrom(sender);
        ttt.setTo(chatId);
        return mGson.get().toJson(ttt);
    }
}
