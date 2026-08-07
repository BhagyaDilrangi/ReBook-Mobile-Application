package com.nibm.rebook.CustomAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nibm.rebook.R;
import com.nibm.rebook.dto.HistoryItem;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private final List<HistoryItem> historyList;

    public HistoryAdapter(List<HistoryItem> historyList) {
        this.historyList = historyList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryItem item = historyList.get(position);

        if (holder.txtMaterialName != null) {
            holder.txtMaterialName.setText(item.getTitle());
        }
        if (holder.txtMaterialStatus != null) {
            holder.txtMaterialStatus.setText("Status: Completed");
        }
        if (holder.txtSoldCount != null) {
            holder.txtSoldCount.setText("Sold Count: " + item.getDate());
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView txtMaterialName;
        final TextView txtMaterialStatus;
        final TextView txtSoldCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMaterialName = itemView.findViewById(R.id.txtMaterialName);
            txtMaterialStatus = itemView.findViewById(R.id.txtMaterialStatus);
            txtSoldCount = itemView.findViewById(R.id.txtSoldCount);
        }
    }
}