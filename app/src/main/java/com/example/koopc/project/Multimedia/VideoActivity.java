package com.example.koopc.project.Multimedia;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.example.koopc.project.R;

// 튜토리얼 비디오
public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        VideoView video = (VideoView)findViewById(R.id.video);
        MediaController mc = new MediaController(this);
        mc.setAnchorView(video);
        // 내부 비디오 URL을 사용해서 비디오 재생
        Uri uri = Uri.parse("android.resource://"+getPackageName()+"/"+R.raw.video);
        video.setMediaController(mc);
        video.setVideoURI(uri);
        video.requestFocus();
        video.start();

        // 비디오 시작시 BGM 멈춤
        Intent intent = new Intent(this, BGM.class);
        stopService(intent);

        video.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                finish();
            }
        });
    }

    //  destroy할 경우 멈춘 BGM을 다시 시작
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