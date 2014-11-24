package com.example.shoppingcart;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.shoppingcart.pojo.Place;
import com.example.shoppingcart.storage.DBHelper;

public class StoresFragment extends Fragment 
{
	public static StoresFragment getInstance()
	{
		return new StoresFragment();
	}

	public String longitude;
	public String latitude;
	TextView temperatureTextView;
	public Location currentLocation;
	ListView storeListView;
	List<Place> storeList;
	StoreListAdapter adapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) 
	{
		View view = inflater.inflate(R.layout.activity_storelist,null);
		
		temperatureTextView = (TextView) view.findViewById(R.id.localTemperatureTextView);
		storeListView = (ListView) view.findViewById(R.id.storeListView);
		storeList =  new ArrayList<Place>();
		adapter = new StoreListAdapter(this.getActivity(), storeList);
		storeListView.setAdapter(adapter);
		
		storeListView.setOnItemClickListener(new OnItemClickListener()
		{
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3)
			{
				Intent intent = new Intent(StoresFragment.this.getActivity(),ProductListActivity.class);
				startActivity(intent);
			}
		});
		
		ImageView mapImageView = (ImageView) view.findViewById(R.id.storeMapImageView);
		mapImageView.setOnClickListener(new View.OnClickListener() 
		{
			@Override
			public void onClick(View arg0)
			{
				Intent intent = new Intent(StoresFragment.this.getActivity(), MapViewActivity.class);
				startActivity(intent);
			}
		});
		
		LocationManager locationManager = (LocationManager) this.getActivity().getSystemService(Context.LOCATION_SERVICE);
		LocationListener listener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 10, listener);
		
		return view;
	}
	
	/*---------- Listener class to get coordinates ------------- */
	private class MyLocationListener implements LocationListener {

	    @Override
	    public void onLocationChanged(Location loc)
	    {
	        longitude =String.valueOf(loc.getLongitude());
	        Log.v("long : ", longitude);
	        latitude = String.valueOf(loc.getLatitude());
	        Log.v("lat : ", latitude);
	        
	        currentLocation = new Location("");
	        currentLocation.setLatitude(loc.getLatitude());
	        currentLocation.setLongitude(loc.getLongitude());
	        if(temperatureTextView.getText().toString().equals(""))
	        {
	        	new UpdateTemperatureTask().execute();
	        }
	    }

	    @Override
	    public void onProviderDisabled(String provider) {}

	    @Override
	    public void onProviderEnabled(String provider) {}

	    @Override
	    public void onStatusChanged(String provider, int status, Bundle extras) {}
	}
	
	
	class GetNearByStoresTask extends AsyncTask<Void, Void, Void>
	{
		String result = "";
		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			dialog = new ProgressDialog(StoresFragment.this.getActivity());
			dialog.setTitle("Getting stores...");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) 
		{
			InputStream inputStream = null;
			
			try 
			{
				// create HttpClient
				HttpClient httpclient = new DefaultHttpClient();

				// make GET request to the given URL
				HttpResponse httpResponse = httpclient.execute(new HttpGet("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+latitude+","+longitude+"&radius=10000&types=store&key=AIzaSyBAGoU0__rhU7fpDfBSjL1DrhaWSE9St2I"));

				// receive response as inputStream
				inputStream = httpResponse.getEntity().getContent();

				// convert inputstream to string
				if(inputStream != null)
					result = convertInputStreamToString(inputStream);
				else
					result = "Did not work!";

			} catch (Exception e) {
				Log.d("InputStream", e.getLocalizedMessage());
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);
			dialog.dismiss();
			Log.e("response : ", this.result);
			try
			{
				if(!this.result.equals(""))
				{
				JSONArray placesArray = new JSONObject(this.result).getJSONArray("results");
				for(int i=0;i<placesArray.length();i++)
				{
					JSONObject placeJsonobject = new JSONObject(placesArray.getString(i));
					Place place = new Place();
					place.setName(placeJsonobject.getString("name"));
					place.setAddress(placeJsonobject.getString("vicinity"));
					JSONObject locationObject = placeJsonobject.getJSONObject("geometry").getJSONObject("location");
					Location location = new Location("");
					location.setLatitude(Double.parseDouble(locationObject.getString("lat")));
					location.setLongitude(Double.parseDouble(locationObject.getString("lng")));
					place.setLocation(location);
					float distance = currentLocation.distanceTo(location);
					place.setDistance(String.valueOf(new BigDecimal(distance/1000).setScale(2,RoundingMode.DOWN)));
					storeList.add(place);
				}
				DBHelper dbHelper = new DBHelper(StoresFragment.this.getActivity());
				dbHelper.setStoreList(storeList);
				dbHelper.setCurrentLocation(currentLocation);
				adapter.notifyDataSetChanged();
				}
				else
				{
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(StoresFragment.this.getActivity());
					alertDialog.setTitle("Error");
					alertDialog.setMessage("Unable to get store details");
					alertDialog.setPositiveButton("OK", new OnClickListener() {
						
						@Override
						public void onClick(DialogInterface arg0, int arg1) 
						{
							dialog.dismiss();
						}
					});
					alertDialog.show();
				}
			}
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
		}
	}
	
	class UpdateTemperatureTask extends AsyncTask<Void, Void, Void>
	{
		String result = "";
		ProgressDialog dialog;
		
		@Override
		protected void onPreExecute() 
		{
			super.onPreExecute();
			dialog = new ProgressDialog(StoresFragment.this.getActivity());
			dialog.setTitle("Updating location...");
			dialog.show();
		}

		@Override
		protected Void doInBackground(Void... arg0) 
		{
			InputStream inputStream = null;
			
			try {

				// create HttpClient
				HttpClient httpclient = new DefaultHttpClient();

				// make GET request to the given URL
				HttpResponse httpResponse = httpclient.execute(new HttpGet("http://api.openweathermap.org/data/2.5/weather?lat="+latitude+"&lon="+longitude));

				// receive response as inputStream
				inputStream = httpResponse.getEntity().getContent();

				// convert inputstream to string
				if(inputStream != null)
					result = convertInputStreamToString(inputStream);
				else
					result = "Did not work!";

			} catch (Exception e) {
				Log.d("InputStream", e.getLocalizedMessage());
			}
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) 
		{
			super.onPostExecute(result);
			dialog.dismiss();
			Log.e("response : ", this.result);
			try
			{
				JSONObject jsonObject = new JSONObject(this.result);
				JSONObject object = (JSONObject) jsonObject.get("main");
				float temperatureInCelsius = (float) (Float.parseFloat(object.getString("temp")) - 273.15);
				temperatureTextView.setText(String.valueOf(new BigDecimal(temperatureInCelsius).setScale(2,RoundingMode.DOWN))+"C");
				new GetNearByStoresTask().execute();
			}
			catch (JSONException e) 
			{
				e.printStackTrace();
			}
			
		}
	}
	
    private static String convertInputStreamToString(InputStream inputStream) throws IOException
    {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
        {
            result += line;
        } 
        inputStream.close();
        return result;
    }
}
