package com.nibm.rebook.CustomAdapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nibm.rebook.EditMaterialActivity;
import com.nibm.rebook.dto.Material;
import com.nibm.rebook.R;

import java.util.List;

public class ListingAdapter extends RecyclerView.Adapter<ListingAdapter.ViewHolder> {

    private List<Material> materialList;

    public ListingAdapter(List<Material> materialList) {
        this.materialList = materialList;
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
        holder.txtMaterialStatus.setText("Status: " + material.getStatus());

        holder.btnEdit.setOnClickListener(v -> {
            android.content.Context context = v.getContext();
            Intent intent = new Intent(context, EditMaterialActivity.class);
            intent.putExtra("material_title", material.getTitle());
            // Pass price or ID if your Material DTO supports it, e.g.:
            // intent.putExtra("material_price", material.getPrice());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return materialList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtMaterialName, txtMaterialStatus;
        Button btnEdit;

        public ViewHolder(View itemView) {
            super(itemView);
            txtMaterialName = itemView.findViewById(R.id.txtMaterialName);
            txtMaterialStatus = itemView.findViewById(R.id.txtMaterialStatus);
            btnEdit = itemView.findViewById(R.id.btnEditListing);
        }
    }
}