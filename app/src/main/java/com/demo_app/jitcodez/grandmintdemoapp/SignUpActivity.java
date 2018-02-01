package com.demo_app.jitcodez.grandmintdemoapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    
    EditText emailText;
    EditText userName;
    EditText password;
    FirebaseAuth mFirebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        mFirebaseAuth=FirebaseAuth.getInstance();

        emailText=(EditText)findViewById(R.id.signup_emailId);
        userName=(EditText)findViewById(R.id.signup_username);
        password=(EditText)findViewById(R.id.signup_password);

    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.signupActivity_signUp_button:

                String emailId=emailText.getText().toString();
                String username=userName.getText().toString();
                String pass=password.getText().toString();
                if(emailId.isEmpty()||username.isEmpty()||pass.isEmpty())
                {
                    Toast.makeText(SignUpActivity.this,R.string.complete_form_warning,Toast.LENGTH_LONG).show();

                }
                else if(pass.length()<6)
                {
                    //If password size is less than zero then dont accept
                    Toast.makeText(SignUpActivity.this,R.string.password_len,Toast.LENGTH_LONG).show();

                }
                else
                {
                    sign_up(emailId,pass,username);
                }
        }
    }
/*
sign_up validates by sending a email fro confirmation to user and adds the value to database
 */
    void sign_up(final String email1, final String p1, final String name1)
    {
        mFirebaseAuth.createUserWithEmailAndPassword(email1,p1)
                .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {


                        if (!task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this,R.string.account_failed,Toast.LENGTH_LONG).show();

                        } else {
                            Toast.makeText(SignUpActivity.this,R.string.account_success,Toast.LENGTH_LONG).show();
                          sendVerification(email1,p1,name1);
                        }
                    }
                });
    }
    public boolean sendVerification(final String email1, final String p1, final String name1)
    {
        final boolean[] flag = {false};
        final FirebaseUser user = mFirebaseAuth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(this, new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        // Re-enable button

                        if (task.isSuccessful()) {
                            Toast.makeText(SignUpActivity.this,
                                    R.string.verification_mail + user.getEmail(),
                                    Toast.LENGTH_SHORT).show();
                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            DatabaseReference myRef = database.getReference("user");
                            Map<String, String> user = new HashMap<>();
                            user.put("email",email1);
                            user.put("name", name1);
                            String s[]=email1.split("@");
                            myRef.child(s[0]).setValue(user);

                            Intent in = new Intent(SignUpActivity.this, LoginActivity.class);
                            startActivity(in);
                            finish();
                            flag[0] =true;
                        } else {
                            Toast.makeText(SignUpActivity.this,
                                    R.string.verification_mail_fail,
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        return flag[0];
    }
}
/*

 */