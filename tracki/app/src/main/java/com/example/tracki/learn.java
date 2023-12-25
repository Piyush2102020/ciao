package com.example.tracki;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class learn extends Fragment {
    ListView messages,roomL;
    activeAdapter adapter,adapter1;

    ArrayList <userModel> chat;
    ArrayList<userModel> room;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_learn, container, false);
        messages=view.findViewById(R.id.messages);
        roomL=view.findViewById(R.id.roomL);
        room=new ArrayList<>();
        chat=new ArrayList<>();
        adapter=new activeAdapter(chat,getContext());
        adapter1=new activeAdapter(room,getContext());
        if (getContext()!=null){
            SharedPreferences pre=getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
            String username= pre.getString("username","");
            DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("active").child(username).child("messages");

            reference.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    if (snapshot.exists()){

                        String otherPerson=snapshot.getKey();
                        String rGender=snapshot.child("gender").getValue(String.class);
                        String msgId=snapshot.child("msgId").getValue(String.class);
                        userModel userModel=new userModel(rGender,"0",otherPerson);
                        chat.add(userModel);
                        adapter.notifyDataSetChanged();
                    }

                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot snapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            checkRoom(username);
        }
        roomL.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getContext(),room_chat.class);
                intent.putExtra("roomName",room.get(i).username);
                intent.putExtra("roomId",room.get(i).gender);
                startActivity(intent);
            }
        });


        messages.setAdapter(adapter);

        messages.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getContext(),chat_activity.class);
                 intent.putExtra("receiver",chat.get(i).username);
                intent.putExtra("gender",chat.get(i).gender);
                startActivity(intent);
            }
        });


        roomL.setAdapter(adapter1);

        return view;
    }

    public void checkRoom(String username){
        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("active").child(username).child("room");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String roomId=snapshot.child("roomId").getValue(String.class);
                String roomName=snapshot.child("roomName").getValue(String.class);
                userModel userModel=new userModel("room",roomId,roomName);
                room.add(userModel);
                adapter1.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}