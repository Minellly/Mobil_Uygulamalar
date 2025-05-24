package com.example.gunluk_uygulamasi;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DailyTitleAdapter extends RecyclerView.Adapter<DailyTitleAdapter.DailyTitleViewHolder> {

    private List<DailyItem> items; // 🔄 String değil, artık DailyItem
    private final Context context;

    public DailyTitleAdapter(Context context, List<DailyItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public DailyTitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.daily_title_item, parent, false);
        return new DailyTitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyTitleViewHolder holder, int position) {
        DailyItem item = items.get(position);

        holder.titleTextView.setText(item.getTitle());
        holder.dateTextView.setText(item.getDate());

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddDailyActivity.class);
            intent.putExtra("title", item.getTitle());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    // 🔄 Listeyi güncellemek için
    public void updateData(List<DailyItem> newItems) {
        items.clear();
        items.addAll(newItems);
        notifyDataSetChanged();
    }

    public static class DailyTitleViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView dateTextView;

        public DailyTitleViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.dailyTitle);
            dateTextView = itemView.findViewById(R.id.dateTextView); // 💡 Tarih gösterimi
        }
    }
}
