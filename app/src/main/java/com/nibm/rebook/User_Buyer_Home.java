package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class User_Buyer_Home extends AppCompatActivity {
    Button btnCalculator,btnPastPapers,btnNotes,btnBooks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_buyer_home);

        btnCalculator = findViewById(R.id.btnCalculator);
        btnPastPapers=findViewById(R.id.btnPastPapers);
        btnBooks=findViewById(R.id.btnBooks);
        btnNotes=findViewById(R.id.btnNotes);



        btnCalculator.setOnClickListener(view ->{
                startActivity(new Intent(User_Buyer_Home.this,User_Buyer_CalculaterFilter.class));
        }
        );

        btnPastPapers.setOnClickListener(view -> {
            startActivity(new Intent(User_Buyer_Home.this, User_Buyer_PastPapersFilter.class));
        });

        btnNotes.setOnClickListener(view -> {
            startActivity(new Intent(User_Buyer_Home.this, User_Buyer_NotesFilter.class));
        });

        btnBooks.setOnClickListener(view -> {
            startActivity(new Intent(User_Buyer_Home.this,User_Biyer_BooksFilter.class));
        });


    }
}
