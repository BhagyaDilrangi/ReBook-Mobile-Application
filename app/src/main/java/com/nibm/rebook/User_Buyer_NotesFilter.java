package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class User_Buyer_NotesFilter extends AppCompatActivity {
    Button btn_notes_donations_filter,btn_notes_sales_filter,btn_notes_borow_filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_buyer_notes_filter);

        btn_notes_donations_filter=findViewById(R.id.btn_notes_donations_filter);
        btn_notes_borow_filter=findViewById(R.id.btn_notes_borow_filter);
        btn_notes_sales_filter=findViewById(R.id.btn_notes_sales_filter);

        btn_notes_donations_filter.setOnClickListener(view -> {
            startActivity(new Intent(User_Buyer_NotesFilter.this,User_Buyer_NotesDonate.class));
        });

        btn_notes_sales_filter.setOnClickListener(view -> {
            startActivity(new Intent(User_Buyer_NotesFilter.this,User_Buyer_NotesSales.class));
        });
        btn_notes_borow_filter.setOnClickListener(view -> {
            startActivity(new Intent(User_Buyer_NotesFilter.this, User_Buyer_LectureNotes.class));
        });
    }
}