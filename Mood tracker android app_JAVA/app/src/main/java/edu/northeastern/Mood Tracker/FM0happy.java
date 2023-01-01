package edu.northeastern.mobileapplicationteam18;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FM0happy extends AppCompatActivity {

    String userName;
    TextView userInHappyTV;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fm0_happy);

        Bundle extras = getIntent().getExtras();
        System.out.println("extras in moods: " + extras );
        if (extras != null) {
            userName = extras.getString("user_name");
        }
        userInHappyTV = (TextView) findViewById(R.id.userInHappy);
        userInHappyTV.setText(userName);

        Button getlist0 = (Button) findViewById(R.id.list0);
        Button shareMood = (Button) findViewById(R.id.shareMood);

        getlist0.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FM0happy.this, FHappyActivityList.class);
                intent.putExtra("user_name", userName);
                startActivity(intent);
            }
        });

        shareMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FM0happy.this, FSendEmoji.class);
                intent.putExtra("user_name", userName);
                startActivity(intent);
            }
        });

        // for navigation bar
        BottomNavigationView bottomNavigationView=(BottomNavigationView) findViewById(R.id.navigationBar);
        bottomNavigationView=(BottomNavigationView) findViewById(R.id.navigationBar);
        // Set Home selected
        bottomNavigationView.setSelectedItemId(R.id.home);
        // Perform item selected listener
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
                        Intent intentHome = new Intent(getApplicationContext(),FMoods.class);
                        intentHome.putExtra("user_name", userName);
                        startActivity(intentHome);
                        overridePendingTransition(0,0);
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
    } // end of on create
}