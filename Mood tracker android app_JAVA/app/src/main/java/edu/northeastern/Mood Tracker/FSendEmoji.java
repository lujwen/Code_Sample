package edu.northeastern.mobileapplicationteam18;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import edu.northeastern.mobileapplicationteam18.databinding.ActivityFmoodsBinding;
import edu.northeastern.mobileapplicationteam18.databinding.ActivityFsendEmojiBinding;

public class FSendEmoji extends AppCompatActivity implements LocationListener {
    BottomNavigationView bottomNavigationView;
    ActivityFsendEmojiBinding binding;
    private DatabaseReference userDB;

    private FirebaseDatabase firebaseDatabase;
    private static final int PERMISSIONS_FINE_LOCATION = 99;
    private ImageView emojiAngry, emojiConfused, emojiHeartbreak, emojiSad, emojiSleepy, emojiNaughty;
    private TextView emojiAngryTV, emojiConfusedTV, emojiHeartbreakTV, emojiSadTV, emojiSleepyTV, emojiNaughtyTV;
    private String userName;
    private TextView userNameTV;
    private Spinner friends;
    private Map<ImageView, Integer> emojiSentCount = new HashMap<>();
    private Map<ImageView, Boolean> emojiClicked = new HashMap<>();
    private Map<ImageView, TextView> emojiWithText = new HashMap<>();
    private Map<String, String> userNameUserIdPair = new HashMap<>();
    private Map<String, String> userIdUserNamePair = new HashMap<>();
    public static final String DATE = "yyyy-MM-dd hh:mm:ss";
    private static String SERVER_KEY;
    private TextView laTVSender;
    private TextView loTVSender;
    private TextView laTVReceiver;
    private TextView loTVReceiver;
    private TextView moodtv;
    private String mood;
    private TextView moodTVReceiver;
    private String moodReceiver;
    private Location startLocation;
    private LocationManager locationManager;
    private DatabaseReference ref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        System.out.println("extras in send : " + extras);
        if (extras != null) {
            userName = extras.getString("user_name");
            System.out.println("username in send : " + userName);
        }

        SERVER_KEY = "key=" + getProperties(getApplicationContext()).getProperty("SERVER_KEY");
        setContentView(R.layout.activity_fsend_emoji);
        createNotificationChannel();
        Button received = (Button) findViewById(R.id.receivedButton);
        friends = findViewById(R.id.ffriend_spinner);
        userDB = FirebaseDatabase.getInstance().getReference();
        userNameTV = (TextView) findViewById(R.id.userName);
//        userNameTV.setText(userName);
        laTVSender = (TextView) findViewById(R.id.txtLa);
        loTVSender = (TextView) findViewById(R.id.txtLo);
        laTVReceiver = (TextView) findViewById(R.id.txtLaReceiver);
        loTVReceiver = (TextView) findViewById(R.id.txtLoReceiver);
        moodtv = (TextView) findViewById(R.id.selectFriend);
        moodTVReceiver = (TextView) findViewById(R.id.moodReceiver);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        initializeAllEmoji();
        readDataFromDB();
        initializeSpinner();

