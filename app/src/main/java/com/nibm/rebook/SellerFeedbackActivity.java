package com.nibm.rebook;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SellerFeedbackActivity extends AppCompatActivity {

    private ListView lvReviews;
    private ArrayList<String> feedbackList;
    private ArrayAdapter<String> adapter;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_feedback);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("feedbacks");

        lvReviews = findViewById(R.id.lvReviews);
        feedbackList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, feedbackList);
        lvReviews.setAdapter(adapter);

        fetchSellerFeedback();
    }

    private void fetchSellerFeedback() {
        if (mAuth.getCurrentUser() == null) return;
        
        // In a real app, you should match by Seller ID. 
        // For now, we'll try matching by the email/name used in the review page.
        String currentUserEmail = mAuth.getCurrentUser().getEmail();

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                feedbackList.clear();
                for (DataSnapshot data : snapshot.getChildren()) {
                    FeedbackModel feedback = data.getValue(FeedbackModel.class);
                    if (feedback != null && currentUserEmail != null && currentUserEmail.equalsIgnoreCase(feedback.getTargetSeller())) {
                        feedbackList.add("Rating: " + feedback.getRating() + " ★\n" + feedback.getComment() + "\n- " + feedback.getUser());
                    }
                }
                adapter.notifyDataSetChanged();
                if (feedbackList.isEmpty()) {
                    feedbackList.add("No reviews yet.");
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SellerFeedbackActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}