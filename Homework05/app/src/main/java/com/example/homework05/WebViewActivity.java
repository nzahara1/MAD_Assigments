package com.example.homework05;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        Intent intent = getIntent();

        if (intent != null) {
            WebView webView = findViewById(R.id.web_view);
            setTitle(intent.getStringExtra(NewsActivity.NEWS_NAME));
            webView.loadUrl(intent.getStringExtra(NewsActivity.NEWS_URL));
        }
    }
}
