package me.gavin.app.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;

import java.util.ArrayList;
import java.util.List;

import me.gavin.base.BindingFragment;
import me.gavin.base.RxTransformers;
import me.gavin.base.recycler.BindingAdapter;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.LayoutToolbarRecyclerBinding;

/**
 * 好友申请列表
 *
 * @author gavin.xiong 2018/3/5
 */
public class RequestsFragment extends BindingFragment<LayoutToolbarRecyclerBinding> {

    private final List<Request> mList = new ArrayList<>();
    private BindingAdapter<Request> mAdapter;

    public static RequestsFragment newInstance() {
        return new RequestsFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_toolbar_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {
        mBinding.includeToolbar.toolbar.setNavigationIcon(R.drawable.vt_arrow_back_24dp);
        mBinding.includeToolbar.toolbar.setNavigationOnClickListener(v -> pop());
        mBinding.includeToolbar.toolbar.setTitle("新的朋友");

        mBinding.refreshLayout.setEnabled(false);

        mAdapter = new BindingAdapter<>(getContext(), mList, R.layout.item_contact_request);
        mAdapter.setOnItemClickListener(i -> applyRequest(mList.get(i)));
        mBinding.recycler.setAdapter(mAdapter);

        getData();
    }

    private void getData() {
        getDataLayer().getContactService()
                .getRequests()
                .subscribe(requests -> {
                    mList.clear();
                    mList.addAll(requests);
                    mAdapter.notifyDataSetChanged();
                }, t -> Snackbar.make(mBinding.recycler, t.getMessage(), Snackbar.LENGTH_LONG).show());
    }

    private void applyRequest(Request request) {
        getDataLayer().getContactService()
                .applyRequest(request.getUid())
                .compose(RxTransformers.applySchedulers())
                .subscribe(result -> {
                    if (result.isSuccess()) {
                        Snackbar.make(mBinding.recycler, "已同意", Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(mBinding.recycler, result.getMsg(), Snackbar.LENGTH_LONG).show();
                    }
                }, t -> Snackbar.make(mBinding.recycler, t.getMessage(), Snackbar.LENGTH_LONG).show());
    }

}
