package com.example.putitdown;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;

public class MainActivity extends Activity {

	static private final int GET_TRIP_SUCCESS_CODE = 1;
	// default minimum time between new readings
	private long mMinTime = 5000;
		
	private static final long FIVE_MINS = 5 * 60 * 1000;
	// default minimum distance between old and new readings.
	private float mMinDistance = 1000.0f;
	
	private String GET_CURR_LOCATION = "GET_CURR_LOCATION";
	private String GET_DESTINATION_ADDRESS = "GET_DESTINATION_ADDRESS";
	private String TRIP_SUCCESS = "TRIP_SUCCESS";
	
	private int MAX_RESULTS = 5;
	private int mAddressIndex = 0;
	private List<Address> mAddresses = null;
	private Location mCurrLocation;
	private Location mDestinationLocation;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.activity_main);
		LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		mCurrLocation = locationManager.getLastKnownLocation(locationManager.GPS_PROVIDER);
		Button setDestinationButton = (Button) this.findViewById(R.id.set_destination);
		
		
		setDestinationButton.setOnClickListener(new OnClickListener(){
		    @Override
		    public void onClick(View v) {
		      getDestination();
		      showAddresses();
		    }
		  });
		Button startTripButton = (Button) this.findViewById(R.id.start_trip);
		startTripButton.setOnClickListener(new OnClickListener(){
		    @Override
		    public void onClick(View v) {
		      startTrip();
		    }
		  });
		
	}
	
	private void startTrip(){
		//Address destinationAddress = mAddresses.get(mAddressIndex);
		String destinationAddress = "Hello";
		Intent i = new Intent(MainActivity.this,Trip.class);
		i.putExtra(GET_CURR_LOCATION, mCurrLocation);
		i.putExtra(GET_DESTINATION_ADDRESS,destinationAddress);
		startActivityForResult(i,GET_TRIP_SUCCESS_CODE);
	}
	
	private void getDestination(){
		EditText addressTextField = (EditText) this.findViewById(R.id.destination_address_field);
		String address = addressTextField.getText().toString();
		Geocoder location = new Geocoder(getApplicationContext(), Locale.getDefault());

		try {
			List<Address> myList = location.getFromLocationName(address, MAX_RESULTS);
			if (myList != null && myList.size() > 0){
				mAddresses = myList;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	private void showAddresses(){
		//CharSequence[] items = new CharSequence[mAddresses.size()];
		CharSequence[] items = {"abc","def"};
		/*
		for(int i=0; i < mAddresses.size();i++){
			Address address = mAddresses.get(i);
            String addressText = String.format(
                    "%s, %s, %s",
                    // If there's a street address, add it
                    address.getMaxAddressLineIndex() > 0 ?
                            address.getAddressLine(0) : "",
                    // Locality is usually a city
                    address.getLocality(),
                    // The country of the address
                    address.getCountryName());

			
			items[i] = addressText;
		}*/
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Which is your destination?");
		builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
            	mAddressIndex = id;
            }
		});
		
		
		//LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		AlertDialog dialog = builder.create();
		dialog.show();
		
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		
		// TODO - Process the result only if this method received both a
		// RESULT_OK result code and a recognized request code
		// If so, update the Textview showing the user-entered text.
		if(resultCode == RESULT_OK && requestCode == GET_TRIP_SUCCESS_CODE){
			
		
		}
			
			

	}
	
}
