package com.example.tracki;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class chat_activity extends AppCompatActivity {
    TextView receiverName;
    ImageView camera,send,genderC,image;
    private final int Gallery_Req_code=123;
    Uri selectedImage;
    EditText msg;
    String sender;
    RecyclerView chats;
    boolean newChat=true;
    String receiver;
    chatAdapter chatAdapter;
    String msgId;
    ArrayList<msgModel> chat;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        receiverName=findViewById(R.id.receiver);
        camera=findViewById(R.id.cameraC);
        chats=findViewById(R.id.chatC);
        send=findViewById(R.id.sendC);
        msg=findViewById(R.id.msgSendC);
        image=findViewById(R.id.imageC);
        chat=new ArrayList<>();
        chats.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        chatAdapter=new chatAdapter(chat,getApplicationContext());
        genderC=findViewById(R.id.genderC);
        Intent intent=getIntent();
        receiver=intent.getStringExtra("receiver");
        String gender=intent.getStringExtra("gender");
        SharedPreferences pre=getSharedPreferences("user",MODE_PRIVATE);
        sender=pre.getString("username","");
        String gender1=pre.getString("gender","");
        String age=pre.getString("age","");
        fun fun=new fun();


        if (gender!=null){
            if (gender.equalsIgnoreCase("Male")){
                genderC.setImageResource(R.drawable.male);

            } else if (gender.equalsIgnoreCase("Female")) {
                genderC.setImageResource(R.drawable.female);
            } else if (gender.equalsIgnoreCase("Not Confirmed")) {
                genderC.setImageResource(R.drawable.femenine);
            }
            else {
                genderC.setImageResource(R.drawable.non_binary);
            }
        }


        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectedImage==null){

                    Intent intent1=new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent1,Gallery_Req_code);
                }
                else {

                }
            }
        });

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("active").child(sender).child("messages");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.hasChild(receiver) || snapshot.hasChild(sender)){
                    newChat=false;
                    if (snapshot.hasChild(receiver)){
                        msgId=snapshot.child(receiver).child("msgId").getValue(String.class);
                        snapshot.child(receiver).child("gender").getValue(String.class);
                        childMsg(msgId);

                    }
                    else {
                        msgId=snapshot.child(sender).child("msgId").getValue(String.class);
                        snapshot.child(sender).child("gender").getValue(String.class);
                        childMsg(msgId);
                    }
                }
                else {
                    newChat=true;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        if (msgId!=null){
            childMsg(msgId);

        }      if (selectedImage==null){
            camera.setImageResource(R.drawable.baseline_camera_alt_24);
        }
        else {
            camera.setImageResource(R.drawable.baseline_clear_24);
        }
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg1=msg.getText().toString().strip();
                if (msg1.isEmpty() && selectedImage== null){
                    msg.setText("");
                } else if (selectedImage!=null) {
                    if (newChat){
                        newChat=false;
                        fun.sendMessageChat(msg1,sender,age,gender1,selectedImage,receiver,gender,null);
                        selectedImage=null;
                        msg.setText("");
                        image.setVisibility(View.GONE);
                    }
                    else {
                        msg.setText("");
                        fun.sendMessageChat(msg1,sender,age,gender1,selectedImage,receiver,gender,msgId);
                    }

                }
                else {
                    if (newChat){
                        newChat=false;
                        msg.setText("");
                        fun.sendMessageChat(msg1,sender,age,gender1,null,receiver,gender,null);
                    }
                    else {
                        msg.setText("");
                        fun.sendMessageChat(msg1,sender,age,gender1,null,receiver,gender,msgId);

                    }
                }
            }
        });

        chats.setAdapter(chatAdapter);

        receiverName.setText(receiver);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Gallery_Req_code && resultCode == Activity.RESULT_OK && data != null) {
            selectedImage = data.getData();
            image.setImageURI(selectedImage);
            image.setVisibility(View.VISIBLE);
        }
    }


    public void childMsg(String msgId1) {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("chat").child(msgId1);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()){
                    processChild(snapshot);
                }
                else {
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("chat").child(sender).child(receiver);
                    reference1.addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            processChild(snapshot);
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
        chats.scrollToPosition(chat.size()-1);
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
            chatAdapter.notifyDataSetChanged();
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