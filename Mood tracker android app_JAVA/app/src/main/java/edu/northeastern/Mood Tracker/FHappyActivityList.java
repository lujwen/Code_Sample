package edu.northeastern.mobileapplicationteam18;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class FHappyActivityList extends AppCompatActivity implements FAdapter.OnItemClickListener{
    private RecyclerView mRecyclerView;
    private FAdapter mAdapter;
    private DatabaseReference mDatabaseReference;
    private List<FActivity> factivity;
    private ProgressBar mProgressCircle;
    public FloatingActionButton addBtn2;
    private FirebaseStorage mStorage;
    private ValueEventListener mDBListener;
    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fimage_happylist);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            userName = extras.getString("user_name");
        }

        addBtn2 = findViewById(R.id.addActivityBtn);

        addBtn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FHappyActivityList.this, FUploadActivityHappy.class);
                startActivity(intent);
            }
        });


        mRecyclerView = findViewById(R.id.mRecyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressCircle = findViewById(R.id.progress_circle);

        factivity = new ArrayList<>();
        mStorage = FirebaseStorage.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("teachers_uploads");
        mDBListener = mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                factivity.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()){
                    FActivity fActivity = postSnapshot.getValue(FActivity.class);
                    fActivity.setKey(postSnapshot.getKey());
                    factivity.add(fActivity);
                }
                mAdapter = new FAdapter(FHappyActivityList.this, factivity);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(FHappyActivityList.this);
                mProgressCircle.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(FHappyActivityList.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
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
                        Intent intentMessage = new Intent(getApplicationContext(),FSendEmoji.class);
                        intentMessage.putExtra("user_name", userName);
                        startActivity(intentMessage);
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





    @Override
    public void onItemClick(int position) {
        //Toast.makeText(this, "Normal click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteItemClick(int position) {
        FActivity item = factivity.get(position);
        String itemKey = item.getKey();
        StorageReference itemRef = mStorage.getReferenceFromUrl(item.getImageUrl());
        itemRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseReference.child(itemKey).removeValue();
                Toast.makeText(FHappyActivityList.this, "Activity deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDatabaseReference.removeEventListener(mDBListener);
    }

}
