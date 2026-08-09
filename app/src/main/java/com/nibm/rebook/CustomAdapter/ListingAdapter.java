package com.nibm.rebook.CustomAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nibm.rebook.BuyerBorowing;
import com.nibm.rebook.BuyerDonating;
import com.nibm.rebook.BuyerSelling;
import com.nibm.rebook.EditMaterialActivity;
import com.nibm.rebook.dto.Material;
import com.nibm.rebook.R;

import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ViewHolder> {

    private List<Material> materialList;
    private boolean isBuyer;

    public ListingAdapter(List<Material> materialList) {
        this.materialList = materialList;
        this.isBuyer = false;
    }

    public ListingAdapter(List<Material> materialList, boolean isBuyer) {
        this.materialList = materialList;
        this.isBuyer = isBuyer;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_item_listing, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Material material = materialList.get(position);

        holder.txtMaterialName.setText(material.getTitle());

        // Display price along with status/type if available
        String details = isBuyer ? "Type: " + material.getType() : "Status: " + material.getStatus();
        holder.txtMaterialStatus.setText(details + " | LKR " + material.getPrice());

        // Decode and display Base64 image stored directly in database
        String base64Image = material.getImageUrl();
        if (base64Image != null && !base64Image.isEmpty()) {
            try {
                byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                holder.imgListing.setImageBitmap(decodedBitmap);
            } catch (Exception e) {
                holder.imgListing.setImageResource(R.drawable.ic_inventory);
            }
        } else {
            holder.imgListing.setImageResource(R.drawable.ic_inventory);
        }

        if (isBuyer) {
            holder.btnAction.setText("Get Item");
            holder.btnAction.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent;

                String type = material.getType();
                if ("Sale".equalsIgnoreCase(type)) {
                    intent = new Intent(context, BuyerSelling.class);
                } else if ("Borrow".equalsIgnoreCase(type)) {
                    intent = new Intent(context, BuyerBorowing.class);
                } else if ("Donate".equalsIgnoreCase(type)) {
                    intent = new Intent(context, BuyerDonating.class);
                } else {
                    intent = new Intent(context, BuyerSelling.class);
                }

                intent.putExtra("MATERIAL_ID", material.getMaterialId());
                intent.putExtra("MATERIAL_TITLE", material.getTitle());
                intent.putExtra("MATERIAL_PRICE", String.valueOf(material.getPrice()));
                intent.putExtra("SELLER_ID", material.getSellerId());
                context.startActivity(intent);
            });
        } else {
            holder.btnAction.setText("Edit");
            holder.btnAction.setOnClickListener(v -> {
                Context context = v.getContext();
                Intent intent = new Intent(context, EditMaterialActivity.class);
                intent.putExtra("MATERIAL_ID", material.getMaterialId());
                intent.putExtra("material_title", material.getTitle());
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMaterialName, txtMaterialStatus;
        Button btnAction;
        ImageView imgListing; // Added ImageView reference for item preview

        public ViewHolder(View itemView) {
            super(itemView);
            txtMaterialName = itemView.findViewById(R.id.txtMaterialName);
            txtMaterialStatus = itemView.findViewById(R.id.txtMaterialStatus);
            btnAction = itemView.findViewById(R.id.btnEditListing);
            imgListing = itemView.findViewById(R.id.imgMaterialThumbnail); // Corrected to match activity_item_listing.xml ID
        }
    }
}
