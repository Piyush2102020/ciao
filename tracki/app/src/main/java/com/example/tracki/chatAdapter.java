package com.example.tracki;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

public class chatAdapter extends RecyclerView.Adapter<chatAdapter.ViewHolder> {
    boolean img=false;
    Context context;
    ArrayList<msgModel>msgModels;
    public chatAdapter(ArrayList<msgModel> chat, Context context){
        this.context=context;
        this.msgModels=chat;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.chat_lay,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (msgModels.get(position).imageUrl!=null){
            holder.viewFull.setVisibility(View.VISIBLE);
            Glide.with(context).load(msgModels.get(position).imageUrl).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.image);
        }else {
            holder.viewFull.setVisibility(View.GONE);
        }

        holder.viewFull.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isImageVisible = holder.image.getVisibility() == View.VISIBLE;
                if (!isImageVisible) {
                    holder.image.setVisibility(View.VISIBLE);
                    holder.viewFull.setText("Hide Image");
                } else {
                    holder.image.setVisibility(View.GONE);
                    holder.viewFull.setText("View Full Image");
                }
            }
        });


        if (msgModels.get(position).gender.equalsIgnoreCase("Male")){
            holder.gender.setImageResource(R.drawable.male);
        } else if (msgModels.get(position).gender.equalsIgnoreCase("Female")) {
            holder.gender.setImageResource(R.drawable.female);
        } else if (msgModels.get(position).gender.equalsIgnoreCase("Not Confirmed")) {
            holder.gender.setImageResource(R.drawable.femenine);
        }
        else {
            holder.gender.setImageResource(R.drawable.non_binary);
        }
        if (msgModels.get(position).message==null || msgModels.get(position).message.isEmpty()){
            holder.msg.setVisibility(View.GONE);
        }

        else {
            holder.msg.setVisibility(View.VISIBLE);
        }
        holder.sender.setText(msgModels.get(position).sender+", ");
        holder.age.setText(msgModels.get(position).age);
        holder.msg.setText(msgModels.get(position).message);
    }

    @Override
    public int getItemCount() {
        return msgModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView msg,age,sender,viewFull;
        ImageView gender,image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            gender=itemView.findViewById(R.id.gender);
            image=itemView.findViewById(R.id.imageP);
            viewFull=itemView.findViewById(R.id.viewFull);
            msg=itemView.findViewById(R.id.messageP);
            age=itemView.findViewById(R.id.age);
            sender=itemView.findViewById(R.id.sender);

        }
    }



}
