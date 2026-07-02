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
            f.setStatus("Hidden");
            notifyDataSetChanged();
            Toast.makeText(context, "Feedback Hidden", Toast.LENGTH_SHORT).show();
        });

        // DELETE FEEDBACK
        holder.deleteBtn.setOnClickListener(v -> {
            list.remove(position);
            notifyDataSetChanged();
            Toast.makeText(context, "Feedback Deleted", Toast.LENGTH_SHORT).show();
        });
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