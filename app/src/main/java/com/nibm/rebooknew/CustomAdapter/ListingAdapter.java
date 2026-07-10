package com.nibm.rebooknew.CustomAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nibm.rebooknew.dto.Material;
import com.nibm.rebooknew.R;

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
            Toast.makeText(v.getContext(), "Edit: " + material.getTitle(), Toast.LENGTH_SHORT).show();

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