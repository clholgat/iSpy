package com.ispy_androidapp;

import java.util.List;

import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

public class ViewGamesInArea extends MapActivity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_games_in_area);
		
		MapView mapView = (MapView) findViewById(R.id.mapview);
	    mapView.setBuiltInZoomControls(true);
	    
	    /*
	     * 1. Get the list of games which are in the vicinity of the user
	     * 2. Display the games on map view
	     * 3. On selecting a game, bring up the game view 
	     */
	    
	    List<Overlay> mapOverlays = mapView.getOverlays();
	    Drawable drawable = this.getResources().getDrawable(R.drawable.ispy_overlay);
	    
	    iSpyOverlay itemizedoverlay = new iSpyOverlay(drawable, this);
	    
	    
	    
	}
	
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
}
