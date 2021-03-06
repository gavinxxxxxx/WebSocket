package me.gavin.app.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import java.util.ArrayList;
import java.util.List;

import me.gavin.app.contact.RequestsFragment;
import me.gavin.app.main.StartFragmentEvent;
import me.gavin.base.BindingFragment;
import me.gavin.base.RxBus;
import me.gavin.base.RxTransformers;
import me.gavin.base.recycler.BindingAdapter;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.LayoutRecyclerBinding;

/**
 * 消息列表
 *
 * @author gavin.xiong 2018/2/6
 */
public class MessageFragment extends BindingFragment<LayoutRecyclerBinding> {

    private final List<Message> mMessageList = new ArrayList<>();
    private BindingAdapter<Message> mAdapter;

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mBinding.refreshLayout.setEnabled(false);

        mAdapter = new BindingAdapter<>(getContext(), mMessageList, R.layout.item_chat);
        mBinding.recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(this::onItemClick);
    }

    @Override
    public void onSupportVisible() {
        super.onSupportVisible();
        getData();
    }

    private void getData() {
        getDataLayer().getMessageService()
                .getChat()
                .compose(RxTransformers.applySchedulers())
                .doOnSubscribe(mCompositeDisposable::add)
                .subscribe(messages -> {
                    mMessageList.clear();
                    mMessageList.addAll(messages);
                    mAdapter.notifyDataSetChanged();
                }, throwable -> Snackbar.make(mBinding.recycler, throwable.getMessage(), Snackbar.LENGTH_LONG).show());
    }

    private void onItemClick(int position) {
        Message t = mMessageList.get(position);
        switch (t.getChatType()) {
            case Message.CHAT_TYPE_SINGLE:
            case Message.CHAT_TYPE_GROUP:
            case Message.CHAT_TYPE_OFFICIAL:
                ChatFragment fragment = ChatFragment.newInstance(t.getChatType(), t.getChatId());
                RxBus.get().post(new StartFragmentEvent(fragment));
                break;
            case Message.CHAT_TYPE_SYSTEM:
                onSystemClick(t);
                break;
        }
    }

    private void onSystemClick(Message t) {
        if (t.getChatId() == Message.SYSTEM_CONTACT_REQUEST) {
            RequestsFragment fragment = RequestsFragment.newInstance();
            RxBus.get().post(new StartFragmentEvent(fragment));
        }
    }
}
