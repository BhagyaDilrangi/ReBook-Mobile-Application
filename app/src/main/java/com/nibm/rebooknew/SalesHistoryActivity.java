package com.nibm.rebooknew;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nibm.rebooknew.CustomAdapter.HistoryAdapter;
import com.nibm.rebooknew.dto.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class SalesHistoryActivity extends AppCompatActivity {

    RecyclerView rvHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_history);

        if (getSupportActionBar() != null) getSupportActionBar().hide();

        RecyclerView rv = findViewById(R.id.rvHistory);
        rv.setLayoutManager(new LinearLayoutManager(this));

        // 1. Create Dummy Data
        List<HistoryItem> historyList = new ArrayList<>();
        historyList.add(new HistoryItem("Calculus Book", "2026-07-01"));
        historyList.add(new HistoryItem("Engineering Kit", "2026-06-25"));

        // 2. Attach Adapter
        HistoryAdapter adapter = new HistoryAdapter(historyList);
        rv.setAdapter(adapter);
    }
}