package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class BuyerLectureNotes extends AppCompatActivity {
    Button btn_notes_Borrow1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_buyer_lecture_notes);
        btn_notes_Borrow1=findViewById(R.id.btn_notes_Borrow1);

        btn_notes_Borrow1.setOnClickListener(view -> {
            startActivity(new Intent(BuyerLectureNotes.this, BuyerBorowing.class));
        });

    }
}