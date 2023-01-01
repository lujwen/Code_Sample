package edu.northeastern.mobileapplicationteam18;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FM2Sad extends AppCompatActivity {
    String userName;
    TextView userInSadTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fm2_sad);

        Bundle extras = getIntent().getExtras();
        System.out.println("extras in moods in sad: " + extras );
        if (extras != null) {
            userName = extras.getString("user_name");
        }
        userInSadTV = (TextView) findViewById(R.id.userInSad);
        userInSadTV.setText(userName);
        Button shareMood = (Button) findViewById(R.id.shareMood);

        Button getlist2 = (Button) findViewById(R.id.list2);
//        Button addlist2 = (Button) findViewById(R.id.addlist2);
        getlist2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FM2Sad.this, FImage2.class);
                intent.putExtra("user_name", userName);
                startActivity(intent);
            }
        });

        shareMood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FM2Sad.this, FSendEmoji.class);
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
    }// end of oon create
}