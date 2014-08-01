package com.example.putitdown;

import com.google.android.gms.location.Geofence;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class Trip extends Activity {
	private String GET_CURR_LOCATION = "GET_CURR_LOCATION";
	private String GET_DESTINATION_ADDRESS = "GET_DESTINATION_ADDRESS";
	private String TRIP_SUCCESS = "TRIP_SUCCESS";
	private boolean mTripIsSuccessful;
	private Geofence mGeofence;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.trip);
			
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
		}

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
		}

		@Override
		public void onDestroy() {
			super.onDestroy();
		}

		@Override
		public void onSaveInstanceState(Bundle savedInstanceState) {

			/*// TODO:
			// Save counter state information with a collection of key-value pairs
			// 4 lines of code, one for every count variable
			savedInstanceState.putInt(CREATE_KEY,onCreateCounter);
			savedInstanceState.putInt(START_KEY,onStartCounter);
			savedInstanceState.putInt(RESUME_KEY,onResumeCounter);
			savedInstanceState.putInt(RESTART_KEY,onRestartCounter);	*/
		}
	}


