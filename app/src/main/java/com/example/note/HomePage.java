package com.example.note;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import java.util.List;

public class HomePage extends AppCompatActivity {

    private SimpleNoteAdapter simpleNoteAdapter;
    private RecordNoteAdapter recordNoteAdapter;
    private ListNoteAdapter listNoteAdapter;
    private RecyclerView recyclerView;
    private AppDatabase db;
    private TabLayout tabLayout;
    private EditText searchEt;

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

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "notes").allowMainThreadQueries().build();

        recyclerView = findViewById(R.id.recyclerView);
        tabLayout = findViewById(R.id.tabs_homePage);
        searchEt = findViewById(R.id.searchEt);

        setupTabSelection();
        setupSearchFilter();
    }

    private void setupTabSelection() {
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                loadNotesForTab(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void setupSearchFilter() {
        searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                loadNotesForTab(tabLayout.getSelectedTabPosition());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void loadNotesForTab(int tabIndex) {
        String query = searchEt.getText().toString();
        if (tabIndex == 0) {
            List<SimpleNote> simpleNotes = query.isEmpty() ? db.getSimpleNoteDao().getAll() : db.getSimpleNoteDao().searchNote(query);
            simpleNoteAdapter = new SimpleNoteAdapter(simpleNotes, HomePage.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(HomePage.this));
            recyclerView.setAdapter(simpleNoteAdapter);
        } else if (tabIndex == 1) {
            List<ListNote> listNotes = query.isEmpty() ? db.getListNoteDao().getAll() : db.getListNoteDao().searchNote(query);
            listNoteAdapter = new ListNoteAdapter(listNotes, HomePage.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(HomePage.this));
            recyclerView.setAdapter(listNoteAdapter);
        } else if (tabIndex == 2) {
            List<RecordNote> recordNotes = query.isEmpty() ? db.getRecordNoteDao().getAll() : db.getRecordNoteDao().searchNote(query);
            recordNoteAdapter = new RecordNoteAdapter(recordNotes, HomePage.this);
            recyclerView.setLayoutManager(new LinearLayoutManager(HomePage.this));
            recyclerView.setAdapter(recordNoteAdapter);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadNotesForTab(tabLayout.getSelectedTabPosition());

        if (getIntent().hasExtra("class")) {
            String intentClass = getIntent().getStringExtra("class");
            if (intentClass != null) {
                switch (intentClass) {
                    case "simpleNote":
                        tabLayout.selectTab(tabLayout.getTabAt(0));
                        break;
                    case "listNote":
                        tabLayout.selectTab(tabLayout.getTabAt(1));
                        break;
                    case "recordNote":
                        tabLayout.selectTab(tabLayout.getTabAt(2));
                        break;
                }
            }
        }
    }
}
