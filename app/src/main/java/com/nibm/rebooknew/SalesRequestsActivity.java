package com.nibm.rebooknew;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.nibm.rebooknew.CustomAdapter.RequestAdapter;
import com.nibm.rebooknew.dto.Request;

import java.util.ArrayList;
import java.util.List;

public class SalesRequestsActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_requests);

        getSupportActionBar().hide();

        RecyclerView rvRequests = findViewById(R.id.rvRequests);

        rvRequests.setLayoutManager(new LinearLayoutManager(this));

        List<Request> requestList = new ArrayList<>();

        requestList.add(new Request("Calculus Book", "Student A"));

        RequestAdapter adapter = new RequestAdapter(requestList);
        rvRequests.setAdapter(adapter);



    }
}