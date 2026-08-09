package com.nibm.rebook;

import android.annotation.SuppressLint;
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
import com.nibm.rebook.CustomAdapter.ListingAdapter;
import com.nibm.rebook.dto.Material;

import java.util.ArrayList;
import java.util.List;

public class BuyerHome extends AppCompatActivity {

    private RecyclerView rvBuyerMaterials;
    private ListingAdapter adapter;
    private List<Material> materialList;
    private List<Material> fullMaterialList;
    private DatabaseReference mDatabase;

    private final String DATABASE_URL = "https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_buyer_home);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mDatabase = FirebaseDatabase.getInstance(DATABASE_URL).getReference("materials");

        // Initialize RecyclerView
        rvBuyerMaterials = findViewById(R.id.rvBuyerMaterials);
        rvBuyerMaterials.setLayoutManager(new LinearLayoutManager(this));

        materialList = new ArrayList<>();
        fullMaterialList = new ArrayList<>();

        adapter = new ListingAdapter(materialList, true);
        rvBuyerMaterials.setAdapter(adapter);

        fetchAllMaterials();
        setupCategoryFilters();
    }

    private void fetchAllMaterials() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                fullMaterialList.clear();
                materialList.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Material material = dataSnapshot.getValue(Material.class);
                    if (material != null) {
                        fullMaterialList.add(material);
                        materialList.add(material);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(BuyerHome.this, "Failed to load materials: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupCategoryFilters() {
        // Updated IDs to match activity_user_buyer_home.xml
        try {
            findViewById(R.id.btnFilterAll).setOnClickListener(v -> filterByCategory("All"));
            findViewById(R.id.btnBooks).setOnClickListener(v -> filterByCategory("Books"));
            findViewById(R.id.btnNotes).setOnClickListener(v -> filterByCategory("Notes"));
            findViewById(R.id.btnPastPapers).setOnClickListener(v -> filterByCategory("Past Papers"));
            findViewById(R.id.btnCalculator).setOnClickListener(v -> filterByCategory("Calculators"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void filterByCategory(String category) {
        materialList.clear();
        if ("All".equalsIgnoreCase(category)) {
            materialList.addAll(fullMaterialList);
        } else {
            for (Material material : fullMaterialList) {
                if (material.getCategory() != null && material.getCategory().equalsIgnoreCase(category)) {
                    materialList.add(material);
                }
            }
        }
        adapter.notifyDataSetChanged();
    }
}
