package com.example.healthcareproject;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private List<Message> messageList;

    public MessageAdapter(List<Message> messageList){
        this.messageList = messageList;
    }


    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.items_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.textMessage.setText(message.getText());

        if(message.getSentBy() == Message.SENT_BY_USER) {
            holder.textMessage.setGravity(Gravity.END);
        }else {
            holder.textMessage.setGravity(Gravity.START);
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public void addMessage(Message message){
        messageList.add(message);
        notifyItemInserted(messageList.size() -1);
    }

   static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView textMessage;
        MessageViewHolder(View itemView){
            super(itemView);
            textMessage = itemView.findViewById(R.id.textMessage);
        }
    }
}
