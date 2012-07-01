package com.ispy_androidapp;

import java.util.List;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;

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
	    
	    //Log.e("ViewGamesInArea", mapOverlays.toString());
	    
	    Drawable drawable = this.getResources().getDrawable(R.drawable.ispy_overlay);
	    
	    iSpyOverlay itemizedoverlay = new iSpyOverlay(drawable, this);
	    
	    //GeoPoint point = new GeoPoint(19240000,-99120000);
		//OverlayItem overlay = new OverlayItem(point, "iSpyGame", "Fun Stuff");
		
		//itemizedoverlay.addOverlay(overlay);
		//mapOverlays.add(itemizedoverlay);
	    
	    new FetchGamesTask(itemizedoverlay,mapOverlays).execute();
	    
	}
	
	
	@Override
	protected boolean isRouteDisplayed() {
	    return false;
	}
}
