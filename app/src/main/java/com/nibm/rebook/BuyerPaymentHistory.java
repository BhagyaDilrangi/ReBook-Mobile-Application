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

import java.util.ArrayList;

public class BuyerPaymentHistory extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TransactionAdapter adapter;
    private ArrayList<TransactionModel> list;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_buyer_payment_history);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            finish();
            return;
        }

        String uid = mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("transactions");

        recyclerView = findViewById(R.id.recyclerViewPaymentHistory);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        list = new ArrayList<>();
        // Admin uses TransactionAdapter, we can reuse it but typically buyers don't need 'Mark as Paid' buttons.
        // For simplicity, we use it, but in a real app, we'd have a specific BuyerTransactionAdapter.
        adapter = new TransactionAdapter(list, this);
        recyclerView.setAdapter(adapter);

        fetchPaymentHistory(uid);
    }

    private void fetchPaymentHistory(String uid) {
        // Query transactions where I am the buyer
        mDatabase.orderByChild("buyerId").equalTo(uid)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            TransactionModel transaction = data.getValue(TransactionModel.class);
                            if (transaction != null && "Sale".equals(data.child("type").getValue(String.class))) {
                                list.add(transaction);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(BuyerPaymentHistory.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}