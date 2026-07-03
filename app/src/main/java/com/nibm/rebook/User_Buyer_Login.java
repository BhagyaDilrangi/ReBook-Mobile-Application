package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class User_Buyer_Login extends AppCompatActivity {
 Button btnlogin;
 TextView txtregister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_buyer_login);


        txtregister= findViewById(R.id.txtregister);
        btnlogin=findViewById(R.id.btnlogin);
        txtregister.setOnClickListener(v ->
                startActivity(new Intent(User_Buyer_Login.this,User_Buyer_Register.class)));

        btnlogin.setOnClickListener(
                view ->
                        startActivity(new Intent(User_Buyer_Login.this,User_Buyer_Home.class))
        );
    }
}