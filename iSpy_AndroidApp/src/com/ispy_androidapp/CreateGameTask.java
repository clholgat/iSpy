package com.ispy_androidapp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import com.google.gson.Gson;

import android.os.AsyncTask;
import android.util.Log;

public class CreateGameTask extends AsyncTask<Game, Void, String> {
	private String TAG = "CreateGameTask";
	@Override
	protected String doInBackground(Game... params) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constant.server+"/creategame");
			post.setHeader("Cookie", Constant.authCookie);
			
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("name",params[0].name ));
			list.add(new BasicNameValuePair("range", params[0].range));
			list.add(new BasicNameValuePair("clue", params[0].clue));
			
			post.setEntity(new UrlEncodedFormEntity(list));
			
			HttpResponse response = client.execute(post);
			
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
		
		Response response = gson.fromJson(json, Response.class);
		if (response.error != null) {
			Log.e(TAG, response.error);
			return;
		} else {
			Constant.gameId = response.gameId;
		}
	}
	
	private class Response {
		public String error;
		public long gameId;
	}

}
