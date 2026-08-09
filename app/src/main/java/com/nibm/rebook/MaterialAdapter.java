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

    private ArrayList<MaterialModel> list;
    private OnMaterialActionListener listener;

    public interface OnMaterialActionListener {
        void onView(MaterialModel material);
        void onHide(MaterialModel material);
        void onDelete(MaterialModel material);
    }

    public MaterialAdapter(ArrayList<MaterialModel> list, OnMaterialActionListener listener) {
        this.list = list;
        this.listener = listener;
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

        // Use the listener to handle actions in the Activity
        holder.viewBtn.setOnClickListener(v -> {
            if (listener != null) listener.onView(material);
        });

        holder.hideBtn.setOnClickListener(v -> {
            if (listener != null) listener.onHide(material);
        });

        holder.deleteBtn.setOnClickListener(v -> {
            if (listener != null) listener.onDelete(material);
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
