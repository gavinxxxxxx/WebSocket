package me.gavin.app.message;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.base.BindingFragment;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.LayoutRecyclerBinding;

/**
 * 消息列表
 *
 * @author gavin.xiong 2018/2/6
 */
public class MessageFragment extends BindingFragment<LayoutRecyclerBinding> {

    public static MessageFragment newInstance() {
        return new MessageFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.layout_recycler;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {

    }
}
