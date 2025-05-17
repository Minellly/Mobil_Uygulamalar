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

    private final List<String> titles;
    private final Context context;

    public DailyTitleAdapter(Context context, List<String> titles) {
        this.titles = titles;
        this.context = context;
    }

    @NonNull
    @Override
    public DailyTitleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.daily_title_item, parent, false);
        return new DailyTitleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyTitleViewHolder holder, int position) {
        String title = titles.get(position);
        holder.titleTextView.setText(title);

        // 🔹 Tıklama olayı
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, AddDailyActivity.class);
            intent.putExtra("title", title); // Günlük 1, Günlük 2...
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public static class DailyTitleViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;

        public DailyTitleViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.dailyTitle);
        }
    }
}
