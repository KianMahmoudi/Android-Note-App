package com.example.note;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.tabs.TabLayout;

public class AddNoteDialog extends DialogFragment {

    BottomNavigationView bottomNavigation;

    public AddNoteDialog(BottomNavigationView bottomNavigation) {
        this.bottomNavigation = bottomNavigation;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_new_note_custom_dialog, null);
        builder.setView(view);
        builder.setCancelable(false);

        TextView tvSimpleNote = view.findViewById(R.id.tv_simpleNote);
        tvSimpleNote.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), SimpleNoteActivity.class);
            startActivity(intent);
        });

        TextView tvListNote = view.findViewById(R.id.tv_listNote);
        tvListNote.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), ListNoteActivity.class);
            startActivity(intent);
        });

        TextView tvRecordNote = view.findViewById(R.id.tv_recordNote);
        tvRecordNote.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), RecordNoteActivity.class);
            startActivity(intent);
        });

        ExtendedFloatingActionButton exitFab = view.findViewById(R.id.fab_exit_addNewNoteDialog);
        exitFab.setOnClickListener(v -> {
            dismiss();
        });

        return builder.create();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        bottomNavigation.setSelectedItemId(R.id.item_1);
    }

    @Override
    public void onPause() {
        super.onPause();
        bottomNavigation.setSelectedItemId(R.id.item_1);
    }
}
