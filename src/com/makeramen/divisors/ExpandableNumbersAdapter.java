package com.makeramen.divisors;

import java.util.ArrayList;
import java.util.Collections;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

@SuppressWarnings("unchecked")
public class ExpandableNumbersAdapter extends BaseExpandableListAdapter {
	
	public static final String LABEL_DIVISORS = " divisors";
	public static final String LABEL_PRIME = "prime number";
	public static final String LABEL_SPECIAL = "special number";
	public static final int GROUP_COUNT = 10000;
	
	LayoutInflater mInflater;
	ArrayList<Integer>[] mArrayList = new ArrayList[10000];
	
	public ExpandableNumbersAdapter(LayoutInflater inflater) {
		super();
		mInflater = inflater;
	}
	
	public ArrayList<Integer> generateChildren(int number, ArrayList<Integer> list) {
		// sanity check: list should always be null
		if (list != null) {
			return list;
		}
		
		list = new ArrayList<Integer>();
		
		double sq = Math.sqrt(number);
		
		// create integer version of sq for performance
		int intsq = (int) sq;
		
		if (sq == intsq) {
			// add sqrt if number is perfect square
			list.add(intsq);
			
			// decrement intsq so square doesnt get counted again
			intsq--;
		}
		
		//loop up to the square root
		for (int f = 1; f <= intsq; f++) {
			
			if (number % f == 0) {
				// add factors 
				list.add(f);
				list.add(number/f);
			}
		}
		
		Collections.sort(list);
		
		return list;
	}

	@Override
	public Object getChild(int groupPosition, int childPosition) {
		if (mArrayList[groupPosition] == null) {
			mArrayList[groupPosition] = generateChildren(groupPosition + 1, mArrayList[groupPosition]);
		}
		return mArrayList[groupPosition].get(childPosition);
	}

	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return Long.parseLong(getChild(groupPosition, childPosition).toString());
	}

	@Override
	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		ViewHolder holder;
				
		// correct use of convertView to handle recycling
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.divisor_list_item, null);
			
			holder = new ViewHolder();
			holder.text1 = (TextView) convertView.findViewById(android.R.id.text1);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.text1.setText(getChild(groupPosition, childPosition).toString());
		
		return convertView;
	}

	@Override
	public int getChildrenCount(int groupPosition) {
		if (mArrayList[groupPosition] == null) {
			mArrayList[groupPosition] = generateChildren(groupPosition + 1, mArrayList[groupPosition]);
		}
		
		return mArrayList[groupPosition].size();
	}

	@Override
	public Object getGroup(int groupPosition) {
		return Integer.toString(groupPosition + 1);
	}

	@Override
	public int getGroupCount() {
		return GROUP_COUNT;
	}

	@Override
	public long getGroupId(int groupPosition) {
		return groupPosition + 1;
	}

	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		ViewHolder holder;
		
		int number = groupPosition + 1;
		
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
		
		int fcount = getChildrenCount(groupPosition);
		
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

	@Override
	public boolean hasStableIds() {
		return false;
	}

	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}
	
	// correct use of viewholder as explained in Romain Guy's talk
	static class ViewHolder {
		TextView text1;
		TextView text2;
	}
}