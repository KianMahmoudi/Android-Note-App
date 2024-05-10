package com.example.note;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ListNoteItemsAdapter extends RecyclerView.Adapter<ListNoteItemsAdapter.ItemsViewHolder> {
    private List<ListNoteItems> items;
    private Context context;

    public ListNoteItemsAdapter(List<ListNoteItems> items, Context context) {
        this.items = items;
        this.context = context;
    }

    public void setItems(List<ListNoteItems> items) {
        this.items = items;
        notifyDataSetChanged();
    }

    public List<ListNoteItems> getItems() {
        return items;
    }

    public void addItem(ListNoteItems item) {
        items.add(item);
        updateItems(items);
    }

    public void updateItems(List<ListNoteItems> items) {
        this.items = items;
        notifyItemInserted(items.size()-1);
    }

    @NonNull
    @Override
    public ItemsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkbox_edittext_item, parent, false);
        return new ItemsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemsViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder {
        EditText editText;
        CheckBox checkBox;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.editText);
            checkBox = itemView.findViewById(R.id.checkBox);
        }

        public void bind(int position) {
            ListNoteItems item = items.get(position);
            editText.setText(item.getDescription());
            checkBox.setChecked(item.isChecked());
        }
    }
}
