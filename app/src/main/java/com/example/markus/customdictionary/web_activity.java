package com.example.markus.customdictionary;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class web_activity extends ActionBarActivity {
private String url;
    private ProgressDialog prog;
    Activity act;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_web_activity);
        Intent intent = getIntent();
        url = intent.getStringExtra("URL");
        url = "https://translate.google.com";
        Log.d("WEBVIEW", "" + url);
        act = this;
        WebView Wview = (WebView) findViewById(R.id.webView);

prog = ProgressDialog.show(act,"Loading"," please wait",true);
        prog.setCancelable(true);
        WebSettings settings = Wview.getSettings();
        settings.setJavaScriptEnabled(true);

        Wview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
      Wview.setWebViewClient(new WebViewClient() {
          @Override
          public boolean shouldOverrideUrlLoading(WebView view, String url) {
              view.loadUrl(url);
              prog.show();
              return true;
          }

          @Override
          public void onPageFinished(WebView view, final String url) {
              prog.dismiss();
          }
      });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_web_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
