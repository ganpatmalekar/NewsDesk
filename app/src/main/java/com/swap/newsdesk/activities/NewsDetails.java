package com.swap.newsdesk.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.swap.newsdesk.R;

public class NewsDetails extends AppCompatActivity {

    private Toolbar toolbar;
    private FloatingActionButton fabShareNews;
    private WebView webView;
    private ProgressBar progressBar;

    String news_title, news_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        toolbar = findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webView = findViewById(R.id.webView);
        progressBar = findViewById(R.id.progressBar);

        showNewsDetail();

        fabShareNews = findViewById(R.id.share_news);
        fabShareNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareNews();
            }
        });
    }

    private void showNewsDetail() {
        news_url = getIntent().getExtras().getString("NEWS_URL");
        news_title = getIntent().getExtras().getString("NEWS_TITLE");

        toolbar.setTitle(getResources().getString(R.string.app_name));

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        webView.setWebViewClient(new WebViewClient(){

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest url) {
                view.loadUrl(url.getUrl().toString());
                progressBar.setVisibility(View.GONE);
                return true;
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
            }

            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                alertDialogBuilder.setTitle("Error");
                alertDialogBuilder.setMessage(error.getDescription().toString() + "\nPlease check internet connection and try agian...");
                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.setCancelable(true);
                alertDialog.setCanceledOnTouchOutside(false);

                alertDialog.show();
            }
        });
        webView.loadUrl(news_url);
    }

    private void shareNews() {
        news_title = getIntent().getExtras().getString("NEWS_TITLE");
        news_url = getIntent().getExtras().getString("NEWS_URL");

        Intent sendIntent = new Intent(Intent.ACTION_SEND);

        // (Optional) Here we're setting the title of the content
        sendIntent.putExtra(Intent.EXTRA_TITLE, news_title);
        // (Required) Here we're setting the actual content
        sendIntent.putExtra(Intent.EXTRA_TEXT, news_url);

        // Set MIME Type (e.g. text, image, video, application/pdf)
        sendIntent.setType("text/plain");

        // Show the Sharesheet
        startActivity(Intent.createChooser(sendIntent, null));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return false;
    }
}
