
package com.texas.poker.ui.dialog;

import com.texas.poker.R;
import com.texas.poker.ui.BaseDialog;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;

/*
 * author FrankChan
 * description 
 * time 2014-9-26
 *
 */
public class ConfirmDialog extends BaseDialog{
	private TextView txtTitle,txtTopic;
	private Button btnPositive,btnNegative;
	
	private DialogConfirmInterface dialogInterface;
	
	public ConfirmDialog(Context context, int duration, Effectstype type,DialogConfirmInterface dialogInterface) {
		super(context, duration, type);
		this.dialogInterface = dialogInterface;
		// TODO Auto-generated constructor stub
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_confirm, null);
		mBackground = view.findViewById(R.id.dialog_common_bg);
		txtTitle = (TextView) view.findViewById(R.id.dialog_confirm_title);
		txtTopic = (TextView) view.findViewById(R.id.dialog_confirm_topic);
		btnPositive = (Button) view.findViewById(R.id.dialog_btn_confirm);
		btnNegative = (Button) view.findViewById(R.id.dialog_btn_cancel);
		btnPositive.setOnClickListener(this);
		btnNegative.setOnClickListener(this);
		mBackground.setOnClickListener(this);
		builder.setCustomView(view, context);
	}

	public void setTitle(String strTitle){
		txtTitle.setText(strTitle);
	}
	public void setTopic(String strTopic){
		txtTopic.setText(strTopic);
	}
	
	public void setPositive(String strPositive){
		btnPositive.setText(strPositive);
	}
	
	public void setNegative(String strNegative){
		btnNegative.setText(strNegative);
	}
	
	public interface DialogConfirmInterface{
		void onConfirm();
		void onCancel();
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.dialog_btn_confirm:
			if(null!=dialogInterface){
				dialogInterface.onConfirm();
			}
			hide();
			break;
		case R.id.dialog_btn_cancel:
			if(null!=dialogInterface){
				dialogInterface.onCancel();
			}
			hide();
			break;
		case R.id.dialog_common_bg:
			break;
		default:
			break;
		}
	}
	
}


