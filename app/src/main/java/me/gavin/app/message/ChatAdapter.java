package me.gavin.app.message;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;

import java.util.List;

import me.gavin.base.App;
import me.gavin.base.recycler.RecyclerAdapter;
import me.gavin.base.recycler.RecyclerHolder;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.ItemMessageBinding;
import me.gavin.util.ImageLoader;

/**
 * 聊天消息列表适配器
 *
 * @author gavin.xiong 2018/2/2
 */
public class ChatAdapter extends RecyclerAdapter<Message, ItemMessageBinding> {

    ChatAdapter(Context context, @NonNull List<Message> list) {
        super(context, list, R.layout.item_message);
    }

    @Override
    protected void onBind(RecyclerHolder<ItemMessageBinding> holder, int position, Message t) {
        if (t.getSender() != App.getUser().getId()) { // 收
            holder.binding.ivLAvatar.setVisibility(View.VISIBLE);
            holder.binding.ivRAvatar.setVisibility(View.GONE);
            if (TextUtils.isEmpty(t.getAvatar())) {
                holder.binding.ivLAvatar.setImageResource(R.mipmap.ic_launcher);
            } else {
                ImageLoader.loadAvatar(holder.binding.ivLAvatar, t.getAvatar());
            }
            holder.binding.tvLText.setText(t.getContent());
        } else { // 发
            holder.binding.ivLAvatar.setVisibility(View.GONE);
            holder.binding.ivRAvatar.setVisibility(View.VISIBLE);
            if (TextUtils.isEmpty(t.getAvatar())) {
                holder.binding.ivRAvatar.setImageResource(R.mipmap.ic_launcher);
            } else {
                ImageLoader.loadAvatar(holder.binding.ivRAvatar, t.getAvatar());
            }
            holder.binding.tvRText.setText(t.getContent());
        }

        holder.binding.executePendingBindings();
    }
}
