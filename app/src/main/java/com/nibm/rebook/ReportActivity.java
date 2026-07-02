package com.nibm.rebook;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReportActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ReportAdapter adapter;
    ArrayList<ReportModel> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        recyclerView = findViewById(R.id.recyclerViewReports);

        list = new ArrayList<>();

        // Dummy Data
        list.add(new ReportModel("Spam Content", "User posted spam materials", "Pending"));
        list.add(new ReportModel("Fake Notes", "Uploaded incorrect PDF", "Pending"));
        list.add(new ReportModel("Abuse", "Inappropriate content found", "Resolved"));

        adapter = new ReportAdapter(list, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }

    // Optional helper method for dialogs
    public void showWarning(String title) {
        new AlertDialog.Builder(this)
                .setTitle("Warning")
                .setMessage("Take action on: " + title)
                .setPositiveButton("OK", null)
                .show();
    }
}