package com.nibm.rebook;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MaterialActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    MaterialAdapter adapter;
    ArrayList<MaterialModel> list;
    ArrayList<MaterialModel> filteredList;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_material);

        recyclerView = findViewById(R.id.recyclerViewMaterials);
        search = findViewById(R.id.searchMaterial);

        list = new ArrayList<>();
        filteredList = new ArrayList<>();

        // Dummy Data
        list.add(new MaterialModel("Math Notes", "PDF", "Available"));
        list.add(new MaterialModel("Science Guide", "Video", "Hidden"));
        list.add(new MaterialModel("English Book", "PDF", "Available"));
        list.add(new MaterialModel("IT Lecture", "Video", "Deleted"));

        filteredList.addAll(list);

        adapter = new MaterialAdapter(filteredList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Search
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

        for (MaterialModel m : list) {
            if (m.getTitle().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(m);
            }
        }

        adapter.notifyDataSetChanged();
    }
}