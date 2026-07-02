package com.nibm.rebooknew;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nibm.rebooknew.CustomAdapter.ListingAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyListingsActivity extends AppCompatActivity {

    private RecyclerView rvMyListings;
    private ListingAdapter adapter;
    private List<Material> materialList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_listings);

        getSupportActionBar().hide();

        // 1. Initialize the RecyclerView
        rvMyListings = findViewById(R.id.rvMyListings);
        rvMyListings.setLayoutManager(new LinearLayoutManager(this));

        // 2. Prepare the data list
        materialList = new ArrayList<>();
        loadSampleData(); // Replace this with your actual database fetch logic

        // 3. Initialize and set the Adapter
        adapter = new ListingAdapter(materialList);
        rvMyListings.setAdapter(adapter);
    }

    private void loadSampleData() {
        // Adding dummy data
        materialList.add(new Material("Data Structures Notes", "Available"));
        materialList.add(new Material("Financial Accounting Book", "Sold"));
        materialList.add(new Material("Advanced Calculus Guide", "Borrowed"));
    }
}