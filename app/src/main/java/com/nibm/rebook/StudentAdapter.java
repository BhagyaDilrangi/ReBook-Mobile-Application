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

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private ArrayList<StudentModel> list;

    public StudentAdapter(ArrayList<StudentModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_student, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentModel student = list.get(position);

        holder.name.setText(student.getName());
        holder.email.setText(student.getEmail());
        holder.status.setText(student.getStatus());

        holder.verifyBtn.setOnClickListener(v -> updateStatus(student.getId(), "Verified", v.getContext()));
        holder.rejectBtn.setOnClickListener(v -> updateStatus(student.getId(), "Rejected", v.getContext()));
        holder.suspendBtn.setOnClickListener(v -> updateStatus(student.getId(), "Suspended", v.getContext()));
    }

    private void updateStatus(String uid, String status, android.content.Context context) {
        if (uid == null) return;
        FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("users").child(uid).child("status").setValue(status)
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "User " + status, Toast.LENGTH_SHORT).show())
                .addOnFailureListener(e -> Toast.makeText(context, "Update Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, email, status;
        Button verifyBtn, rejectBtn, suspendBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.studentName);
            email = itemView.findViewById(R.id.studentEmail);
            status = itemView.findViewById(R.id.studentStatus);
            verifyBtn = itemView.findViewById(R.id.btnVerify);
            rejectBtn = itemView.findViewById(R.id.btnReject);
            suspendBtn = itemView.findViewById(R.id.btnSuspend);
        }
    }
}