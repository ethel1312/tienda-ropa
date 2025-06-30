package com.example.tienda_ropa;

import android.os.Bundle;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class WebViewQRActivity extends AppCompatActivity {

    private WebView webViewQR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webViewQR = new WebView(this);
        setContentView(webViewQR);

        String qrData = getIntent().getStringExtra("qrData");
        String qrUrl = "https://quickchart.io/qr?text=" + qrData;

        webViewQR.getSettings().setJavaScriptEnabled(true);
        webViewQR.loadUrl(qrUrl);
    }
}
