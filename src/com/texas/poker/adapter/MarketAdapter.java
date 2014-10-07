
package com.texas.poker.adapter;

import java.util.ArrayList;

import com.texas.poker.R;
import com.texas.poker.entity.Property;
import com.texas.poker.util.ImageUtil;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/*
 * author FrankChan
 * description 
 * time 2014-10-2
 *
 */
public class MarketAdapter extends BaseAdapter {

	private class ViewHolder{
		TextView txtName,txtMoney,txtNone;
		ImageView imgAvatar,imgBet;
		View mBlock;
	}
	
	private OnGridItemClickListener mListener ;
	
	private ArrayList<Property>mList;
	
	private LayoutInflater mInflater;
	
	public MarketAdapter(Context context,OnGridItemClickListener listener) {
		super();
		mList = new ArrayList<Property>();
		mInflater = LayoutInflater.from(context);
		mListener = listener;
	}

	public void setData(ArrayList<Property>newList){
		mList.clear();
		mList.addAll(newList);
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder = null;
		if(convertView==null){
			convertView = mInflater.inflate(R.layout.adapter_market_property, null);
			holder = new ViewHolder();
			holder.mBlock = convertView.findViewById(R.id.market_layout);
			holder.txtName = (TextView) convertView.findViewById(R.id.market_item_name);
			holder.txtMoney = (TextView) convertView.findViewById(R.id.market_item_price);
			holder.imgAvatar = (ImageView) convertView.findViewById(R.id.market_item_avatar);
			holder.txtNone = (TextView) convertView.findViewById(R.id.market_item_none);
			holder.imgBet= (ImageView) convertView.findViewById(R.id.market_item_bet);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final Property result = mList.get(position);
		if(result.getType()<=Property.TYPE_FIVE){
			holder.txtName.setText(result.getName());
			holder.txtMoney.setText(String.valueOf(result.getMoney()));
			holder.imgAvatar.setImageResource(ImageUtil.PROPERTY[result.getType()]);
			holder.mBlock.setOnClickListener(new OnClickListener() {
			
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					if(mListener!=null){
						mListener.onGridItemClick(result);
					}
				}
			});
		}else{
			holder.imgBet.setVisibility(View.INVISIBLE);
			holder.txtMoney.setVisibility(View.INVISIBLE);
			holder.txtNone.setVisibility(View.VISIBLE);
		}
		return convertView;
	}

	public interface OnGridItemClickListener{
		void onGridItemClick(Property property);
	}
	
}


