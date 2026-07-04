package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class User_Buyer_BooksSales extends AppCompatActivity {

    Button btnBook_sell_get;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_buyer_books_sales);

    btnBook_sell_get=findViewById(R.id.btnBook_sell_get);

    btnBook_sell_get.setOnClickListener(view -> {
        startActivity(new Intent(User_Buyer_BooksSales.this, User_Buyer_Selling.class));
    });
    }
}