package com.demo_app.jitcodez.grandmintdemoapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{
    
    EditText emailText;
    EditText passwordText;
    ProgressBar pb;
    Intent in;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mFirebaseAuth=FirebaseAuth.getInstance();
        emailText=(EditText)findViewById(R.id.login_emailId);
        passwordText=(EditText)findViewById(R.id.login_password);
        pb=(ProgressBar)findViewById(R.id.login_pb);

    }
    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if(currentUser!=null)
        {
            in = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(in);
        }

    }

    @Override
    public void onClick(View v) {
        pb.setVisibility(View.VISIBLE);

        switch (v.getId())
        {
            case R.id.signup_button:
                in=new Intent(this,SignUpActivity.class);
                startActivity(in);
                
                break;
            case R.id.signin_button:
                String emailId=emailText.getText().toString();
                String pass=passwordText.getText().toString();
                if(emailId.isEmpty())
                {

                    Toast.makeText(this,R.string.emailId,Toast.LENGTH_LONG).show();

                }
                else if(pass.isEmpty())
                {

                    Toast.makeText(this,R.string.password,Toast.LENGTH_LONG).show();

                }
                else if(pass.length()<6)
                {

                    Toast.makeText(this,R.string.password_len,Toast.LENGTH_LONG).show();

                }
                else {

                    mFirebaseAuth.signInWithEmailAndPassword(emailId,pass).addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            Toast.makeText(getApplicationContext(),R.string.welcome_text,Toast.LENGTH_LONG).show();
                            in = new Intent(getApplicationContext(), WelcomeActivity.class);
                            startActivity(in);
                        }

                    });

                }
                break;
        }
    }
}
