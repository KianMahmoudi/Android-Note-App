package com.example.note;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface RecordNoteDao {

    @Insert
    long insert(RecordNote recordNote);

    @Delete
    void delete(RecordNote recordNote);

    @Update
    void update(RecordNote recordNote);

    @Query("SELECT * FROM RecordNotes")
    List<RecordNote> getAll();

    @Query("SELECT * FROM RecordNotes WHERE id = :id")
    RecordNote getById(long id);

}
