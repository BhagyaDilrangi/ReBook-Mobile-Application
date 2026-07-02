package com.nibm.rebook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {

    ArrayList<LeaderboardModel> list;

    public LeaderboardAdapter(ArrayList<LeaderboardModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        LeaderboardModel user = list.get(position);

        holder.name.setText(user.getName());
        holder.points.setText("Points: " + user.getPoints());
        holder.rank.setText("Rank #" + user.getRank());

        holder.progressBar.setProgress(user.getPoints());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView name, points, rank;
        ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.lbName);
            points = itemView.findViewById(R.id.lbPoints);
            rank = itemView.findViewById(R.id.lbRank);
            progressBar = itemView.findViewById(R.id.lbProgress);
        }
    }
}