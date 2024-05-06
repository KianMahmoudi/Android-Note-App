package com.example.note;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.List;

public class ListNoteAdapter extends RecyclerView.Adapter<ListNoteAdapter.ViewHolder> {
    private AppDatabase db;

    private List<ListNote> listNotes;

    private Context context;

    public ListNoteAdapter(List<ListNote> listNotes, Context context) {
        this.listNotes = listNotes;
        this.context = context;
        db = Room.databaseBuilder(context,
                AppDatabase.class, "notes").allowMainThreadQueries().build();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_note, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.title.setText(listNotes.get(position).getTitle());
        holder.itemView.setOnClickListener(v -> {
            Intent i = new Intent(context, ListNoteActivity.class);
            i.putExtra("id", listNotes.get(position).getId());
            i.putExtra("title", listNotes.get(position).getTitle().toString());
            context.startActivity(i);
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.getListNoteDao().delete(listNotes.get(position));
                listNotes.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listNotes.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.item_listNote_title);
            btnDelete = itemView.findViewById(R.id.btn_delete);

        }
    }
}
