package com.example.shoppingcart.storage;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.location.Location;

import com.example.shoppingcart.pojo.Place;
import com.example.shoppingcart.pojo.Product;

public class DBHelper extends SQLiteOpenHelper 
{
	Context context;
	public static List<Place> storeList = new ArrayList<Place>();
	public static Location currentLocation = new Location("");
	
	public Location getCurrentLocation() 
	{
		return currentLocation;
	}
	public void setCurrentLocation(Location currentLocation) 
	{
		this.currentLocation = currentLocation;
	}
	public List<Place> getStoreList() 
	{
		return storeList;
	}
	public void setStoreList(List<Place> storeList) 
	{
		this.storeList = storeList;
	}

	public DBHelper(Context context) 
	{
		super(context, "ShoppingCartDB", null, 1);
		this.context = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) 
	{
		String USER_TABLE = "create table users(userId text,password text)";
		String ADDED_ITEM_TABLE = "create table addedItems(item text)";
		String PRODUCT_TABLE = "create table product(name text,price text,imageId text)";
		String CART_TABLE = "create table cart(productName text,price text,imageId text,quantity text)";
		db.execSQL(USER_TABLE);
		db.execSQL(ADDED_ITEM_TABLE);
		db.execSQL(PRODUCT_TABLE);
		db.execSQL(CART_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) 
	{
	}
	
	public void addUserToTable(String userId,String password)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("userId", userId);
		values.put("password", password);
		db.insert("users", null, values);
		db.close();
	}
	
	public void addItemsToTable(String item)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("item", item);
		db.insert("addedItems", null, values);
		db.close();
	}
	
	public List<String> getAllItem()
	{
		List<String> addedItemList = new ArrayList<String>();
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "select * from addedItems";
		Cursor cursor = db.rawQuery(sql, null);
		while(cursor.moveToNext())
		{
			String item = cursor.getString(0);
			addedItemList.add(item);
		}
		db.close();
		return addedItemList;
	}
	
	public void addProductsToTable(String name,String price,String imageId)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("name", name);
		values.put("price", price);
		values.put("imageId", imageId);
		db.insert("product", null, values);
		db.close();
	}
	
	public List<Product> getAllProducts()
	{
		List<Product> productList = new ArrayList<Product>();
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "select * from product";
		Cursor cursor = db.rawQuery(sql, null);
		while(cursor.moveToNext())
		{
			Product product = new Product();
			product.setProductName(cursor.getString(0));
			product.setProductPrice(cursor.getString(1));
			product.setProductImageId(cursor.getString(2));
			productList.add(product);
		}
		db.close();
		return productList;
	}

	public String validateUser(String userId,String password)
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String result = "";
		String sql = "select * from users where userId = '"+userId+"'";
		Cursor cursor = db.rawQuery(sql, null);
		if(cursor.getCount() != 0)
		{
			result = "User exists";
		}
		else
		{
			result = "User does not exists.Please register first";
		}
		db.close();
		return result;
	}

	public void addProductToCart(Product product) 
	{
		SQLiteDatabase db = this.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("productName", product.getProductName());
		values.put("price", product.getProductPrice());
		values.put("imageId", product.getProductImageId());
		db.insert("cart", null, values);
		db.close();
	}
	public List<Product> getAllProductsForCart()
	{
		List<Product> productList = new ArrayList<Product>();
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "select * from cart";
		Cursor cursor = db.rawQuery(sql, null);
		while(cursor.moveToNext())
		{
			Product product = new Product();
			product.setProductName(cursor.getString(0));
			product.setProductPrice(cursor.getString(1));
			product.setProductImageId(cursor.getString(2));
			productList.add(product);
		}
		db.close();
		return productList;
	}

	public void deleteAllProducts() 
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "delete from product";
		db.execSQL(sql);
		db.close();
	}
	public void deleteAllAddedItems() 
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "delete from addedItems";
		db.execSQL(sql);
		db.close();
	}
	public void deleteAllCartProducts() 
	{
		SQLiteDatabase db = this.getWritableDatabase();
		String sql = "delete from cart";
		db.execSQL(sql);
		db.close();
	}
}
