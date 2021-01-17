package com.example.scaheadule.ImportEvents;

import android.webkit.WebView;
import android.webkit.WebViewClient;

public class Browser extends WebViewClient {

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        view.loadUrl("javascript:INTERFACE.processContent(document.getElementsByTagName('html')[0].innerText);");
        //view.loadUrl("javascript:INTERFACE.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
    }

}