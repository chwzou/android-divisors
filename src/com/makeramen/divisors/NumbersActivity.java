package com.makeramen.divisors;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class NumbersActivity extends ListActivity implements ListView.OnItemClickListener {
	
	public static final String LABEL_DIVISORS = " divisors";
	public static final String LABEL_PRIME = "prime number";
	public static final String LABEL_SPECIAL = "special case";
	public static final String KEY_NUMBER = "number";
	public static final String KEY_DESCRIPTION = "description";
	public static final String EXTRA_NUMBER = "number";
	public static final String EXTRA_DIVISORS = "divisors";
	public static final int INTENT_CODE = 0;
	
	ListView mListView;
	SharedPreferences mPreferences;
	SimpleAdapter mAdapter;
	ListNumberBinder mBinder;
	ArrayList<HashMap<String, String>> mArrayList = new ArrayList<HashMap<String, String>>();
	DivisorTask mThread;
	ProgressDialog progressDialog;
	Intent mIntent;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// request progress bar access
//		requestWindowFeature(Window.FEATURE_PROGRESS);
//		requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);		
		setContentView(R.layout.main);
		
		mListView = (ListView) findViewById(android.R.id.list);
		mBinder = new ListNumberBinder();
		
		mAdapter = new SimpleAdapter(
				this,
				mArrayList,
				R.layout.number_list_item,
				new String[] {KEY_NUMBER, KEY_DESCRIPTION},
				new int[] {android.R.id.text1, android.R.id.text2});
		
		mAdapter.setViewBinder(mBinder);
		
		mListView.setOnItemClickListener(this);
		

		progressDialog = new ProgressDialog(this);
		progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		progressDialog.setMessage("Loading...");
		progressDialog.setMax(10000);
		progressDialog.setCancelable(false);
		
		mThread = new DivisorTask();
		mThread.execute();
	}
	
	@Override
	public void onDestroy() {
		mThread.cancel(true);
		super.onDestroy();
	}

	@Override
	public void onItemClick(AdapterView<?> list, View view, int position, long id) {
		
		// get the label of clicked textview
		TextView mTextView = (TextView)view.findViewById(android.R.id.text2);
		String label = mTextView.getText().toString();
		
		if (label.equals(LABEL_PRIME) || label.equals(LABEL_SPECIAL)) {
			// if prime or special, do nothing
			return;
		} else {
			// if not prime or special, show divisors
			mIntent = new Intent(this, DivisorsActivity.class);
			
			// pass the number that was clicked
			mIntent.putExtra(EXTRA_NUMBER, position + 1);
			// pass the number of factors it had
//			mIntent.putExtra(EXTRA_DIVISORS, Integer.parseInt(label.substring(0, label.length() - LABEL_DIVISORS.length())));
			
			startActivityForResult(mIntent, INTENT_CODE);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		mListView.setSelection(resultCode-1);
	}
	
	/** Thread for creating list of numbers and number of divisors **/
	private class DivisorTask extends AsyncTask<Void, Integer, Void> {

		long startTime;
		int fcount;
		HashMap<String, String> mMap;
		
		@Override
		protected void onPreExecute() {			
			// begin progress bar
//			setTitle(R.string.loading_title);
//			setProgressBarVisibility(true);

			progressDialog.show();
			
			// save start time for benchmarking
			startTime = System.currentTimeMillis();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			int time = 0;	//used to track ui thread updates
				
			// BEGIN special case for 1
			mMap = new HashMap<String , String>();
			mMap.put(KEY_NUMBER, "1");
			
			mMap.put(KEY_DESCRIPTION, "special case");
			
			mArrayList.add(mMap);
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
				mArrayList.add(mMap);
				
				
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
//			setProgress(progress[0]);
			progressDialog.setProgress(progress[0]);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			
			// log 
			Log.d("Divisors", "time elapsed: " + (System.currentTimeMillis() - startTime) + " ms");
			
			// update the list
			mListView.setAdapter(mAdapter);
			
			// fill and fade the progressbar
//			setProgress(10000);
//			setTitle(R.string.app_name);
			
			progressDialog.setProgress(10000);
			progressDialog.cancel();
		}
		
		@Override
		protected void onCancelled() {
			Log.d("Divisors", "Thread cancelled");
			progressDialog.cancel();
		}
	}
	
}