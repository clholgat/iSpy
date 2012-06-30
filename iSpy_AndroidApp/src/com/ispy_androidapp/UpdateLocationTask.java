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

public class UpdateLocationTask extends AsyncTask<Long, Void, String> {

	@Override
	protected String doInBackground(Long... params) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constant.server+"/location");
			post.setHeader("Cookie", Constant.authCookie);
			
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("lat", params[0]+""));
			list.add(new BasicNameValuePair("lon", params[1]+""));
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

}
