package com.example.tracki;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.FOREGROUND_SERVICE;
import static android.Manifest.permission.RECORD_AUDIO;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class home extends Fragment {


    ImageView camera,send,image;
    EditText message;
    ArrayList<msgModel> chat;
    private final int Gallery_Req_code=123;
    Uri selectedImage;
    feedAdapter adapter;
    RecyclerView mainChat;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_home, container, false);
        camera=view.findViewById(R.id.camera);
        send=view.findViewById(R.id.send);
        message=view.findViewById(R.id.msg);
        image=view.findViewById(R.id.image);
        mainChat=view.findViewById(R.id.mainChat);
        chat=new ArrayList<>();
        fun fun=new fun();
        adapter=new feedAdapter(chat,getContext());
        mainChat.setLayoutManager(new LinearLayoutManager(getContext()));
        SharedPreferences pre= getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String userName=pre.getString("username","");
        String gender=pre.getString("gender","");
        String age=pre.getString("age","");
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent,Gallery_Req_code);
            }
        });


        childMsg();

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=message.getText().toString().strip();
                if (msg.isEmpty() && selectedImage==null){
                    message.setText("");
                }
                else if (selectedImage!=null){
                    fun.sendMessage(msg,userName,age,gender,"home",selectedImage);
                    image.setVisibility(View.GONE);
                    selectedImage=null;
                    message.setText("");
                }

                else {
                    fun.sendMessage(msg,userName,age,gender,"home",null);
                    message.setText("");
                }
            }
        });


        mainChat.setAdapter(adapter);


        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode==Gallery_Req_code&& resultCode== Activity.RESULT_OK&& data != null){
            selectedImage=data.getData();
            image.setImageURI(selectedImage);
            image.setVisibility(View.VISIBLE);
        }
    }


    public void fetchMsg(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("home");
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    String msgId=snapshot1.getKey();
                    String sender=snapshot1.child("sender").getValue(String.class);
                    String message=snapshot1.child("message").getValue(String.class);
                    String gender=snapshot1.child("gender").getValue(String.class);
                    String age=snapshot1.child("age").getValue(String.class);
                    String imageUrl=snapshot1.child("imageUrl").getValue(String.class);
                    String uid=snapshot1.child("uid").getValue(String.class);
                    populate(sender,gender,uid,age,msgId,message,imageUrl);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public void childMsg() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("home");
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                processChild(snapshot);

            } // Notify adapter of data change


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

            // Other overridden methods (onChildChanged, onChildRemoved, onChildMoved) go here
            // Implement logic for handling changes, removals, and movements if needed
        });
    }



    public void populate(String sender, String gender, String uid, String age, String msgId, String message, String imageUrl){
        msgModel msgModel=new msgModel(sender,gender,uid,age,msgId,message,imageUrl);
        chat.add(msgModel);
        mainChat.scrollToPosition(chat.size()-1);
    }

    private void processChild(DataSnapshot snapshot) {
        if (snapshot.exists() && snapshot.hasChildren()) {
            String msgId = snapshot.getKey();
            String sender = snapshot.child("sender").getValue(String.class);
            String message = snapshot.child("message").getValue(String.class);
            String gender = snapshot.child("gender").getValue(String.class);
            String age = snapshot.child("age").getValue(String.class);
            String imageUrl = snapshot.child("imageUrl").getValue(String.class);
            String uid = snapshot.child("uid").getValue(String.class);
            populate(sender, gender, uid, age, msgId, message, imageUrl);
            adapter.notifyDataSetChanged();
        }

        else {
            reprocessChild(snapshot);
        }
    }

    private void reprocessChild(DataSnapshot snapshot) {
        snapshot.getRef().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                processChild(snapshot); // Re-check the data for completeness
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle onCancelled event if needed
            }
        });
    }


}