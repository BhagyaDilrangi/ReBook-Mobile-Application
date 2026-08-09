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
import com.nibm.rebook.CustomAdapter.ListingAdapter;
import com.nibm.rebook.dto.Material;

import java.util.ArrayList;
import java.util.List;

public class MyListingsActivity extends AppCompatActivity {

    private RecyclerView rvMyListings;
    private ListingAdapter adapter;
    private List<Material> materialList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    // Matched with your correct Firebase Realtime Database URL from google-services.json
    private final String DATABASE_URL = "https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mDatabase = FirebaseDatabase.getInstance(DATABASE_URL).getReference("materials");

        // 1. Initialize the RecyclerView
        rvMyListings = findViewById(R.id.rvMyListings);
        rvMyListings.setLayoutManager(new LinearLayoutManager(this));

        // 2. Prepare the data list
        materialList = new ArrayList<>();

        // 3. Initialize and set the Adapter
        adapter = new ListingAdapter(materialList);
        rvMyListings.setAdapter(adapter);

        // 4. Fetch User's Listings from Firebase
        fetchMyListings();
    }

    private void fetchMyListings() {
        if (mAuth.getCurrentUser() == null) return;

        String currentUserId = mAuth.getCurrentUser().getUid();

        // Query materials where sellerId matches the current user's UID
        mDatabase.orderByChild("sellerId").equalTo(currentUserId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        materialList.clear();
                        for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                            Material material = dataSnapshot.getValue(Material.class);
                            if (material != null) {
                                materialList.add(material);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(MyListingsActivity.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}