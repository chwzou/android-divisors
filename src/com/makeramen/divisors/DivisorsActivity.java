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
		
		int number = this.getIntent().getIntExtra(NumbersActivity.EXTRA_NUMBER, -1);
//		divisors = this.getIntent().getIntExtra(NumbersActivity.EXTRA_DIVISORS, -1);
		
		ArrayList<Integer> divisors = new ArrayList<Integer>();
		
		this.setTitle("Divisors of " + number + ":");
		
		double sq = Math.sqrt(number);
		
		for (int f = 1; f <= sq; f++) {
			//loop up to the square root
			
			if (number % f == 0) {
				
				divisors.add(f);
				
				if (f != sq) {
					divisors.add(number/f);
				}
				
			}
		}
		
		Collections.sort(divisors);
		
		ArrayAdapter<Integer> adapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_list_item_1, divisors);
		
		ListView mListView = (ListView) findViewById(android.R.id.list);		
		
		mListView.setAdapter(adapter);
		
		mListView.setOnItemClickListener(this);
	}
	
	@Override
	public void onItemClick(AdapterView<?> list, View view, int position, long id) {
		setResult((Integer) list.getAdapter().getItem(position));
		this.finish();
	}
	
}