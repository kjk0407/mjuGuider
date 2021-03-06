package com.example.koopc.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.koopc.project.Map.MapActivity;
import com.example.koopc.project.Multimedia.BGM;
import com.example.koopc.project.Multimedia.VideoActivity;
import com.example.koopc.project.WebView.WebFood;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedPreferences opt = getSharedPreferences("Option",MODE_PRIVATE);
        int isMute = opt.getInt("isMute",0);
        if(isMute == 0) {
            Intent intent = new Intent(this, BGM.class);
            startService(intent);
        }
        FirebaseMessaging.getInstance().subscribeToTopic("notice"); // 주제를 구독.
        FirebaseInstanceId.getInstance().getToken(); // FCM서버에 연결.
    }

    // 시작하기 누름.
    public void main_pressStart(View view) {
        Intent intent = new Intent(this, MapActivity.class);

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activites = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean isintentSafe = activites.size() > 0;

        if(isintentSafe) startActivity(intent);
    }
    //튜토리얼 버튼
    public void main_pressTutorial(View view){
        Intent intent = new Intent(this,VideoActivity.class);
        startActivity(intent);
    }
// 명지대 식단 이동
    public void main_webFood(View v) {
        Intent intent = new Intent(this, WebFood.class);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Intent intent = new Intent(this, BGM.class);
        stopService(intent);
    }

}
