
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
			if (data == NumberActivity.LABEL_PRIME) {
				((TextView)view).setTextColor(Color.WHITE);
			} else if (data == NumberActivity.LABEL_SPECIAL) {
				((TextView)view).setTextColor(Color.BLUE);
			} else {
				((TextView)view).setTextColor(Color.GREEN);
			}
		}
		// send back to default handler
		return false;
	}

}
