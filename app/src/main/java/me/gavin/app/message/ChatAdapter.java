package me.gavin.app.message;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import java.util.List;

import me.gavin.base.recycler.RecyclerAdapter;
import me.gavin.base.recycler.RecyclerHolder;
import me.gavin.im.ws.R;
import me.gavin.im.ws.databinding.ItemMessageBinding;

/**
 * 聊天消息列表适配器
 *
 * @author gavin.xiong 2018/2/2
 */
public class ChatAdapter extends RecyclerAdapter<Message, ItemMessageBinding> {

    public ChatAdapter(Context context, @NonNull List<Message> list) {
        super(context, list, R.layout.item_message);
    }

    @Override
    protected void onBind(RecyclerHolder<ItemMessageBinding> holder, int position, Message t) {
        holder.binding.ivLAvatar.setVisibility(View.GONE);
        holder.binding.ivRAvatar.setVisibility(View.VISIBLE);
        holder.binding.tvRText.setText(t.getContent());
        holder.binding.executePendingBindings();
    }

}
