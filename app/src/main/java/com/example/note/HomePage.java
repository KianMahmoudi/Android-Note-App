package com.example.note;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

// HomePage.java

import com.google.android.material.tabs.TabLayout;

public class HomePage extends AppCompatActivity {

    private SimpleNoteAdapter simpleNoteAdapter;

    private ListNoteAdapter listNoteAdapter;
    private RecyclerView recyclerView;
    private AppDatabase db;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        BottomNavigationView bottomNavigation = findViewById(R.id.bottom_navigation);
        bottomNavigation.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.item_1) {
                return true;
            } else if (item.getItemId() == R.id.item_2) {
                AddNoteDialog dialog = new AddNoteDialog(bottomNavigation);
                dialog.show(getSupportFragmentManager(), "dialog");
                return true;
            }
            return false;
        });

        db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "notes").allowMainThreadQueries().build();

        recyclerView = findViewById(R.id.recyclerView);

        TabLayout tabLayout = findViewById(R.id.tabs_homePage);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    List<SimpleNote> simpleNotes = db.getSimpleNoteDao().getAll();
                    simpleNoteAdapter = new SimpleNoteAdapter(simpleNotes, HomePage.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HomePage.this));
                    recyclerView.setAdapter(simpleNoteAdapter);
                } else if (tab.getPosition() == 1) {
                    List<ListNote> listNotes = db.getListNoteDao().getAll();
                    listNoteAdapter = new ListNoteAdapter(listNotes, HomePage.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HomePage.this));
                    recyclerView.setAdapter(listNoteAdapter);
                } else if (tab.getPosition() == 2) {
                    simpleNoteAdapter = null;
                    recyclerView.setAdapter(simpleNoteAdapter);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        List<SimpleNote> simpleNotes = db.getSimpleNoteDao().getAll();
        simpleNoteAdapter = new SimpleNoteAdapter(simpleNotes, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(simpleNoteAdapter);


    }



}
