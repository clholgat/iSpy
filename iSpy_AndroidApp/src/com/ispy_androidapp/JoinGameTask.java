package com.ispy_androidapp;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;


public class JoinGameTask extends AsyncTask<Long, Void, String> {

	private String TAG = "JoinGameTask";
	
	private Gson gson = new Gson();
	
	@Override
	protected String doInBackground(Long... params) {
		try {
			
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(Constant.server+"/join/"+params[0]);
			get.addHeader("Cookie", Constant.authCookie);
			HttpResponse response = client.execute(get);
			
			InputStream in = response.getEntity().getContent();
			String json = new Scanner(in).useDelimiter("\\A").next();
			return json;
		}
		catch(Exception e)
		{
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	@Override
	protected void onPostExecute(String json) {
		Response response = gson.fromJson(json, Response.class);
		if (response.error != null) {
			Log.e(TAG, response.error);
		} else {
			Log.d(TAG, response.success);
		}
	}
	
	private class Response {
		public String success;
		public String error;
	}
	
}
