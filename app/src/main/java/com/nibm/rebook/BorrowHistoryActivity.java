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
import com.nibm.rebook.CustomAdapter.BorrowAdapter;

import java.util.ArrayList;
import java.util.List;

public class BorrowHistoryActivity extends AppCompatActivity {

    private RecyclerView rvHistory;
    private BorrowAdapter adapter;
    private List<com.nibm.rebook.BorrowItem> borrowList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrow_history);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("transactions");

        rvHistory = findViewById(R.id.rvHistory);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        borrowList = new ArrayList<>();
        adapter = new BorrowAdapter(borrowList);
        rvHistory.setAdapter(adapter);

        fetchBorrowHistory();
    }

    private void fetchBorrowHistory() {
        if (mAuth.getCurrentUser() == null) return;
        String sellerId = mAuth.getCurrentUser().getUid();

        // Query transactions where I am the seller and type is Borrow
        mDatabase.orderByChild("sellerId").equalTo(sellerId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        borrowList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            String type = data.child("type").getValue(String.class);
                            if ("Borrow".equals(type)) {
                                String title = data.child("materialTitle").getValue(String.class);
                                String duration = data.child("duration").getValue(String.class);
                                String status = data.child("status").getValue(String.class);
                                borrowList.add(new com.nibm.rebook.BorrowItem(title, "Duration: " + duration, status));
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(BorrowHistoryActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}