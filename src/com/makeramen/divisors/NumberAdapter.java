package com.makeramen.divisors;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class NumberAdapter extends BaseAdapter {
	
	LayoutInflater mInflater;
	
	public NumberAdapter(LayoutInflater inflater) {
		super();
		mInflater = inflater;
	}
	
	
	
	public int getCount() {
		return 10000;
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
		
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.number_list_item, null);
			
			holder = new ViewHolder();
			holder.text1 = (TextView) convertView.findViewById(android.R.id.text1);
			holder.text2 = (TextView) convertView.findViewById(android.R.id.text2);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.text1.setText(Integer.toString(number));
		
		int fcount = 0;
		double sq = Math.sqrt(number);
		
		for (int f = 1; f <= sq; f++) {
			//loop up to the square root
			
			if (number % f == 0) {
				// double count number of factors, unless f is the square root 
				fcount += ((f == sq) ? 1 : 2);
			}
		}
		
		if (fcount == 1) {
			holder.text2.setText(NumbersActivity.LABEL_SPECIAL);
			holder.text2.setTextColor(Color.YELLOW);
		} else if  (fcount == 2) {
			holder.text2.setText(NumbersActivity.LABEL_PRIME);
			holder.text2.setTextColor(Color.WHITE);
		} else {
			holder.text2.setText(Integer.toString(fcount) +NumbersActivity.LABEL_DIVISORS);
			holder.text2.setTextColor(Color.GREEN);
		}
		
		return convertView;
	}
	
	static class ViewHolder {
		TextView text1;
		TextView text2;
	}
}