package com.example.tracki;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class details extends AppCompatActivity {
    Spinner spinner;
    ArrayList<String>gender;
    EditText username,age;
    Button start;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        spinner=findViewById(R.id.spinner);
        username=findViewById(R.id.username);
        age=findViewById(R.id.age);
        start=findViewById(R.id.start);
        gender=new ArrayList<>();
        gender.add("Male");
        gender.add("Female");
        gender.add("Not Confirmed");
        gender.add("Non Binary");
        SharedPreferences pre=getSharedPreferences("user",MODE_PRIVATE);
        SharedPreferences.Editor editor=pre.edit();
        ArrayAdapter<String> adapter=new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,gender);
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("active");
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username1=username.getText().toString().strip();
                String age1=age.getText().toString().strip();
                String gender=spinner.getSelectedItem().toString();
                if (username1.isEmpty() && age1.isEmpty()){
                    Toast.makeText(details.this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
                } else {


                    reference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(username1)){
                                Toast.makeText(details.this, "Username already taken", Toast.LENGTH_SHORT).show();


                            }

                            else {

                                editor.putString("username",username1);
                                editor.putString("gender",gender);
                                editor.putString("age",age1);
                                editor.commit();
                                String uid=pre.getString("uid","");
                                reference.child(username1).child("age").setValue(age1);
                                reference.child(username1).child("gender").setValue(gender);
                                reference.child(username1).child("uid").setValue(uid);
                                Intent intent=new Intent(getApplicationContext(),frameMain.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }



            }
        });
        spinner.setAdapter(adapter);
    }
}