package com.example.note;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface SimpleNoteDao {
    @Insert
    long insert(SimpleNote simpleNote);
    @Delete
    void delete(SimpleNote simpleNote);

    @Query("SELECT * FROM SimpleNote")
    List<SimpleNote> getAll();

    @Query("SELECT * FROM SimpleNote WHERE note_id = :id")
    SimpleNote getById(long id);

    @Update
    void update(SimpleNote simpleNote);

}

