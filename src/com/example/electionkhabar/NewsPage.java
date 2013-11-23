package com.example.electionkhabar;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;

public class NewsPage extends Activity {
	WebView OurBrow;
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle gotBasket = getIntent().getExtras();
		setContentView(R.layout.news_page);
		// TextView displayUrl=(TextView)findViewById(R.id.tvGotUrl);
		url = gotBasket.getString("key");
		// displayUrl.setText(url);

		OurBrow = (WebView) findViewById(R.id.wvBrow);
		OurBrow.getSettings().setJavaScriptEnabled(true);
		OurBrow.getSettings().setLoadWithOverviewMode(true);
		OurBrow.getSettings().setUseWideViewPort(true);

		Button sharebutton = (Button) findViewById(R.id.share_button);

		sharebutton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent share = new Intent(Intent.ACTION_SEND);
				share.setType("text"); // might be text, sound, whatever
				share.putExtra(Intent.EXTRA_TEXT, url);
				share.setType("text/plain");
				startActivity(Intent.createChooser(share, "share"));
			}
		});
		OurBrow.setWebViewClient(new ourViewClient());
		try {
			OurBrow.loadUrl(url);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
