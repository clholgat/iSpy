package com.ispy_androidapp;

import java.io.ByteArrayOutputStream;
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

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import com.google.gson.Gson;

public class SendMessageTask extends AsyncTask<Void, Void, String> {
	private String TAG = "SendMessageTask";
	
	private Gson gson = new Gson();
	
	private String text;
	private Bitmap bitmap;
	
	public SendMessageTask(String text, Bitmap bitmap) {
		this.text = text;
		this.bitmap = bitmap;
	}
	
	@Override
	protected String doInBackground(Void... params) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constant.server+"messages/"+Constant.gameId);
			post.setHeader("Cookie", Constant.authCookie);
			
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("text", text));
			
			if (bitmap != null) {
				ByteArrayOutputStream stream = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
				byte[] byteArray = stream.toByteArray();
				list.add(new BasicNameValuePair("image", Base64.encodeToString(byteArray, Base64.DEFAULT)));
			} else {
				list.add(new BasicNameValuePair("image", ""));
			}
			
			
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
