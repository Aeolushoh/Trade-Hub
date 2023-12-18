package com.example.myapplication;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.entity.Msg;
import com.example.myapplication.entity.Product;

import java.util.ArrayList;
import java.util.List;

public class msgAdapter extends RecyclerView.Adapter<msgAdapter.ViewHolder> {

    private List<Msg> msglist;
    // 构造函数接受上下文参数

    public msgAdapter(List<Msg> msglist) {
        this.msglist = msglist;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView msg_from;
        TextView msg_msg;

        ViewHolder(View itemView) {
            super(itemView);
            msg_from = itemView.findViewById(R.id.from);
            msg_msg = itemView.findViewById(R.id.msg);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.msg_item, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Msg msg = msglist.get(position);
        holder.msg_from.setText(new String(String.valueOf(position+1)) +"."+msg.getFrom()+":");
        holder.msg_msg.setText("    "+msg.getMsg());

    }
    @Override
    public int getItemCount() {
        if (msglist != null) {
            return msglist.size();
        } else {
            return 0; // 或者根据实际情况返回其他合适的值
        }
    }
}