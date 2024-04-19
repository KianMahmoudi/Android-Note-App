package com.example.note;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ListNoteDao {
    @Insert
    void insert(ListNote listNote);

    @Delete
    void delete(ListNote listNote);

    @Query("SELECT * FROM ListNote")
    List<ListNote> getAll();

    @Query("SELECT * FROM ListNote WHERE note_id = :id")
    ListNote getById(long id);

}
