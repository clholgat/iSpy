package com.ispy_androidapp;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

public class GCMIntentService extends IntentService {
	private static final int MESSAGE_ID = 1;
	
	public GCMIntentService(String name) {
		super(name);
		// TODO Auto-generated constructor stub
	}
	
	public GCMIntentService() {
		super("GCMIntentService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub

	}
	
	protected void onRegistered(Context context, String regId){
		Constant.registrationId = regId;
	}
	
	protected void onUnRegistered(Context context, String regId) {
		// Getting to this later
	}
	
	protected void onMessage(Context context, Intent intent) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager manager = (NotificationManager) context.getSystemService(ns);
		int icon = R.drawable.ispy;
		CharSequence tickerText = "New Message";
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		
		CharSequence title = "iSpy New Message";
		CharSequence text = "You have a message waiting in iSpy";
		Intent notificationIntent = new Intent(this, ViewMessagesActivity.class);
		PendingIntent pending = PendingIntent.getActivity(this, 0, notificationIntent, 0);
		
		notification.setLatestEventInfo(context, title, text, pending);
		manager.notify(MESSAGE_ID, notification);
	}
	
	protected void onError(Context context, String errorId) {
		
	}
	
	protected void onRecoverableError(Context context, String errorId) {
		
	}

}
