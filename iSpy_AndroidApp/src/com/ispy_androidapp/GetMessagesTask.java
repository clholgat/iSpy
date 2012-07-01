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

import com.google.gson.Gson;

public class GetMessagesTask extends AsyncTask<Long, Void, String> {
	private String TAG = "GetMessagesTask";
	private List<Message> messages;
	private MessageAdapter adapter;
	
	public GetMessagesTask(List<Message> messages, MessageAdapter adapter) {
		this.messages = messages;
		this.adapter = adapter;
	}
	
	@Override
	protected String doInBackground(Long... params) {
		if (Constant.gameId == 0) {
			return null;
		}
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(Constant.server+"/messages/"+Constant.gameId
					+"/"+Constant.lastUpdate);
			get.addHeader("Cookie", Constant.authCookie);
			HttpResponse response = client.execute(get);
			
			InputStream in = response.getEntity().getContent();
			String json = new Scanner(in).useDelimiter("\\A").next();
			Constant.lastUpdate = System.currentTimeMillis();
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
		if (json == null) {
			return;
		}
		Log.e(TAG, json);
		Message[] newMessages = gson.fromJson(json, Message[].class);
		for (Message m : newMessages) {
			messages.add(m);
			Log.e(TAG, m.text+" ");
		}
		adapter.notifyDataSetChanged();
	}

}
