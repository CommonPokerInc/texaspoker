
package com.texas.poker.adapter;

import java.util.ArrayList;

import com.texas.poker.R;
import com.texas.poker.entity.SearchResult;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/*
 * author FrankChan
 * description 
 * time 2014-10-2
 *
 */
public class RoomAdapter extends BaseAdapter {

	private class ViewHolder{
		TextView txtName,txtMoney;
		View mBlock;
	}
	
	private OnGridItemClickListener mListener ;
	
	private ArrayList<SearchResult>mList;
	
	private LayoutInflater mInflater;
	
	public RoomAdapter(Context context,OnGridItemClickListener listener) {
		super();
		mList = new ArrayList<SearchResult>();
		mInflater = LayoutInflater.from(context);
		mListener = listener;
	}

	public void setData(ArrayList<SearchResult>newList){
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
			convertView = mInflater.inflate(R.layout.adapter_room_item, null);
			holder = new ViewHolder();
			holder.mBlock = convertView.findViewById(R.id.room_item_block);
			holder.txtName = (TextView) convertView.findViewById(R.id.room_item_name);
			holder.txtMoney = (TextView) convertView.findViewById(R.id.room_item_money);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		final SearchResult result = mList.get(position);
		holder.txtName.setText(result.getName());
		holder.txtMoney.setText(String.valueOf(result.getMoney()));
		holder.mBlock.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mListener.onGridItemClick(result.getFullName());
			}
		});
		return convertView;
	}

	public interface OnGridItemClickListener{
		void onGridItemClick(String fullName);
	}
	
}


