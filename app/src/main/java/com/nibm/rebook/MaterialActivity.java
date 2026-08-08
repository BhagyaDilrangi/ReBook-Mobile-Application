package com.nibm.rebook;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
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

public class MaterialActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MaterialAdapter adapter;
    ArrayList<MaterialModel> list;
    ArrayList<MaterialModel> filteredList;
    EditText search;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);

        recyclerView = findViewById(R.id.recyclerViewMaterials);
        search = findViewById(R.id.searchMaterial);

        list = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Initialize Firebase Database Reference
        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("materials");

        adapter = new MaterialAdapter(filteredList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Fetch data from Firebase
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    MaterialModel material = dataSnapshot.getValue(MaterialModel.class);
                    if (material != null) {
                        list.add(material);
                    }
                }
                filteredList.clear();
                filteredList.addAll(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MaterialActivity.this, "Failed to load materials: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Search
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

        for (MaterialModel m : list) {
            if (m.getTitle() != null && m.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(m);
            }
        }

        adapter.notifyDataSetChanged();
    }
}