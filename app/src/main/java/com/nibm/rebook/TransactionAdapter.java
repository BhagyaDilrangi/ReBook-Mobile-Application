package com.nibm.rebook;

import android.app.AlertDialog;
import android.content.Context;
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

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.ViewHolder> {

    ArrayList<TransactionModel> list;
    Context context;

    public TransactionAdapter(ArrayList<TransactionModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_transaction, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        TransactionModel t = list.get(position);

        holder.id.setText(t.getId());
        holder.customer.setText(t.getCustomer());
        holder.amount.setText("Rs. " + t.getAmount());
        holder.status.setText(t.getStatus());

        // VIEW DETAILS
        holder.viewBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Transaction Details")
                    .setMessage(
                            "ID: " + t.getId() + "\n" +
                                    "Customer: " + t.getCustomer() + "\n" +
                                    "Amount: Rs. " + t.getAmount() + "\n" +
                                    "Status: " + t.getStatus()
                    )
                    .setPositiveButton("OK", null)
                    .show();
        });

        // MARK AS PAID
        holder.paidBtn.setOnClickListener(v -> {
            updateTransactionStatus(t.getId(), "Paid");
        });

        // MARK AS PENDING
        holder.pendingBtn.setOnClickListener(v -> {
            updateTransactionStatus(t.getId(), "Pending");
        });
    }

    private void updateTransactionStatus(String transactionId, String status) {
        if (transactionId == null) return;
        FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("transactions")
                .child(transactionId)
                .child("status")
                .setValue(status)
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Transaction " + status, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView id, customer, amount, status;
        Button viewBtn, paidBtn, pendingBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            id = itemView.findViewById(R.id.transactionId);
            customer = itemView.findViewById(R.id.transactionCustomer);
            amount = itemView.findViewById(R.id.transactionAmount);
            status = itemView.findViewById(R.id.transactionStatus);

            viewBtn = itemView.findViewById(R.id.btnView);
            paidBtn = itemView.findViewById(R.id.btnPaid);
            pendingBtn = itemView.findViewById(R.id.btnPending);
        }
    }
}