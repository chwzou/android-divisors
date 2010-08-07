package com.makeramen.divisor;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class NumberActivity extends Activity {
	
	public static final String LABEL_DIVISORS = " divisors";
	public static final String LABEL_PRIME = "prime number";
	public static final String KEY_NUMBER = "number";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_PREFS = "preferences";
	
	TextView text;
	ListView list;
	ProgressDialog progressDialog;
	SharedPreferences prefs;
	SimpleAdapter adapter;
	ArrayList<HashMap<String, String>> divisors = new ArrayList<HashMap<String, String>>();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		text = (TextView) findViewById(R.id.text);
		list = (ListView) findViewById(R.id.list);
		
		adapter = new SimpleAdapter(
				this,
				divisors,
				R.layout.number_list_item,
				new String[] {KEY_NUMBER, KEY_DESCRIPTION},
				new int[] {android.R.id.text1, android.R.id.text2});
		
		new DivisorTask().execute();
		
	}
	
	
	//Thread for creating list of numbers and number of divisors
	private class DivisorTask extends AsyncTask<Void, Integer, Void> {

		long startTime;
		int count = 0;
		int fcount;
		HashMap<String, String> mMap;
		
		@Override
		protected void onPreExecute() {
			
			progressDialog = new ProgressDialog(NumberActivity.this);
			progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			progressDialog.setMessage("Loading...");
			progressDialog.setCancelable(false);
			progressDialog.setMax(10000);
			progressDialog.show();
			
			startTime = System.currentTimeMillis();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			int time = 0;
			
			mMap = new HashMap<String , String>();
			mMap.put(KEY_NUMBER, "1");
			
			mMap.put(KEY_DESCRIPTION, "special case");
			
			divisors.add(mMap);
			
			for (int n = 2; n <= 10000; n++) {
				
				fcount = 0;
				
				double sq = Math.sqrt(n);
				
				for (int f = 1; f <= sq; f++) {
					
					if (n % f == 0) {
						fcount += ((f == sq) ? 1 : 2);
					}
					
				}

				mMap = new HashMap<String , String>();
				mMap.put(KEY_NUMBER, Integer.toString(n));
				
				mMap.put(KEY_DESCRIPTION, (fcount == 2) ? LABEL_PRIME :(Integer.toString(fcount) + LABEL_DIVISORS));
				
				divisors.add(mMap);
				
				
				if (n % 379 == 0 && System.currentTimeMillis() - startTime > time){
					publishProgress(n);
					time += 500;
				}

			}
			
			publishProgress(10000);
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			progressDialog.setProgress(progress[0]);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			text.append("\ntime elapsed(ms): " + (System.currentTimeMillis() - startTime));
			text.append("\ntotal number of factors: " + count);
			list.setAdapter(adapter);
			progressDialog.dismiss();
		}
	}
	
}