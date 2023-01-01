package edu.northeastern.mobileapplicationteam18;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.widget.EditText;
import android.provider.Settings.Secure;

public class Realtime extends AppCompatActivity {
    private EditText enterName;
    private Button loginBtn;
    private DatabaseReference userDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime);
        enterName = findViewById(R.id.editUsername);
        loginBtn = findViewById(R.id.login_button);
        userDB = FirebaseDatabase.getInstance().getReference();
        loginBtn.setOnClickListener(v -> userAccount());
    }

    public void userAccount() {
        String userName;
        userName = enterName.getText().toString();
        @SuppressLint("HardwareIds") String android_id = Secure.getString(getApplicationContext().getContentResolver(), Secure.ANDROID_ID);
        if (TextUtils.isEmpty(userName)) {
            Toast.makeText(getApplicationContext(), "Username cannot be empty",  Toast.LENGTH_LONG).show();
            return;
        }
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Log.w("RealTimeActivity", "Failed to fetch FCM registration", task.getException());
                        return;
                    }
                    String token = task.getResult();
                    User user = new User(userName, android_id, token);
                    userDB.child("users").child(android_id).setValue(user);
                });
        Intent sendEmoji = new Intent(this, SentEmojiActivity.class);
        sendEmoji.putExtra("user_name", userName);
        startActivity(sendEmoji);
    }
}