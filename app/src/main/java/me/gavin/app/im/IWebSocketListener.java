package me.gavin.app.im;

import io.reactivex.Emitter;
import io.reactivex.Observable;
import me.gavin.util.L;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2018/3/8
 */
public class IWebSocketListener extends WebSocketListener {

    private static final String TAG = "WebSocket";

    private Observable<String> observable;
    private Emitter<String> emitter;

    public IWebSocketListener() {
        observable = Observable.create(observableEmitter -> emitter = observableEmitter);
    }

    public Observable<String> getObservable() {
        return observable;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        L.e(TAG, "onOpen - " + response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        L.d(TAG, "onMessage - " + text);
        emitter.onNext(text);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        L.e(TAG, "onClosed - " + code + " - " + reason + " - ");
        emitter.onComplete();
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        L.e(TAG, "onFailure - " + t);
        emitter.onError(new Throwable(t));
    }
}
