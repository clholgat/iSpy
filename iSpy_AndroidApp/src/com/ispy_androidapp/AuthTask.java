package com.ispy_androidapp;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;


import android.os.AsyncTask;
import android.util.Log;


public class AuthTask extends AsyncTask<String, Void, Boolean> {
	private String TAG = "AuthTask";

	@Override
	protected Boolean doInBackground(String... arg0) {
		String authToken = arg0[0];
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet("https://ispy-server.appspot.com/_ah/login?continue=http://localhost/&auth="+authToken);
			HttpResponse response = client.execute(get);
			
			if (response.getStatusLine().getStatusCode() != 302) {
				Log.e(TAG, "could not authenticate");
				return false;
			}
			
			for (Header header : response.getAllHeaders()){
				if ("Set-Cookie".equals(header.getName())) {
					String[] parts = header.getValue().split(";");
					for (String part : parts) {
						if (part.contains("ACSID")) {
							Constant.authCookie = part;
							return true;
						}
					}
				}
			}
		} catch (Exception e){
			Log.e(TAG, e.getMessage());
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void onPostExecute(Boolean response) {
		if (response == null) {
			Log.e(TAG, "Could not find correct header");
		} else {
			Log.e(TAG, "Done");
		}
	}

}
