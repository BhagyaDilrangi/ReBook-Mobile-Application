package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class User_Buyer_Register extends AppCompatActivity {

    Button regi_btnregister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_buyer_register);

        regi_btnregister=findViewById(R.id.regi_btnregister);
        regi_btnregister.setOnClickListener(v ->
                startActivity(new Intent(User_Buyer_Register.this,User_Buyer_Home.class)));

    }
}