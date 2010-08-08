package com.makeramen.divisors;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NumberAdapter extends BaseAdapter {
	
	public static final String LABEL_DIVISORS = " divisors";
	public static final String LABEL_PRIME = "prime number";
	public static final String LABEL_SPECIAL = "special number";
	public static final int COUNT = 10000;
	
	LayoutInflater mInflater;
	
	public NumberAdapter(LayoutInflater inflater) {
		super();
		mInflater = inflater;
	}
	
	public int getCount() {
		return COUNT;
	}
	
	public String getItem(int position) {
		return Integer.toString(position + 1);
	}

	@Override
	public long getItemId(int position) {
		return (position + 1);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		int number = position + 1;
		
		// correct use of convertView to handle recycling
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.number_list_item, null);
			
			holder = new ViewHolder();
			holder.text1 = (TextView) convertView.findViewById(android.R.id.text1);
			holder.text2 = (TextView) convertView.findViewById(android.R.id.text2);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		// set text1 as the number
		holder.text1.setText(Integer.toString(number));
		
		int fcount = 0;
		double sq = Math.sqrt(number);
		
		// create integer version of sq for performance
		int intsq = (int) sq;
		
		if (sq == intsq) {
			// add a count to factors if number is a perfect square
			fcount += 1;
			
			// decrement intsq so square doesnt get counted again
			intsq--;
		}
		
		//loop up to the square root
		for (int f = 1; f <= intsq; f++) {
			
			if (number % f == 0) {
				// double count number of factors 
				fcount += 2;
			}
		}
		
		switch (fcount) {
		case 1:
			// label 1 as special and yellow
			holder.text2.setText(LABEL_SPECIAL);
			holder.text2.setTextColor(Color.YELLOW);
			break;
		case 2:
			// label primes white
			holder.text2.setText(LABEL_PRIME);
			holder.text2.setTextColor(Color.WHITE);
			break;
		default:
			// label all others green with respective number of factors
			holder.text2.setText(Integer.toString(fcount) + LABEL_DIVISORS);
			holder.text2.setTextColor(Color.GREEN);
			break;
		}
		
		return convertView;
	}
	
	// correct use of viewholder as explained in Romain Guy's talk
	static class ViewHolder {
		TextView text1;
		TextView text2;
	}
}