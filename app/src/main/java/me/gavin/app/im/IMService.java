package me.gavin.app.im;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.util.Random;

import io.reactivex.disposables.CompositeDisposable;
import me.gavin.app.message.Message;
import me.gavin.base.App;
import me.gavin.base.RxBus;
import me.gavin.util.L;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * 消息服务 - 收发消息
 *
 * @author gavin.xiong 2018/2/28
 */
public class IMService extends Service {

    private CompositeDisposable mCompositeDisposable;

    WebSocket mWebSocket;

    @Override
    public void onCreate() {
        super.onCreate();
        mCompositeDisposable = new CompositeDisposable();
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
        mWebSocket.send("END");
        mWebSocket.close(1000, "close by me");
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            startService(new Intent(this, IMService.class));
        }
    }

    private void subscribe() {
        RxBus.get().toObservable(SendMsgEvent.class)
                .map(event -> event.message)
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(msg -> {
                    boolean result = mWebSocket.send(createMessageRe(msg));
                    L.e(result + " - " + msg);

                    RxBus.get().post(new ReceiveMsgEvent(getRe(msg)));
                    L.e(createMessageRe(msg));
                });
    }

    private void createWebSocket() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        Request request = new Request.Builder()
                .url("ws://m.yy-happy.com/yy-app-web/websocket/socketServer.do")
                .header("A-SID", App.getUser().getToken())
                .build();
        mWebSocket = client.newWebSocket(request, new MyWebSocketListener());
    }

    private class MyWebSocketListener extends WebSocketListener {
        @Override
        public void onOpen(WebSocket webSocket, Response response) {
            L.e("onOpen - " + response);
        }

        @Override
        public void onMessage(WebSocket webSocket, String text) {
            L.e("onMessage - " + text);
            RxBus.get().post(new ReceiveMsgEvent(getRe(text)));
        }

        @Override
        public void onMessage(WebSocket webSocket, ByteString bytes) {
            L.e("onMessageB - " + bytes);
        }

        @Override
        public void onClosed(WebSocket webSocket, int code, String reason) {
            L.e("onClosed - " + reason);
            createWebSocket();
        }

        @Override
        public void onFailure(WebSocket webSocket, Throwable t, Response response) {
            L.e("onFailure - " + t.getMessage());
            t.printStackTrace();
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
        t.setContent("Re:" + content);
        t.setTime(time);
        t.setSender(sender);
        t.setChatId(chatId);
        String json = new Gson().toJson(t);

        TTT ttt = new TTT();
        ttt.setType("MSG");
        ttt.setContent(json);
        ttt.setFrom(sender);
        ttt.setTo(chatId);
        return new Gson().toJson(ttt);
    }
}
