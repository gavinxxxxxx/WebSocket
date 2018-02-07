package me.gavin.app.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import me.gavin.app.main.StartFragmentEvent;
import me.gavin.app.message.ChatFragment;
import me.gavin.base.BindingFragment;
import me.gavin.base.RxBus;
import me.gavin.base.RxTransformers;
import me.gavin.base.recycler.BindingAdapter;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.LayoutRecyclerBinding;

/**
 * 通讯录
 *
 * @author gavin.xiong 2018/2/6
 */
public class ContactsFragment extends BindingFragment<LayoutRecyclerBinding> {

    private final List<Contact> mContacts = new ArrayList<>();
    private BindingAdapter<Contact> mAdapter;

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mAdapter = new BindingAdapter<>(getContext(), mContacts, R.layout.item_contact);
        mBinding.recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(i ->
                RxBus.get().post(new StartFragmentEvent(ChatFragment.newInstance(mContacts.get(i).getId()))));

        mBinding.refreshLayout.setOnRefreshListener(this::getData);
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        getData();
    }

    private void getData() {
        getDataLayer().getContactService()
                .getContacts()
                .delay(1500, TimeUnit.MILLISECONDS)
                .compose(RxTransformers.applySchedulers())
                .doOnSubscribe(disposable -> {
                    mCompositeDisposable.add(disposable);
                    mBinding.refreshLayout.setRefreshing(true);
                })
                .doOnComplete(() -> mBinding.refreshLayout.setRefreshing(false))
                .doOnError(throwable -> mBinding.refreshLayout.setRefreshing(false))
                .subscribe(contacts -> {
                    mContacts.clear();
                    mContacts.addAll(contacts);
                    mAdapter.notifyDataSetChanged();
                }, throwable -> Snackbar.make(mBinding.recycler, throwable.getMessage(), Snackbar.LENGTH_LONG).show());
    }
}
