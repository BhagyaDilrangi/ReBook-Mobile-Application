package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class User_Buyer_CalculaterFilter extends AppCompatActivity {

    Button btndonationsfilter,btnsalesfilter,btnborowfilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_buyer_calculater_filter);

        btnborowfilter=findViewById(R.id.btnborowfilter);
        btndonationsfilter=findViewById(R.id.btndonationsfilter);
        btnsalesfilter=findViewById(R.id.btnsalesfilter);

        btnborowfilter.setOnClickListener(view -> {
            startActivity(new Intent(User_Buyer_CalculaterFilter.this,User_Buyer_Calcuator.class));
        });

        btnsalesfilter.setOnClickListener(view -> {
            startActivity(new Intent(User_Buyer_CalculaterFilter.this, User_Buyer_CalculatarSales.class));
        });

        btndonationsfilter.setOnClickListener(view -> {
            startActivity(new Intent(User_Buyer_CalculaterFilter.this, User_Buyer_CalculculatorDonate.class));
        });


    }
}