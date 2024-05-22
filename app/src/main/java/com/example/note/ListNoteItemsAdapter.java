package com.example.note;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

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
        notifyItemInserted(items.size() - 1);
    }

    public void removeItem(int position) {
        if (position >= 0 && position < items.size()) {
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size());
        }
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
        holder.deleteBtn.setOnClickListener(v -> removeItem(holder.getAdapterPosition()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ItemsViewHolder extends RecyclerView.ViewHolder {
        EditText editText;
        CheckBox checkBox;
        ImageButton deleteBtn;

        public ItemsViewHolder(@NonNull View itemView) {
            super(itemView);
            editText = itemView.findViewById(R.id.editText);
            checkBox = itemView.findViewById(R.id.checkBox);
            deleteBtn = itemView.findViewById(R.id.btn_delete);
        }

        public void bind(int position) {
            ListNoteItems item = items.get(position);
            editText.setText(item.getDescription());
            checkBox.setChecked(item.isChecked());

            // Listen for changes in the CheckBox state
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                item.setChecked(isChecked);
            });

            // Listen for changes in the EditText
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                    item.setDescription(s.toString());
                }
            });
        }
    }
}
