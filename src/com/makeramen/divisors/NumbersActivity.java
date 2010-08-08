package com.makeramen.divisors;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
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
	NumberAdapter mAdapter;
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

		mAdapter = new NumberAdapter((LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE)); 
		
		mListView.setOnItemClickListener(this);
		
		mListView.setAdapter(mAdapter);
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
}