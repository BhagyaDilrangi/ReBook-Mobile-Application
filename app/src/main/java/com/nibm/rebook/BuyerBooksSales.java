package com.nibm.rebook;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

public class BuyerBooksSales extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListingAdapter adapter;
    private List<Material> bookList;
    private List<Material> filteredList;
    private DatabaseReference mDatabase;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_buyer_books_sales);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.recyclerViewBooksSales);
        searchView = findViewById(R.id.searchBooksSales);

        bookList = new ArrayList<>();
        filteredList = new ArrayList<>();
        
        // Using a modified ListingAdapter or just handling clicks carefully
        // For now, reusing ListingAdapter. In a real scenario, we'd pass a "isBuyer" flag.
        adapter = new ListingAdapter(filteredList);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("materials");

        fetchSalesBooks();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
    }

    private void fetchSalesBooks() {
        mDatabase.orderByChild("type").equalTo("Sale")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        bookList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Material material = data.getValue(Material.class);
                            if (material != null && "Available".equals(material.getStatus())) {
                                bookList.add(material);
                            }
                        }
                        filter(""); // Initial display
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(BuyerBooksSales.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void filter(String text) {
        filteredList.clear();
        for (Material m : bookList) {
            if (m.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(m);
            }
        }
        adapter.notifyDataSetChanged();
    }
}