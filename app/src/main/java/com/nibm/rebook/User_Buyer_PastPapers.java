package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class User_Buyer_PastPapers extends AppCompatActivity {
    Button btn_pastpapers_Borrow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_buyer_past_papers);

        btn_pastpapers_Borrow=findViewById(R.id.btn_pastpapers_Borrow);

        btn_pastpapers_Borrow.setOnClickListener(view -> {
            startActivity(new Intent(User_Buyer_PastPapers.this,User_Buyer_borowing.class));
        });
    }
}