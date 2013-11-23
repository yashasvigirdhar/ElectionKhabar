package com.example.electionkhabar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

public class TopNews extends Activity {

	TextView res, partytitle;
	LinearLayout layout;
	ImageView partyimg;
	List<String> news_links = new ArrayList<String>();
	List<String> hyperlinks = new ArrayList<String>();
	String partyname;
	String title;
	PartyAdapter p_adapter;
	int index;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		overridePendingTransition(R.anim.slideleft, R.anim.slideleft2);
		setContentView(R.layout.activity_top_news);
		// partyname="congress";
		res = (TextView) findViewById(R.id.tvNews);
		layout = (LinearLayout) findViewById(R.id.layoutparty);
		partytitle = (TextView) findViewById(R.id.tvParty);
		partyimg = (ImageView) findViewById(R.id.imgParty);
		// String icon="drawable/"+partyname+"image";
		final ListView lv = (ListView) findViewById(R.id.listNews);
		p_adapter = new PartyAdapter(TopNews.this, news_links);

		lv.setAdapter(p_adapter);

		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String url = hyperlinks.get(position);
				Bundle basket = new Bundle();
				basket.putString("key", url);
				Intent a = new Intent(TopNews.this, NewsPage.class);
				a.putExtras(basket);
				startActivity(a);

			}

		});

		Thread downloadThread = new Thread() {
			@SuppressWarnings("unused")
			public void run() {
				Document doc;
				try {

					doc = Jsoup.connect("http://in.news.yahoo.com/politics/")
							.get();
					final String title = doc.title();
					// final Elements links =
					// doc.select("div[class=lft-wd1]").select("a[href]");
					final Elements links = doc.select(
							"div[class=yom-mod yom-blist]").select("a[href]");
					// final Elements links =
					// doc.select("div[class=left-in-contant]").select("a[href]");

					runOnUiThread(new Runnable() {

						public void run() {
							/*
							 * Toast.makeText(getApplicationContext(),
							 * "entering here", Toast.LENGTH_LONG).show();
							 */
							System.out.println("Entering Here");
							for (Element link : links) {
								String l1 = String.format("%s", link.text());
								String l2 = String.format("%s",
										link.attr("abs:href"));
								System.out
										.println("l1 :" + l1 + "\nl2 : " + l2);
								/*
								 * Toast.makeText(getApplicationContext(),
								 * "l1 :" + l1 + "\nl2 : " + l2,
								 * Toast.LENGTH_LONG).show();
								 */
								if (l1.length() >= 30) {
									int i;
									// for(i=0;i<10;i++)
									{
										String search = l1.toLowerCase();
										// if(search.contains(filter[index-1][i]))
										{
											news_links.add(l1);
											hyperlinks.add(l2);
										}
									}
								}
								p_adapter.add(news_links);
								lv.setAdapter(p_adapter);
							}

						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};

		downloadThread.start();
	}

	

}
