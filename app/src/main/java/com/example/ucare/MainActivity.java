package com.example.ucare;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ucare.calorie_counter.EatActivity;
import com.example.ucare.calorie_counter.GetStart;
import com.example.ucare.calorie_counter.Overview;
import com.example.ucare.calorie_counter.OverviewActivity;
import com.example.ucare.medicine.MedicineActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnv;

    private CircleImageView userProfileImage;
    private StorageReference UserProfileImagesRef;
    private DatabaseReference RootRef;
    private String currentUserID ;
    private FirebaseAuth mAuth;
    private TextView username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bnv=findViewById(R.id.BottomNavigationView);
        userProfileImage=findViewById(R.id.user_pic);
        username=findViewById(R.id.user_name);
        bnv.setBackground(null);
        //profile info
        UserProfileImagesRef= FirebaseStorage.getInstance().getReference().child("Profile Images");
        mAuth= FirebaseAuth.getInstance();
        currentUserID=mAuth.getCurrentUser().getUid();
        RootRef= FirebaseDatabase.getInstance().getReference();
        //bnv.getMenu().getItem(1).setChecked(true);
        bnv.setSelectedItemId(R.id.holder);
        bnv.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();
                switch(id){
                    case R.id.account:
                        Intent intent=new Intent(MainActivity.this,profile_user.class);
                        startActivity(intent);
                        return true;

                }
                return false;
            }
        });
        retrive_userName_pic();

    }

    private void retrive_userName_pic() {
        RootRef.child("Users").child(currentUserID)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange( DataSnapshot dataSnapshot) {
                        if((dataSnapshot.exists()) &&(dataSnapshot.hasChild("name") &&(dataSnapshot.hasChild("image"))))
                        {
                            String retrieveUserName =dataSnapshot.child("name").getValue().toString();
                            String retrieveProfileImage =dataSnapshot.child("image").getValue().toString();

                            username.setText(retrieveUserName);
                            Picasso.get().load(retrieveProfileImage).into(userProfileImage);
                        }

                    }

                    @Override
                    public void onCancelled( DatabaseError error) {

                    }
                });
    }

    public void homeOnClick(View view){
        bnv.getMenu().getItem(0).setChecked(false);
        bnv.getMenu().getItem(1).setChecked(false);
    }
    public void bmiCalculate(View view){
        Intent intent=new Intent(MainActivity.this,bmi_calculator.class);
        startActivity(intent);
    }
    public void signup(View v){
        Intent intent=new Intent(MainActivity.this,sign_in.class);
        startActivity(intent);
    }
    public void pill_remainder(View v){
        Intent intent=new Intent(MainActivity.this, MedicineActivity.class);
        startActivity(intent);
    }

    public void FirstAid (View v){
        Intent intent=new Intent(MainActivity.this,firstAid_main.class);
        startActivity(intent);
    }

    public void pdf (View v){
        Intent intent=new Intent(MainActivity.this,med_pdf.class);
        startActivity(intent);
    }





    public void callorie_counter(View view) {
        Intent intent=new Intent(MainActivity.this, OverviewActivity.class);
        startActivity(intent);
    }

}