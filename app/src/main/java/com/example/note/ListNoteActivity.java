package com.example.note;import android.content.res.ColorStateList;import android.graphics.Color;import android.os.Bundle;import android.util.TypedValue;import android.view.View;import android.widget.CheckBox;import android.widget.EditText;import android.widget.ImageView;import android.widget.LinearLayout;import androidx.annotation.Nullable;import androidx.appcompat.app.AppCompatActivity;import androidx.core.content.ContextCompat;import androidx.core.widget.CompoundButtonCompat;import androidx.room.Room;import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;import com.google.android.material.floatingactionbutton.FloatingActionButton;import com.google.android.material.tabs.TabLayout;import com.google.android.material.textfield.TextInputEditText;import com.google.android.material.textfield.TextInputLayout;import java.util.ArrayList;import java.util.List;public class ListNoteActivity extends AppCompatActivity {    private LinearLayout container;    private FloatingActionButton fabAdd;    private ExtendedFloatingActionButton saveBtn;    private AppDatabase db;    private TextInputEditText titleEt;    private List<String> checkBoxTexts;    @Override    protected void onCreate(@Nullable Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.list_note);        ImageView btnClose = findViewById(R.id.btn_close_simpleNote);        btnClose.setOnClickListener(v -> finish());        container = findViewById(R.id.conteiner);        fabAdd = findViewById(R.id.fab_addCheckbox);        saveBtn = findViewById(R.id.fab_save_listNote);        titleEt = findViewById(R.id.note_title);        checkBoxTexts = new ArrayList<>();        LinearLayout layout = new LinearLayout(this);        layout.setOrientation(LinearLayout.HORIZONTAL);        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(                LinearLayout.LayoutParams.MATCH_PARENT,                LinearLayout.LayoutParams.WRAP_CONTENT        );        layout.setLayoutParams(layoutParams);        LinearLayout.LayoutParams layoutParamsCheckBox = new LinearLayout.LayoutParams(                LinearLayout.LayoutParams.WRAP_CONTENT,                LinearLayout.LayoutParams.WRAP_CONTENT        );        layoutParamsCheckBox.leftMargin = 42;        CheckBox checkBox = new CheckBox(this);        checkBox.setLayoutParams(layoutParamsCheckBox);        ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.black);        CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);        TextInputLayout textInputLayout = new TextInputLayout(this);        textInputLayout.setHintEnabled(false);        LinearLayout.LayoutParams layoutParamsTextInput = new LinearLayout.LayoutParams(                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT        );        textInputLayout.setLayoutParams(layoutParamsTextInput);        TextInputEditText editText = new TextInputEditText(this);        LinearLayout.LayoutParams layoutParamsEditText = new LinearLayout.LayoutParams(                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT        );        editText.setLayoutParams(layoutParamsEditText);        editText.setBackgroundColor(0000000000);        editText.setHint("New thing in a list");        editText.setTextColor(Color.BLACK);        editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);        textInputLayout.addView(editText);        layout.addView(checkBox);        layout.addView(textInputLayout);        container.addView(layout);        checkBoxTexts.add(editText.getText().toString());        ColorStateList csl = ContextCompat.getColorStateList(this, R.color.black);        CompoundButtonCompat.setButtonTintList(checkBox, csl);        fabAdd.setOnClickListener(v -> addCheckBox());        db = Room.databaseBuilder(getApplicationContext(),                AppDatabase.class, "notes").allowMainThreadQueries().build();        saveBtn.setOnClickListener(v -> {            ListNote note = new ListNote(titleEt.getText().toString());            db.getListNoteDao().insert(note);            finish();        });    }    public void addCheckBox() {        LinearLayout layout = new LinearLayout(this);        layout.setOrientation(LinearLayout.HORIZONTAL);        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(                LinearLayout.LayoutParams.MATCH_PARENT,                LinearLayout.LayoutParams.WRAP_CONTENT        );        layout.setLayoutParams(layoutParams);        LinearLayout.LayoutParams layoutParamsCheckBox = new LinearLayout.LayoutParams(                LinearLayout.LayoutParams.WRAP_CONTENT,                LinearLayout.LayoutParams.WRAP_CONTENT        );        layoutParamsCheckBox.leftMargin = 42;        CheckBox checkBox = new CheckBox(this);        checkBox.setLayoutParams(layoutParamsCheckBox);        ColorStateList colorStateList = ContextCompat.getColorStateList(this, R.color.black);        CompoundButtonCompat.setButtonTintList(checkBox, colorStateList);        TextInputLayout textInputLayout = new TextInputLayout(this);        textInputLayout.setHintEnabled(false);        LinearLayout.LayoutParams layoutParamsTextInput = new LinearLayout.LayoutParams(                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT        );        textInputLayout.setLayoutParams(layoutParamsTextInput);        TextInputEditText editText = new TextInputEditText(this);        LinearLayout.LayoutParams layoutParamsEditText = new LinearLayout.LayoutParams(                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT        );        editText.setLayoutParams(layoutParamsEditText);        editText.setBackgroundColor(0000000000);        editText.setHint("New thing in a list");        editText.setTextColor(Color.BLACK);        editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16);        textInputLayout.addView(editText);        checkBoxTexts.add(editText.getText().toString());        layout.addView(checkBox);        layout.addView(textInputLayout);        container.addView(layout);    }}