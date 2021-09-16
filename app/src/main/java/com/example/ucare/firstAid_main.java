package com.example.ucare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class firstAid_main extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_aid_main);
    }
    public void HeartAttack (View v){
        Intent intent=new Intent(firstAid_main.this,heartAttack.class);
        startActivity(intent);
    }
}