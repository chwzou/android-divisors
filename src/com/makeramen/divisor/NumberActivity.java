package com.makeramen.divisor;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.ListView;
import android.widget.SimpleAdapter;

public class NumberActivity extends Activity {
	
	public static final String LABEL_DIVISORS = " divisors";
	public static final String LABEL_PRIME = "prime number";
	public static final String LABEL_SPECIAL = "special case";
	public static final String KEY_NUMBER = "number";
	public static final String KEY_DESCRIPTION = "description";
	public static final String KEY_PREFS = "preferences";
	
	ListView list;
	ProgressDialog progressDialog;
	SharedPreferences prefs;
	SimpleAdapter adapter;
	ListNumberBinder mBinder;
	ArrayList<HashMap<String, String>> divisors = new ArrayList<HashMap<String, String>>();
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_PROGRESS); // request progress bar access		
		setContentView(R.layout.main);
		
		list = (ListView) findViewById(R.id.list);
		mBinder = new ListNumberBinder();
		
		adapter = new SimpleAdapter(
				this,
				divisors,
				R.layout.number_list_item,
				new String[] {KEY_NUMBER, KEY_DESCRIPTION},
				new int[] {android.R.id.text1, android.R.id.text2});
		
		adapter.setViewBinder(mBinder);
		
		list.setAdapter(adapter);
		
		new DivisorTask().execute();
	}
	
	
	//Thread for creating list of numbers and number of divisors
	private class DivisorTask extends AsyncTask<Void, Integer, Void> {

		long startTime;
		int fcount;
		HashMap<String, String> mMap;
		
		@Override
		protected void onPreExecute() {			
			setTitle(R.string.loading_title);
			setProgressBarVisibility(true);
			startTime = System.currentTimeMillis();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			int time = 0;	//used to track ui thread updates
			
			
			// BEGIN special case for 1
			mMap = new HashMap<String , String>();
			mMap.put(KEY_NUMBER, "1");
			
			mMap.put(KEY_DESCRIPTION, "special case");
			
			divisors.add(mMap);
			// END special case
			
			
			for (int n = 2; n <= 10000; n++) {
				
				//reset factor count for each number
				fcount = 0;
				
				double sq = Math.sqrt(n);
				
				for (int f = 1; f <= sq; f++) {
					//loop up to the square root
					
					if (n % f == 0) {
						// double count number of factors, unless f is the square root 
						fcount += ((f == sq) ? 1 : 2);
					}
				}

				// create new hashmap to hold number and label
				mMap = new HashMap<String , String>();
				mMap.put(KEY_NUMBER, Integer.toString(n));
				mMap.put(KEY_DESCRIPTION, (fcount == 2) ? LABEL_PRIME :(Integer.toString(fcount) + LABEL_DIVISORS));
				
				// add hashmap to arraylist
				divisors.add(mMap);
				
				
				if (n % 379 == 0 && System.currentTimeMillis() - startTime > time){
					//check for time elapsed every 379 loops so it looks random :P
					//this way we're not calling currentTimeMillis every loop
					
					publishProgress(n);
					
					time += 300;	// dont update ui thread for at least another 300ms
				}

			}
			
			return null;
		}
		
		@Override
		protected void onProgressUpdate(Integer... progress) {
			// UI update callback
			setProgress(progress[0]);
			if (progress[0] % 2 == 0) {
				// update the listview when progress is even
				// cuts down on UI updates and keeps things reasonable fast
				adapter.notifyDataSetChanged();
			}
		}
		
		@Override
		protected void onPostExecute(Void result) {
			Log.d("Divisors", "time elapsed: " + (System.currentTimeMillis() - startTime) + " ms");
			
			adapter.notifyDataSetChanged();
			// fill the progressbar
			setProgress(10000);
			setTitle(R.string.app_name);
		}
	}
	
}