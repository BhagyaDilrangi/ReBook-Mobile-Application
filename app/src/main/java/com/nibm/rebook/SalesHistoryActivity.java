package com.nibm.rebook;

import android.os.Bundle;
import android.widget.TextView;
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
import com.nibm.rebook.CustomAdapter.HistoryAdapter;
import com.nibm.rebook.dto.HistoryItem;

import java.util.ArrayList;
import java.util.List;

public class SalesHistoryActivity extends AppCompatActivity {

    private RecyclerView rvHistory;
    private TextView txtTotalBuyersCount;
    private List<HistoryItem> historyList;
    private HistoryAdapter adapter;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_history);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("transactions");

        rvHistory = findViewById(R.id.rvHistory);
        txtTotalBuyersCount = findViewById(R.id.txtTotalBuyersCount);

        historyList = new ArrayList<>();
        adapter = new HistoryAdapter(historyList);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));
        rvHistory.setAdapter(adapter);

        fetchSalesHistory();
    }

    private void fetchSalesHistory() {
        if (mAuth.getCurrentUser() == null) return;
        String sellerId = mAuth.getCurrentUser().getUid();

        mDatabase.orderByChild("sellerId").equalTo(sellerId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        historyList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            String type = data.child("type").getValue(String.class);
                            String status = data.child("status").getValue(String.class);
                            
                            // Only count Sales that are Paid or Accepted
                            if ("Sale".equals(type) && ("Paid".equals(status) || "Accepted".equals(status))) {
                                String title = data.child("materialTitle").getValue(String.class);
                                String buyer = data.child("buyerName").getValue(String.class);
                                historyList.add(new HistoryItem(title, "Buyer: " + buyer));
                            }
                        }
                        adapter.notifyDataSetChanged();
                        if (txtTotalBuyersCount != null) {
                            txtTotalBuyersCount.setText("Total Sales: " + historyList.size());
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(SalesHistoryActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}