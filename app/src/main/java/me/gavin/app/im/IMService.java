package me.gavin.app.im;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Lazy;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;
import me.gavin.app.message.Message;
import me.gavin.base.App;
import me.gavin.base.Config;
import me.gavin.base.RxBus;
import me.gavin.inject.component.ApplicationComponent;
import me.gavin.service.base.DataLayer;
import me.gavin.util.L;
import me.gavin.util.NotificationHelper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;

/**
 * 消息服务 - 收发消息
 *
 * @author gavin.xiong 2018/2/28
 */
public class IMService extends Service {

    public static final String TAG = "WebSocket";

    @Named("WebSocket")
    @Inject
    protected Lazy<OkHttpClient> mOkHttpClient;
    @Inject
    protected Lazy<IWebSocketListener> mListener;
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
        // startForeground(-99, NotificationHelper.newNotificationBuilder(this).build());
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        L.e("onStartCommand - " + System.currentTimeMillis() + " - " + this);
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        if (mWebSocket != null) {
            mWebSocket.close(1000, "close by me");
        }
        mCompositeDisposable = new CompositeDisposable();
        createWebSocket();
        subscribe();
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.O ? START_STICKY : START_NOT_STICKY;
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
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        if (mWebSocket != null) {
            mWebSocket.close(1000, "close by me");
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            startService(new Intent(App.get(), IMService.class));
        } else {
            // startForegroundService(new Intent(App.get(), IMService.class));
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
        toObservable()
//                .compose(retryWhen())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(d -> {
                    mCompositeDisposable.add(d);
                    Request request = new Request.Builder().url(Config.WS_URL).build();
                    mWebSocket = mOkHttpClient.get().newWebSocket(request, mListener.get());
                })
                .subscribe(s -> {
                    L.e("onNext - " + s);
                    handleMessage(s);
                }, throwable -> {
                    L.e("onError - " + throwable);
                    throwable.printStackTrace();
                }, () -> {
                    L.e("onComplete - ");
                });
    }

    /**
     * 断线重连
     */
    public <T> ObservableTransformer<T, T> retryWhen() {
        return upstream -> upstream
                .retryWhen(throwableObservable -> throwableObservable
                        .delay(1, TimeUnit.SECONDS)
                        .map(t -> 0))
                .repeatWhen(objectObservable -> objectObservable
                        .delay(1, TimeUnit.SECONDS)
                        .map(o -> 0));
    }

    private Observable<String> toObservable() {
        L.e("toObservable - " + System.currentTimeMillis());
        return Observable.defer(() -> {
            return mListener.get().getObservable();
        });
    }

    public void handleMessage(String text) {
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

                mDataLayer.get().getContactService()
                        .getContact(msg.getSender())
                        .doOnSubscribe(mCompositeDisposable::add)
                        .subscribe(contact -> {
                            String name = contact.getNick() != null ? contact.getNick() : contact.getName();
                            NotificationHelper.notify(App.get(), name, msg.getContent(), msg.getContent(), null);
                        }, throwable -> {
                            NotificationHelper.notify(App.get(), String.valueOf(msg.getSender()), msg.getContent(), msg.getContent(), null);
                        });
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
}
