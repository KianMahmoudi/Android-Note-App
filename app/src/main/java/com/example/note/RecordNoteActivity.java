package com.example.note;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import java.io.IOException;

public class RecordNoteActivity extends AppCompatActivity {

    private ExtendedFloatingActionButton recordingAudio;
    private ImageView playingAudio;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioSavePath = null;
    private int playingStatus = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_note);

        recordingAudio = findViewById(R.id.recordingAudio);
        playingAudio = findViewById(R.id.playingAudio);
        ImageView btnClose = findViewById(R.id.btn_close_recordNote);

        btnClose.setOnClickListener(v -> finish());

        recordingAudio.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d("RecordNote", "ACTION_DOWN event fired");
                    if (checkPermissions() == true) {
                        startRecording();
                    } else {
                        ActivityCompat.requestPermissions(RecordNoteActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, 1);
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    Log.d("RecordNote", "ACTION_UP event fired");
                    stopRecording();
                    return true;
                default:
                    return false;
            }
        });

        playingAudio.setOnClickListener(v -> {
            if (audioSavePath != null && !audioSavePath.isEmpty()) {
                if (mediaPlayer == null) {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setOnCompletionListener(mp -> {
                        playingStatus = 0;
                        playingAudio.setImageResource(R.drawable.ic_play);
                    });
                    try {
                        mediaPlayer.setDataSource(audioSavePath);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }

                if (playingStatus == 0) {
                    mediaPlayer.start();
                    playingAudio.setImageResource(R.drawable.ic_pause);
                    playingStatus = 1;
                } else {
                    mediaPlayer.pause();
                    playingAudio.setImageResource(R.drawable.ic_play);
                    playingStatus = 0;
                }
            }
        });


    }

    private boolean checkPermissions() {
        int first = ActivityCompat.checkSelfPermission(getApplicationContext(), RECORD_AUDIO);
        int second = ActivityCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);

        return first == PackageManager.PERMISSION_GRANTED &&
                second == PackageManager.PERMISSION_GRANTED;
    }

    private void startRecording() {

        stopMediaPlayer();

        audioSavePath = getExternalFilesDir(Environment.DIRECTORY_MUSIC).getAbsolutePath() + "/" + "recordingAudio.mp3";
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
        mediaRecorder.setOutputFile(audioSavePath);
        try {
            mediaRecorder.prepare();
            mediaRecorder.start();
            Toast.makeText(this, "Start Recording", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Log.e("RecordNote", "Error starting recording: " + e.getMessage());
        }
    }

    private void stopMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                Toast.makeText(this, "Stop Recording", Toast.LENGTH_SHORT).show();
            } catch (RuntimeException e) {
                Log.e("RecordNote", "Error stopping recording: " + e.getMessage());
            }
        }
    }


}
