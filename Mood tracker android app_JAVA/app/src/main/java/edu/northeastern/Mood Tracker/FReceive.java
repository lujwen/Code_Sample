//package edu.northeastern.mobileapplicationteam18;
//
//import androidx.annotation.NonNull;
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.view.MenuItem;
//
//import com.google.android.material.bottomnavigation.BottomNavigationView;
//
//public class FReceive extends AppCompatActivity {
//    BottomNavigationView bottomNavigationView;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_freceive);
//
//        bottomNavigationView = findViewById(R.id.bottomNavigationView);
//        bottomNavigationView.setSelectedItemId(R.id.send);
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId())
//                {
//                    case R.id.search:
//                        startActivity(new Intent(getApplicationContext(), FSearch.class));
//                        overridePendingTransition(0, 0);
//                        return true;
//                    case R.id.list:
//                        startActivity(new Intent(getApplicationContext(), FAct0.class));
//                        overridePendingTransition(0, 0);
//                        return true;
//                    case R.id.send:
//                        startActivity(new Intent(getApplicationContext(), FSend.class));
//                        overridePendingTransition(0, 0);
//                        return true;
//                    case R.id.receive:
//                        return true;
//                }
//                return false;
//            }
//        });
//    }
//}