package com.example.shoppingcart;

import java.util.ArrayList;
import java.util.List;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import com.example.shoppingcart.storage.DBHelper;

public class ShoppingListFragment extends Fragment
{
	EditText addItemEdtiText;
	ListView addedItemListView;
	ImageView addButton;
	List<String> addeditemList;
	ArrayAdapter<String> adapter;
	
	public static ShoppingListFragment getInstance()
	{
		return new ShoppingListFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.fragment_shopping_list, null);
		
		final DBHelper dbHelper = new DBHelper(this.getActivity());
		addeditemList = new ArrayList<String>();
		addeditemList = dbHelper.getAllItem();
		adapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_list_item_1,addeditemList);
		addItemEdtiText = (EditText) view.findViewById(R.id.addItemEditText);
		addedItemListView = (ListView) view.findViewById(R.id.addedItemlistView);
		addedItemListView.setAdapter(adapter);
		addButton = (ImageView) view.findViewById(R.id.addButton);
		addButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				addeditemList.add(addItemEdtiText.getText().toString());
				adapter.notifyDataSetChanged();
				dbHelper.addItemsToTable(addItemEdtiText.getText().toString());
				addItemEdtiText.setText("");
			}
		});
		return view;
	}
}