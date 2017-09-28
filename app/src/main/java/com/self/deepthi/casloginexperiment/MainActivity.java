package com.self.deepthi.casloginexperiment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";
    public static WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mWebView = (WebView) findViewById(R.id.caswebview);

        mWebView.setInitialScale(1);

        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        mWebView.loadUrl("https://login.umd.edu/");
        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url){
                String cookies = CookieManager.getInstance().getCookie(url);
                if (url.contains("demo") && cookies.contains("shib_idp_session")) {
                    destroyWebView();
                    Intent myIntent = new Intent(MainActivity.this,
                            Main2Activity.class);
                    startActivity(myIntent);
                }
            }
        });
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void destroyWebView() {

        // Make sure you remove the WebView from its parent view before doing anything.
        mWebView.removeAllViews();

        mWebView.clearHistory();

        // NOTE: clears RAM cache, if you pass true, it will also clear the disk cache.
        // Probably not a great idea to pass true if you have other WebViews still alive.
        mWebView.clearCache(true);

        // Loading a blank page is optional, but will ensure that the WebView isn't doing anything when you destroy it.
        mWebView.loadUrl("about:blank");

        mWebView.onPause();
        mWebView.removeAllViews();
        mWebView.destroyDrawingCache();

        // NOTE: This pauses JavaScript execution for ALL WebViews,
        // do not use if you have other WebViews still alive.
        // If you create another WebView after calling this,
        // make sure to call mWebView.resumeTimers().
        mWebView.pauseTimers();

        // NOTE: This can occasionally cause a segfault below API 17 (4.2)
        mWebView.destroy();

        // Null out the reference so that you don't end up re-using it.
        mWebView = null;
    }
}
