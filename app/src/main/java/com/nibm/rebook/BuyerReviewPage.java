package com.nibm.rebook;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BuyerReviewPage extends AppCompatActivity {

    private EditText edtSellerName, edtComment;
    private RatingBar ratingBar;
    private Button btnSubmit;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_buyer_review_page);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("feedbacks");

        edtSellerName = findViewById(R.id.edit_regi_firstname);
        edtComment = findViewById(R.id.editComment);
        ratingBar = findViewById(R.id.ratingBar);
        btnSubmit = findViewById(R.id.btnSubmitReview);

        btnSubmit.setOnClickListener(v -> {
            String sellerName = edtSellerName.getText().toString().trim();
            String comment = edtComment.getText().toString().trim();
            float rating = ratingBar.getRating();

            if (sellerName.isEmpty() || comment.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            submitFeedback(sellerName, comment, String.valueOf(rating));
        });
    }

    private void submitFeedback(String sellerName, String comment, String rating) {
        String feedbackId = mDatabase.push().getKey();
        String buyerName = mAuth.getCurrentUser().getEmail(); // Or fetch name from DB

        FeedbackModel feedback = new FeedbackModel(buyerName, sellerName, comment, rating, "Visible");

        if (feedbackId != null) {
            mDatabase.child(feedbackId).setValue(feedback)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Review submitted successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}