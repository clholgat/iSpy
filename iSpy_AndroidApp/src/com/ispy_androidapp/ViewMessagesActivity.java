package com.ispy_androidapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

public class ViewMessagesActivity extends Activity {
	
	private Bitmap bitmap = null;
	private EditText text = null;
	private ImageButton attach = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages);
		
		ListView listView = (ListView) findViewById(R.id.message_list);
		final List<Message> messages = new ArrayList<Message>();
		
		final MessageAdapter adapter = new MessageAdapter(this, R.layout.message, messages);
		
		listView.setAdapter(adapter);
		
		new GetMessagesTask(messages, adapter).execute(Constant.gameId);
		
		attach = (ImageButton) findViewById(R.id.attach_button);
		attach.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
			    startActivityForResult(takePictureIntent, 1);
			}
		});
		
		Button send = (Button) findViewById(R.id.send);
		send.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				new SendMessageTask(text.getText().toString(), bitmap).execute();
				new GetMessagesTask(messages, adapter).execute(Constant.gameId);
				text.clearComposingText();
				text.setText("");
			}
		});
		
		text = (EditText) findViewById(R.id.input_text);
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		bitmap = (Bitmap) data.getExtras().get("data");
		Drawable drawable = new BitmapDrawable(getResources(), bitmap);
		text.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null);
		attach.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				// do nothing				
			}
		});
	}
	

}
