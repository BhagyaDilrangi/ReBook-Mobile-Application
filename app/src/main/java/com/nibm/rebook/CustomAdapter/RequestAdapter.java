package com.nibm.rebook.CustomAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.FirebaseDatabase;
import com.nibm.rebook.R;
import com.nibm.rebook.dto.Request;

import java.util.List;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.ViewHolder> {
    private List<Request> requestList;

    public RequestAdapter(List<Request> requestList) { this.requestList = requestList; }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_request, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Request request = requestList.get(position);
        holder.txtTitle.setText(request.getMaterialTitle());
        holder.txtBuyer.setText("Buyer: " + request.getBuyerName());
        holder.txtStatus.setText("Status: " + request.getStatus());

        holder.btnAccept.setOnClickListener(v -> {
            updateStatus(request.getId(), "Accepted", v.getContext());
        });

        holder.btnReject.setOnClickListener(v -> {
            updateStatus(request.getId(), "Rejected", v.getContext());
        });
    }

    private void updateStatus(String requestId, String status, android.content.Context context) {
        if (requestId == null) return;
        FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("transactions").child(requestId).child("status").setValue(status)
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Request " + status, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() { return requestList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtBuyer, txtStatus;
        Button btnAccept, btnReject;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtMaterialTitle);
            txtBuyer = itemView.findViewById(R.id.txtBuyerName);
            txtStatus = itemView.findViewById(R.id.txtStatus); // Ensure this exists in XML or add it
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}