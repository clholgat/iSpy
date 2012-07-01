package com.ispy_androidapp;

import java.io.InputStream;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;
import com.google.gson.Gson;

public class FetchGamesTask extends AsyncTask<Void, Void, String>{
	private String TAG = "FetchGamesTask";
	iSpyOverlay overlayitem;
	List<Overlay> mapoverlay;
	
	public FetchGamesTask(iSpyOverlay overlay, List<Overlay> Mapoverlays ){
		this.overlayitem = overlay;
		this.mapoverlay = Mapoverlays;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(Constant.server+"/fetchgames");
			get.addHeader("Cookie", Constant.authCookie);
			HttpResponse response = client.execute(get);
			
			InputStream in = response.getEntity().getContent();
			String json = new Scanner(in).useDelimiter("\\A").next();
			
			return json;
		} catch (Exception e) {
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	protected void onPostExecute(String json) {
		Gson gson = new Gson();
		Log.e(TAG, json);
		
		LatLon[] latlon = gson.fromJson(json, LatLon[].class);
		
		for(int i = 0; i< latlon.length; i++)
		{
			GeoPoint point = new GeoPoint((int)(latlon[i].lat*1E6),(int)(latlon[i].lon*1E6));
			Log.e("Latitude",Integer.toString((int)(latlon[i].lat*1E6)));
			Log.e("Longitude",Integer.toString((int)(latlon[i].lon*1E6)));
			OverlayItem overlay = new OverlayItem(point, "iSpyGame", "Fun Stuff");
			
			this.overlayitem.addOverlay(overlay);
			this.mapoverlay.add(this.overlayitem);
		}
		//GeoPoint point = new GeoPoint(19240000,-99120000);
		//OverlayItem overlay = new OverlayItem(point, "iSpyGame", "Fun Stuff");
		
		//this.overlayitem.addOverlay(overlay);
		//this.mapoverlay.add(this.overlayitem);
		
	}
	
	class LatLon{
		double lat;
		double lon;
	}
}
