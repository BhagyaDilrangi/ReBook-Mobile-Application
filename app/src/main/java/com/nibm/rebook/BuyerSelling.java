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

public class BuyerSelling extends AppCompatActivity {

    private EditText editName, editPhone, editAddress;
    private Button btnRequest;
    private String materialId, materialTitle, materialPrice, sellerId;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private final String DATABASE_URL = "https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_buyer_selling);

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
        materialPrice = getIntent().getStringExtra("MATERIAL_PRICE");
        sellerId = getIntent().getStringExtra("SELLER_ID");

        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editAddress = findViewById(R.id.editAddress);
        btnRequest = findViewById(R.id.btnUploadSlip);

        btnRequest.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();
            String address = editAddress.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
                return;
            }

            if (materialId == null || materialId.isEmpty()) {
                Toast.makeText(this, "Error: Invalid material reference.", Toast.LENGTH_SHORT).show();
                return;
            }

            processPurchase(name, phone, address);
        });
    }

    private void processPurchase(String name, String phone, String address) {
        String transactionId = mDatabase.child("transactions").push().getKey();
        String buyerId = mAuth.getCurrentUser().getUid();

        Map<String, Object> transactionData = new HashMap<>();
        transactionData.put("id", transactionId);
        transactionData.put("buyerId", buyerId);
        transactionData.put("buyerName", name);
        transactionData.put("phone", phone);
        transactionData.put("address", address);
        transactionData.put("materialId", materialId);
        transactionData.put("materialTitle", materialTitle != null ? materialTitle : "Untitled");
        transactionData.put("amount", materialPrice != null ? materialPrice : "0.00");
        transactionData.put("sellerId", sellerId != null ? sellerId : "");
        transactionData.put("status", "Paid");
        transactionData.put("type", "Sale");

        if (transactionId != null) {
            btnRequest.setEnabled(false);
            mDatabase.child("transactions").child(transactionId).setValue(transactionData)
                    .addOnSuccessListener(aVoid -> {
                        // Update material status to Sold
                        mDatabase.child("materials").child(materialId).child("status").setValue("Sold")
                                .addOnCompleteListener(task -> {
                                    Toast.makeText(this, "Purchase Request Sent Successfully!", Toast.LENGTH_LONG).show();
                                    startActivity(new Intent(BuyerSelling.this, BuyerHome.class));
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