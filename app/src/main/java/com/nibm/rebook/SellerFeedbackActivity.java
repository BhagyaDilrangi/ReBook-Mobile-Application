package com.nibm.rebook;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class SellerFeedbackActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_feedback);

        getSupportActionBar().hide();

        ListView lvReviews = findViewById(R.id.lvReviews);

        // Dummy data
        ArrayList<String> feedbackList = new ArrayList<>();
        feedbackList.add("Excellent condition! - Buyer A");
        feedbackList.add("Very useful notes, thanks! - Buyer B");

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, feedbackList);
        lvReviews.setAdapter(adapter);
    }
}