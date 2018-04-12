package me.gavin.app.im;

import io.reactivex.Emitter;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import me.gavin.util.L;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * WebSocketListener
 *
 * @author gavin.xiong 2018/3/8
 */
public class IWebSocketListener2 extends WebSocketListener implements ObservableOnSubscribe<String> {

    private static final String TAG = "WebSocket";

    private Emitter<? super String> mEmitter;

    @Override
    public void subscribe(ObservableEmitter<String> emitter) throws Exception {
        mEmitter = emitter;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        L.e(TAG, "onOpen - " + response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        L.d(TAG, "onMessage - " + text);
        mEmitter.onNext(text);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        L.e(TAG, "onClosed - " + code + " - " + reason + " - ");
        mEmitter.onComplete();
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        L.e(TAG, "onFailure - " + t);
        mEmitter.onError(t);
    }
}
