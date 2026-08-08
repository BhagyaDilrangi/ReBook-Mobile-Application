package com.nibm.rebook;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nibm.rebook.CustomAdapter.LeaderboardAdapter;
import com.nibm.rebook.dto.LeaderboardItem;

import java.util.ArrayList;
import java.util.List;

public class LeaderboardActivity extends AppCompatActivity {
    
    private RecyclerView rvLeaderboard;
    private LeaderboardAdapter adapter;
    private List<LeaderboardItem> leaderboardList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        rvLeaderboard = findViewById(R.id.rvLeaderboard);
        rvLeaderboard.setLayoutManager(new LinearLayoutManager(this));

        leaderboardList = new ArrayList<>();
        adapter = new LeaderboardAdapter(leaderboardList);
        rvLeaderboard.setAdapter(adapter);

        // Initialize Firebase Database Reference
        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("leaderboard");

        // Fetch data from Firebase
        mDatabase.orderByChild("score").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                leaderboardList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    LeaderboardItem item = dataSnapshot.getValue(LeaderboardItem.class);
                    if (item != null) {
                        // Adding to index 0 to show highest score at top if ascending, 
                        // or better yet, handle sorting via Firebase or Collections.reverse
                        leaderboardList.add(0, item);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LeaderboardActivity.this, "Failed to load leaderboard: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}