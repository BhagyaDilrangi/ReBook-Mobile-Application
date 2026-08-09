package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class BuyerBorowing extends AppCompatActivity {

    private EditText editName, editPhone, editAddress, editDuration;
    private Button btnRequest;
    private String materialId, materialTitle, sellerId;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private final String DATABASE_URL = "https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_buyer_borowing);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(this, "Session expired. Please log in again.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        mDatabase = FirebaseDatabase.getInstance(DATABASE_URL).getReference();

        // Get data safely from intent
        materialId = getIntent().getStringExtra("MATERIAL_ID");
        materialTitle = getIntent().getStringExtra("MATERIAL_TITLE");
        sellerId = getIntent().getStringExtra("SELLER_ID");

        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editAddress = findViewById(R.id.editAddress);
        editDuration = findViewById(R.id.editDuration);
        btnRequest = findViewById(R.id.btnUploadSlip);

        btnRequest.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();
            String address = editAddress.getText().toString().trim();
            String duration = editDuration.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || duration.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
                return;
            }

            if (materialId == null || materialId.isEmpty()) {
                Toast.makeText(this, "Error: Invalid material reference.", Toast.LENGTH_SHORT).show();
                return;
            }

            processBorrowRequest(name, phone, address, duration);
        });
    }

    private void processBorrowRequest(String name, String phone, String address, String duration) {
        String transactionId = mDatabase.child("transactions").push().getKey();
        String buyerId = mAuth.getCurrentUser().getUid();

        Map<String, Object> transactionData = new HashMap<>();
        transactionData.put("id", transactionId);
        transactionData.put("buyerId", buyerId);
        transactionData.put("buyerName", name);
        transactionData.put("phone", phone);
        transactionData.put("address", address);
        transactionData.put("duration", duration);
        transactionData.put("materialId", materialId);
        transactionData.put("materialTitle", materialTitle != null ? materialTitle : "Untitled");
        transactionData.put("sellerId", sellerId != null ? sellerId : "");
        transactionData.put("status", "Pending");
        transactionData.put("type", "Borrow");

        if (transactionId != null) {
            btnRequest.setEnabled(false);
            mDatabase.child("transactions").child(transactionId).setValue(transactionData)
                    .addOnSuccessListener(aVoid -> {
                        // Update material status to Borrowed
                        mDatabase.child("materials").child(materialId).child("status").setValue("Borrowed")
                                .addOnCompleteListener(task -> {
                                    Toast.makeText(this, "Borrow Request Sent Successfully!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(BuyerBorowing.this, BuyerHome.class));
                                    finish();
                                });
                    })
                    .addOnFailureListener(e -> {
                        btnRequest.setEnabled(true);
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}