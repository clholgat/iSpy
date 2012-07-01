package com.ispy_androidapp;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class ViewMessagesActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.messages);
		
		ListView listView = (ListView) findViewById(R.id.message_list);
		List<Message> messages = new ArrayList<Message>();
		
		MessageAdapter adapter = new MessageAdapter(this, R.layout.message, messages);
		
		listView.setAdapter(adapter);
		
		new GetMessagesTask(messages, adapter).execute(Constant.gameId);
			
	}
}
