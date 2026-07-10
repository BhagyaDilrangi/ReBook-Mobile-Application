package com.nibm.rebooknew.CustomAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nibm.rebooknew.R;
import com.nibm.rebooknew.dto.Request;

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

        holder.btnAccept.setOnClickListener(v -> {

            Toast.makeText(v.getContext(), "Request Accepted", Toast.LENGTH_SHORT).show();
        });

        holder.btnReject.setOnClickListener(v -> {

            Toast.makeText(v.getContext(), "Request Rejected", Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() { return requestList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtBuyer;
        Button btnAccept, btnReject;

        public ViewHolder(View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txtMaterialTitle);
            txtBuyer = itemView.findViewById(R.id.txtBuyerName);
            btnAccept = itemView.findViewById(R.id.btnAccept);
            btnReject = itemView.findViewById(R.id.btnReject);
        }
    }
}