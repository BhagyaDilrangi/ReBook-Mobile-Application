package com.nibm.rebook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

        holder.verifyBtn.setOnClickListener(v -> {
            student.setStatus("Verified");
            notifyItemChanged(position);
        });

        holder.rejectBtn.setOnClickListener(v -> {
            student.setStatus("Rejected");
            notifyItemChanged(position);
        });

        holder.suspendBtn.setOnClickListener(v -> {
            student.setStatus("Suspended");
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name;
        TextView email;
        TextView status;

        Button verifyBtn;
        Button rejectBtn;
        Button suspendBtn;

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