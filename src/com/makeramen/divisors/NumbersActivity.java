package com.makeramen.divisors;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class NumbersActivity extends ListActivity implements ListView.OnItemClickListener, OnKeyListener {
	
	public static final String LABEL_DIVISORS = " divisors";
	public static final String LABEL_PRIME = "prime number";
	public static final String LABEL_SPECIAL = "special number";
	public static final String KEY_NUMBER = "number";
	public static final String KEY_DESCRIPTION = "description";
	public static final String EXTRA_NUMBER = "number";
	public static final String EXTRA_DIVISORS = "divisors";
	public static final int INTENT_CODE = 0;
	
	ListView mListView;
	SimpleAdapter mAdapter;
	ListNumberBinder mBinder;
	ArrayList<HashMap<String, String>> mArrayList = new ArrayList<HashMap<String, String>>();
	DivisorTask mThread;
	Intent mIntent;
	ProgressDialog progressDialog;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.main);
		
		mListView = (ListView) findViewById(android.R.id.list);
		
		// hide ime on start
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		// bind onKey to EditText
		findViewById(android.R.id.edit).setOnKeyListener(this);
		
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
		progressDialog.setCancelable(false);
		progressDialog.setMax(10000);

		mThread = new DivisorTask();
		mThread.execute();
	}
	
	@Override
	public void onDestroy() {
		mThread.cancel(true);
		super.onDestroy();
	}
	
	public boolean onKey(View view, int keyCode, KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch(keyCode) {
			case KeyEvent.KEYCODE_ENTER:
				try {
					int i = Integer.parseInt(((EditText) view).getText().toString());
					if ( 0 < i && i <=10000) {
						mListView.setSelection(i-1);
					} else {
						((EditText) view).setText(null);
					}
				} catch (Exception e) {
					((EditText) view).setText(null);
				}
			}
		}

		return false;
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
			setTitle(R.string.loading_title);
			setProgressBarVisibility(true);

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
			
			mMap.put(KEY_DESCRIPTION, LABEL_SPECIAL);
			
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
			setProgress(progress[0]);
//			mAdapter.notifyDataSetChanged();
			progressDialog.setProgress(progress[0]);
		}
		
		@Override
		protected void onPostExecute(Void result) {
			
			// log 
			Log.d("Divisors", "time elapsed: " + (System.currentTimeMillis() - startTime) + " ms");
			
			// update the list
//			mAdapter.notifyDataSetChanged();
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