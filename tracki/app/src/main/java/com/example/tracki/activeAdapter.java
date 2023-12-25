package com.example.tracki;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class activeAdapter extends BaseAdapter {

    Context context;
    ArrayList<userModel>user;
    public activeAdapter(ArrayList<userModel> user, Context context){
        this.user=user;
        this.context=context;
    }
    @Override
    public int getCount() {
        return user.size();
    }

    @Override
    public Object getItem(int i) {
        return user.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.active_list, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = convertView.findViewById(R.id.userA);
            viewHolder.genderA=convertView.findViewById(R.id.genderA);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.textView.setText(user.get(position).username);
        if (user.get(position).gender !=null){
            if (user.get(position).gender.equalsIgnoreCase("Male")){
                viewHolder.genderA.setImageResource(R.drawable.male);
            } else if (user.get(position).gender.equalsIgnoreCase("Female") ){
                viewHolder.genderA.setImageResource(R.drawable.female);
            } else if (user.get(position).gender.equalsIgnoreCase("Not Confirmed")) {
                viewHolder.genderA.setImageResource(R.drawable.femenine);
            } else if (user.get(position).gender.equalsIgnoreCase("room")) {
                viewHolder.genderA.setImageResource(R.drawable.baseline_supervised_user_circle_24);
            } else {
                viewHolder.genderA.setImageResource(R.drawable.non_binary);
            }

        }

        return convertView;
    }

    static class ViewHolder {
        TextView textView;
        ImageView genderA;
        // Other views in the list item layout
    }
}
