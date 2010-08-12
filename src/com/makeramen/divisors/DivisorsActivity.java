package com.makeramen.divisors;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnKeyListener;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Toast;

public class DivisorsActivity extends Activity implements ExpandableListView.OnChildClickListener, OnKeyListener {
	
	public static final String EXTRA_NUMBER = "number";
	public static final String EXTRA_DIVISORS = "divisors";
	public static final int INTENT_DIVISORS = 0;
	
	ExpandableListView mListView;
	ExpandableDivisorsAdapter mAdapter;
	Intent mIntent;
	ProgressDialog progressDialog;
	Toast mToast;
	
	
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
		mAdapter = new ExpandableDivisorsAdapter((LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE)); 
		
		mListView.setOnChildClickListener(this);
		
		// bind adapter to listview
		mListView.setAdapter(mAdapter);
	}

	public boolean onKey(View view, int keyCode, KeyEvent event) {

		if (event.getAction() == KeyEvent.ACTION_DOWN) {
			switch(keyCode) {
			case KeyEvent.KEYCODE_ENTER:
				String input = ((EditText) view).getText().toString();
				
				if (input.trim().length() > 0) {
					// parse input, this shouldn't throw an exception
					// due to the restriction of inputType = number
					// but if it does, we can always add a try/catch
					int i = Integer.parseInt(input);
					
					if ( 0 < i && i <=10000) {
						// if number is within range, jump to number
						mListView.setSelectedGroup(i-1);
						
					} else {
						// if number is out of range, show toast with error message
						if (mToast == null) {
							mToast = Toast.makeText(this, R.string.error_range, Toast.LENGTH_LONG);
						}
						mToast.show();
					}
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