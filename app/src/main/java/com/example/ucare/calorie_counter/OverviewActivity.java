package com.example.ucare.calorie_counter;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ucare.MainActivity;
import com.example.ucare.R;

import com.example.ucare.bmiactivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

import im.dacer.androidcharts.PieHelper;
import im.dacer.androidcharts.PieView;

public class OverviewActivity extends AppCompatActivity {

    TextView basicCalories_tv;
    TextView gotCalories;
    TextView lostCalories;

    TextView charGotCalories_tv;
    Intent intent;

    private FirebaseUser user;
    DatabaseReference databaseReference;
    DatabaseReference ref_overview;

    int sumOfEatCal = 0;
    int sumofMoveCal = 0;
    int currentCalories = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = database.getReference("basicInfo").child(user.getUid());


        final PieView pieView = findViewById(R.id.pie_view);
        final ArrayList<PieHelper> pieHelperArrayList = new ArrayList<>();


        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                GetStartModel getStartModel = dataSnapshot.getValue(GetStartModel.class);
                if (getStartModel != null) {
                    currentCalories = getStartModel.getTargetCalories();

                }else{
                    Intent intent=new Intent(getApplicationContext(),GetStart.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                basicCalories_tv = findViewById(R.id.basicCalories);
                basicCalories_tv.setText(String.valueOf(currentCalories));


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Date today=new Date();
        final String date = today.getYear()+1900 + "-" + (1+today.getMonth()) + "-" + today.getDate();

        ref_overview = database.getReference("overview").child(user.getUid()).child(date);

        ref_overview.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Overview overview = dataSnapshot.getValue(Overview.class);
                if(!dataSnapshot.exists()) {
                    Overview ov = new Overview(0,0);
                    ref_overview.setValue(ov);
                    sumOfEatCal = 0;
                    sumofMoveCal = 0;
                }else{
                    sumOfEatCal = overview.getSumOfEatCal();
                    sumofMoveCal = overview.getSumOfMoveCal() * -1;
                }




                if (sumOfEatCal !=0 && sumofMoveCal !=0 &&currentCalories != 0) {
                    System.out.println("current : " +currentCalories);
                    System.out.println("eat cal : " + sumOfEatCal);
                    System.out.println("move cal : " + sumofMoveCal);

                    gotCalories = findViewById(R.id.gotCalories);
                    lostCalories = findViewById(R.id.lostCalories);
                    charGotCalories_tv = findViewById(R.id.chartGotCalories_tv);

                    gotCalories.setText(String.valueOf(sumOfEatCal));
                    lostCalories.setText(String.valueOf(sumofMoveCal));
                    charGotCalories_tv.setText(String.valueOf((currentCalories + sumofMoveCal - sumOfEatCal)));

                    //gragh

                    //to percentage
                    int percentageOfBurn = 100 * (sumOfEatCal - sumofMoveCal)/currentCalories ;
                    int percentageOfLeft = 100 * (sumofMoveCal) / currentCalories;
//                    int percentageOfLeft = 100 * (currentCalories- sumOfEatCal) / currentCalories;
                    System.out.println(percentageOfBurn);

                    pieHelperArrayList.add(new PieHelper(percentageOfBurn, Color.rgb(78, 186, 106)));
                    pieHelperArrayList.add(new PieHelper(100-percentageOfBurn, Color.rgb(74, 141, 181)));
//                    pieHelperArrayList.add(new PieHelper(percentageOfLeft, Color.BLUE));

                    pieView.setDate(pieHelperArrayList);
                    pieView.showPercentLabel(false); //optional

                }else {
                    Toast.makeText(OverviewActivity.this,"Please record today's activity",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                if(databaseError != null) {
                    Toast.makeText(OverviewActivity.this,databaseError.getMessage(),Toast.LENGTH_LONG).show();
                }
            }


        });

    }
    public void goToRecord(View view) {
        intent = new Intent(this, EatActivity.class);
        startActivity(intent);
    }

    public void goToHistory(View view) {
        intent = new Intent(this, HistoryController.class);
        startActivity(intent);
    }

    public void newGoal(View view) {
        intent = new Intent(this, GetStart.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Override
    public void onBackPressed(){
        finishAffinity();
        Intent intent=new Intent(OverviewActivity.this, MainActivity.class);
        startActivity(intent);
    }
}