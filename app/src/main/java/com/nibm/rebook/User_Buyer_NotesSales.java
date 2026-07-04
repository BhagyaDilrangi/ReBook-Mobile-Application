package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class User_Buyer_NotesSales extends AppCompatActivity {

    Button btn_notes_sell_Save,btn_notes_sell_get,btn_notes_sales_viewDetails;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_buyer_notes_sales);

      btn_notes_sell_Save=findViewById(R.id.btn_notes_sell_Save);
      btn_notes_sell_get=findViewById(R.id.btn_notes_sell_get);
      btn_notes_sales_viewDetails=findViewById(R.id.btn_notes_sales_viewDetails);

      btn_notes_sell_get.setOnClickListener(view -> {
          startActivity(new Intent(User_Buyer_NotesSales.this, User_Buyer_Selling.class));
      });



    }
}