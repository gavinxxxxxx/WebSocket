package me.gavin.app.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import me.gavin.app.contact.Contact;
import me.gavin.app.im.event.ReceiveMsgEvent;
import me.gavin.app.im.event.SendMsgEvent;
import me.gavin.base.App;
import me.gavin.base.BindingFragment;
import me.gavin.base.BundleKey;
import me.gavin.base.RxBus;
import me.gavin.base.RxTransformers;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.FragmentChatBinding;

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

    public static ChatFragment newInstance(int chatType, long chatId) {
        Bundle args = new Bundle();
        args.putInt(BundleKey.CHAT_TYPE, chatType);
        args.putLong(BundleKey.CHAT_ID, chatId);
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
        subscribeEvent();
        mChatType = getArguments().getInt(BundleKey.CHAT_TYPE);
        mChatId = getArguments().getLong(BundleKey.CHAT_ID);

        mBinding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        mBinding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
        getDataLayer().getContactService()
                .getContact(mChatId)
                .compose(RxTransformers.applySchedulers())
                .subscribe(contact -> {
                    mContact = contact;
                    mBinding.includeToolbar.toolbar.setTitle(mContact.getNick() != null ? mContact.getNick() : mContact.getName());
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
                    RxBus.get().post(new SendMsgEvent(msg));
                }
                return true;
            }
            return false;
        });
    }

//    @Override
//    public void onNewBundle(Bundle args) {
//        super.onNewBundle(args);
//    }

    public void subscribeEvent() {
        RxBus.get().toObservable(ReceiveMsgEvent.class)
                .map(event -> event.message)
                .filter(msg -> msg.getChatType() == mChatType)
                .filter(msg -> msg.getChatId() == mChatId)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(msg -> {
                    mMessageList.add(0, msg);
                    mAdapter.notifyItemInserted(0);
                    mBinding.recycler.scrollToPosition(0);
                }, Throwable::printStackTrace);
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
        message.setChatType(mChatType);
        message.setChatId(mChatId);

        message.setName(App.getUser().getName());
        message.setAvatar(App.getUser().getAvatar());
        return message;
    }

    private void getData() {
        getDataLayer().getMessageService()
                .getMessage(mChatType, mChatId, 0)
                .compose(RxTransformers.applySchedulers())
                .subscribe(messages -> {
                    mMessageList.clear();
                    mMessageList.addAll(messages);
                    Collections.reverse(mMessageList);
                    mAdapter.notifyDataSetChanged();
                }, throwable -> Snackbar.make(mBinding.recycler, throwable.getMessage(), Snackbar.LENGTH_LONG).show());
    }
}
