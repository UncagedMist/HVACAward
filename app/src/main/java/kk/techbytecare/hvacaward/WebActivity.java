package kk.techbytecare.hvacaward;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import kk.techbytecare.hvacaward.Common.Common;
import kk.techbytecare.hvacaward.Model.Project;

public class WebActivity extends AppCompatActivity {

    WebView webView;

    String projectId = "";
    String url;

    Project currentProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        webView = findViewById(R.id.webView);

        if (getIntent() != null)    {
            projectId = getIntent().getStringExtra("ProjectId");
        }

        if (!projectId.isEmpty())  {
            url = Common.currentProject.getDescription().toString();
        }

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);
        webView.setWebViewClient(new WebViewClient());
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack())    {
            webView.goBack();
        }
        else {
            super.onBackPressed();
        }
    }
}
