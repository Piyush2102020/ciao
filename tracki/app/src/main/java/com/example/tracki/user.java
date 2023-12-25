package com.example.tracki;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class user extends Fragment {

    ListView rooms;
    ArrayList<userModel> room;
    TextView addRoom;
    LinearLayout rDetails;
    EditText roomName;
    AppCompatButton roomBtn;
    DatabaseReference reference;
    boolean visible=false;

    activeAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user,container,false);
        rooms=view.findViewById(R.id.rooms);
        addRoom=view.findViewById(R.id.addRoom);
        rDetails=view.findViewById(R.id.rDetail);
        roomName=view.findViewById(R.id.roomName);
        roomBtn=view.findViewById(R.id.roomBtn);
        SharedPreferences pre=getContext().getSharedPreferences("user", Context.MODE_PRIVATE);
        String username=pre.getString("username","");
        String gender=pre.getString("gender","");
        room=new ArrayList<>();
        adapter=new activeAdapter(room,getContext());
        reference=FirebaseDatabase.getInstance().getReference();


        roomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String roomName1=roomName.getText().toString().strip();

                if (roomName1.isEmpty()){
                    Toast.makeText(getContext(), "Name cannot be empty", Toast.LENGTH_SHORT).show();
                }  else {
                    String pushRoom=reference.child("room").push().getKey();
                    Map<String,String> details=new HashMap<>();
                    details.put("admin",username);
                    roomName.setText("");
                    details.put("name",roomName1);
                    Map<String,String>roomD=new HashMap<>();
                    roomD.put("roomId",pushRoom);
                    roomD.put("roomName",roomName1);
                    reference.child("active").child(username).child("room").child(pushRoom).setValue(roomD);
                    reference.child("room").child(pushRoom).child("member").child(username).setValue(gender);
                    reference.child("room").child(pushRoom).child("name").setValue(roomName1);
                    reference.child("room").child(pushRoom).child("admin").setValue(username);
                    Intent intent=new Intent(getContext(),room_chat.class);
                    intent.putExtra("roomName",roomName1);
                    intent.putExtra("roomId",pushRoom);
                    startActivity(intent);
                }
            }
        });


        rooms.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getContext(),room_chat.class);
                intent.putExtra("roomName",room.get(i).username);
                intent.putExtra("roomId",room.get(i).gender);
                Map<String,String>roomD=new HashMap<>();
                roomD.put("roomId",room.get(i).gender);
                roomD.put("roomName",room.get(i).username);
                reference.child("active").child(username).child("room").child(room.get(i).gender).setValue(roomD);
                startActivity(intent);
            }
        });

        addRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (visible){
                    visible=false;
                    addRoom.setText("Add my room");
                    rDetails.setVisibility(View.GONE);
                }
                else {
                    visible=true;
                    addRoom.setText("Cancel");
                    rDetails.setVisibility(View.VISIBLE);
                }
            }
        });
        fetchRoom();
        rooms.setAdapter(adapter);
        return view;
    }

    public void fetchRoom(){
        DatabaseReference reference1=FirebaseDatabase.getInstance().getReference().child("room");
        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String roomName=snapshot.child("name").getValue(String.class);
                String roomId=snapshot.getKey();
                userModel userModel=new userModel("room",roomId,roomName);
                room.add(userModel);
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
}