package com.example.koopc.project.WebView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.koopc.project.R;

public class WebFood extends AppCompatActivity {
    WebView wv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_food);

        // 명지대 식단표 웹뷰로 보여줌.
        wv = (WebView)findViewById(R.id.webView);
        wv.loadUrl("http://m.mju.ac.kr/mbs/mjumob/jsp/subview.jsp?id=mjumob_050200000000");
        wv.setWebViewClient(new WebViewClient());
    }
}
