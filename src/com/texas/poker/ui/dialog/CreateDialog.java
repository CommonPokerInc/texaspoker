
package com.texas.poker.ui.dialog;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.texas.poker.entity.Room;
import com.texas.poker.ui.BaseDialog;

/*
 * author FrankChan
 * description 
 * time 2014-10-8
 *
 */
public class CreateDialog extends BaseDialog {

	private TextView txtName,txtMoeny;
	
	private Button btnCreate,btnClose;
	
	public CreateDialog(Context context, int duration, Effectstype type) {
		super(context, duration, type);
		// TODO Auto-generated constructor stub
		
	}

	public void show(Room room){
		
	}
	
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub

	}

}


