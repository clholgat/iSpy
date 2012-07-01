package com.ispy_androidapp;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.accounts.AccountManagerFuture;
import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;


public class AuthTask extends AsyncTask<Account, Void, Boolean> {
	private String TAG = "AuthTask";
	private Activity context;
	
	public AuthTask(Activity context) {
		this.context = context;
	}
		
	@Override
	protected Boolean doInBackground(Account... arg0) {
		try {
			Account account = arg0[0];
			AccountManagerFuture<Bundle> manager = AccountManager.get((Context) context)
					.getAuthToken(account, "ah", null, context, null, null);			
			String authToken = manager.getResult().get(AccountManager.KEY_AUTHTOKEN).toString();
			
			
			HttpClient client = new DefaultHttpClient();
			HttpParams params = new BasicHttpParams();
			params.setParameter("http.protocol.handle-redirects",false);
			HttpGet get = new HttpGet(Constant.server+"/_ah/login?continue=www.google.com&auth="+authToken);
			get.setParams(params);
			
			get.addHeader("Cookie", Constant.authCookie);
			HttpResponse response = client.execute(get);		
			
			if (response.getStatusLine().getStatusCode() != 302) {
				Log.e(TAG, "could not authenticate "+response.getStatusLine().getStatusCode());
				// return false;
			}
			
			for (Header header : response.getAllHeaders()){
				Log.e(TAG, header.getName()+" "+header.getValue());
				if ("Set-Cookie".equals(header.getName())) {
					String[] parts = header.getValue().split(";");
					for (String part : parts) {
						String[] start = part.split("=");
						if (start[0].equals("ACSID")) {
							Log.e(TAG, part);
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
	    	new RegisterTask().execute(context);
		}
		
	}

}
