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

public class BuyerSavedItems extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListingAdapter adapter;
    private List<Material> savedList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_buyer_saved_items);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            finish();
            return;
        }

        String uid = mAuth.getCurrentUser().getUid();
        // Pointing to a hypothetical 'saved_items' node for this user
        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("saved_items").child(uid);

        recyclerView = findViewById(R.id.recyclerViewSavedItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        savedList = new ArrayList<>();
        adapter = new ListingAdapter(savedList, true);
        recyclerView.setAdapter(adapter);

        fetchSavedItems();
    }

    private void fetchSavedItems() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                savedList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    Material material = data.getValue(Material.class);
                    if (material != null) {
                        savedList.add(material);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BuyerSavedItems.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}