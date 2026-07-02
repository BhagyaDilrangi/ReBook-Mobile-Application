package com.nibm.rebook;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    TransactionAdapter adapter;
    ArrayList<TransactionModel> list;
    ArrayList<TransactionModel> filteredList;

    androidx.appcompat.widget.AppCompatEditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);

        recyclerView = findViewById(R.id.recyclerViewTransactions);
        search = findViewById(R.id.searchTransaction);

        list = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Dummy Data
        list.add(new TransactionModel("T001", "Kasun Perera", "2500", "Paid"));
        list.add(new TransactionModel("T002", "Nimal Silva", "1800", "Pending"));
        list.add(new TransactionModel("T003", "Amaya Fernando", "3200", "Failed"));
        list.add(new TransactionModel("T004", "Kavindi Jay", "1500", "Paid"));

        filteredList.addAll(list);

        adapter = new TransactionAdapter(filteredList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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
            if (t.getCustomer().toLowerCase().contains(text.toLowerCase()) ||
                    t.getId().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(t);
            }
        }

        adapter.notifyDataSetChanged();
    }
}