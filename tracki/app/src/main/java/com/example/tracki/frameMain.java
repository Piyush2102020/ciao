package com.example.tracki;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class frameMain extends AppCompatActivity {


    TabLayout tab;
    ViewPager viewPager;
    TextView cUser;
    DrawerLayout drawerLayout;
    ListView activeUsersList;
    ArrayList<userModel>userModels;
    activeAdapter activeAdapter;
    LinearLayout activeUser;
    ImageView active;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frame_main);
        tab=findViewById(R.id.tab);
        viewPager=findViewById(R.id.viewPage);
        activeUsersList=findViewById(R.id.activeUserList);
        drawerLayout=findViewById(R.id.drawer);
        active=findViewById(R.id.active);
        userModels=new ArrayList<>();
        activeUser=findViewById(R.id.activeUsers);
        cUser=findViewById(R.id.cUser);
        activeAdapter=new activeAdapter(userModels,getApplicationContext());
        viewPagerAdapter adapter=new viewPagerAdapter(getSupportFragmentManager());
        SharedPreferences pre=getSharedPreferences("user",MODE_PRIVATE);
        String user=pre.getString("username","");
        cUser.setText(user);

        users();
        active.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.END);
            }
        });
        viewPager.setAdapter(adapter);
        tab.setupWithViewPager(viewPager);



        activeUsersList.setAdapter(activeAdapter);

       activeUsersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent=new Intent(getApplicationContext(),chat_activity.class);
                intent.putExtra("receiver",userModels.get(i).username);
                intent.putExtra("gender",userModels.get(i).gender);
                startActivity(intent);
            }
        });



    }


    public void users(){
        DatabaseReference reference= FirebaseDatabase.getInstance().getReference().child("active");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    String username=snapshot1.getKey();
                    String gender=snapshot1.child("gender").getValue(String.class);
                    String age=snapshot1.child("age").getValue(String.class);
                    userModel userModel=new userModel(gender,age,username);
                    userModels.add(userModel);
                    activeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }




}