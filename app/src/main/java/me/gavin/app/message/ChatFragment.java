package me.gavin.app.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import me.gavin.base.App;
import me.gavin.base.BindingFragment;
import me.gavin.base.BundleKey;
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
    private final List<Message> mMessageList = new ArrayList<>();
    private ChatAdapter mAdapter;

    public static ChatFragment newInstance(long chatId) {
        Bundle args = new Bundle();
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
        mChatId = getArguments().getLong(BundleKey.CHAT_ID);

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
                    // TODO: 2018/2/3 发消息
                }
                return true;
            }
            return false;
        });
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
        message.setFrom(App.getUser().getId());
        message.setTo(mChatId);
        message.setName(App.getUser().getNick());
        message.setContent(content);
        message.setTime(time);
        return message;
    }

    private void getData() {
        getDataLayer().getMessageService()
                .getMessage(mChatId, 0)
                .compose(RxTransformers.applySchedulers())
                .subscribe(messages -> {
                    mMessageList.clear();
                    mMessageList.addAll(messages);
                    Collections.reverse(mMessageList);
                    mAdapter.notifyDataSetChanged();
                }, throwable -> Snackbar.make(mBinding.recycler, throwable.getMessage(), Snackbar.LENGTH_LONG).show());
    }
}
