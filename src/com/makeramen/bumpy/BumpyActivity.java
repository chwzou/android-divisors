package com.makeramen.bumpy;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

public class BumpyActivity extends Activity {
	
	TextView text;
	
	int[] factors = new int[10000];
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		
		text = (TextView) findViewById(R.id.text);
		
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
			for (int n = 1; n <= 10000; n++) {
				
				factors[n-1] = 0;
				
				double sq = Math.sqrt(n);
				
				for (int f = 1; f <= sq; f++) {
					
					if (n % f == 0) {
						factors[n-1] += ((f == sq) ? 1 : 2); 
					}
					
				}
				
				count += factors[n-1];
				
			}
			
			return null;
		}
		
//		@Override
//		protected void onProgressUpdate(Integer... progress) {
//			Log.d("bumpy", "i: " + progress[0] + " j: " + progress[1]);
//		}
		
		@Override
		protected void onPostExecute(Void result) {
			text.append("\ntime elapsed(ms): " + (System.currentTimeMillis() - startTime));
			text.append("\ntotal number of factors: " + count);
			
		}
	}
	
}