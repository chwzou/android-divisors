package com.makeramen.bumpy;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class BumpyActivity extends Activity {
	
	TextView mTextView;
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		mTextView = (TextView) findViewById(R.id.text);
		
		// performance test
		// currently takes almost 3 minutes...
		
		new DivisorTask().execute();
		
	}
	
	private class DivisorTask extends AsyncTask<Void, Integer, Void> {

		long startTime;
		int count = 0;
		
		@Override
		protected void onPreExecute() {
			startTime = System.currentTimeMillis();
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			for (int i = 1; i <= 10000; i++) {
				double s = Math.sqrt(i);
				for (int j = 1; j <= s; j++) {
					if (i%j==0)
						count++;
//					publishProgress(i, j);
				}
			}
			
			return null;
		}
		
//		@Override
//		protected void onProgressUpdate(Integer... progress) {
//			Log.d("bumpy", "i: " + progress[0] + " j: " + progress[1]);
//		}
		
		@Override
		protected void onPostExecute(Void result) {
			Log.d("bumpy", "count: " + count*2);
			Log.d("bumpy", "time elapsed(ms): " + (System.currentTimeMillis() - startTime) );
			
		}
	}
	
}