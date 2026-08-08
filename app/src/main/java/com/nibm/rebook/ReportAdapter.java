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

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    ArrayList<ReportModel> list;
    Context context;

    public ReportAdapter(ArrayList<ReportModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ReportModel report = list.get(position);

        holder.title.setText(report.getTitle());
        holder.description.setText(report.getDescription());
        holder.status.setText(report.getStatus());

        // VIEW DETAILS
        holder.viewBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle(report.getTitle())
                    .setMessage(report.getDescription())
                    .setPositiveButton("Close", null)
                    .show();
        });

        // RESOLVE
        holder.resolveBtn.setOnClickListener(v -> {
            // Updating status in Firebase
            // Note: ReportModel should have an 'id' for this to work perfectly.
            // If it doesn't, we'd need to match by title/description or add ID.
            FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                    .getReference("reports")
                    .child(report.getTitle().replace(".", "_")) // Hacky way if no ID
                    .child("status").setValue("Resolved")
                    .addOnSuccessListener(aVoid -> Toast.makeText(context, "Report Resolved", Toast.LENGTH_SHORT).show());
        });

        // WARN
        holder.warnBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Warning Sent")
                    .setMessage("User has been warned for: " + report.getTitle())
                    .setPositiveButton("OK", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView title, description, status;
        Button viewBtn, resolveBtn, warnBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.reportTitle);
            description = itemView.findViewById(R.id.reportDescription);
            status = itemView.findViewById(R.id.reportStatus);

            viewBtn = itemView.findViewById(R.id.btnView);
            resolveBtn = itemView.findViewById(R.id.btnResolve);
            warnBtn = itemView.findViewById(R.id.btnWarn);
        }
    }
}