        // for navigation bar
        bottomNavigationView=(BottomNavigationView) findViewById(R.id.navigationBar);
        bottomNavigationView.setSelectedItemId(R.id.messaging);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch(item.getItemId())
                {
                    case R.id.messaging:
                        return true;

                    case R.id.home:
                        Intent intent = new Intent(getApplicationContext(),FMoods.class);
                        intent.putExtra("user_name", userName);
                        startActivity(intent);
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
        }); //end of navigation bar


        getLocation();

        received.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FSendEmoji.this, FReceiveEmoji.class);
                intent.putExtra("user_name", userName);
                startActivity(intent);
            }
        });

        userDB.child("FUser").child(userName).child("mood").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mood = snapshot.getValue().toString();
                laTVSender.setText(mood);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


    public static Properties getProperties(Context context)  {
        Properties properties = new Properties();
        AssetManager assetManager = context.getAssets();
        InputStream inputStream = null;
        try {
            inputStream = assetManager.open("firebase.properties");
            properties.load(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }


    private void readDataFromDB() {
        userDB.child("emojis").get().addOnCompleteListener((task) -> {
            HashMap<String, HashMap<String, String>> emojiMap = (HashMap) task.getResult().getValue();
            if (!emojiMap.isEmpty()) {
                for (String entryKey : emojiMap.keySet()) {
                    String fromUser = emojiMap.get(entryKey).get("fromUser");
                    if (fromUser != null && fromUser.equals(userName)) {
                        String id = String.valueOf(emojiMap.get(entryKey).get("id"));
                        ImageView imageViewRef = getImageViewById(Integer.parseInt(id));
                        emojiSentCount.merge(imageViewRef, 1, Integer::sum);
                    }
                }
            }
        });
    }

    // set spinner
    private void initializeSpinner() {
        userDB.child("FUser").get().addOnCompleteListener((task) -> {
            HashMap<String, HashMap<String, String>> myMap = (HashMap) task.getResult().getValue();
            List<String> userNames = new ArrayList<>();
            for (String userId : myMap.keySet()) {
                String anotherUserName = myMap.get(userId).get("name");
                String userCity = myMap.get(userId).get("city");
                if (anotherUserName == null || anotherUserName.equals(userName)) {
                    continue;
                }
               String userNameCity = anotherUserName + " @ " + userCity;
                    userNames.add(userNameCity);
                    userIdUserNamePair.put(userId, userNameCity);
                    userNameUserIdPair.put(userNameCity, userId);
            }
            ArrayAdapter<String> adapter
                    = new ArrayAdapter<>(getApplicationContext(),
                    androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                    userNames);
            friends.setAdapter(adapter);
            String selectedUsername = friends.getSelectedItem().toString();
            userDB.child("FUser").child(selectedUsername).child("mood").addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.getValue() == null){
                        moodTVReceiver.setText("Unavailable");
                    } else{
                        moodReceiver = snapshot.getValue().toString();
                        moodTVReceiver.setText("Mood: "+moodReceiver);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                }
            });

        });
    }  // end of spinner

    public void emojiOnClickListener(View v) {
        if (emojiClicked.get(v)) {
            unselectEmoji((ImageView) v);
        } else {
            for (ImageView emoji : emojiClicked.keySet()) {
                unselectEmoji(emoji);
            }
            ((ImageView) v).setColorFilter(ContextCompat
                            .getColor(this.getApplicationContext()
                                    , R.color.blue_200)
                    , android.graphics.PorterDuff.Mode.MULTIPLY);
            emojiClicked.put((ImageView) v, true);
        }
    }

    private void unselectEmoji(ImageView v) {
        v.setColorFilter(null);
        emojiClicked.put(v, false);
    }

    // cited from `https://developer.android.com/develop/ui/views/notifications/channels`
    public void createNotificationChannel() {
        CharSequence name = getString(R.string.channel_name);
        String description = getString(R.string.channel_description);
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel(getString(R.string.channel_id), name, importance);
        channel.setDescription(description);
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);
    }

    public void onSendButtonPressed(View v) {
        String selectedUsername = friends.getSelectedItem().toString();
        System.out.println("selected user name in send: " + selectedUsername);
        int selectedEmojiId = getCurrentSelectedId();
        if (selectedEmojiId == -1) {
            Context context = getApplicationContext();
            Toast.makeText(context, "no Emoji is selected for " + selectedUsername, Toast.LENGTH_SHORT).show();
            return;
        }
        // debug received list here
        Emoji emoji = new Emoji(selectedEmojiId, userName, selectedUsername.split(" ")[0], timeNow());
        userDB.child("emojis").child(emoji.getKey()).setValue(emoji).addOnSuccessListener(
                (task) -> {
                    Context context_success = getApplicationContext();
                    CharSequence text_success = "Sticker successfully send to " + selectedUsername;
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context_success, text_success, duration);
                    toast.show();

                    ImageView imageViewById = getImageViewById(selectedEmojiId);
                    if (imageViewById == null) {
                        return;
                    }
                    emojiSentCount.merge(getImageViewById(selectedEmojiId), 1, Integer::sum);

                });
