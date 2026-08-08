package com.nibm.rebook;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class EditMaterialActivity extends AppCompatActivity {

    private EditText etEditMaterialTitle, etEditMaterialPrice;
    private MaterialButton btnUpdateListing;
    private String materialId;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_material);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("materials");

        etEditMaterialTitle = findViewById(R.id.etEditMaterialTitle);
        etEditMaterialPrice = findViewById(R.id.etEditMaterialPrice);
        btnUpdateListing = findViewById(R.id.btnUpdateListing);

        materialId = getIntent().getStringExtra("MATERIAL_ID");
        String currentTitle = getIntent().getStringExtra("material_title");
        String currentPrice = getIntent().getStringExtra("material_price");

        if (currentTitle != null) etEditMaterialTitle.setText(currentTitle);
        if (currentPrice != null) etEditMaterialPrice.setText(currentPrice);

        btnUpdateListing.setOnClickListener(v -> {
            String newTitle = etEditMaterialTitle.getText().toString().trim();
            String newPriceStr = etEditMaterialPrice.getText().toString().trim();

            if (newTitle.isEmpty() || newPriceStr.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double newPrice = Double.parseDouble(newPriceStr);

            if (materialId != null) {
                Map<String, Object> updates = new HashMap<>();
                updates.put("title", newTitle);
                updates.put("price", newPrice);

                mDatabase.child(materialId).updateChildren(updates)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(this, "Listing updated successfully!", Toast.LENGTH_SHORT).show();
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(this, "Update failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });
    }
}