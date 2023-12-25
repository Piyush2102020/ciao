package com.example.tracki;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class room_chat extends AppCompatActivity {
    ImageView roomBtn,sendR,cameraR,imageR;
    DrawerLayout roomDrawer;
    EditText msgR;
    ListView activeUserListR;
    RecyclerView mainChatR;
    activeAdapter adapter1;
    ArrayList<msgModel> chat;
    Uri selectedImage;
    TextView roomName;
    feedAdapter adapter;
    DatabaseReference reference;
    ArrayList<userModel> arrayList;
    private final int gallery_code=123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_chat);
        roomDrawer=findViewById(R.id.roomDrawer);
        roomBtn=findViewById(R.id.roomDbtn);
        imageR=findViewById(R.id.imageR);
        activeUserListR=findViewById(R.id.activeUserListR);
        msgR=findViewById(R.id.msgR);
        arrayList=new ArrayList<>();
        adapter1=new activeAdapter(arrayList,getApplicationContext());
        mainChatR=findViewById(R.id.mainChatR);
        sendR=findViewById(R.id.sendR);
        cameraR=findViewById(R.id.cameraR);
        roomName=findViewById(R.id.roomName);

        chat=new ArrayList<>();
        mainChatR.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        adapter=new feedAdapter(chat,getApplicationContext());
        Intent intent=getIntent();
        String roomName1=intent.getStringExtra("roomName");
        String roomId=intent.getStringExtra("roomId");
        SharedPreferences pre=getSharedPreferences("user",MODE_PRIVATE);
        String userName=pre.getString("username","");
        String gender=pre.getString("gender","");
        String age=pre.getString("age","");
        roomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                roomDrawer.openDrawer(GravityCompat.END);
            }
        });
        roomName.setText(roomName1);

        activeUserListR.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),chat_activity.class);
                intent.putExtra("receiver",arrayList.get(i).username);
                intent.putExtra("gender",arrayList.get(i).gender);
                startActivity(intent);
            }
        });
        sendR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msg=msgR.getText().toString().strip();
                if (msg.isEmpty() && selectedImage==null){
                    msgR.setText("");
                }
                else if (selectedImage!=null){
                    fun.sendMessage1(msg,userName,age,gender,"room",selectedImage,roomId);
                    selectedImage=null;
                    imageR.setVisibility(View.GONE);
                    msgR.setText("");
                }

                else {
                    fun.sendMessage1(msg,userName,age,gender,"room",null,roomId);
                    msgR.setText("");
                }
            }
        });

        if (roomId!=null){
            childMsg(roomId);
            activeUser(roomId);
        }

        cameraR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1=new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent1,gallery_code);
            }
        });
        activeUserListR.setAdapter(adapter1);
        mainChatR.setAdapter(adapter);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==gallery_code &&resultCode== Activity.RESULT_OK&& data!=null){
            selectedImage=data.getData();
            imageR.setImageURI(selectedImage);
            imageR.setVisibility(View.VISIBLE);
        }
    }

    public void childMsg(String roomId){

        DatabaseReference reference=FirebaseDatabase.getInstance().getReference().child("room").child(roomId);
        reference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
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


    public void populate(String sender, String gender, String uid, String age, String msgId, String message, String imageUrl){
        msgModel msgModel=new msgModel(sender,gender,uid,age,msgId,message,imageUrl);
        chat.add(msgModel);
        mainChatR.scrollToPosition(chat.size()-1);
    }

    public void  activeUser(String roomId){
        reference=FirebaseDatabase.getInstance().getReference().child("room").child(roomId).child("member");

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    String name=snapshot1.getKey();
                    String gender=snapshot1.getValue(String.class);
                    userModel userModel=new userModel(gender,"0",name);
                    arrayList.add(userModel);
                    adapter1.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }








}