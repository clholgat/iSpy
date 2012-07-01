package com.ispy_androidapp;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

public class CreateGameTask extends AsyncTask<Game, Void, String> {
	private String TAG = "CreateGameTask";
	@Override
	protected String doInBackground(Game... params) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constant.server+"/creategame");
			post.setHeader("Cookie", Constant.authCookie);
			Log.e(TAG, Constant.authCookie);
			
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("name",params[0].name ));
			list.add(new BasicNameValuePair("range", params[0].range));
			list.add(new BasicNameValuePair("clue", params[0].clue));
			
			post.setEntity(new UrlEncodedFormEntity(list));
			
			HttpResponse response = client.execute(post);
			
			InputStream in = response.getEntity().getContent();
			StringBuilder builder = new StringBuilder();
			BufferedReader reader = new BufferedReader(new InputStreamReader(in));
			String json;
			while((json = reader.readLine()) != null) {
				builder.append(json);
			}
			json = builder.toString();
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
		Log.e(TAG, json);
		Response response = gson.fromJson(json, Response.class);
		if (response.error != null) {
			Log.e(TAG, response.error);
			return;
		} else {
			Constant.gameId = response.gameid;
			Constant.creator = true;
		}
	}
	
	private class Response {
		public String error;
		public long gameid;
	}

}
