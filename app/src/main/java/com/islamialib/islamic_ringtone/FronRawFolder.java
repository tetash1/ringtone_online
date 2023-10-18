package com.islamialib.islamic_ringtone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FronRawFolder extends AppCompatActivity {

    private Button btnSetRingtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fron_raw_folder);

        btnSetRingtone = findViewById(R.id.btnSetRingtone);
        btnSetRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyAudioFromRawToExternalStorage();
            }
        });
    }

    private void copyAudioFromRawToExternalStorage() {
        String fileName = "audio.mp3";
        File audioFile = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), fileName);
        String filePath = audioFile.getAbsolutePath();

        // Check if the audio file already exists in the media store.
        Uri queryUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.MediaColumns._ID};
        String selection = MediaStore.MediaColumns.DATA + "=?";
        String[] selectionArgs = {filePath};

        Cursor cursor = getContentResolver().query(queryUri, projection, selection, selectionArgs, null);

        if (cursor != null && cursor.moveToFirst()) {
            // The audio file already exists; delete it before inserting the new one.
            int mediaId = cursor.getInt(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID));
            Uri mediaContentUri = Uri.withAppendedPath(queryUri, String.valueOf(mediaId));
            getContentResolver().delete(mediaContentUri, null, null);
            cursor.close();
        }

        try {
            InputStream inputStream = getResources().openRawResource(R.raw.audio);
            FileOutputStream outputStream = new FileOutputStream(audioFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, bytesRead);
            }
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            Log.e("Ringtone", "Error copying audio file: " + e.getMessage());
            return;
        }

        // Continue with inserting the audio file and setting it as a ringtone.
        // (This part of the code remains unchanged.)
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DATA, filePath);
        values.put(MediaStore.MediaColumns.TITLE, "My tausif");
        values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        values.put(MediaStore.MediaColumns.SIZE, audioFile.length());
        values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
        values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
        values.put(MediaStore.Audio.Media.IS_ALARM, false);
        values.put(MediaStore.Audio.Media.IS_MUSIC, false);

        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.getContentUriForPath(filePath);
        Uri newUri = contentResolver.insert(uri, values);

        RingtoneManager.setActualDefaultRingtoneUri(
                this,
                RingtoneManager.TYPE_RINGTONE,
                newUri
        );

        Toast.makeText(this, "Ringtone set successfully", Toast.LENGTH_SHORT).show();
    }
}
