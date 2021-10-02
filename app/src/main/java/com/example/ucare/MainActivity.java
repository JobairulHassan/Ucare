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

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.ucare.calorie_counter.OverviewActivity;
import com.example.ucare.medicine.MedicineActivity;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
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

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView bnv;

    private CircleImageView userProfileImage;
    private StorageReference UserProfileImagesRef;
    private DatabaseReference RootRef;
    private String currentUserID ;
    private FirebaseAuth mAuth;
    private TextView username;
    private TextView userOccupation;
    private AdView mAdView,adView;

    private ImageSlider sliderView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bnv=findViewById(R.id.BottomNavigationView);
        userProfileImage=findViewById(R.id.user_pic);
        username=findViewById(R.id.user_name);
        userOccupation=findViewById(R.id.user_occupation);
        sliderView=findViewById(R.id.image_slider);
        bnv.setBackground(null);
        // add view
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                adView = findViewById(R.id.adView1);
                AdRequest adReq = new AdRequest.Builder().build();
                adView.loadAd(adReq);
            }
        });

        //extra line I am adding
        MobileAds.initialize(this);

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
//        adView = findViewById(R.id.adView1);
//        AdRequest adReq = new AdRequest.Builder().build();
//        adView.loadAd(adReq);

//        AdView adView = new AdView(this);
//        mAdView.setAdSize(AdSize.BANNER);
//        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

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
        sliderimg();

    }

    private void sliderimg() {
        final List<SlideModel> remoteImages=new ArrayList<>();
        RootRef.child("Image_Slider")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot data: snapshot.getChildren()){
                            remoteImages.add(new SlideModel(data.child("url").getValue().toString(), ScaleTypes.FIT));
                        }
                        sliderView.setImageList(remoteImages,ScaleTypes.FIT);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
                            String retrieveProfileAge =dataSnapshot.child("age").getValue().toString();

                            username.setText(retrieveUserName);
                            userOccupation.setText(retrieveProfileAge);
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