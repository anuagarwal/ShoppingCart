package com.example.shoppingcart;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.shoppingcart.pojo.Product;

public class ProductGridAdapter extends BaseAdapter 
{
	List<Product> productList;
	LayoutInflater inflater;
	
	public ProductGridAdapter(Context context,List<Product> productList) 
	{
		this.productList = productList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public int getCount()
	{
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
		View view = convertView;
		Product product = productList.get(position);
		if(convertView == null)
		{
			view = inflater.inflate(R.layout.product_grid_item, null);
		}
		
		ImageView productImageView = (ImageView) view.findViewById(R.id.productImageView);
		productImageView.setImageResource(Integer.parseInt(product.getProductImageId()));
		TextView productNameTextView = (TextView) view.findViewById(R.id.productNameTextView);
		productNameTextView.setText(product.getProductName());
		TextView productPriceTextView = (TextView) view.findViewById(R.id.priceTextView);
		productPriceTextView.setText(product.getProductPrice());
		
		return view;
	}

}
