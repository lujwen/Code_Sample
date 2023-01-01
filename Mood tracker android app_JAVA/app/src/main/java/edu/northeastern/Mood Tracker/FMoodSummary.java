package edu.northeastern.mobileapplicationteam18;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class FMoodSummary extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    String userName;
    private TextView happyNumTV;
    private TextView sadNumTV;
    private TextView angryNumTV;
    private TextView embarrNumTV;
    private TextView hystNumTV;
    private TextView fatiNumTV;

    private TextView sumTV;

    String[] biggest = {"0","0"};
    String[] smallest = {"1", "1"};

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fmood_summary);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString("user_name");
        }

        happyNumTV = (TextView) findViewById(R.id.happyNumber);
        sadNumTV = (TextView) findViewById(R.id.sadNumber);
        angryNumTV = (TextView) findViewById(R.id.angryNumber);
        embarrNumTV = (TextView) findViewById(R.id.embNumber);
        hystNumTV = (TextView) findViewById(R.id.hysNumber);
        fatiNumTV = (TextView) findViewById(R.id.fatNumber);

        sumTV = (TextView) findViewById(R.id.summaryTV);
        readCountFromDB();
        generateSummary();

        // for navigation bar
        bottomNavigationView=findViewById(R.id.navigationBar);
        bottomNavigationView.setSelectedItemId(R.id.tracking);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.messaging:
                        Intent intentMessaging = new Intent(getApplicationContext(),FSendEmoji.class);
                        intentMessaging.putExtra("user_name", userName);
                        startActivity(intentMessaging);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.home:
                        Intent intent = new Intent(getApplicationContext(),FMoods.class);
                        intent.putExtra("user_name", userName);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.tracking:
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

    }// end of on create

    // read mood counts from DB
    private void readCountFromDB() {
        databaseReference.child("FUser").child(userName).child("MoodCount").get().addOnCompleteListener((task) -> {
            HashMap<String, Long> countMap = (HashMap) task.getResult().getValue();
            if (countMap != null) {
                System.out.println("countMap.keySet() :" + countMap.keySet() + "sum_____");
                System.out.println("countMap.values() :" + countMap.values() + "sum_____");
                for(String mood:countMap.keySet()){
                    if(mood.equals("Happy")){
                        String cntMood = countMap.get(mood) + "";
                        happyNumTV.setText(cntMood);
                    }
                    else if(mood.equals("Sad")){
                        String cntMood = countMap.get(mood) + "";
                        sadNumTV.setText(cntMood);
                    }
                    else if(mood.equals("Angry")){
                        String cntMood = countMap.get(mood) + "";
                        angryNumTV.setText(cntMood);
                    }
                    else if(mood.equals("Embarrassment")){
                        String cntMood = countMap.get(mood) + "";
                        embarrNumTV.setText(cntMood);
                    }
                    else if(mood.equals("Hysterical")){
                        String cntMood = countMap.get(mood) + "";
                        hystNumTV.setText(cntMood);
                    }else{
                            String cntMood = countMap.get(mood) + "";
                            fatiNumTV.setText(cntMood);

                    }
                }
            }
        });
    } // end of read mood count from db

    // generate mood summary
    private void generateSummary(){

        databaseReference.child("FUser").child(userName).child("MoodCount").get().addOnCompleteListener((task) -> {
            HashMap<String, Long> countMap = (HashMap) task.getResult().getValue();
            if (countMap != null){
                String init = countMap.keySet().iterator().next();
                biggest[0] = init;
                biggest[1] = "-1";
                smallest[0] = init;
                smallest[1] = String.valueOf(Double.POSITIVE_INFINITY);

                for(String mood : countMap.keySet()){
                    if(countMap.get(mood) >= Long.parseLong(biggest[1])){
                        Array.set(biggest, 0, mood);
                        Array.set(biggest, 1,  countMap.get(mood) + "");
                    }
                    else{
                        Array.set(smallest, 0, mood);
                        Array.set(smallest, 1,  countMap.get(mood) + "");
                    }
                }
                if (biggest[0] == smallest[0]){
                    sumTV.setText("Most of the time, you felt " + biggest[0]);
                }else{
                    sumTV.setText("Most of the time, you felt " + biggest[0] + ", and you barely " + smallest[0]);
                }

            }else{
                sumTV.setText("Start tracking your mood by selecting your mood on home page.");
            }
        });
    }

}