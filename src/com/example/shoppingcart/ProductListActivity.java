package com.example.shoppingcart;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.shoppingcart.pojo.Product;
import com.example.shoppingcart.storage.DBHelper;

public class ProductListActivity extends Activity 
{
	private GridView productGridView;
	DBHelper dbHelper;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.product_list_activity);
		getActionBar().hide();
		
		dbHelper = new DBHelper(this);
		dbHelper.deleteAllProducts();
		dbHelper.addProductsToTable("Glade Premium Room Spray", "Rs.500", String.valueOf(R.drawable.room_spray));
		dbHelper.addProductsToTable("Glade Jar Candles", "Rs.200", String.valueOf(R.drawable.candles));
		dbHelper.addProductsToTable("Glade Wax Melts Refills", "Rs.150", String.valueOf(R.drawable.melt_wax));
		dbHelper.addProductsToTable("Minute Maid Orange Juice", "Rs.65", String.valueOf(R.drawable.minute));
		dbHelper.addProductsToTable("Minute Maid Juice Box", "Rs.75", String.valueOf(R.drawable.minute_juic));
		
		productGridView = (GridView)findViewById(R.id.productGridView);
		final List<Product> productList = dbHelper.getAllProducts();
		ProductGridAdapter adapter = new ProductGridAdapter(this,productList);
		productGridView.setAdapter(adapter);
		productGridView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) 
			{
				dbHelper.addProductToCart(productList.get(pos));
				Toast.makeText(ProductListActivity.this, "Product added to cart.", Toast.LENGTH_LONG).show();
			}
		});
		Button backButton = (Button) findViewById(R.id.backButton);
		backButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View arg0) 
			{
				ProductListActivity.this.finish();
			}
		});
	}
	
	@Override
	public void onBackPressed() {
	}
	
}
