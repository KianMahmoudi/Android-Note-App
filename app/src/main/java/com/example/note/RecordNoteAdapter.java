package com.example.note;

import android.content.Context;
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

public class RecordNoteAdapter extends RecyclerView.Adapter<RecordNoteAdapter.ViewHolder> {

    private Context context;
    private AppDatabase db;
    private List<RecordNote> recordNotes;
    private AlertDialog.Builder builder;

    public RecordNoteAdapter(List<RecordNote> recordNotes, Context context) {
        this.context = context;
        this.recordNotes = recordNotes;
        db = Room.databaseBuilder(context,
                AppDatabase.class, "notes").allowMainThreadQueries().build();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, RecordNoteActivity.class);
            intent.putExtra("id", recordNotes.get(position).getId());
            intent.putExtra("title", recordNotes.get(position).getTitle());
            intent.putExtra("filePath", recordNotes.get(position).getPath());
            context.startActivity(intent);
        });
        holder.btnDelete.setOnClickListener(v -> {
            builder = new AlertDialog.Builder(context);
            builder.setTitle("Are you sure?");
            builder.setMessage("Are you sure to delete this note?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", (dialog, which) -> {
                        db.getRecordNoteDao().delete(recordNotes.get(position));
                        recordNotes.remove(position);
                        notifyItemRemoved(position);
                    }).setNegativeButton("No", (dialog, which) -> {
                        dialog.cancel();
                    });
            AlertDialog dialog = builder.create();
            dialog.setTitle("Are you sure?");
            dialog.show();
        });
        holder.title.setText(recordNotes.get(position).getTitle().toString());
    }

    @Override
    public int getItemCount() {
        return recordNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView title;
        private ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.item_recordNote_title);
            btnDelete = itemView.findViewById(R.id.btn_delete);
        }
    }
}
