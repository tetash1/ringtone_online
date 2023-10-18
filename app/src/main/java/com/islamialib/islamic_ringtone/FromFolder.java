package com.islamialib.islamic_ringtone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;

public class FromFolder extends AppCompatActivity {
    private static final int REQUEST_PERMISSIONS = 123;
    private static final int REQUEST_RINGTONE_PICKER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_from_folder);




            Button selectRingtoneButton = findViewById(R.id.selectRingtoneButton);
            selectRingtoneButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                        openRingtonePicker();

                }
            });
        }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openRingtonePicker();
            } else {
                Toast.makeText(this, "Permission denied. You can't set a ringtone without permission.", Toast.LENGTH_SHORT).show();
            }
        }

    private void openRingtonePicker() {
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_RINGTONE);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Ringtone");
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, (Uri) null);

        startActivityForResult(intent, REQUEST_RINGTONE_PICKER);
    }


        private void setRingtone(Uri ringtoneUri) {
            // Specify the path to the selected ringtone.
            String pathToRingtone = ringtoneUri.toString();

            ContentResolver contentResolver = getContentResolver();
            Uri uri = Uri.parse(pathToRingtone);

            try {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Audio.Media.IS_RINGTONE, true);
                contentResolver.update(uri, values, null, null);
                RingtoneManager.setActualDefaultRingtoneUri(this, RingtoneManager.TYPE_RINGTONE, uri);
                Toast.makeText(this, "Ringtone set successfully!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Error setting the ringtone: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }
