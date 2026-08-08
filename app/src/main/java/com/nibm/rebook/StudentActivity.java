package com.nibm.rebook;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    StudentAdapter adapter;
    ArrayList<StudentModel> list;
    ArrayList<StudentModel> filteredList;
    EditText search;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        recyclerView = findViewById(R.id.recyclerViewStudents);
        search = findViewById(R.id.searchStudent);

        list = new ArrayList<>();
        filteredList = new ArrayList<>();

        mDatabase = FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/").getReference("users");

        adapter = new StudentAdapter(filteredList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Fetch data from Firebase
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    String uid = dataSnapshot.getKey();
                    String fName = dataSnapshot.child("firstName").getValue(String.class);
                    String lName = dataSnapshot.child("lastName").getValue(String.class);
                    String email = dataSnapshot.child("email").getValue(String.class);
                    String status = dataSnapshot.child("status").getValue(String.class);
                    
                    if (status == null) status = "Pending";
                    
                    String fullName = (fName != null ? fName : "") + " " + (lName != null ? lName : "");
                    list.add(new StudentModel(uid, fullName.trim(), email, status));
                }
                filteredList.clear();
                filteredList.addAll(list);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StudentActivity.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Search Function
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filter(String text) {
        filteredList.clear();

        for (StudentModel s : list) {
            if (s.getName() != null && s.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(s);
            }
        }

        adapter.notifyDataSetChanged();
    }
}