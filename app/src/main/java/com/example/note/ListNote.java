package com.example.note;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.note.ListNoteItems;

import java.util.List;

@Entity(tableName = "listNote")
public class ListNote {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String title;
    private List<ListNoteItems> items;

    public ListNote(String title, List<ListNoteItems> items) {
        this.title = title;
        this.items = items;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ListNoteItems> getItems() {
        return items;
    }

    public void setItems(List<ListNoteItems> items) {
        this.items = items;
    }
}
