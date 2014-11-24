package com.example.shoppingcart;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.shoppingcart.pojo.Place;

public class StoreListAdapter extends BaseAdapter 
{
	List<Place> storeList;
	LayoutInflater inflater;
	
	public StoreListAdapter(Context context,List<Place> storeList) 
	{
		this.storeList = storeList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	@Override
	public int getCount() 
	{
		return storeList.size();
	}

	@Override
	public Object getItem(int arg0) 
	{
		return null;
	}

	@Override
	public long getItemId(int arg0) 
	{
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup arg2) 
	{
		View view = convertView;
		if(convertView == null)
		{
			view = inflater.inflate(R.layout.store_list_item, null);
		}
		
		Place place = storeList.get(position);
		TextView nameTextView = (TextView) view.findViewById(R.id.nameTextView);
		nameTextView.setText(place.getName());
		TextView addressTextView = (TextView) view.findViewById(R.id.addressTextView);
		addressTextView.setText(place.getAddress());
		TextView distanceTextView = (TextView) view.findViewById(R.id.distanceTextView);
		distanceTextView.setText(place.getDistance()+"km");
		
		return view;
	}

}
