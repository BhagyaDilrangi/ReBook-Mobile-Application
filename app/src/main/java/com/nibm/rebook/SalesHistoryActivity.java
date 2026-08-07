package com.nibm.rebook;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nibm.rebook.CustomAdapter.HistoryAdapter;
import com.nibm.rebook.dto.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class SalesHistoryActivity extends AppCompatActivity {

    private RecyclerView rvHistory;
    private TextView txtTotalBuyersCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_history);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize views safely
        rvHistory = findViewById(R.id.rvHistory);
        txtTotalBuyersCount = findViewById(R.id.txtTotalBuyersCount);

        if (rvHistory != null) {
            rvHistory.setLayoutManager(new LinearLayoutManager(this));

            // Create sample data items
            List<HistoryItem> historyList = new ArrayList<>();
            historyList.add(new HistoryItem("Calculus Book", "3"));
            historyList.add(new HistoryItem("Engineering Kit", "1"));

            // Update summary text
            if (txtTotalBuyersCount != null) {
                txtTotalBuyersCount.setText("Total Transactions Tracked: " + historyList.size());
            }

            // Attach Adapter
            HistoryAdapter adapter = new HistoryAdapter(historyList);
            rvHistory.setAdapter(adapter);
        } else {
            Toast.makeText(this, "Error initializing list view layout.", Toast.LENGTH_SHORT).show();
        }
    }
}