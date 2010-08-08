package com.makeramen.divisors;

import java.util.ArrayList;
import java.util.Collections;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class DivisorsActivity extends ListActivity implements ListView.OnItemClickListener {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.divisors);
		
		// get number passed from intent
		int number = this.getIntent().getIntExtra(NumbersActivity.EXTRA_NUMBER, 1);
		
		// set title
		this.setTitle("Divisors of " + number + ":");
		
		// arraylist to hold divisors
		ArrayList<Integer> divisors = new ArrayList<Integer>();
		
		// hold sqrt of number
		double sq = Math.sqrt(number);
		
		for (int f = 1; f <= sq; f++) {
			//loop up to the square root
			
			if (number % f == 0) {

				// populate the arraylist with divisor
				divisors.add(f);
				
				if (f != sq) {
					// add conjugate(?) divisor to arraylist if divisor is not the sqrt
					divisors.add(number/f);
				}
				
			}
		}
		
		// sort divisors
		Collections.sort(divisors);
		
		// create arrayadapter to do handle arraylist 
		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, R.layout.divisor_list_item, divisors);		
		
		// set adapter to listview
		ListView mListView = (ListView) findViewById(android.R.id.list);
		mListView.setAdapter(adapter);
		
		// bind click listener to listview
		mListView.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> list, View view, int position, long id) {
		// if a number is clicked, set result code as clicked number
		setResult((Integer) list.getAdapter().getItem(position));
		this.finish();
	}
	
}