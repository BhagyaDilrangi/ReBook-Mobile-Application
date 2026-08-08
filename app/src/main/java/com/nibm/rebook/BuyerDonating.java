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

public class BuyerDonating extends AppCompatActivity {

    private EditText editName, editPhone, editAddress;
    private Button btnRequest;
    private String materialId, materialTitle, sellerId;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_buyer_donating);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        // Get data from intent
        materialId = getIntent().getStringExtra("MATERIAL_ID");
        materialTitle = getIntent().getStringExtra("MATERIAL_TITLE");
        sellerId = getIntent().getStringExtra("SELLER_ID");

        editName = findViewById(R.id.editName);
        editPhone = findViewById(R.id.editPhone);
        editAddress = findViewById(R.id.editAddress);
        btnRequest = findViewById(R.id.btnRequestDonation);

        btnRequest.setOnClickListener(v -> {
            String name = editName.getText().toString().trim();
            String phone = editPhone.getText().toString().trim();
            String address = editAddress.getText().toString().trim();

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show();
                return;
            }

            processDonationRequest(name, phone, address);
        });
    }

    private void processDonationRequest(String name, String phone, String address) {
        String transactionId = mDatabase.child("transactions").push().getKey();
        String buyerId = mAuth.getCurrentUser().getUid();

        Map<String, Object> transactionData = new HashMap<>();
        transactionData.put("id", transactionId);
        transactionData.put("buyerId", buyerId);
        transactionData.put("buyerName", name);
        transactionData.put("phone", phone);
        transactionData.put("address", address);
        transactionData.put("materialId", materialId);
        transactionData.put("materialTitle", materialTitle);
        transactionData.put("sellerId", sellerId);
        transactionData.put("status", "Requested");
        transactionData.put("type", "Donation");

        if (transactionId != null) {
            mDatabase.child("transactions").child(transactionId).setValue(transactionData)
                    .addOnSuccessListener(aVoid -> {
                        // Update material status to Requested
                        mDatabase.child("materials").child(materialId).child("status").setValue("Donated");
                        
                        Toast.makeText(this, "Donation Request Sent!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(BuyerDonating.this, BuyerHome.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}