package com.nibm.rebook;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nibm.rebook.CustomAdapter.NotificationAdapter;
import com.nibm.rebook.dto.NotificationItem;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity {

    private RecyclerView rvNotifications;
    private NotificationAdapter adapter;
    private List<NotificationItem> notifyList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private final String DATABASE_URL = "https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String currentUserId = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance(DATABASE_URL).getReference();

        rvNotifications = findViewById(R.id.rvNotifications);
        rvNotifications.setLayoutManager(new LinearLayoutManager(this));

        notifyList = new ArrayList<>();
        adapter = new NotificationAdapter(notifyList);
        rvNotifications.setAdapter(adapter);

        // Fetch notifications targeted specifically to this logged-in seller/user profile
        fetchSellerNotifications(currentUserId);
    }

    private void fetchSellerNotifications(String sellerId) {
        // Query notifications where the sellerId matches the current user or stored under notifications/{uid}
        mDatabase.child("notifications").child(sellerId).orderByChild("timestamp")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        notifyList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            NotificationItem item = data.getValue(NotificationItem.class);
                            if (item != null) {
                                notifyList.add(0, item); // Newest notifications at the top
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(NotificationsActivity.this, "Error loading notifications: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}