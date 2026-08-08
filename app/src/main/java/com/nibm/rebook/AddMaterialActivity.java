package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nibm.rebook.dto.Material;

public class AddMaterialActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button btnPublish;
    private EditText etTitle, etPrice;
    private Spinner spinnerType, spinnerCategory;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_material);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("materials");

        // Initialize UI Elements
        progressBar = findViewById(R.id.progressBar);
        btnPublish = findViewById(R.id.btnPublishListing);
        etTitle = findViewById(R.id.etMaterialTitle);
        etPrice = findViewById(R.id.etMaterialPrice);
        spinnerType = findViewById(R.id.spinnerMaterialType);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        // Spinner Setup for Types
        String[] types = {"Sale", "Borrow", "Donate"};
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, types);
        spinnerType.setAdapter(adapterType);

        // Spinner Setup for Categories
        String[] categories = {"Books", "Notes", "Past Papers", "Calculators"};
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapterCategory);

        // Upload Buttons
        Button btnUploadImage = findViewById(R.id.btnUploadImage);
        Button btnUploadPreview = findViewById(R.id.btnUploadPreview);

        btnUploadImage.setOnClickListener(v -> mGetContent.launch("image/*"));
        btnUploadPreview.setOnClickListener(v -> mGetContent.launch("application/pdf"));

        // Publish Logic
        btnPublish.setOnClickListener(v -> {
            String title = etTitle.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();
            String type = spinnerType.getSelectedItem().toString();
            String category = spinnerCategory.getSelectedItem().toString();

            if (title.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);
            String sellerId = mAuth.getCurrentUser().getUid();

            // 1. Show Loading UI
            btnPublish.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

            // 2. Save to Firebase
            String materialId = mDatabase.push().getKey();
            Material newMaterial = new Material(materialId, title, "Available", type, category, sellerId, price);
            
            if (materialId != null) {
                mDatabase.child(materialId).setValue(newMaterial)
                        .addOnCompleteListener(task -> {
                            progressBar.setVisibility(View.GONE);
                            btnPublish.setEnabled(true);
                            if (task.isSuccessful()) {
                                Toast.makeText(this, "Listing Published Successfully!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(AddMaterialActivity.this, MyListingsActivity.class));
                                finish();
                            } else {
                                Toast.makeText(this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
            }
        });
    }

    // Handles URI results
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    Toast.makeText(this, "File Selected", Toast.LENGTH_SHORT).show();
                }
            });
}