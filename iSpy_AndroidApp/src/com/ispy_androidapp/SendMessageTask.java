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

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;

public class SendMessageTask extends AsyncTask<Message, Void, String> {
	private String TAG = "SendMessageTask";
	
	private Gson gson = new Gson();
	@Override
	protected String doInBackground(Message... params) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constant.server+"messages/"+Constant.gameId);
			post.setHeader("Cookie", Constant.authCookie);
			
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("message", gson.toJson(params[0])));
			
			post.setEntity(new UrlEncodedFormEntity(list));
			
			HttpResponse response = client.execute(post);
			
			InputStream in = response.getEntity().getContent();
			String json = new Scanner(in).useDelimiter("\\A").next();
			return json;
		} catch (Exception e){
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
