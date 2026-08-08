package com.nibm.rebook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;

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

        // VIEW (Make Available)
        holder.viewBtn.setOnClickListener(v -> updateStatus(material, "Available", v.getContext()));

        // HIDE
        holder.hideBtn.setOnClickListener(v -> updateStatus(material, "Hidden", v.getContext()));

        // DELETE
        holder.deleteBtn.setOnClickListener(v -> updateStatus(material, "Deleted", v.getContext()));
    }

    private void updateStatus(MaterialModel material, String status, android.content.Context context) {
        // Ideally MaterialModel should have an ID. 
        // If not, we use the title as a key (less safe but works with the current dummy data structure)
        String key = material.getTitle().replace(".", "_").replace("#", "_").replace("$", "_").replace("[", "_").replace("]", "_");
        
        FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("materials")
                .child(key)
                .child("status").setValue(status)
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Material " + status, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
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