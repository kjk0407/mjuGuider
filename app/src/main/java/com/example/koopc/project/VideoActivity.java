package com.example.koopc.project;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        VideoView video = (VideoView)findViewById(R.id.video);
        MediaController mc = new MediaController(this);
        mc.setAnchorView(video);
        Uri uri = Uri.parse("http://sites.google.com/site/ubiaccessmobile/sample_video.mp4");
        video.setMediaController(mc);
        video.setVideoURI(uri);
        video.requestFocus();
        video.start();
    }
}