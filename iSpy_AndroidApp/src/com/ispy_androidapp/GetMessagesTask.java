package com.ispy_androidapp;

import java.io.InputStream;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

public class GetMessagesTask extends AsyncTask<Long, Void, String> {
	private String TAG = "GetMessagesTask";
	@Override
	protected String doInBackground(Long... params) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(Constant.server+"/messages/"+params[0]
					+"?since="+Constant.lastUpdate);
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
	
	@Override
	protected void onPostExecute(String json) {
		Gson gson = new Gson();
		Message[] messages = gson.fromJson(json, Message[].class);
		// Will eventually do something with these.
	}

}
