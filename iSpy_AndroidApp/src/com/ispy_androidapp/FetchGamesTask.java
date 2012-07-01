package com.ispy_androidapp;

import java.io.InputStream;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gson.Gson;

import android.os.AsyncTask;
import android.util.Log;

public class FetchGamesTask extends AsyncTask<Void, Void, String>{
	private String TAG = "FetchGamesTask";
	
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
		
		
	}
}
