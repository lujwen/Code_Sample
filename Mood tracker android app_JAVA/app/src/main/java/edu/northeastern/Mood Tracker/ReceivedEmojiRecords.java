package edu.northeastern.mobileapplicationteam18;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ReceivedEmojiRecords extends AppCompatActivity {
    private List<Emoji> Emojis;
    private String myName;
    private DatabaseReference myDataBase;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_received_emoji_records);
        System.out.println("run get emoji...");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            myName = extras.getString("user_name");
        }
        myDataBase = FirebaseDatabase.getInstance().getReference();
        updateReceivedRecordsRV();
        readEmojisFromDB();
    }

    private void updateReceivedRecordsRV() {
        RecyclerView receivedRecordsRV = findViewById(R.id.receivedRecordsRV);
        receivedRecordsRV.setLayoutManager(new LinearLayoutManager(this));
        receivedRecordsRV.setAdapter(new EmojiAdapter(Emojis));
    }

    private void readEmojisFromDB() {
        Emojis = new ArrayList<>();
        System.out.println("read data from db...");
        myDataBase.child("emojis").get().addOnCompleteListener((task) -> {
            HashMap<String, HashMap<String, String>> tempMap = (HashMap) task.getResult().getValue();
            System.out.println("read hashmap...");
            if (tempMap == null) {
                System.out.println("empty hashmap....");
                return;
            }
            for (String entryKey : tempMap.keySet()) {
                String fromUser = Objects.requireNonNull(tempMap.get(entryKey)).get("fromUser");
                String id = String.valueOf(Objects.requireNonNull(tempMap.get(entryKey)).get("id"));
                String sendTime = Objects.requireNonNull(tempMap.get(entryKey)).get("sendTime");
                String toUser = Objects.requireNonNull(tempMap.get(entryKey)).get("toUser");
                if (toUser != null && toUser.equals(myName)) {
                    Emojis.add(new Emoji(Integer.parseInt(id), fromUser, toUser, sendTime));
                }
            }
            Emojis.sort(Collections.reverseOrder());
            updateReceivedRecordsRV();
        });
    }
}