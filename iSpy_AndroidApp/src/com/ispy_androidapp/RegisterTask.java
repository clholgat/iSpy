package com.ispy_androidapp;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;



public class RegisterTask extends AsyncTask<Void, Void, Void> {

	@Override
	protected Void doInBackground(Void... params) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constant.server+"/register");
			post.setHeader("Cookie", Constant.authCookie);
			
			HttpResponse response = client.execute(post);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
