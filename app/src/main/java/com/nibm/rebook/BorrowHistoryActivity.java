package com.nibm.rebook;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nibm.rebook.CustomAdapter.BorrowAdapter;

import java.util.ArrayList;
import java.util.List;

public class BorrowHistoryActivity extends AppCompatActivity {

    RecyclerView rvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_history);

        // Hide the Action Bar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize RecyclerView
        RecyclerView rvHistory = findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        // Create Dummy Data
        List<com.nibm.rebook.BorrowItem> borrowList = new ArrayList<>();
        borrowList.add(new com.nibm.rebook.BorrowItem("Operating Systems Book", "2026-08-15", "Returned"));
        borrowList.add(new com.nibm.rebook.BorrowItem("Scientific Calculator", "2026-07-20", "Overdue"));
        borrowList.add(new com.nibm.rebook.BorrowItem("Lab Coat", "2026-07-10", "Returned"));

        // Set Adapter
        BorrowAdapter adapter = new BorrowAdapter(borrowList);
        rvHistory.setAdapter(adapter);
    }

}