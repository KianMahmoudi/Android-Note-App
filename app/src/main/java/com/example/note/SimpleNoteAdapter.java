package com.example.note;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class SimpleNoteAdapter extends RecyclerView.Adapter<SimpleNoteAdapter.ViewHolder> {
    private AppDatabase db;
    private List<SimpleNote> simpleNotes;
    private Context context;
    private AlertDialog.Builder builder;


    public SimpleNoteAdapter(List<SimpleNote> simpleNotes, Context context) {
        this.simpleNotes = simpleNotes;
        this.context = context;
        db = Room.databaseBuilder(context,
                AppDatabase.class, "notes").allowMainThreadQueries().build();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_simple_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(simpleNotes.get(position));
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, SimpleNoteActivity.class);
            intent.putExtra("id", simpleNotes.get(position).getId());
            intent.putExtra("title", simpleNotes.get(position).getTitle());
            intent.putExtra("note", simpleNotes.get(position).getNote());
            context.startActivity(intent);
        });
        holder.btnDelete.setOnClickListener(v -> {
            builder = new AlertDialog.Builder(context);
            builder.setTitle("Are you sure?");
            builder.setMessage("Are you sure to delete this note?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        db.getSimpleNoteDao().delete(simpleNotes.get(position));
                        simpleNotes.remove(position);
                        notifyItemRemoved(position);
                    }).setNegativeButton("No", (dialog, which) -> {
                        dialog.cancel();
                    });
            AlertDialog dialog = builder.create();
            dialog.setTitle("Are you sure?");
            dialog.show();

        });

    }

    @Override
    public int getItemCount() {
        return simpleNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_simpleNote_title);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(SimpleNote simpleNote) {
            title.setText(simpleNote.getTitle());
        }
    }

}


