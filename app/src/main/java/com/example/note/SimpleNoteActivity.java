package com.example.note;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class SimpleNoteActivity extends Activity {
    private AppDatabase db;

    TextInputEditText noteText;
    TextInputEditText noteTitle;
    ExtendedFloatingActionButton saveBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.simple_note);

        noteTitle = findViewById(R.id.note_title);
        noteText = findViewById(R.id.note_text);
        saveBtn = findViewById(R.id.fab_save_simpleNote);

        ImageView btnClose = findViewById(R.id.btn_close_simpleNote);
        btnClose.setOnClickListener(v -> navigateToHomePage());

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "notes").allowMainThreadQueries().build();

        if (getIntent().hasExtra("id")) {
            long id = getIntent().getLongExtra("id", 0);
            SimpleNote simpleNote = db.getSimpleNoteDao().getById(id);
            noteTitle.setText(simpleNote.getTitle());
            noteText.setText(simpleNote.getNote());
        }

        saveBtn.setOnClickListener(v -> {

            if (getIntent().hasExtra("id")) {
                long id = getIntent().getLongExtra("id", 0);
                SimpleNote simpleNote = db.getSimpleNoteDao().getById(id);
                simpleNote.setTitle(noteTitle.getText().toString());
                simpleNote.setNote(noteText.getText().toString());
                db.getSimpleNoteDao().update(simpleNote);
            } else {
                SimpleNote simpleNote = new SimpleNote(noteTitle.getText().toString(), noteText.getText().toString());
                db.getSimpleNoteDao().insert(simpleNote);
            }
            navigateToHomePage();
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, HomePage.class);
        i.putExtra("class", "simpleNote");
        startActivity(i);
        finish();
    }

    private void navigateToHomePage() {
        Intent i = new Intent(this, HomePage.class);
        i.putExtra("class", "simpleNote");
        startActivity(i);
        finish();
    }

}
