package com.nibm.rebook;

import android.os.Bundle;
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

public class BuyerBooksDonate extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ListingAdapter adapter;
    private List<Material> bookList;
    private List<Material> filteredList;
    private DatabaseReference mDatabase;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_buyer_books_donate);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.recyclerViewBooksDonate);
        searchView = findViewById(R.id.searchBooksDonate);

        bookList = new ArrayList<>();
        filteredList = new ArrayList<>();
        
        adapter = new ListingAdapter(filteredList);
        
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("materials");

        fetchDonateBooks();

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

    private void fetchDonateBooks() {
        mDatabase.orderByChild("type").equalTo("Donate")
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
                        filter(""); 
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(BuyerBooksDonate.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
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