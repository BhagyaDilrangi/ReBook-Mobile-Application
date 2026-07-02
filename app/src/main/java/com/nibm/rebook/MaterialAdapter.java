package com.nibm.rebook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MaterialAdapter extends RecyclerView.Adapter<MaterialAdapter.ViewHolder> {

    ArrayList<MaterialModel> list;

    public MaterialAdapter(ArrayList<MaterialModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_material, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        MaterialModel material = list.get(position);

        holder.title.setText(material.getTitle());
        holder.type.setText(material.getType());
        holder.status.setText(material.getStatus());

        // VIEW
        holder.viewBtn.setOnClickListener(v -> {
            material.setStatus("Available");
            notifyDataSetChanged();
        });

        // HIDE
        holder.hideBtn.setOnClickListener(v -> {
            material.setStatus("Hidden");
            notifyDataSetChanged();
        });

        // DELETE
        holder.deleteBtn.setOnClickListener(v -> {
            material.setStatus("Deleted");
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, type, status;
        Button viewBtn, hideBtn, deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.materialTitle);
            type = itemView.findViewById(R.id.materialType);
            status = itemView.findViewById(R.id.materialStatus);

            viewBtn = itemView.findViewById(R.id.btnView);
            hideBtn = itemView.findViewById(R.id.btnHide);
            deleteBtn = itemView.findViewById(R.id.btnDelete);
        }
    }
}