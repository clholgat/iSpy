package com.ispy_androidapp;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;


public class LocationService extends Service {
	private String TAG = "LocationService";
	
	private static final int MIN_DIST = 5; 
	private static final long MIN_TIME = 100000;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		criteria.setAccuracy(Criteria.ACCURACY_FINE);
		String provider = manager.getBestProvider(criteria, true);
		manager.requestLocationUpdates(provider, MIN_TIME, MIN_DIST, new UserLocationListener());
	}
	
	private class UserLocationListener implements LocationListener{
		

		public void onLocationChanged(Location location) {
			// TODO Auto-generated method stub

		}

		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

	}

}
