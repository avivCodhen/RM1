package com.strongest.savingdata.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.strongest.savingdata.R;

public class WebViewActivity extends AppCompatActivity {

    public static final String PAGE = "page";

    private String page;
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        page = getIntent().getStringExtra(PAGE);
        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl(page);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Enable Javascript
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        // Force links and redirects to open in the WebView instead of in a browser
        webView.setWebViewClient(new WebViewClient());
        Log.d("aviv", "onCreateView: " + page);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
