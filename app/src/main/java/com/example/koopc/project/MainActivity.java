package com.example.koopc.project;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void main_pressStart(View view) {
        Intent intent = new Intent(this, MapActivity.class);

        PackageManager packageManager = getPackageManager();
        List<ResolveInfo> activites = packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        boolean isintentSafe = activites.size() > 0;

        if(isintentSafe) startActivity(intent);
    }
}
