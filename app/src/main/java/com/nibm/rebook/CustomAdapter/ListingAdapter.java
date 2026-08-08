package com.nibm.rebook.CustomAdapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        holder.txtMaterialStatus.setText(isBuyer ? "Type: " + material.getType() : "Status: " + material.getStatus());

        if (isBuyer) {
            holder.btnAction.setText("Get Item");
            holder.btnAction.setOnClickListener(v -> {
                android.content.Context context = v.getContext();
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
                
                intent.putExtra("MATERIAL_ID", material.getId());
                intent.putExtra("MATERIAL_TITLE", material.getTitle());
                intent.putExtra("MATERIAL_PRICE", String.valueOf(material.getPrice()));
                intent.putExtra("SELLER_ID", material.getSellerId());
                context.startActivity(intent);
            });
        } else {
            holder.btnAction.setText("Edit");
            holder.btnAction.setOnClickListener(v -> {
                android.content.Context context = v.getContext();
                Intent intent = new Intent(context, EditMaterialActivity.class);
                intent.putExtra("MATERIAL_ID", material.getId());
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

        public ViewHolder(View itemView) {
            super(itemView);
            txtMaterialName = itemView.findViewById(R.id.txtMaterialName);
            txtMaterialStatus = itemView.findViewById(R.id.txtMaterialStatus);
            btnAction = itemView.findViewById(R.id.btnEditListing);
        }
    }
}