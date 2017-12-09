package com.example.koopc.project.Multimedia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.koopc.project.R;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        VideoView video = (VideoView)findViewById(R.id.video);
        MediaController mc = new MediaController(this);
        mc.setAnchorView(video);
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video);
        video.setMediaController(mc);
        video.setVideoURI(uri);
        video.requestFocus();
        video.start();

        Intent intent = new Intent(this, BGM.class);
        stopService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences opt = getSharedPreferences("Option",MODE_PRIVATE);
        int isMute = opt.getInt("isMute",0);
        if(isMute == 0) {
            Intent intent = new Intent(this, BGM.class);
            startService(intent);
        }
    }
}