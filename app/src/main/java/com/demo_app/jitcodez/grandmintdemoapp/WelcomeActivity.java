package com.demo_app.jitcodez.grandmintdemoapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class WelcomeActivity extends AppCompatActivity implements View.OnClickListener {

    TextView welcomeText;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        welcomeText = (TextView) findViewById(R.id.welcome_user);
        setupFirebaseListener();

    }

    private void setupFirebaseListener() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                final FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    final String email = user.getEmail();

                    final String s[] = email.split("@");
                    final FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference ref = database.getReference();
                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            String a = dataSnapshot.child("user").child(s[0]).child("name").getValue(String.class);
                            Toast.makeText(getApplicationContext(), s[0], Toast.LENGTH_LONG).show();
                            welcomeText.setText("Hello, "+a);

                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                        }
                    });
                }
                   else {
                    Intent in = new Intent(WelcomeActivity.this, LoginActivity.class);
                    startActivity(in);
                    Toast.makeText(getApplicationContext(), R.string.signed_out, Toast.LENGTH_LONG).show();
                }
            }
        };

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.welcome_signout:

                FirebaseAuth.getInstance().signOut();
                break;
        }
    }
    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if(mAuthStateListener!=null)
        {
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }
}
