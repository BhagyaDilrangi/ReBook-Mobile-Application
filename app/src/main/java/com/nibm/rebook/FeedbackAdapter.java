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

public class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ViewHolder> {

    ArrayList<FeedbackModel> list;
    Context context;

    public FeedbackAdapter(ArrayList<FeedbackModel> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_feedback, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        FeedbackModel f = list.get(position);

        holder.user.setText(f.getUser());
        holder.comment.setText(f.getComment());
        holder.rating.setText("Rating: " + f.getRating());
        holder.status.setText(f.getStatus());

        // VIEW COMMENT
        holder.viewBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle(f.getUser() + " Feedback")
                    .setMessage(f.getComment())
                    .setPositiveButton("Close", null)
                    .show();
        });

        // HIDE FEEDBACK
        holder.hideBtn.setOnClickListener(v -> {
            updateStatus(f, "Hidden");
        });

        // DELETE FEEDBACK
        holder.deleteBtn.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Feedback")
                    .setMessage("Are you sure you want to delete this feedback?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        deleteFeedback(f);
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    private void updateStatus(FeedbackModel feedback, String status) {
        // Since FeedbackModel might not have an ID yet, we'll need to handle it.
        // Assuming we update the model to include an ID from Firebase push()
        // For now, using a combination of user and comment as a fallback if ID is missing
        String key = feedback.getUser() != null ? feedback.getUser().replace(".", "_") : "unknown";
        
        FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("feedbacks")
                .child(key) // This is a placeholder, ideally use feedback.getId()
                .child("status").setValue(status)
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Feedback updated to " + status, Toast.LENGTH_SHORT).show());
    }

    private void deleteFeedback(FeedbackModel feedback) {
        String key = feedback.getUser() != null ? feedback.getUser().replace(".", "_") : "unknown";
        FirebaseDatabase.getInstance("https://rebook-cff2e-default-rtdb.asia-southeast1.firebasedatabase.app/")
                .getReference("feedbacks")
                .child(key)
                .removeValue()
                .addOnSuccessListener(aVoid -> Toast.makeText(context, "Feedback Deleted", Toast.LENGTH_SHORT).show());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView user, comment, rating, status;
        Button viewBtn, hideBtn, deleteBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            user = itemView.findViewById(R.id.feedbackUser);
            comment = itemView.findViewById(R.id.feedbackComment);
            rating = itemView.findViewById(R.id.feedbackRating);
            status = itemView.findViewById(R.id.feedbackStatus);

            viewBtn = itemView.findViewById(R.id.btnView);
            hideBtn = itemView.findViewById(R.id.btnHide);
            deleteBtn = itemView.findViewById(R.id.btnDelete);
        }
    }
}