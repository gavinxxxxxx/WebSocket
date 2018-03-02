package me.gavin.app.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import java.util.ArrayList;
import java.util.List;

import me.gavin.base.BindingFragment;
import me.gavin.base.BundleKey;
import me.gavin.base.RxTransformers;
import me.gavin.base.recycler.BindingAdapter;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.LayoutToolbarRecyclerBinding;

/**
 * 搜索联系人
 *
 * @author gavin.xiong 2018/3/2
 */
public class QueryContactFragment extends BindingFragment<LayoutToolbarRecyclerBinding> {

    private String mQuery;

    private final List<Contact> mContactList = new ArrayList<>();
    private BindingAdapter<Contact> mAdapter;

    public static QueryContactFragment newInstance(String query) {
        Bundle args = new Bundle();
        args.putString(BundleKey.QUERY, query);
        QueryContactFragment fragment = new QueryContactFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_toolbar_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mQuery = getArguments().getString(BundleKey.QUERY);

        mBinding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        mBinding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
        mBinding.includeToolbar.toolbar.setTitle(mQuery);

        mAdapter = new BindingAdapter<>(getActivity(), mContactList, R.layout.item_contact);
        mBinding.recycler.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(i -> doApply(mContactList.get(i).getId()));

        doQuery();
    }

    private void doQuery() {
        getDataLayer().getContactService()
                .queryContact(mQuery)
                .compose(RxTransformers.applySchedulers())
                .compose(RxTransformers.filterResultCD())
                .subscribe(list -> {
                    mContactList.clear();
                    mContactList.addAll(list);
                    mAdapter.notifyDataSetChanged();
                }, t -> Snackbar.make(mBinding.recycler, t.getMessage(), Snackbar.LENGTH_LONG).show());
    }

    private void doApply(long fid) {
        getDataLayer().getContactService()
                .applyContact(fid)
                .compose(RxTransformers.applySchedulers())
                .subscribe(result -> {
                    if (result.isSuccess()) {
                        Snackbar.make(mBinding.recycler, "已发送好友请求", Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(mBinding.recycler, "好友请求发送失败 - " + result.getMsg(), Snackbar.LENGTH_LONG).show();
                    }
                }, t -> Snackbar.make(mBinding.recycler, t.getMessage(), Snackbar.LENGTH_LONG).show());
    }
}
