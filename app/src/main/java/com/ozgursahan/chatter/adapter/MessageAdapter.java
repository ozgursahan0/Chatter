package com.ozgursahan.chatter.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ozgursahan.chatter.databinding.RecyclerRowBinding;
import com.ozgursahan.chatter.model.Message;

import java.util.ArrayList;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageHolder> {

    private ArrayList<Message> messageArrayList;

    public MessageAdapter(ArrayList<Message> messageArrayList) {
        this.messageArrayList=messageArrayList;
    }

    @NonNull
    @Override
    public MessageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        RecyclerRowBinding recyclerRowBinding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new MessageHolder(recyclerRowBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageHolder holder, int position) {
        holder.recyclerRowBinding.recyclerViewUserEmailText.setText(messageArrayList.get(position).email);
        holder.recyclerRowBinding.recyclerViewCommentText.setText(messageArrayList.get(position).comment);
    }

    @Override
    public int getItemCount() {
        return messageArrayList.size();
    }

    class MessageHolder extends RecyclerView.ViewHolder {

        RecyclerRowBinding recyclerRowBinding;

        public MessageHolder(RecyclerRowBinding recyclerRowBinding) {
            super(recyclerRowBinding.getRoot());
            this.recyclerRowBinding = recyclerRowBinding;
        }
    }
}
