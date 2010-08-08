
package com.makeramen.divisors;

import android.graphics.Color;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.TextView;


public class ListNumberBinder implements SimpleAdapter.ViewBinder {
	@Override
	public boolean setViewValue(View view, Object data,
			String textRepresentation) {
		
		if (view.getId() == android.R.id.text2) {
			if (data == NumbersActivity.LABEL_PRIME) {
				
				// text color for prime number
				((TextView)view).setTextColor(Color.WHITE);
				
			} else if (data == NumbersActivity.LABEL_SPECIAL) {
				// text color for number 1
				((TextView)view).setTextColor(Color.YELLOW);
			} else {
				
				// text color for non-prime numbers
				((TextView)view).setTextColor(Color.GREEN);
			}
		}
		// send back to default handler
		return false;
	}

}
