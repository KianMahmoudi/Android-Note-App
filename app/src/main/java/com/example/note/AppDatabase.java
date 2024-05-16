package com.example.note;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {SimpleNote.class,ListNote.class,RecordNote.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract SimpleNoteDao getSimpleNoteDao();

    public abstract ListNoteDao getListNoteDao();
    public abstract RecordNoteDao getRecordNoteDao();
}