package edu.northeastern.mobileapplicationteam18;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

public class FSignup extends AppCompatActivity {
    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fsignup);

        EditText username = (EditText) findViewById(R.id.username);
        EditText email = (EditText) findViewById(R.id.email);
        EditText password = (EditText) findViewById(R.id.password);
        EditText repassword = (EditText) findViewById(R.id.repassword);
        MaterialButton regbtn = (MaterialButton) findViewById(R.id.signupbtn);
        MaterialButton loginbtn = (MaterialButton) findViewById(R.id.loginbtn);


        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // get data from EditTexts into String variables
                String nameTxt = username.getText().toString();
                String emailTxt = email.getText().toString();
                String passwordTxt = password.getText().toString();
                String repasswordTxt = repassword.getText().toString();

                @SuppressLint("HardwareIds") String android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

                // check if user fill all the fields before sending data to firebase
                if (nameTxt.isEmpty() || emailTxt.isEmpty() || passwordTxt.isEmpty() || repasswordTxt.isEmpty()) {
                    Toast.makeText(FSignup.this,"Please Fill All Fields",Toast.LENGTH_SHORT).show();
                }

                // check if passwords are matching with each other
                // if not matching with each other, then show a toast message
                else if (!passwordTxt.equals(repasswordTxt)){
                    Toast.makeText(FSignup.this,"Passwords Do Not Match",Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseReference.child("FUser").child("abc").child("email").setValue("def");
                    databaseReference.child("FUser").addListenerForSingleValueEvent(new ValueEventListener() {

                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            // check if email is not registered before
                            if (snapshot.hasChild(nameTxt)){
                                Toast.makeText(FSignup.this, "Username is already registered", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                // sending data to firebase Realtime Database
                                databaseReference.child("FUser").child(nameTxt).child("email").setValue(emailTxt);
                                databaseReference.child("FUser").child(nameTxt).child("name").setValue(nameTxt);
                                databaseReference.child("FUser").child(nameTxt).child("password").setValue(passwordTxt);
                                databaseReference.child("FUser").child(nameTxt).child("mood").setValue("Happy");
                                databaseReference.child("FUser").child(nameTxt).child("MoodCount").child("Angry").setValue(0);
                                databaseReference.child("FUser").child(nameTxt).child("MoodCount").child("Embarrassment").setValue(0);
                                databaseReference.child("FUser").child(nameTxt).child("MoodCount").child("Fatigued").setValue(0);
                                databaseReference.child("FUser").child(nameTxt).child("MoodCount").child("Happy").setValue(0);
                                databaseReference.child("FUser").child(nameTxt).child("MoodCount").child("Hysterical").setValue(0);
                                databaseReference.child("FUser").child(nameTxt).child("MoodCount").child("Sad").setValue(0);
                                FirebaseMessaging.getInstance().getToken()
                                        .addOnCompleteListener(task -> {
                                            if (!task.isSuccessful()) {
                                                Log.w("MainActivity", "onCreate Fetching FCM registration token failed", task.getException());
                                                return;
                                            }
                                            String token = task.getResult();
                                            databaseReference.child("FUser").child(nameTxt).child("token").setValue(token);
                                            databaseReference.child("FUser").child(nameTxt).child("android_id").setValue(android_id);
                                        });

                                Toast.makeText(FSignup.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(FSignup.this, FMoods.class);
                                intent.putExtra("user_name", nameTxt);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                }
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(FSignup.this, Mood.class);
        startActivity(intent);
    }


}