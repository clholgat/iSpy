package com.ispy_androidapp;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;

import com.google.android.gcm.GCMRegistrar;



public class RegisterTask extends AsyncTask<Activity, Void, Void> {

	@Override
	protected Void doInBackground(Activity... params) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constant.server+"/register");
			post.setHeader("Cookie", Constant.authCookie);
			
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("deviceId", GCMRegistrar.getRegistrationId(params[0])));
			post.setEntity(new UrlEncodedFormEntity(list));
			
			HttpResponse response = client.execute(post);
			
			LocationManager manager = (LocationManager) params[0].getSystemService(Context.LOCATION_SERVICE);
			Criteria crit = new Criteria();
			crit.setAccuracy(Criteria.ACCURACY_FINE);
			String provider = manager.getBestProvider(crit, true);
			Location loc = manager.getLastKnownLocation(provider);
			new UpdateLocationTask().execute(loc.getLatitude(), loc.getLongitude());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
