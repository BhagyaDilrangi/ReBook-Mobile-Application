package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.unusedapprestrictions.IUnusedAppRestrictionsBackportService;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class User_Biyer_BooksFilter extends AppCompatActivity {

    Button btndonationsfilter,btnsalesfilter,btnborowfilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_biyer_books_filter);

        btndonationsfilter=findViewById(R.id.btndonationsfilter);
        btnborowfilter=findViewById(R.id.btnborowfilter);
        btndonationsfilter=findViewById(R.id.btndonationsfilter);

        btnborowfilter.setOnClickListener(view -> {
            startActivity(new Intent(User_Biyer_BooksFilter.this, User_Buyer_Books.class));
        });

        btnsalesfilter.setOnClickListener(view -> {
            startActivity(new Intent(User_Biyer_BooksFilter.this, User_Buyer_BooksSales.class));
        });

        btnsalesfilter.setOnClickListener(view -> {
            startActivity(new Intent(User_Biyer_BooksFilter.this, User_Buyer_BooksDonate.class));
        });
    }
}