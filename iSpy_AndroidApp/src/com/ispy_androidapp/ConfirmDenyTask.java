package com.ispy_androidapp;

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

public class ConfirmDenyTask extends AsyncTask<Boolean, Void, String> {
	private long messageId;
	
	public ConfirmDenyTask(long messageId) {
		this.messageId = messageId;
	}
	
	@Override
	protected String doInBackground(Boolean... params) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpPost post = new HttpPost(Constant.server+"/confirm/"+messageId);
			post.setHeader("Cookie", Constant.authCookie);
			
			List<NameValuePair> list = new ArrayList<NameValuePair>();
			list.add(new BasicNameValuePair("confirm", params[0].toString()));
			post.setEntity(new UrlEncodedFormEntity(list));
			
			HttpResponse response = client.execute(post);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
