package com.example.ucare;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;

public class login extends AppCompatActivity implements View.OnClickListener{
    private TextView recover;
    private EditText email,password;
    private Button login;
    private FirebaseAuth mAuth;
    private ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        email=findViewById(R.id.emailTxt);
        password=findViewById(R.id.passwordTxt);
        login=findViewById(R.id.logInBtn);
        recover=findViewById(R.id.recoverTxt);
        pb=findViewById(R.id.pg_bar);

        recover.setOnClickListener(this);
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.recoverTxt:
                recoverPassword();
                break;
            case R.id.logInBtn:
                LogIn();
                break;
        }
    }

    private void recoverPassword() {
    }

    private void LogIn() {
        String mail= email.getText().toString().trim();
        String pass= password.getText().toString().trim();


    }
}