package com.nibm.rebook;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;

public class EditMaterialActivity extends AppCompatActivity {

    private EditText etEditMaterialTitle, etEditMaterialPrice;
    private MaterialButton btnUpdateListing;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_material);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        etEditMaterialTitle = findViewById(R.id.etEditMaterialTitle);
        etEditMaterialPrice = findViewById(R.id.etEditMaterialPrice);
        btnUpdateListing = findViewById(R.id.btnUpdateListing);

        // Receive data sent from your inventory adapter/activity if available
        String currentTitle = getIntent().getStringExtra("material_title");
        String currentPrice = getIntent().getStringExtra("material_price");

        if (currentTitle != null) etEditMaterialTitle.setText(currentTitle);
        if (currentPrice != null) etEditMaterialPrice.setText(currentPrice);

        btnUpdateListing.setOnClickListener(v -> {
            // TODO: Write code to update item in Firebase / Local Database
            Toast.makeText(this, "Listing updated successfully!", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}