package com.nibm.rebooknew.CustomAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.nibm.rebooknew.dto.HistoryItem;
import com.nibm.rebooknew.R;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private List<HistoryItem> list;

    // Constructor
    public HistoryAdapter(List<HistoryItem> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // CORRECTED: Inflate your custom XML instead of the system layout
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryItem item = list.get(position);

        // CORRECTED: Use the IDs from item_history.xml
        holder.txtTitle.setText(item.getTitle());
        holder.txtDate.setText("Sold on: " + item.getDate());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // ViewHolder class matches the IDs in item_history.xml
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDate;

        public ViewHolder(View itemView) {
            super(itemView);
            // CORRECTED: Finding IDs from your custom layout file
            txtTitle = itemView.findViewById(R.id.txtHistoryTitle);
            txtDate = itemView.findViewById(R.id.txtHistoryDate);
        }
    }
}