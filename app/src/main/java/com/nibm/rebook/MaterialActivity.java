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

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        recyclerView = findViewById(R.id.recyclerViewMaterials);
        search = findViewById(R.id.searchMaterial);

        list = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Initialize Firebase Database Reference
        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("materials");

        // Pass activity context or listener callbacks to adapter for View, Hide/Toggle Status, and Delete operations
        adapter = new MaterialAdapter(filteredList, new MaterialAdapter.OnMaterialActionListener() {
            @Override
            public void onView(MaterialModel material) {
                // Handle View Action (e.g., open PDF or show material details)
                if (material.getPdfFile() != null && !material.getPdfFile().isEmpty()) {
                    try {
                        android.content.Intent intent = new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(material.getPdfFile()));
                        startActivity(intent);
                    } catch (Exception e) {
                        Toast.makeText(MaterialActivity.this, "Unable to open document file.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(MaterialActivity.this, "Title: " + material.getTitle() + " | Price: LKR " + material.getPrice(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onHide(MaterialModel material) {
                // Handle Hide / Status Toggle Action
                String materialId = material.getMaterialId();
                if (materialId != null) {
                    String newStatus = "Hidden".equalsIgnoreCase(material.getStatus()) ? "Available" : "Hidden";
                    mDatabase.child(materialId).child("status").setValue(newStatus)
                            .addOnSuccessListener(aVoid -> Toast.makeText(MaterialActivity.this, "Material status updated to " + newStatus, Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(MaterialActivity.this, "Failed to update status: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }

            @Override
            public void onDelete(MaterialModel material) {
                // Handle Delete Action
                String materialId = material.getMaterialId();
                if (materialId != null) {
                    mDatabase.child(materialId).removeValue()
                            .addOnSuccessListener(aVoid -> Toast.makeText(MaterialActivity.this, "Material deleted successfully", Toast.LENGTH_SHORT).show())
                            .addOnFailureListener(e -> Toast.makeText(MaterialActivity.this, "Failed to delete material: " + e.getMessage(), Toast.LENGTH_SHORT).show());
                }
            }
        });

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

        // Search Filter
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