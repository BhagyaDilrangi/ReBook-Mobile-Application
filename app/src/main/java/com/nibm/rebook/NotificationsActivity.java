package com.nibm.rebook;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nibm.rebook.CustomAdapter.NotificationAdapter;
import com.nibm.rebook.dto.NotificationItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications); // Ensure this matches your XML filename

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        RecyclerView rv = findViewById(R.id.rvNotifications); // Matches your XML ID
        rv.setLayoutManager(new LinearLayoutManager(this));

        // Create Dummy Data
        List<NotificationItem> notifyList = new ArrayList<>();
        notifyList.add(new NotificationItem("Request Accepted", "Your request for 'Calculus Book' was accepted."));
        notifyList.add(new NotificationItem("New Message", "User 'Student B' sent you a message regarding a listing."));
        notifyList.add(new NotificationItem("Return Reminder", "Your book 'OS Essentials' is due tomorrow!"));

        // Set Adapter
        NotificationAdapter adapter = new NotificationAdapter(notifyList);
        rv.setAdapter(adapter);
    }
}