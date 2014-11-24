package com.example.shoppingcart;

import java.util.List;

import android.app.Activity;
import android.os.Bundle;

import com.example.shoppingcart.pojo.Place;
import com.example.shoppingcart.storage.DBHelper;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapViewActivity extends Activity
{
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		DBHelper dbHelper = new DBHelper(this);
		List<Place> storeList = dbHelper.getStoreList();
		GoogleMap googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
		for (Place place : storeList) 
		{
			LatLng location = new LatLng(place.getLocation().getLatitude(),place.getLocation().getLongitude());
			MarkerOptions markerOptions = new MarkerOptions();
			markerOptions.position(location);
			markerOptions.title(place.getName());
			googleMap.addMarker(markerOptions);
		}
		CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(dbHelper.getCurrentLocation().getLatitude(),dbHelper.getCurrentLocation().getLongitude()));
		googleMap.moveCamera(center);
		CameraUpdate zoom = CameraUpdateFactory.zoomTo(15);
		googleMap.animateCamera(zoom);
	}
}