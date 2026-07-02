package com.nibm.rebook;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class StudentActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    StudentAdapter adapter;
    ArrayList<StudentModel> list;
    ArrayList<StudentModel> filteredList;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        recyclerView = findViewById(R.id.recyclerViewStudents);
        search = findViewById(R.id.searchStudent);

        list = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Dummy Data
        list.add(new StudentModel("Kasun Perera", "kasun@gmail.com", "Pending"));
        list.add(new StudentModel("Nimal Silva", "nimal@gmail.com", "Verified"));
        list.add(new StudentModel("Amaya Fernando", "amaya@gmail.com", "Suspended"));
        list.add(new StudentModel("Kavindi Jay", "kavindi@gmail.com", "Pending"));

        filteredList.addAll(list);

        adapter = new StudentAdapter(filteredList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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
            if (s.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(s);
            }
        }

        adapter.notifyDataSetChanged();
    }
}