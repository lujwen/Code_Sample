package edu.northeastern.mobileapplicationteam18;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import edu.northeastern.mobileapplicationteam18.databinding.ActivityFmoodsBinding;

public class FMoods extends AppCompatActivity implements LocationListener {
    ActivityFmoodsBinding binding;
    String userName;
    BottomNavigationView bottomNavigationView;

    private LocationManager locationManager;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    GridLayout mainGrid;
    Integer count = 0;
    Integer cntMood = 0;
    private TextView currUserNameTV;
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fmoods);

        Bundle extras = getIntent().getExtras();
        System.out.println("extras in moods: " + extras);
        if (extras != null) {
            userName = extras.getString("user_name");
        }

        // for navigation bar
        bottomNavigationView=(BottomNavigationView) findViewById(R.id.navigationBar);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.messaging:
                        Intent intent = new Intent(getApplicationContext(),FSendEmoji.class);
                        intent.putExtra("user_name", userName);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
                        return true;

                    case R.id.tracking:
                        Intent intentTracking = new Intent(getApplicationContext(),FMoodSummary.class);
                        intentTracking.putExtra("user_name", userName);
                        startActivity(intentTracking);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.moment:
                        Intent intentMoment = new Intent(getApplicationContext(),FMemory.class);
                        intentMoment.putExtra("user_name", userName);
                        startActivity(intentMoment);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getLocation();
        currUserNameTV = (TextView) findViewById(R.id.currentUser);
        currUserNameTV.setText(userName);
        mainGrid = (GridLayout) findViewById(R.id.mainGrid);

        CardView cardView0 = (CardView) mainGrid.getChildAt(0);
        CardView cardView1 = (CardView) mainGrid.getChildAt(1);
        CardView cardView2 = (CardView) mainGrid.getChildAt(2);
        CardView cardView3 = (CardView) mainGrid.getChildAt(3);
        CardView cardView4 = (CardView) mainGrid.getChildAt(4);
        CardView cardView5 = (CardView) mainGrid.getChildAt(5);
        CardView cardView6 = (CardView) mainGrid.getChildAt(6);

        // 0 - press happy mood
        cardView0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("FUser").child(userName).child("mood").setValue("Happy");
                updateCountFromDB("Happy");
                Intent intent = new Intent(FMoods.this, FM0happy.class);
                intent.putExtra("user_name", userName);
                startActivity(intent);
            }
        });


        // 1:press angry mood
        cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("FUser").child(userName).child("mood").setValue("Angry");
                updateCountFromDB("Angry");
                Intent intent = new Intent(FMoods.this, FM1Angry.class);
                intent.putExtra("user_name", userName);
                startActivity(intent);
            }
        });

        //2-press sad mood
        cardView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("FUser").child(userName).child("mood").setValue("Sad");
                updateCountFromDB("Sad");
                Intent intent = new Intent(FMoods.this, FM2Sad.class);
                intent.putExtra("user_name", userName);
                startActivity(intent);
            }
        });

        //3 - press Hysterical mood
        cardView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("FUser").child(userName).child("mood").setValue("Hysterical");
                updateCountFromDB("Hysterical");
                Intent intent = new Intent(FMoods.this, FM3Hysterical.class);
                intent.putExtra("user_name", userName);
                startActivity(intent);
            }
        });

        //4 - press embarrassed mood
        cardView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("FUser").child(userName).child("mood").setValue("Embarrassment");
                updateCountFromDB("Embarrassment");
                Intent intent = new Intent(FMoods.this, FM4Embarrassed.class);
                intent.putExtra("user_name", userName);
                startActivity(intent);
            }
        });

        //5 -  press Fatigued mood
        cardView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.child("FUser").child(userName).child("mood").setValue("Fatigued");
                updateCountFromDB("Fatigued");
                Intent intent = new Intent(FMoods.this, FM5Fatigued.class);
                intent.putExtra("user_name", userName);
                startActivity(intent);
            }
        });
    }  // end of onCreate

    // for navigation bar
    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.scrollView,fragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        count++;
        if (count == 2) {
            super.onBackPressed();
            Intent intent = new Intent(FMoods.this, Mood.class);
            startActivity(intent);

        } else {
            Toast.makeText(getBaseContext(), "Click again to sign out", Toast.LENGTH_SHORT).show();
        }
    }

    // get user's location(la, lo, city name) when sign in and post to database
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(FMoods.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, (LocationListener) this);
        } else {
            ActivityCompat.requestPermissions(FMoods.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        postToDatabase(location);
    }

    public void postToDatabase(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String city = getCity(latitude, longitude);

        databaseReference.child("FUser").child(userName).child("location").child("latitude").setValue(latitude);
        databaseReference.child("FUser").child(userName).child("location").child("longitude").setValue(longitude);
        databaseReference.child("FUser").child(userName).child("location").child("city").setValue(city);
        databaseReference.child("FUser").child(userName).child("city").setValue(city);
    }

    // get city name by la and lo
    private String getCity(double la, double lo) {
        String cityName = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(la, lo, 10);
            if (addresses.size() > 0) {
                for (Address ad : addresses) {
                    if (ad.getLocality() != null && ad.getLocality().length() > 0) {
                        cityName = ad.getLocality();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }

    // update mood counts from DB
    private void updateCountFromDB(String mood) {
        databaseReference.child("FUser").child(userName).child("MoodCount").get().addOnCompleteListener((task) -> {
            HashMap<String, Long> localCountMap = (HashMap<String, Long>) task.getResult().getValue();
            Integer cnt = Math.toIntExact(localCountMap.get(mood));
            databaseReference.child("FUser").child(userName).child("MoodCount").child(mood).setValue(cnt + 1);
        });
    }

}