//        userDB.child("emojis").child(emoji.getKey()).setValue(emoji);
        userDB.child("FUser").child(userNameUserIdPair.get(selectedUsername)).get().addOnCompleteListener((task) -> {
        });
        Toast.makeText(getApplicationContext(), "Send successfully", Toast.LENGTH_SHORT).show();
    }

    //send emoji notification to users
    private void sendEmojiNotification(String targetToken, Emoji emoji) {
        JSONObject jPayload = new JSONObject();
        JSONObject jNotification = new JSONObject();
        JSONObject jdata = new JSONObject();
        try {
            String title = "You have a message sent from " + emoji.fromUser;
            jNotification.put("title", title);
            jdata.put("title", "data:" + title);
            jdata.put("image_id", emoji.id);
            jPayload.put("to", targetToken);
            jPayload.put("priority", "high");
            jPayload.put("notification", jNotification);
            jPayload.put("data", jdata);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        final String response = fcmHttpConnection(SERVER_KEY, jPayload);
    }

    //cited from `https://firebase.google.com/docs/cloud-messaging/http-server-ref`
    private static String fcmHttpConnection(String serverToken, JSONObject jsonObject) {
        try {
            URL url = new URL("https://fcm.googleapis.com/fcm/send");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", serverToken);
            connection.setDoOutput(true);
            OutputStream outputStream = connection.getOutputStream();
            outputStream.write(jsonObject.toString().getBytes());
            outputStream.close();
            InputStream inputStream = connection.getInputStream();
            return convertStreamToString(inputStream);
        } catch (IOException e) {
            return "NULL";
        }
    }

    // cited from `https://www.baeldung.com/convert-input-stream-to-string`
    private static String convertStreamToString(InputStream inputStream) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String len;
            while ((len = bufferedReader.readLine()) != null) {
                stringBuilder.append(len);
            }
            bufferedReader.close();
            return stringBuilder.toString().replace(",", ",\n");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static String timeNow() {
        Calendar cal = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat myDate = new SimpleDateFormat(DATE);
        String dateFormat = myDate.format(cal.getTime());
        return dateFormat;
    }

    private ImageView getImageViewById(int id) {
        if (id == 1) {
            return emojiAngry;
        } else if (id == 2) {
            return emojiConfused;
        } else if (id == 3) {
            return emojiHeartbreak;
        } else if (id == 4) {
            return emojiSad;
        } else if (id == 5) {
            return emojiSleepy;
        } else if (id == 6) {
            return emojiNaughty;
        }
        return null;
    }

    @SuppressLint("NonConstantResourceId")
    public int getCurrentSelectedId() {
        for (ImageView imageView : emojiClicked.keySet()) {
            if (Boolean.TRUE.equals(emojiClicked.get(imageView))) {
                switch (imageView.getId()) {
                    case R.id.emojiAngry:
                        return 1;
                    case R.id.emojiConfused:
                        return 2;
                    case R.id.emojiHeartBreak:
                        return 3;
                    case R.id.emojiSad:
                        return 4;
                    case R.id.emojiSleepy:
                        return 5;
                    case R.id.emojiNaughty:
                        return 6;
                    default:
                        return -1;
                }
            }
        }
        return -1;
    }

    private void initializeAllEmoji() {
        emojiAngry = findViewById(R.id.emojiAngry);
        emojiConfused = findViewById(R.id.emojiConfused);
        emojiHeartbreak = findViewById(R.id.emojiHeartBreak);
        emojiSad = findViewById(R.id.emojiSad);
        emojiSleepy = findViewById(R.id.emojiSleepy);
        emojiNaughty = findViewById(R.id.emojiNaughty);
        emojiAngry.setClickable(true);
        emojiConfused.setClickable(true);
        emojiHeartbreak.setClickable(true);
        emojiSad.setClickable(true);
        emojiSleepy.setClickable(true);
        emojiNaughty.setClickable(true);
        emojiClicked.put(emojiAngry, false);
        emojiClicked.put(emojiConfused, false);
        emojiClicked.put(emojiHeartbreak, false);
        emojiClicked.put(emojiSad, false);
        emojiClicked.put(emojiSleepy, false);
        emojiClicked.put(emojiNaughty, false);
        emojiAngry.setOnClickListener((v) -> emojiOnClickListener(v));
        emojiConfused.setOnClickListener((v) -> emojiOnClickListener(v));
        emojiHeartbreak.setOnClickListener((v) -> emojiOnClickListener(v));
        emojiSad.setOnClickListener((v) -> emojiOnClickListener(v));
        emojiSleepy.setOnClickListener((v) -> emojiOnClickListener(v));
        emojiNaughty.setOnClickListener((v) -> emojiOnClickListener(v));
        emojiSentCount.put(emojiAngry, 0);
        emojiSentCount.put(emojiConfused, 0);
        emojiSentCount.put(emojiHeartbreak, 0);
        emojiSentCount.put(emojiSad, 0);
        emojiSentCount.put(emojiSleepy, 0);
        emojiSentCount.put(emojiNaughty, 0);
        emojiAngryTV = findViewById(R.id.emojiAngrySentCountTV);
        emojiConfusedTV = findViewById(R.id.emojiConfusedSentCountTV);
        emojiHeartbreakTV = findViewById(R.id.emojiHeartBreakSentCountTV);
        emojiSadTV = findViewById(R.id.emojiSadSentCountTV);
        emojiSleepyTV = findViewById(R.id.emojiSleepySentCountTV);
        emojiNaughtyTV = findViewById(R.id.emojiNaughtySentCountTV);
        emojiWithText.put(emojiAngry, emojiAngryTV);
        emojiWithText.put(emojiConfused, emojiConfusedTV);
        emojiWithText.put(emojiHeartbreak, emojiHeartbreakTV);
        emojiWithText.put(emojiSad, emojiSadTV);
        emojiWithText.put(emojiSleepy, emojiSleepyTV);
        emojiWithText.put(emojiNaughty, emojiNaughtyTV);
    }
    private void getLocation(){
        if (ActivityCompat.checkSelfPermission(FSendEmoji.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, this);
        }
        else{
            ActivityCompat.requestPermissions(FSendEmoji.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_FINE_LOCATION);
        }
    }
    @Override
    public void onLocationChanged(@NonNull Location location) {
        if (startLocation == null) {
            startLocation = location;
        }

        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String city = getCity(latitude, longitude);

        if(city == ""){
//            loTVSender.setText(userName + " is at a mysterious place.");
            userNameTV.setText(userName + " is at a mysterious place.");
        }
        loTVSender.setText("@ " + city);
        userNameTV.setText(userName );
        postToDatabase(location);
//        adding user location data to database
    }

    public void postToDatabase(Location location) {
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();
        String city = getCity(latitude, longitude);

        userDB.child("FUser").child(userName).child("location").child("latitude").setValue(latitude);
        userDB.child("FUser").child(userName).child("location").child("longitude").setValue(longitude);
        userDB.child("FUser").child(userName).child("location").child("city").setValue(city);
        userDB.child("FUser").child(userName).child("city").setValue(city);
    }


    // get city name by la and lo
    private String getCity(double la, double lo){
        String cityName = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try{
            addresses = geocoder.getFromLocation(la,lo,10);
            if (addresses.size() > 0){
                for(Address ad : addresses){
                    if (ad.getLocality() != null && ad.getLocality().length() > 0){
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
}

