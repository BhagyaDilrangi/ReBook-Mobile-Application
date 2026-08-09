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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LeaderboardActivity extends AppCompatActivity {

    private RecyclerView rvLeaderboard;
    private LeaderboardAdapter adapter;
    private List<LeaderboardItem> leaderboardList;
    private DatabaseReference mDatabase;

    private final String DATABASE_URL = "https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        rvLeaderboard = findViewById(R.id.rvLeaderboard);
        rvLeaderboard.setLayoutManager(new LinearLayoutManager(this));

        leaderboardList = new ArrayList<>();
        adapter = new LeaderboardAdapter(leaderboardList);
        rvLeaderboard.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance(DATABASE_URL).getReference();

        // Dynamically compute and display top contributors based on highest added material count from users
        fetchTopContributorsLeaderboard();
    }

    private void fetchTopContributorsLeaderboard() {
        // 1. Fetch all users first to map names/profiles
        mDatabase.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot usersSnapshot) {
                Map<String, String> userNamesMap = new HashMap<>();
                for (DataSnapshot userSnap : usersSnapshot.getChildren()) {
                    String userId = userSnap.getKey();
                    String firstName = userSnap.child("firstName").getValue(String.class);
                    String lastName = userSnap.child("lastName").getValue(String.class);

                    String fullName = "User";
                    if (firstName != null && lastName != null) {
                        fullName = firstName + " " + lastName;
                    } else if (firstName != null) {
                        fullName = firstName;
                    }
                    if (userId != null) {
                        userNamesMap.put(userId, fullName);
                    }
                }

                // 2. Fetch materials and count how many each seller added to evaluate scores
                mDatabase.child("materials").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot materialsSnapshot) {
                        Map<String, Integer> sellerMaterialCounts = new HashMap<>();

                        for (DataSnapshot matSnap : materialsSnapshot.getChildren()) {
                            String sellerId = matSnap.child("sellerId").getValue(String.class);
                            if (sellerId != null) {
                                int currentCount = sellerMaterialCounts.getOrDefault(sellerId, 0);
                                sellerMaterialCounts.put(sellerId, currentCount + 1);
                            }
                        }

                        leaderboardList.clear();

                        // 3. Convert counted listings into LeaderboardItems (Score = materialCount * 10)
                        for (Map.Entry<String, Integer> entry : sellerMaterialCounts.entrySet()) {
                            String sellerId = entry.getKey();
                            int materialCount = entry.getValue();
                            int calculatedScore = materialCount * 10;

                            String sellerName = userNamesMap.getOrDefault(sellerId, "Student");

                            LeaderboardItem item = new LeaderboardItem();
                            // Adjust setter methods based on your LeaderboardItem DTO implementation if fields differ
                            item.setName(sellerName);
                            item.setScore(calculatedScore);

                            leaderboardList.add(item);
                        }

                        // 4. Sort descending so the highest contributor/score appears at the top
                        Collections.sort(leaderboardList, (o1, o2) -> Integer.compare(o2.getScore(), o1.getScore()));

                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(LeaderboardActivity.this, "Failed to load materials: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(LeaderboardActivity.this, "Failed to load users: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}