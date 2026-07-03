package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class User_Buyer_Calcuator extends AppCompatActivity {
Button btnBorrow1,btnAddReview1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_buyer_calcuator);
        btnBorrow1=findViewById(R.id.btnBorrow1);

        btnBorrow1.setOnClickListener(view -> {
            startActivity(new Intent(User_Buyer_Calcuator.this, User_Buyer_borowing.class));
        });
        

    }
}