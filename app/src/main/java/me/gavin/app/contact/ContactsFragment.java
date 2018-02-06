package me.gavin.app.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.base.BindingFragment;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.LayoutRecyclerBinding;

/**
 * 通讯录
 *
 * @author gavin.xiong 2018/2/6
 */
public class ContactsFragment extends BindingFragment<LayoutRecyclerBinding> {

    public static ContactsFragment newInstance() {
        return new ContactsFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {

    }
}
