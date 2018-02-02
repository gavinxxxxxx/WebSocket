package me.gavin.app.message;

import android.os.Bundle;
import android.support.annotation.Nullable;

import me.gavin.base.BindingActivity;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.ActivityChatBinding;

/**
 * 这里是萌萌哒注释君
 *
 * @author gavin.xiong 2018/2/2
 */
public class ChatActivity extends BindingActivity<ActivityChatBinding> {

    @Override
    protected int getLayoutId() {
        return R.layout.activity_chat;
    }

    @Override
    protected void afterCreate(@Nullable Bundle savedInstanceState) {

    }
}
