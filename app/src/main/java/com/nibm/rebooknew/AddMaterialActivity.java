package com.nibm.rebooknew;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.BreakIterator;

public class AddMaterialActivity extends AppCompatActivity {

    private ProgressBar progressBar;
    private Button btnPublish;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_material);

        getSupportActionBar().hide();

        findViewById(R.id.btnUploadPreview);
        findViewById(R.id.btnUploadImage);

        progressBar = findViewById(R.id.progressBar);
        btnPublish = findViewById(R.id.btnPublishListing);

        Spinner spinner = findViewById(R.id.spinnerMaterialType);
        String[] types = {"Sale", "Borrow", "Donate", "Exchange"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_dropdown_item, types);
        spinner.setAdapter(adapter);

        Button btnUploadImage = findViewById(R.id.btnUploadImage);
        Button btnUploadPreview = findViewById(R.id.btnUploadPreview);

        btnUploadImage.setOnClickListener(v -> mGetContent.launch("image/*"));
        btnUploadPreview.setOnClickListener(v -> mGetContent.launch("application/pdf"));

        btnPublish.setOnClickListener(v -> {
            // 1. Show Loading UI
            btnPublish.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

            // 2. Simulate "Publishing" (UI Transition)
            new Handler().postDelayed(() -> {
                // 3. UI Success Feedback
                Toast.makeText(this, "Listing Published Successfully!", Toast.LENGTH_SHORT).show();

                // 4. Navigate to Inventory UI
                Intent intent = new Intent(AddMaterialActivity.this, MyListingsActivity.class);
                startActivity(intent);
                finish(); // Close this screen so the user doesn't come back to the empty form
            }, 2000); // 2-second delay to show the "Loading" state
        });
    }

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                // Handle the returned URI (e.g., set to ImageView or store for upload)
            });
}

