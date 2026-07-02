package com.nibm.rebook;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class FeedbackActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    FeedbackAdapter adapter;
    ArrayList<FeedbackModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        recyclerView = findViewById(R.id.recyclerViewFeedback);

        list = new ArrayList<>();

        // Dummy Data
        list.add(new FeedbackModel("Kasun", "Great app!", "5", "Visible"));
        list.add(new FeedbackModel("Nimal", "Very useful system", "4", "Visible"));
        list.add(new FeedbackModel("Amaya", "Needs improvement", "3", "Hidden"));
        list.add(new FeedbackModel("Kavindi", "Excellent UI", "5", "Visible"));

        adapter = new FeedbackAdapter(list, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}