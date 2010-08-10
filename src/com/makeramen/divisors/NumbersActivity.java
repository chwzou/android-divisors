package com.makeramen.divisors;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ExpandableListView;

public class NumbersActivity extends Activity implements ExpandableListView.OnChildClickListener, OnKeyListener {
	
	public static final String EXTRA_NUMBER = "number";
	public static final String EXTRA_DIVISORS = "divisors";
	public static final int INTENT_DIVISORS = 0;
	
	ExpandableListView mListView;
	ExpandableNumbersAdapter mAdapter;
	Intent mIntent;
	ProgressDialog progressDialog;
	
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);
		mListView = (ExpandableListView) findViewById(android.R.id.list);
		
		// hide ime on start
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE |
				WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		
		// bind onKey to EditText
		findViewById(android.R.id.edit).setOnKeyListener(this);

		// pass the NumberAdapter our LayoutInflater
		mAdapter = new ExpandableNumbersAdapter((LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE)); 
		
		mListView.setOnChildClickListener(this);
		
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
						mListView.setSelectedGroup(i-1);
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
	public boolean onChildClick(ExpandableListView parent, View v,
			int groupPosition, int childPosition, long id) {
		mListView.setSelectedGroup((int) id - 1);
		return false;
	}	
}