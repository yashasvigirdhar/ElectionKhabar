package com.example.electionkhabar;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class VoteActivity extends Activity {
	
	int myvote=0;
	String user="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vote);
		myvote=0;
		tryUser();
	}
	public void getQuestion(String user){
		DownloadFile downloadFile = new DownloadFile(1);
        downloadFile.execute("http://election-khabar-2.appspot.com/getquestion?user="+user);
	}
	public void tryUser() {
		SharedPreferences sharedPref = getSharedPreferences("com.example.electionkhabar.userfile",MODE_PRIVATE);
		String saveduser= sharedPref.getString("user", "");
		if(saveduser==""){
			getNewUser();
		}
		else {
			user=saveduser;
			Toast.makeText(VoteActivity.this, "Using old user "+user,
	                Toast.LENGTH_LONG).show();
			getQuestion(user);
		}
	}
	public void setUser(String userr){
		SharedPreferences sharedPref = getSharedPreferences("com.example.electionkhabar.userfile",MODE_PRIVATE);
		SharedPreferences.Editor prefEditor = sharedPref.edit();
		prefEditor.putString("user",userr);
		prefEditor.commit();
		user=userr;
		Toast.makeText(VoteActivity.this, "Created new user "+user,
                Toast.LENGTH_LONG).show();
		getQuestion(user);
	}
	public void getNewUser(){
		DownloadFile downloadFile = new DownloadFile(3);
        downloadFile.execute("http://election-khabar-2.appspot.com/getnewuser");
	}
	public void sayYes(View view) {
		myvote=1;
	}

	public void sayNo(View view) {
		myvote=2;
	}

	public void sayCantSay(View view) {
		myvote=3;
	}
	public void sendVote(View view){
		if(myvote!=0){
			//send vote
			DownloadFile downloadFile = new DownloadFile(2);
	        downloadFile.execute("http://election-khabar-2.appspot.com/vote?vote="+Integer.toString(myvote)+"&user="+user);
		}else {
			// show error and do nothing
			Toast.makeText(VoteActivity.this, "Please select an option",
                    Toast.LENGTH_LONG).show();
		}
	}
	
/*	public void printstr(){
		Toast.makeText(VoteActivity.this, str,
                Toast.LENGTH_LONG).show();
	}*/
	public void setText(String str){

			String [] votes=str.split("#");
			TextView tv1 = (TextView) findViewById(R.id.dtextView2);
			TextView tv2 = (TextView) findViewById(R.id.dtextView3);
			TextView tv3 = (TextView) findViewById(R.id.dtextView4);
			tv1.setText("Yes: "+votes[0]+"%");
			tv2.setText("No: "+votes[1]+"%");
			tv3.setText("Can't Say: "+votes[2]+"%");
			LinearLayout ll1=(LinearLayout)findViewById(R.id.vote_layout_1);
			LinearLayout ll2=(LinearLayout)findViewById(R.id.vote_layout_2);
			ll1.setVisibility(2); //GONE=2
			((ViewGroup)findViewById (R.id.voteBigDaddyLayout)).removeView(ll1);
			ll2.setVisibility(0); //VISIBLE=0
			((ViewGroup)findViewById (R.id.voteBigDaddyLayout)).invalidate();
		
	}
	// usually, subclasses of AsyncTask are declared inside the activity class.
    // that way, you can easily modify the UI thread from here
	private class DownloadFile extends AsyncTask<String, Integer, String> {
            String s,str;
            Boolean flag = false;
            int mode; //1 means download question, 2 means send vote and download votes, 3 means get username
            public DownloadFile(int md){
            	mode=md;
            	str = "";
            }
            @Override
            protected String doInBackground(String... sUrl) {
                    try {
                            URL url = new URL(sUrl[0]);
                            BufferedReader in = new BufferedReader(new InputStreamReader(
                                            url.openStream()));

                            while ((s = in.readLine()) != null) {
                                    str = str + s;
                                    System.out.println(str);
                                    Log.d("str", str);
                                    // str is one line of text; readLine() strips the newline
                                    // character(s)

                            }
                            in.close();
                    } catch (Exception e) {
                            flag = true;

                            e.printStackTrace();
                    }
                    return str;
            }

            protected void onPostExecute(String result) {
                    if (flag == false) {
                            Log.d("in post execute", str);
                            /*createlayout();
                            parsestr();*/
                           if(mode==2) setText(str);
                           else if (mode==1){
                    			TextView tv = (TextView) findViewById(R.id.votequestion);
                    			tv.setText(str);
                           }else if(mode==3){
                        	   setUser(str);
                           }
                    } else {
                            Toast.makeText(VoteActivity.this, "internet problem",
                                            Toast.LENGTH_LONG).show();
                    }
            }
    }

}
