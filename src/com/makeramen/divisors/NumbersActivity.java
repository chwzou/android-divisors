package com.makeramen.divisors;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
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
	
	public static final String EXTRA_NUMBER = "number";
	public static final String EXTRA_DIVISORS = "divisors";
	public static final int INTENT_DIVISORS = 0;
	
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

		// pass the NumberAdapter our LayoutInflater
		mAdapter = new NumberAdapter((LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE)); 
		
		// bind onclicklistener to listview
		mListView.setOnItemClickListener(this);
		
		// bind adapter to listview
		mListView.setAdapter(mAdapter);
	}

	public boolean onKey(View view, int keyCode, KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch(keyCode) {
			case KeyEvent.KEYCODE_ENTER:
				EditText input = (EditText) view;
				
				try {
					int i = Integer.parseInt(input.getText().toString());
					if ( 0 < i && i <=10000) {
						// if number is within range, jump to number
						mListView.setSelection(i-1);
					} else {
						// if number is out of range, highlight red
						input.setHighlightColor(Color.RED);
						input.selectAll();
					}
				} catch (Exception e) {
					// if text cant' be parsed, highlight red
					input.setHighlightColor(Color.RED);
					input.selectAll();
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
		
		// if not prime or special, show divisors, kind of ghetto way to do it
		if (label.substring(label.length() - NumberAdapter.LABEL_DIVISORS.length()).equals(NumberAdapter.LABEL_DIVISORS)) {
			
			// start activity, passing selected number
			mIntent = new Intent(this, DivisorsActivity.class);
			mIntent.putExtra(EXTRA_NUMBER, position + 1);
			startActivityForResult(mIntent, INTENT_DIVISORS);
		}
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// move listview to returned
		if (resultCode != RESULT_CANCELED) {
			mListView.setSelection(resultCode-1);
		}
	}	
}