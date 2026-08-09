package com.nibm.rebook;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

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

    private static final String DATABASE_URL = "https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_material);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance(DATABASE_URL).getReference("materials");

        // Initialize UI Elements
        progressBar = findViewById(R.id.progressBar);
        btnPublish = findViewById(R.id.btnPublishListing);
        etTitle = findViewById(R.id.etMaterialTitle);
        etPrice = findViewById(R.id.etMaterialPrice);
        spinnerType = findViewById(R.id.spinnerMaterialType);
        spinnerCategory = findViewById(R.id.spinnerCategory);

        // Hide upload buttons and image preview as Firebase Storage is not being used
        View btnUploadImage = findViewById(R.id.btnUploadImage);
        View btnUploadPreview = findViewById(R.id.btnUploadPreview);
        View imgPreview = findViewById(R.id.imgPreview);
        
        if (btnUploadImage != null) btnUploadImage.setVisibility(View.GONE);
        if (btnUploadPreview != null) btnUploadPreview.setVisibility(View.GONE);
        if (imgPreview != null) imgPreview.setVisibility(View.GONE);

        setupSpinners();

        // Publish Logic
        btnPublish.setOnClickListener(v -> validateAndPublish());
    }

    private void setupSpinners() {
        String[] types = {"Sale", "Borrow", "Donate"};
        ArrayAdapter<String> adapterType = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, types);
        spinnerType.setAdapter(adapterType);

        spinnerType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (types[position].equals("Donate")) {
                    etPrice.setText("0");
                    etPrice.setEnabled(false);
                } else {
                    etPrice.setEnabled(true);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        String[] categories = {"Books", "Notes", "Past Papers", "Calculators"};
        ArrayAdapter<String> adapterCategory = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, categories);
        spinnerCategory.setAdapter(adapterCategory);
    }

    private void validateAndPublish() {
        String title = etTitle.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String type = spinnerType.getSelectedItem().toString();
        String category = spinnerCategory.getSelectedItem().toString();

        if (title.isEmpty()) {
            etTitle.setError("Title is required");
            return;
        }

        double price;
        try {
            price = priceStr.isEmpty() ? 0.0 : Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            etPrice.setError("Invalid price format");
            return;
        }

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "User session expired. Please login again.", Toast.LENGTH_SHORT).show();
            return;
        }

        btnPublish.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);

        saveListingToDatabase(title, price, type, category);
    }

    private void saveListingToDatabase(String title, double price, String type, String category) {
        String materialId = mDatabase.push().getKey();
        if (materialId == null) {
            handleError("Failed to generate ID");
            return;
        }

        String sellerId = mAuth.getCurrentUser().getUid();
        // Construct Material object (ignoring image/pdf URLs as per user request to not use Storage)
        Material newMaterial = new Material(materialId, title, "Available", type, category, sellerId, price);

        mDatabase.child(materialId).setValue(newMaterial)
                .addOnCompleteListener(task -> {
                    progressBar.setVisibility(View.GONE);
                    btnPublish.setEnabled(true);
                    if (task.isSuccessful()) {
                        Toast.makeText(this, "Listing Published Successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        String error = task.getException() != null ? task.getException().getMessage() : "Unknown";
                        Toast.makeText(this, "Error: " + error, Toast.LENGTH_LONG).show();
                    }
                });
    }

    private void handleError(String message) {
        progressBar.setVisibility(View.GONE);
        btnPublish.setEnabled(true);
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}