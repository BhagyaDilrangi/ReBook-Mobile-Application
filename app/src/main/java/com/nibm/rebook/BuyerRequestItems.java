package com.nibm.rebook;

import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nibm.rebook.CustomAdapter.RequestAdapter;
import com.nibm.rebook.dto.Request;

import java.util.ArrayList;
import java.util.List;

public class BuyerRequestItems extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RequestAdapter adapter;
    private List<Request> requestList;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_buyer_request_items);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("transactions");

        recyclerView = findViewById(R.id.recyclerViewBuyerRequests);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        requestList = new ArrayList<>();
        // Note: RequestAdapter currently has Accept/Reject buttons. 
        // In a real app, you'd hide them for buyers. For simplicity, we use the same adapter.
        adapter = new RequestAdapter(requestList);
        recyclerView.setAdapter(adapter);

        fetchMyRequests();
    }

    private void fetchMyRequests() {
        if (mAuth.getCurrentUser() == null) return;
        String buyerId = mAuth.getCurrentUser().getUid();

        mDatabase.orderByChild("buyerId").equalTo(buyerId)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        requestList.clear();
                        for (DataSnapshot data : snapshot.getChildren()) {
                            Request request = data.getValue(Request.class);
                            if (request != null) {
                                requestList.add(request);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(BuyerRequestItems.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}