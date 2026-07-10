package com.nibm.rebook;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class BuyerBooks extends AppCompatActivity {

    Button btn_booksborrow_Details1,btn_bookBorrow_borrow,btn_bookborrow_Save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_buyer_books);

        btn_bookborrow_Save=findViewById(R.id.btn_bookborrow_Save);
        btn_bookBorrow_borrow=findViewById(R.id.btn_bookBorrow_borrow);
        btn_booksborrow_Details1=findViewById(R.id.btn_booksborrow_Details1);

       btn_bookBorrow_borrow.setOnClickListener(view -> {
           startActivity(new Intent(BuyerBooks.this, BuyerBorowing.class));

       });


    }
}