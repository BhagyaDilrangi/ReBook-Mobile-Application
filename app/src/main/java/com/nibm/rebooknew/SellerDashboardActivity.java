package com.nibm.rebooknew;

import android.content.Intent; // IMPORTANT: Must be imported
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class SellerDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_seller_dashboard);


        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        CardView cardAddMaterial = findViewById(R.id.cardAddMaterial);
        CardView cardMyListings = findViewById(R.id.cardMyListings);
        CardView cardRequests = findViewById(R.id.cardRequests);
        CardView cardRatings = findViewById(R.id.cardRatings);


        cardAddMaterial.setOnClickListener(v -> {

            if (isUserVerified()) {

                Intent intent = new Intent(SellerDashboardActivity.this, AddMaterialActivity.class);
                startActivity(intent);
            } else {

                Toast.makeText(this,
                        "Your account must be verified before publishing materials.",
                        Toast.LENGTH_LONG).show();
            }
        });

        cardMyListings.setOnClickListener(v -> {
            startActivity(new Intent(this, MyListingsActivity.class));
        });


        cardRequests.setOnClickListener(v -> {
            startActivity(new Intent(this, SalesRequestsActivity.class));
        });


        cardRatings.setOnClickListener(v -> {
            startActivity(new Intent(this, SellerFeedbackActivity.class));
        });
    }


    private boolean isUserVerified() {

        return true;
    }
}