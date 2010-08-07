
package com.makeramen.divisor;

import android.graphics.Color;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class ListNumberBinder implements SimpleAdapter.ViewBinder {
	@Override
	public boolean setViewValue(View view, Object data,
			String textRepresentation) {
		
		if (view.getId() == android.R.id.text2) {
			if (data.equals(NumberActivity.LABEL_PRIME)) {
				((TextView)view).setTextColor(Color.rgb(0, 173, 32));
			} else {
				((TextView)view).setTextColor(Color.WHITE);
			}
		}
		// TODO Auto-generated method stub
		return false;
	}

}
