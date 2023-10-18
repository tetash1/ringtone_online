package com.islamialib.islamic_ringtone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileInputStream;
import java.net.URL;
import android.content.ContentValues;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ListRingTone extends AppCompatActivity {
    Button btnPlay, btnStream;
    EditText etURL;
    Boolean isPlaying = false, isStreaming = false;
    MediaPlayer m;
    String ringtoneLink ="";
    String ringtoneName="";
    private Button btnOpenSettings;
    private Button btnDownload;
    private Button btnSetRingtone,fronraw,fronstroge,openSetting;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_ring_tone);

        ringtoneName = getIntent().getStringExtra("ringtoneName");
        ringtoneLink = getIntent().getStringExtra("ringtoneLink");

        openSetting = findViewById(R.id.setrogepermission);

        openSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the app settings using an Intent
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }
        });
        btnOpenSettings = findViewById(R.id.btnOpenSettings);

        btnOpenSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the app settings using an Intent
                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        });
        fronstroge = findViewById(R.id.fronstroge);

        fronstroge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Open the app settings using an Intent
                Intent intent = new Intent(ListRingTone.this,FromFolder.class);
                startActivity(intent);
            }
        });
        fronraw = findViewById(R.id.fronraw);
        fronraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ListRingTone.this,FronRawFolder.class);
                startActivity(intent);
            }
        });


        etURL = findViewById(R.id.etURL);
        btnStream = findViewById(R.id.btnStream);
        btnStream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopPlaying();
                startAudioStream("https://banglasongs.fusionbd.com/downloads/mp3/bangla/Onukabbo-Tausif/Jekhane.mp3");

            }
        });

        btnDownload = findViewById(R.id.btnDownload);
        btnSetRingtone = findViewById(R.id.btnSetRingtone);

        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace with the URL of the audio you want to download.
/*
                String audioUrl = getIntent().getStringExtra("ringtoneLink");
*/
                String audioUrl = "https://banglasongs.fusionbd.com/downloads/mp3/bangla/Onukabbo-Tausif/Jekhane.mp3";
                new DownloadAudioTask().execute(audioUrl);
            }
        });

        btnSetRingtone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Replace with the path to the downloaded audio file.
                setRingtone("audio.mp3");
            }
        });
    }


    public void startAudioStream(String url) {
        stopPlaying();
        if (m == null)
            m = new MediaPlayer();
        try {
            Log.d("mylog", "Playing: " + url);
            m.setAudioStreamType(AudioManager.STREAM_MUSIC);
            m.setDataSource(url);
            //descriptor.close();
            m.prepare();
            m.setVolume(1f, 1f);
            m.setLooping(false);
            m.start();
        } catch (Exception e) {
            Log.d("mylog", "Error playing in SoundHandler: " + e.toString());
        }
    }

    private void stopPlaying() {
        if (m != null && m.isPlaying()) {
            m.stop();
            m.release();
            m = new MediaPlayer();
            m.reset();
        }
    }

    private class DownloadAudioTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.connect();

                String fileName = "audio.mp3";
                FileOutputStream output = openFileOutput(fileName, MODE_PRIVATE);
                InputStream input = connection.getInputStream();

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = input.read(buffer)) != -1) {
                    output.write(buffer, 0, bytesRead);
                }

                output.close();
                input.close();

                return fileName;
            } catch (IOException e) {
                Toast.makeText(ListRingTone.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                Log.e("DownloadAudioTask", "Error: " + e.getMessage());
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result != null) {
                btnSetRingtone.setEnabled(true);
                Toast.makeText(ListRingTone.this, "Audio downloaded successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(ListRingTone.this, "Download failed"+result, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setRingtone(String fileName) {
        File audioFile = new File(getFilesDir(), fileName);

        try {
            // First, we need to copy the file to a public directory.
            File publicDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES);
            Toast.makeText(this, "pd"+publicDirectory, Toast.LENGTH_SHORT).show();
            if (!publicDirectory.exists()) {
                publicDirectory.mkdirs();
            }

            File targetFile = new File(publicDirectory, "my_rinfgtone.mp3");

            FileInputStream in = new FileInputStream(audioFile);
            FileOutputStream out = new FileOutputStream(targetFile);
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            in.close();
            out.close();

            // Now, get the ContentUri of the copied file.
            Uri audioUri = Uri.fromFile(targetFile);

            ContentValues values = new ContentValues();
            values.put(MediaStore.MediaColumns.DATA, targetFile.getAbsolutePath());
            values.put(MediaStore.MediaColumns.TITLE, "My Ringtone");
            values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
            values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            values.put(MediaStore.Audio.Media.IS_NOTIFICATION, false);
            values.put(MediaStore.Audio.Media.IS_ALARM, false);
            values.put(MediaStore.Audio.Media.IS_MUSIC, false);

            Uri newUri = getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values);
            Log.d("newUri", String.valueOf(newUri));

            RingtoneManager.setActualDefaultRingtoneUri(
                    this,
                    RingtoneManager.TYPE_ALARM,
                    newUri
            );

            Toast.makeText(this, "Ringtone set successfully", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Failed to set ringtone: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}

