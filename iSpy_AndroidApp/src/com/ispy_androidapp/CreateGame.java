package com.ispy_androidapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


public class CreateGame extends Activity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_new_game);
		
		Button submitgame = (Button) findViewById(R.id.submitgame);
		submitgame.setOnClickListener(new View.OnClickListener() {
			 public void onClick(View view) {	
				 final EditText name = (EditText) findViewById(R.id.gamename);
				 final EditText range = (EditText) findViewById(R.id.range);
				 final EditText clue = (EditText) findViewById(R.id.clue);
				 
				  
			
				 Game  newgame = new Game(name.getText().toString(),range.getText().toString(),clue.getText().toString());
				 
				 new CreateGameTask().execute(newgame);
				 
				 Intent myIntent = new Intent(view.getContext(), ViewMessagesActivity.class);
	               startActivityForResult(myIntent, 0);
			 }
	});
	}
	
	
}
