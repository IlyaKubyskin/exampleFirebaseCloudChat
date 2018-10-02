package com.example.kubik.chat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.MessageViewHolder> {

    private List<Message> messages;

    void setMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    void addNewMessage(Message message) {
        messages.add(message);
        notifyDataSetChanged();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.li_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.name.setText(messages.get(position).getUserName());
        holder.date.setText(new Date(messages.get(position).getDate()).toString());
        holder.message.setText(messages.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private TextView name;
        private TextView date;
        private TextView message;

        MessageViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tvName);
            date = itemView.findViewById(R.id.tvDate);
            message = itemView.findViewById(R.id.tvMessage);
        }
    }
}
