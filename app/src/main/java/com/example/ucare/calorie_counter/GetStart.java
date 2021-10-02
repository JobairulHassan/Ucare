package com.example.ucare.calorie_counter;

import android.content.Intent;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import com.example.ucare.R;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ucare.profile_user;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetStart extends AppCompatActivity {
    EditText targetWeight_et;
    EditText currentWeight_et;
    TextView calorieTarget;
    TextView calorieCurrent;
    Button calculateDayCalories;
    Button getStarted;
    static double parameter;
    int currentWeight;
    int targetWeight;
    int currentCalories;
    int targetCalories;

    private DatabaseReference databaseReference,dbRef;
    private FirebaseDatabase root;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_start);

        targetWeight_et = findViewById(R.id.targetWeight);
        currentWeight_et = findViewById(R.id.currentWeight);
        calorieTarget = findViewById(R.id.calorieTarget);
        calorieCurrent = findViewById(R.id.calorieCurrent);
        calculateDayCalories = findViewById(R.id.calculateDayCal);

        root=FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference=root.getReference("basicInfo").child(user.getUid());


    }

    //man : kg * 1 * 24
    //woman : kg * 0.9 * 24


    public void getStarted(View view) {
        Intent intent = new Intent(this, EatActivity.class);
            GetStartModel getStart = new GetStartModel(currentWeight, targetWeight, currentCalories, targetCalories);
            databaseReference.setValue(getStart);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }


    public void Calculate(View view) {
        Button button = findViewById(R.id.calculateDayCal);
        dbRef=root.getReference("Users").child(user.getUid()).child("gender");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.getValue().toString().trim().toUpperCase().equals("MALE")){
                    parameter = 1.0;

                }
                else if(snapshot.getValue().toString().trim().toUpperCase().equals("FEMALE")){
                    parameter = 0.9;

                }
                else{
                    parameter = 0.95;

                }
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        currentWeight = Integer.valueOf(String.valueOf(currentWeight_et.getText()));
                        currentCalories = (int)(currentWeight* parameter * 24);
                        calorieCurrent.setText(String.valueOf((int) currentCalories));

                        targetWeight = Integer.valueOf(String.valueOf(targetWeight_et.getText()));
                        targetCalories =  (int)(targetWeight* parameter * 24);
                        calorieTarget.setText(String.valueOf((targetCalories)));
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
