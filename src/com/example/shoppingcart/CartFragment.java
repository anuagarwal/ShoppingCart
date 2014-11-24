package com.example.shoppingcart;

import java.util.List;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shoppingcart.pojo.Product;
import com.example.shoppingcart.storage.DBHelper;

public class CartFragment extends Fragment 
{
	public static CartFragment getInstance()
	{
		return new CartFragment();
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.cart_fragment, null);
		ListView cartListView = (ListView) view.findViewById(R.id.cartListView);
		DBHelper dbHelper = new DBHelper(this.getActivity());
		List<Product> productList = dbHelper.getAllProductsForCart();
		CartListAdapter adapter = new CartListAdapter(this.getActivity(),productList);
		cartListView.setAdapter(adapter);
		
		return view;
	}
	
	class CartListAdapter extends BaseAdapter
	{
		LayoutInflater inflater;
		List<Product> productList;
		
		public CartListAdapter(Context context,List<Product> productList)
		{
			this.productList = productList;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		}
		
		@Override
		public int getCount() {
			return productList.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup arg2) 
		{
			Product product = productList.get(position);
			View view= convertView;
			if(convertView == null)
			{
				view = inflater.inflate(R.layout.cart_list_item, null);
			}
			ImageView productImageView = (ImageView) view.findViewById(R.id.imageView);
			productImageView.setImageResource(Integer.parseInt(product.getProductImageId()));
			TextView productNameTextView = (TextView) view.findViewById(R.id.nameTextView);
			productNameTextView.setText(product.getProductName());
			TextView productPriceTextView = (TextView) view.findViewById(R.id.priceTextView);
			productPriceTextView.setText(product.getProductPrice());
			
			return view;
		}
		
	}

}
