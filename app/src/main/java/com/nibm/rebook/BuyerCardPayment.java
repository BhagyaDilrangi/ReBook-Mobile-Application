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

public class BuyerCardPayment extends AppCompatActivity {

    private EditText editCardHolder, editCardNumber, editCode;
    private Button btnPay;
    private String materialId, materialTitle, materialPrice, sellerId;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_buyer_card_payment);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference();

        // Get data passed from previous listing/selling screen
        materialId = getIntent().getStringExtra("MATERIAL_ID");
        materialTitle = getIntent().getStringExtra("MATERIAL_TITLE");
        materialPrice = getIntent().getStringExtra("MATERIAL_PRICE");
        sellerId = getIntent().getStringExtra("SELLER_ID");

        editCardHolder = findViewById(R.id.edit_cardholdername);
        editCardNumber = findViewById(R.id.edit_cardnumber);
        editCode = findViewById(R.id.edit_code);
        btnPay = findViewById(R.id.card_btnpay);

        btnPay.setOnClickListener(v -> {
            String name = editCardHolder.getText().toString().trim();
            String cardNum = editCardNumber.getText().toString().trim();
            String code = editCode.getText().toString().trim();

            if (name.isEmpty() || cardNum.isEmpty() || code.isEmpty()) {
                Toast.makeText(this, "Please fill all payment details", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simulate card processing and record transaction in Firebase
            processPayment(name);
        });
    }

    private void processPayment(String cardHolderName) {
        if (mAuth.getCurrentUser() == null) return;

        String transactionId = mDatabase.child("transactions").push().getKey();
        String buyerId = mAuth.getCurrentUser().getUid();

        Map<String, Object> transactionData = new HashMap<>();
        transactionData.put("id", transactionId);
        transactionData.put("buyerId", buyerId);
        transactionData.put("buyerName", cardHolderName); // Using card holder name for the record
        transactionData.put("materialId", materialId);
        transactionData.put("materialTitle", materialTitle);
        transactionData.put("amount", materialPrice);
        transactionData.put("sellerId", sellerId);
        transactionData.put("status", "Paid");
        transactionData.put("type", "Sale");
        transactionData.put("timestamp", System.currentTimeMillis());

        if (transactionId != null) {
            mDatabase.child("transactions").child(transactionId).setValue(transactionData)
                    .addOnSuccessListener(aVoid -> {
                        // Update material status in database
                        if (materialId != null) {
                            mDatabase.child("materials").child(materialId).child("status").setValue("Sold");
                        }
                        
                        Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();
                        
                        // Send a notification to the seller (Optional but good practice)
                        sendNotificationToSeller(sellerId, materialTitle);

                        startActivity(new Intent(BuyerCardPayment.this, BuyerHome.class));
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Transaction failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }

    private void sendNotificationToSeller(String sellerId, String title) {
        if (sellerId == null) return;
        DatabaseReference notifyRef = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("notifications").child(sellerId);
        
        String notifyId = notifyRef.push().getKey();
        Map<String, Object> notification = new HashMap<>();
        notification.put("title", "Item Sold!");
        notification.put("message", "Your item '" + title + "' has been purchased.");
        notification.put("timestamp", System.currentTimeMillis());

        if (notifyId != null) {
            notifyRef.child(notifyId).setValue(notification);
        }
    }
}