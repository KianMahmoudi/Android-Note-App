package com.example.note;

import static android.Manifest.permission.RECORD_AUDIO;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.room.Room;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class RecordNoteActivity extends AppCompatActivity {

    private ExtendedFloatingActionButton recordingAudio;
    private ImageView playingAudio;
    private MediaRecorder mediaRecorder;
    private MediaPlayer mediaPlayer;
    private String audioSavePath = null;
    private int playingStatus = 0;
    private ExtendedFloatingActionButton saveBtn;
    private TextInputEditText title;
    private AppDatabase db;
    private ImageView replay;
    private ImageView forward;
    private ImageView btnClose;
    private TextView duration;
    private LottieAnimationView animation;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_note);

        recordingAudio = findViewById(R.id.recordingAudio);
        playingAudio = findViewById(R.id.playingAudio);
        saveBtn = findViewById(R.id.fab_save_RecordNote);
        title = findViewById(R.id.note_title_recordNote);
        btnClose = findViewById(R.id.btn_close_recordNote);
        replay = findViewById(R.id.replay_10_sec);
        forward = findViewById(R.id.forward_10_sec);
        duration = findViewById(R.id.duration);
        animation = findViewById(R.id.animation);

        db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "notes").allowMainThreadQueries().build();

        btnClose.setOnClickListener(v -> finish());

        if (getIntent().hasExtra("id")) {
            long id = getIntent().getLongExtra("id", 0);
            RecordNote recordNote = db.getRecordNoteDao().getById(id);
            title.setText(recordNote.getTitle());
            audioSavePath = recordNote.getPath();
            duration.setText(recordNote.getDuration());
        }

        recordingAudio.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    Log.d("RecordNote", "ACTION_DOWN event fired");
                    if (checkPermissions()) {
                        startRecording();
                        animation.playAnimation();
                        animation.loop(true);
                    } else {
                        ActivityCompat.requestPermissions(RecordNoteActivity.this, new String[]{RECORD_AUDIO, WRITE_EXTERNAL_STORAGE}, 1);
                    }
                    return true;
                case MotionEvent.ACTION_UP:
                    Log.d("RecordNote", "ACTION_UP event fired");
                    stopRecording();
                    animation.pauseAnimation();
                    animation.loop(false);
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
                        animation.pauseAnimation();
                        animation.loop(false);
                    });
                    mediaPlayer.setOnPreparedListener(mp -> setDuration());
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
                    animation.playAnimation();
                    animation.loop(true);
                } else {
                    mediaPlayer.pause();
                    playingAudio.setImageResource(R.drawable.ic_play);
                    playingStatus = 0;
                    animation.pauseAnimation();
                    animation.loop(false);
                }
            }
        });


        saveBtn.setOnClickListener(v -> {
            if (getIntent().hasExtra("id")) {
                long id = getIntent().getLongExtra("id", 0);
                RecordNote recordNote = db.getRecordNoteDao().getById(id);
                recordNote.setTitle(title.getText().toString());
                recordNote.setPath(audioSavePath);
                recordNote.setDuration(duration.getText().toString());
                db.getRecordNoteDao().update(recordNote);
            } else {
                String titleText = title.getText().toString();
                if (audioSavePath != null && !audioSavePath.isEmpty()) {
                    RecordNote recordNote = new RecordNote(titleText, audioSavePath, duration.getText().toString());
                    db.getRecordNoteDao().insert(recordNote);
                } else {
                    Toast.makeText(this, "No recording found to save", Toast.LENGTH_SHORT).show();
                }
            }
            Intent i = new Intent(this, HomePage.class);
            i.putExtra("class", "recordNote");
            startActivity(i);
        });

        replay.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                int newPosition = Math.max(currentPosition - 10000, 0);
                if (currentPosition > newPosition)
                    mediaPlayer.seekTo(newPosition);
                else
                    stopMediaPlayer();
            }
        });

        forward.setOnClickListener(v -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                int currentPosition = mediaPlayer.getCurrentPosition();
                int duration = mediaPlayer.getDuration();
                int newPosition = Math.min(currentPosition + 10000, duration);
                if (currentPosition < newPosition)
                    mediaPlayer.seekTo(newPosition);
                else
                    stopMediaPlayer();
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
            animation.playAnimation();
            animation.loop(true);
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
            setDuration();
            animation.pauseAnimation();
            animation.loop(false);
        }
    }

    private void stopRecording() {
        if (mediaRecorder != null) {
            try {
                mediaRecorder.stop();
                mediaRecorder.release();
                mediaRecorder = null;
                Toast.makeText(this, "Stop Recording", Toast.LENGTH_SHORT).show();
                setDurationAfterRecording();
                animation.pauseAnimation();
                animation.loop(false);
            } catch (RuntimeException e) {
                Log.e("RecordNote", "Error stopping recording: " + e.getMessage());
            }
        }
    }

    private void setDuration() {
        if (mediaPlayer != null) {
            int durationInMillis = mediaPlayer.getDuration();
            String durationString = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(durationInMillis),
                    TimeUnit.MILLISECONDS.toMinutes(durationInMillis) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(durationInMillis) % TimeUnit.MINUTES.toSeconds(1));
            duration.setText(durationString);
        }
    }

    private void setDurationAfterRecording() {
        MediaPlayer tempMediaPlayer = new MediaPlayer();
        try {
            tempMediaPlayer.setDataSource(audioSavePath);
            tempMediaPlayer.prepare();
            int durationInMillis = tempMediaPlayer.getDuration();
            String durationString = String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(durationInMillis),
                    TimeUnit.MILLISECONDS.toMinutes(durationInMillis) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(durationInMillis) % TimeUnit.MINUTES.toSeconds(1));
            duration.setText(durationString);
        } catch (IOException e) {
            Log.e("RecordNote", "Error setting duration after recording: " + e.getMessage());
        } finally {
            tempMediaPlayer.release();
        }
    }

    @Override
    public void finish() {
        super.finish();
        playingStatus = 0;
        stopMediaPlayer();
    }
}
