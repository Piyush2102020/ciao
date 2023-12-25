package com.example.tracki;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    SignInButton signIn;

    GoogleSignInOptions gso;
    FirebaseAuth mAuth;
    GoogleSignInClient mGoogleSignInClient;
    private final int RC_SIGN_IN=100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signIn=findViewById(R.id.signIn);

        SharedPreferences pre=getSharedPreferences("user",MODE_PRIVATE);

        Boolean logged=pre.getBoolean("isLoggedIn",false);
        if (logged){
            startActivity(new Intent(getApplicationContext(),details.class));
            finish();
        }
        mAuth = FirebaseAuth.getInstance();
        //Change the parameters with your database to make the app work
        DatabaseReference reference=FirebaseDatabase.getInstance().getReferenceFromUrl("your_database_url_here");
        GoogleSignInOptions gso=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_id))
                .requestEmail().build();


        mGoogleSignInClient= GoogleSignIn.getClient(this,gso);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signInG();
            }
        });
    }

    private void signInG() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode==RC_SIGN_IN){
            Task<GoogleSignInAccount> task=GoogleSignIn.getSignedInAccountFromIntent(data);


            try {

                GoogleSignInAccount account=task.getResult(ApiException.class);
                firebaseAuth(account.getIdToken());
            }

            catch (Exception e){

                Toast.makeText(this, "error", Toast.LENGTH_SHORT).show();
            }

        }
    }

    private void firebaseAuth(String idToken){

        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    FirebaseUser user=mAuth.getCurrentUser();
                    String uid=user.getUid();

                    DatabaseReference reference= FirebaseDatabase.getInstance().getReference();
                    reference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(uid)){
                                SharedPreferences pre=getSharedPreferences("user",MODE_PRIVATE);
                                SharedPreferences.Editor editor=pre.edit();
                                editor.putString("uid",uid);
                                editor.putBoolean("isLoggedIn",true);
                                editor.commit();
                                Intent intent=new Intent(getApplicationContext(),details.class);
                                startActivity(intent);
                                finish();

                            }

                            else {


                                SharedPreferences pre=getSharedPreferences("user",MODE_PRIVATE);
                                SharedPreferences.Editor editor=pre.edit();
                                editor.putString("uid",uid);
                                editor.commit();
                                Intent intent=new Intent(getApplicationContext(),details.class);
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
    }

}
