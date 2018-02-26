package me.gavin.app.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.gavin.app.contact.Contact;
import me.gavin.base.App;
import me.gavin.base.BindingFragment;
import me.gavin.base.BundleKey;
import me.gavin.base.RxTransformers;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.FragmentChatBinding;
import me.gavin.util.L;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

/**
 * 单聊 & 群聊
 *
 * @author gavin.xiong 2018/2/7
 */
public class ChatFragment extends BindingFragment<FragmentChatBinding> {

    private long mChatId;
    private int mChatType;

    private Contact mContact;

    private final List<Message> mMessageList = new ArrayList<>();
    private ChatAdapter mAdapter;

    public static ChatFragment newInstance(long chatId, int chatType) {
        Bundle args = new Bundle();
        args.putLong(BundleKey.CHAT_ID, chatId);
        args.putInt(BundleKey.CHAT_TYPE, chatType);
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mChatId = getArguments().getLong(BundleKey.CHAT_ID);
        mChatType = getArguments().getInt(BundleKey.CHAT_TYPE);

        mBinding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        mBinding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
        getDataLayer().getContactService()
                .getContact(mChatId)
                .compose(RxTransformers.applySchedulers())
                .subscribe(contact -> {
                    mContact = contact;
                    mBinding.includeToolbar.toolbar.setTitle(mContact.getName());
                }, throwable -> Snackbar.make(mBinding.recycler, throwable.getMessage(), Snackbar.LENGTH_LONG).show());

        mAdapter = new ChatAdapter(getContext(), mMessageList);
        mBinding.recycler.setAdapter(mAdapter);

        mBinding.editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                if (!TextUtils.isEmpty(mBinding.editText.getText().toString().trim())) {
                    Message msg = createMessage(mBinding.editText.getText().toString());
                    getDataLayer().getMessageService().insert(msg);
                    mBinding.editText.setText(null);
                    mMessageList.add(0, msg);
                    mAdapter.notifyItemInserted(0);
                    mBinding.recycler.scrollToPosition(0);

                    Message re = createMessageRe(msg.getContent());
                    getDataLayer().getMessageService().insert(re);
                    mMessageList.add(0, re);
                    mAdapter.notifyItemInserted(0);
                    mBinding.recycler.scrollToPosition(0);
                    // TODO: 2018/2/3 发消息

                    L.e(mWebSocket.send(msg.getContent()));
                }
                return true;
            }
            return false;
        });

        createWebSocket();
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getData();
    }

    private Message createMessage(String content) {
        long time = System.currentTimeMillis();
        Message message = new Message();
        message.setId(String.format("%s%s", App.getUser().getId(), time));
        message.setContent(content);
        message.setTime(time);
        message.setSender(App.getUser().getId());
        message.setChatId(mChatId);
        message.setChatType(mChatType);

        message.setName(App.getUser().getName());
        message.setAvatar(App.getUser().getAvatar());
        return message;
    }

    private Message createMessageRe(String content) {
        long time = System.currentTimeMillis();
        Message message = new Message();
        message.setId(String.format("%s%s", mChatId, time));
        message.setContent("Re:" + content);
        message.setTime(time);
        message.setSender(mChatId);
        message.setChatId(mChatId);
        message.setChatType(mChatType);

        message.setName(mContact.getName());
        message.setAvatar(mContact.getAvatar());
        return message;
    }

    private void getData() {
        getDataLayer().getMessageService()
                .getMessage(mChatId, mChatType, 0)
                .compose(RxTransformers.applySchedulers())
                .subscribe(messages -> {
                    mMessageList.clear();
                    mMessageList.addAll(messages);
                    Collections.reverse(mMessageList);
                    mAdapter.notifyDataSetChanged();
                }, throwable -> Snackbar.make(mBinding.recycler, throwable.getMessage(), Snackbar.LENGTH_LONG).show());
    }

    WebSocket mWebSocket;

    private void createWebSocket() {
        OkHttpClient client = new OkHttpClient.Builder().build();
        mWebSocket = client.newWebSocket(new Request.Builder()
                .url("ws://m.yy-happy.com/yy-app-web/websocket/socketServer.do")
                .build(), new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, Response response) {
                L.e("onOpen - " + response);
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                L.e("onMessage - " + text);
            }

            @Override
            public void onMessage(WebSocket webSocket, ByteString bytes) {
                L.e("onMessageB - " + bytes);
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                L.e("onClosed - " + reason);
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                L.e("onFailure - " + t.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mWebSocket.send("END");
        mWebSocket.close(1000, "close by me");
    }
}
