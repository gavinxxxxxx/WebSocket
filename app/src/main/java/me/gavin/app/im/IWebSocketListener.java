package me.gavin.app.im;

import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import me.gavin.util.L;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

/**
 * WebSocketListener
 *
 * @author gavin.xiong 2018/3/8
 */
public class IWebSocketListener extends WebSocketListener implements ObservableSource<String> {

    private static final String TAG = "WebSocket";

    private Observer<? super String> mObserver;

    @Override
    public void subscribe(Observer<? super String> observer) {
        this.mObserver = observer;
    }

    @Override
    public void onOpen(WebSocket webSocket, Response response) {
        L.e(TAG, "onOpen - " + response);
    }

    @Override
    public void onMessage(WebSocket webSocket, String text) {
        L.d(TAG, "onMessage - " + text);
        mObserver.onNext(text);
    }

    @Override
    public void onClosed(WebSocket webSocket, int code, String reason) {
        L.e(TAG, "onClosed - " + code + " - " + reason + " - ");
        // mObserver.onComplete();
    }

    @Override
    public void onFailure(WebSocket webSocket, Throwable t, Response response) {
        L.e(TAG, "onFailure - " + t);
        mObserver.onError(t);
    }
}
