package com.nibm.rebook;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TransactionAdapter adapter;
    ArrayList<TransactionModel> list;
    ArrayList<TransactionModel> filteredList;
    DatabaseReference mDatabase;

    androidx.appcompat.widget.AppCompatEditText search;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        recyclerView = findViewById(R.id.recyclerViewTransactions);
        search = findViewById(R.id.searchTransaction);

        list = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Initialize Firebase Database Reference
        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("transactions");

        adapter = new TransactionAdapter(filteredList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Fetch data from Firebase
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    TransactionModel transaction = dataSnapshot.getValue(TransactionModel.class);
                    if (transaction != null) {
                        list.add(transaction);
                    }
                }
                filteredList.clear();
                filteredList.addAll(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(TransactionActivity.this, "Failed to load transactions: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // SEARCH
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filter(String text) {
        filteredList.clear();

        for (TransactionModel t : list) {
            if ((t.getCustomer() != null && t.getCustomer().toLowerCase().contains(text.toLowerCase())) ||
                    (t.getId() != null && t.getId().toLowerCase().contains(text.toLowerCase()))) {
                filteredList.add(t);
            }
        }

        adapter.notifyDataSetChanged();
    }
}