package com.example.note;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.airbnb.lottie.L;
import com.google.android.material.bottomnavigation.BottomNavigationView;

// HomePage.java

import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class HomePage extends AppCompatActivity {

    private SimpleNoteAdapter simpleNoteAdapter;
    private RecordNoteAdapter recordNoteAdapter;
    private ListNoteAdapter listNoteAdapter;
    private RecyclerView recyclerView;
    private AppDatabase db;
    private TabLayout tabLayout;

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

        tabLayout = findViewById(R.id.tabs_homePage);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    java.util.List<SimpleNote> simpleNotes = db.getSimpleNoteDao().getAll();
                    simpleNoteAdapter = new SimpleNoteAdapter(simpleNotes, HomePage.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HomePage.this));
                    recyclerView.setAdapter(simpleNoteAdapter);
                } else if (tab.getPosition() == 1) {
                    java.util.List<ListNote> listNotes = db.getListNoteDao().getAll();
                    listNoteAdapter = new ListNoteAdapter(listNotes, HomePage.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HomePage.this));
                    recyclerView.setAdapter(listNoteAdapter);
                } else if (tab.getPosition() == 2) {
                    List<RecordNote> recordNotes = db.getRecordNoteDao().getAll();
                    recordNoteAdapter = new RecordNoteAdapter(recordNotes, HomePage.this);
                    recyclerView.setLayoutManager(new LinearLayoutManager(HomePage.this));
                    recyclerView.setAdapter(recordNoteAdapter);
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

        java.util.List<SimpleNote> simpleNotes = db.getSimpleNoteDao().getAll();
        simpleNoteAdapter = new SimpleNoteAdapter(simpleNotes, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(simpleNoteAdapter);

        if (getIntent().hasExtra("class")) {
            if (getIntent().getExtras().getString("class").equals("simpleNote")) {
                java.util.List<SimpleNote> simpleNotesTwo = db.getSimpleNoteDao().getAll();
                simpleNoteAdapter = new SimpleNoteAdapter(simpleNotes, this);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                recyclerView.setAdapter(simpleNoteAdapter);
                tabLayout.selectTab(tabLayout.getTabAt(0));
            } else if (getIntent().getExtras().getString("class").equals("listNote")) {
                java.util.List<ListNote> listNotes = db.getListNoteDao().getAll();
                listNoteAdapter = new ListNoteAdapter(listNotes, HomePage.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(HomePage.this));
                recyclerView.setAdapter(listNoteAdapter);
                tabLayout.selectTab(tabLayout.getTabAt(1));
            } else if (getIntent().getExtras().getString("class").equals("recordNote")) {
                List<RecordNote> recordNotes = db.getRecordNoteDao().getAll();
                recordNoteAdapter = new RecordNoteAdapter(recordNotes, HomePage.this);
                recyclerView.setLayoutManager(new LinearLayoutManager(HomePage.this));
                recyclerView.setAdapter(recordNoteAdapter);
                tabLayout.selectTab(tabLayout.getTabAt(2));
            }
        }
    }


}
