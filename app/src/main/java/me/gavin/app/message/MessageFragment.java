package me.gavin.app.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import java.util.ArrayList;
import java.util.List;

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
        mAdapter = new BindingAdapter<>(getContext(), mMessageList, R.layout.item_chat);
        mBinding.recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(i ->
                RxBus.get().post(new StartFragmentEvent(ChatFragment.newInstance(mMessageList.get(i).getFrom()))));

        mBinding.refreshLayout.setOnRefreshListener(this::getData);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getData();
    }

    private void getData() {
        getDataLayer().getMessageService()
                .getChat()
                .compose(RxTransformers.applySchedulers())
                .doOnSubscribe(disposable -> {
                    mCompositeDisposable.add(disposable);
                    mBinding.refreshLayout.setRefreshing(true);
                })
                .doOnComplete(() -> mBinding.refreshLayout.setRefreshing(false))
                .doOnError(throwable -> mBinding.refreshLayout.setRefreshing(false))
                .subscribe(messages -> {
                    mMessageList.clear();
                    mMessageList.addAll(messages);
                    mAdapter.notifyDataSetChanged();
                }, throwable -> Snackbar.make(mBinding.recycler, throwable.getMessage(), Snackbar.LENGTH_LONG).show());
    }
}
