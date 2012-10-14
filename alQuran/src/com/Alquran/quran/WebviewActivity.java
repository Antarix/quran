package com.Alquran.quran;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
//import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.TextView;

public class WebviewActivity extends Activity {
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        overridePendingTransition(R.anim.slide_left, R.anim.hold_x);
	        setContentView(R.layout.webview_layout);
	        final TextView txtTitle = (TextView)findViewById(R.id.webview_title);
	        Intent webActivityIntent = getIntent();
	        if(webActivityIntent.hasExtra("WebActivityExtra"))
	        {
	        	txtTitle.setText(webActivityIntent.getStringExtra("WebActivityExtra"));
	        }		
	        final WebView webView = (WebView)findViewById(R.id.webView_local);
	        //final WebSettings webSettings = webView.getSettings();
	        webView.loadUrl("file:///android_asset/html/index.htm");
	        webView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
	        //webSettings.setJavaScriptEnabled(true);
	    }

	@Override
	protected void onPause() {
		overridePendingTransition(R.anim.hold_x, R.anim.slide_right);
		super.onPause();
	}
	
	public void close(View v) {
		finish();
	}
	public void gotoSettings(View v)
	{
		overridePendingTransition(R.anim.hold_x, R.anim.slide_right);
		finish();
	}
	 
	 
}
	  
