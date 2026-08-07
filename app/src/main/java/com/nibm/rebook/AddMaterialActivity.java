package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class AddMaterialActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button btnPublish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_material);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        // Initialize UI Elements
        progressBar = findViewById(R.id.progressBar);
        btnPublish = findViewById(R.id.btnPublishListing);
        Spinner spinner = findViewById(R.id.spinnerMaterialType);

        // Spinner Setup
        String[] types = {"Sale", "Borrow", "Donate", "Exchange"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, types);
        spinner.setAdapter(adapter);

        // Upload Buttons
        Button btnUploadImage = findViewById(R.id.btnUploadImage);
        Button btnUploadPreview = findViewById(R.id.btnUploadPreview);

        btnUploadImage.setOnClickListener(v -> mGetContent.launch("image/*"));
        btnUploadPreview.setOnClickListener(v -> mGetContent.launch("application/pdf"));

        // Publish Logic
        btnPublish.setOnClickListener(v -> {
            // 1. Show Loading UI
            btnPublish.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

            // 2. Simulate Upload (Using Looper.getMainLooper() for stability)
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                // 3. UI Success Feedback
                Toast.makeText(this, "Listing Published Successfully!", Toast.LENGTH_SHORT).show();

                // 4. Navigate to Inventory
                startActivity(new Intent(AddMaterialActivity.this, MyListingsActivity.class));
                finish();
            }, 2000);
        });
    }

    // Handles URI results
    private final ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) {
                    // TODO: Handle the file URI (e.g., save it or set it to an ImageView)
                    Toast.makeText(this, "File Selected", Toast.LENGTH_SHORT).show();
                }
            });
}