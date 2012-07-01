package com.ispy_androidapp;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.util.LruCache;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageAdapter extends ArrayAdapter<Message> {
	private String TAG = "MessageAdapter";
	
	private List<Message> messages;
	private Context context;
	private int resId;
	
	public MessageAdapter(Context ctx,
			int textViewResourceId, List<Message> objects) {
		super(ctx, textViewResourceId, objects);
		messages = objects;
		context = ctx;
		resId = textViewResourceId;
		
	}
	
	@Override 
	public View getView(int position, View row, ViewGroup parent) {
		MessageHolder holder;
		
		if (row == null) {
			LayoutInflater inflater = ((Activity) context).getLayoutInflater();
			row = inflater.inflate(resId, parent, false);
			holder = new MessageHolder();
			holder.userName = (TextView) row.findViewById(R.id.user_name);
			holder.messageImage = (ImageView) row.findViewById(R.id.message_image);
			holder.messageText = (TextView) row.findViewById(R.id.message_text);
			holder.right = (Button) row.findViewById(R.id.right);
			holder.wrong = (Button) row.findViewById(R.id.wrong);
			
			row.setTag(holder);
		} else {
			holder = (MessageHolder) row.getTag();
		}
		
		final Message message = messages.get(position);
		holder.userName.setText(message.username);
		holder.messageImage.setTag(message.img);
		
		if(message.img != null) {
			byte[] bytes = Base64.decode(message.img, Base64.DEFAULT);
			Bitmap b = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			holder.messageImage.setImageBitmap(b);
		}	
		holder.messageText.setText(message.text);
		holder.right.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new ConfirmDenyTask(message.messageId).execute(true);
			}
		});
		holder.wrong.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new ConfirmDenyTask(message.messageId).execute(false);
			}
		});
		
		if (!Constant.creator) {
			holder.right.setVisibility(View.GONE);
			holder.wrong.setVisibility(View.GONE);
		}
		
		return row;
	}
	
	
	private class MessageHolder {
		TextView userName;
		ImageView messageImage;
		TextView messageText;
		Button right;
		Button wrong;
	}
	

}
