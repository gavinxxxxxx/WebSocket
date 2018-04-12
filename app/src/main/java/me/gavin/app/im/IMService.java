package me.gavin.app.im;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Named;

import dagger.Lazy;
import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import me.gavin.app.im.event.ReceiveMsgEvent;
import me.gavin.app.im.event.SendMsgEvent;
import me.gavin.app.main.MainActivity;
import me.gavin.app.message.Message;
import me.gavin.base.App;
import me.gavin.base.BundleKey;
import me.gavin.base.Config;
import me.gavin.base.RxBus;
import me.gavin.base.RxTransformers;
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
        if (mCompositeDisposable != null) {
            mCompositeDisposable.dispose();
        }
        if (mWebSocket != null) {
            mWebSocket.close(1000, "close by me");
        }
        if (App.getUser() != null && App.getUser().isLogged()) {
            startService(new Intent(App.get(), IMService.class));
        }
    }

    private void subscribe() {
        RxBus.get().toObservable(SendMsgEvent.class)
                .map(event -> event.message)
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(msg -> {
                    String content = mGson.get().toJson(msg);
                    IMessage im = new IMessage();
                    im.setType("MSG");
                    im.setContent(content);
                    im.setTo(msg.getChatId());
                    im.setFrom(msg.getSender());
                    String json = mGson.get().toJson(im);
                    boolean result = mWebSocket.send(json);
                    L.d(TAG, "onSend - " + result + " - " + json);
                });
    }

    private void createWebSocket() {
        if (App.getUser() == null || !App.getUser().isLogged()) {
            return;
        }
        toObservable()
                .compose(RxTransformers.applySchedulers())
                .compose(retryAndRepeat())
                .doOnSubscribe(mCompositeDisposable::add)
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

    private Observable<String> toObservable() {
        L.e("toObservable - " + System.currentTimeMillis());
        return Observable.defer(() -> {
            L.e(String.format(Locale.getDefault(), "defer - %tT", new Date()));
            Request request = new Request.Builder().url(Config.WS_URL).build();
            mWebSocket = mOkHttpClient.get().newWebSocket(request, mListener.get());
            return mListener.get();
        });
    }

    /**
     * 断线重连
     * 1: 重连3次 指数退避
     * 2: 结束和出错3s重连
     */
    public <T> ObservableTransformer<T, T> retryAndRepeat() {
        return upstream -> upstream
                .retryWhen(throwableObservable -> throwableObservable
                        .flatMap(t -> Observable.timer(5, TimeUnit.SECONDS)));
//        return upstream -> upstream
//                .retryWhen(throwableObservable -> throwableObservable
//                        .zipWith(Observable.range(0, 3), (t, i) -> i) // 重试3次
//                        .flatMap(retryCount -> Observable.timer((long) Math.pow(3, retryCount), TimeUnit.SECONDS))) // 指数退避
//                .repeatWhen(objectObservable -> objectObservable
//                        .zipWith(Observable.range(0, 3), (o, i) -> i)
//                        .flatMap(retryCount -> Observable.timer((long) Math.pow(5, retryCount), TimeUnit.SECONDS))); // 指数退避
    }

    public void handleMessage(String text) {
        IMessage IMessage = mGson.get().fromJson(text, IMessage.class);
        switch (IMessage.getType()) {
            case "MSG":
                Message msg = mGson.get().fromJson(IMessage.getContent(), Message.class);
                if (msg.getChatType() == Message.CHAT_TYPE_SINGLE
                        && msg.getSender() == msg.getChatId()) { // 自己发的单聊消息不接收
                    return;
                }
                msg.setChatId(msg.getSender());
                mDataLayer.get().getMessageService().insert(msg);
                RxBus.get().post(new ReceiveMsgEvent(msg));

                PendingIntent intent = PendingIntent.getActivity(
                        this,
                        0,
                        new Intent(this, MainActivity.class)
                                .addCategory(Intent.CATEGORY_LAUNCHER)
                                .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                                        | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED) // 关键的一步，设置启动模式
                                .putExtra(BundleKey.MAIN_JUMP_TYPE, 1)
                                .putExtra(BundleKey.CHAT_TYPE, msg.getChatType())
                                .putExtra(BundleKey.CHAT_ID, msg.getChatId()),
                        PendingIntent.FLAG_UPDATE_CURRENT);

                mDataLayer.get().getContactService()
                        .getContact(msg.getSender())
                        .compose(RxTransformers.applySchedulers())
                        .doOnSubscribe(mCompositeDisposable::add)
                        .subscribe(contact -> {
                            String name = contact.getNick() != null ? contact.getNick() : contact.getName();
                            NotificationHelper.notify(App.get(), name, msg.getContent(), msg.getContent(), contact.getAvatar(), intent);
                        }, throwable -> {
                            NotificationHelper.notify(App.get(), String.valueOf(msg.getSender()), msg.getContent(), msg.getContent(), null, intent);
                        });
                break;
            case "ADD_FRIEND":
                me.gavin.app.contact.Request req = mGson.get().fromJson(IMessage.getContent(), me.gavin.app.contact.Request.class);
                req.setUid(IMessage.getFrom());
                mDataLayer.get().getContactService().insetRequest(req);

                long time = System.currentTimeMillis();
                Message reqMsg = new Message();
                reqMsg.setId(IMessage.getFrom() + "" + time);
                reqMsg.setContent(String.format("%s 请求加为好友", req.getName()));
                reqMsg.setTime(time);
                reqMsg.setSender(IMessage.getFrom());
                reqMsg.setChatType(Message.CHAT_TYPE_SYSTEM);
                reqMsg.setChatId(Message.SYSTEM_CONTACT_REQUEST);
                mDataLayer.get().getMessageService().insert(reqMsg);
                break;
        }
    }
}
