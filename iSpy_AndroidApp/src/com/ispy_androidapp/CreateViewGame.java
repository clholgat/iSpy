package com.ispy_androidapp;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CreateViewGame extends Activity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.create_view_game);
		
		
		/*
		 * Listeners for Create Game and View Game 
		 */
		Button creategame = (Button) findViewById(R.id.creategame);
		creategame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
            	
            	
            	
            	}
            });
		
		Button viewgame = (Button) findViewById(R.id.viewgame);
		viewgame.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
	
            	}
            });

	}
}
