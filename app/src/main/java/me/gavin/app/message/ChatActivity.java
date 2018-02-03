package me.gavin.app.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.inputmethod.EditorInfo;

import me.gavin.base.BindingActivity;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.ActivityChatBinding;
import me.gavin.util.L;

/**
 * 单聊 & 群聊
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
        mBinding.editText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEND) {
                if (!TextUtils.isEmpty(mBinding.editText.getText().toString().trim())) {
                    Message msg = createMessage(mBinding.editText.getText().toString());
                    L.e("--> " + msg);
                    // TODO: 2018/2/3 发消息
                }
                return true;
            }
            return false;
        });
    }

    private Message createMessage(String content) {
        long time = System.currentTimeMillis();
        Message message = new Message();
        message.setId("from:" + time);
        message.setFrom("from");
        message.setTo("to");
        message.setName("from");
        message.setContent(content);
        message.setUrl(null);
        message.setState(0);
        message.setType(0);
        message.setTime(time);
        return message;
    }
}
