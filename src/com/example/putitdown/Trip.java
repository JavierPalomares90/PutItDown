package com.example.putitdown;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.location.Geofence;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Address;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import com.example.putitdown.GeofenceUtils.REMOVE_TYPE;
import com.example.putitdown.GeofenceUtils.REQUEST_TYPE;

public class Trip extends Activity {
	private String GET_CURR_LOCATION = "GET_CURR_LOCATION";
	private String GET_DESTINATION_ADDRESS = "GET_DESTINATION_ADDRESS";
	private String TRIP_SUCCESS = "TRIP_SUCCESS";
	private boolean mTripIsSuccessful;
	private float RADIUS = 20; // In meters
	
    private static final long GEOFENCE_EXPIRATION_IN_HOURS = 12;
    private static final long GEOFENCE_EXPIRATION_IN_MILLISECONDS =
            GEOFENCE_EXPIRATION_IN_HOURS * DateUtils.HOUR_IN_MILLIS;
    
    // Store a list of geofences to add
    List<Geofence> mCurrentGeofences;

    /*
     * An instance of an inner class that receives broadcasts from listeners and from the
     * IntentService that receives geofence transition events
     */
    private GeofenceSampleReceiver mBroadcastReceiver;

    
    private GeofenceRequester mGeofenceRequester;

    // An intent filter for the broadcast receiver
    private IntentFilter mIntentFilter;

    // Persistent storage for geofences
    private SimpleGeofenceStore mPrefs;
	
	
	private SimpleGeofence mUIGeofence1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip);
        // Action for broadcast Intents that report successful addition of geofences
		mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_ADDED);

        // Action for broadcast Intents that report successful removal of geofences
        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCES_REMOVED);

        // Action for broadcast Intents containing various types of geofencing errors
        mIntentFilter.addAction(GeofenceUtils.ACTION_GEOFENCE_ERROR);

        // All Location Services sample apps use this category
        mIntentFilter.addCategory(GeofenceUtils.CATEGORY_LOCATION_SERVICES);

        // Instantiate a new geofence storage area
        mPrefs = new SimpleGeofenceStore(this);
		  
        Bundle extras = getIntent().getExtras(); 
        Address destinationAddress = (Address)extras.get(GET_DESTINATION_ADDRESS);
        mUIGeofence1 = new SimpleGeofence(
                "1",
                destinationAddress.getLatitude(),
                destinationAddress.getLongitude(),
                RADIUS,
                // Set the expiration time
                GEOFENCE_EXPIRATION_IN_MILLISECONDS,
                // Only detect entry transitions
                Geofence.GEOFENCE_TRANSITION_ENTER);

            // Store this flat version in SharedPreferences
        mPrefs.setGeofence("1", mUIGeofence1);
        mCurrentGeofences = new ArrayList<Geofence>();
        mCurrentGeofences.add(mUIGeofence1.toGeofence());
		

        // Start the request. Fail if there's already a request in progress
        try {
            // Try to add geofences
            mGeofenceRequester.addGeofences(mCurrentGeofences);
        } catch (UnsupportedOperationException e) {
            // Notify user that previous request hasn't finished.
            Toast.makeText(this, R.string.add_geofences_already_requested_error,
                        Toast.LENGTH_LONG).show();
        }

        
		Button stopTripButton = (Button) findViewById(R.id.stop_trip); 
		stopTripButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// User hit the stop button, trip is failure
				mTripIsSuccessful = false;
				Intent i = new Intent();
				i.putExtra(TRIP_SUCCESS, mTripIsSuccessful);
				setResult(RESULT_OK,i);
				
				if (getParent() == null) {
				    setResult(Activity.RESULT_OK, i);
				} else {
				    getParent().setResult(Activity.RESULT_OK, i);
				}
				finish();
			}
		});	
	
	}

		// Lifecycle callback methods overrides

		@Override
		public void onStart() {
			super.onStart();
			LocalBroadcastManager.getInstance(this).registerReceiver(mBroadcastReceiver, mIntentFilter);
		}
		/*
		@Override
		public void onResume() {
			super.onResume();		
		}

		@Override
		public void onPause() {
			super.onPause();
		}

		@Override
		public void onStop() {
			super.onStop();
		}

		@Override
		public void onRestart() {
			super.onRestart();
		}*/

		@Override
		public void onDestroy() {
			super.onDestroy();
			LocalBroadcastManager.getInstance(this).unregisterReceiver(mBroadcastReceiver);
		}
		/**
	     * Define a Broadcast receiver that receives updates from connection listeners and
	     * the geofence transition service.
	     */
	    public class GeofenceSampleReceiver extends BroadcastReceiver {
	        /*
	         * Define the required method for broadcast receivers
	         * This method is invoked when a broadcast Intent triggers the receiver
	         */
	        @Override
	        public void onReceive(Context context, Intent intent) {

	            // Check the action code and determine what to do
	            String action = intent.getAction();

	            // Intent contains information about errors in adding or removing geofences
	            if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_ERROR)) {

	                handleGeofenceError(context, intent);

	            // Intent contains information about successful addition or removal of geofences
	            } else if (
	                    TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_ADDED)
	                    ||
	                    TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCES_REMOVED)) {

	                handleGeofenceStatus(context, intent);

	            // Intent contains information about a geofence transition
	            } else if (TextUtils.equals(action, GeofenceUtils.ACTION_GEOFENCE_TRANSITION)) {

	                handleGeofenceTransition(context, intent);

	            // The Intent contained an invalid action
	            } else {
	                Log.e(GeofenceUtils.APPTAG, getString(R.string.invalid_action_detail, action));
	                Toast.makeText(context, R.string.invalid_action, Toast.LENGTH_LONG).show();
	            }
	        }

	        /**
	         * If you want to display a UI message about adding or removing geofences, put it here.
	         *
	         * @param context A Context for this component
	         * @param intent The received broadcast Intent
	         */
	        private void handleGeofenceStatus(Context context, Intent intent) {

	        }

	        /**
	         * Report geofence transitions to the UI
	         *
	         * @param context A Context for this component
	         * @param intent The Intent containing the transition
	         */
	        private void handleGeofenceTransition(Context context, Intent intent) {
	            /*
	             * If you want to change the UI when a transition occurs, put the code
	             * here. The current design of the app uses a notification to inform the
	             * user that a transition has occurred.
	             */
	            mTripIsSuccessful = true;
				Intent i = new Intent();
				i.putExtra(TRIP_SUCCESS, mTripIsSuccessful);
				getParent().setResult(Activity.RESULT_OK, i);
				/*
				if (getParent() == null) {
				    setResult(Activity.RESULT_OK, i);
				} else {
				    
				}*/
				finish(); 
	        	
	        	
	        }

	        /**
	         * Report addition or removal errors to the UI, using a Toast
	         *
	         * @param intent A broadcast Intent sent by ReceiveTransitionsIntentService
	         */
	        private void handleGeofenceError(Context context, Intent intent) {
	            String msg = intent.getStringExtra(GeofenceUtils.EXTRA_GEOFENCE_STATUS);
	            Log.e(GeofenceUtils.APPTAG, msg);
	            Toast.makeText(context, msg, Toast.LENGTH_LONG).show();
	        }
	    }



	}


