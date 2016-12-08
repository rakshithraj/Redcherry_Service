package com.app.redcherry.Ulility;


import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import com.app.redcherry.Constants.AppGlobal;
import com.app.redcherry.Interface.LocationInterface;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class DeviceLocation {
	private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in
	// Meters
	private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000;
	private static final int TWO_MINUTES = 1000 * 60 * 2;

	private static LocationManager locationManager;
	private static Location mLocation = null;
	private Context activity;
	private MyLocationListener  locationListener;
	private Service trafficService;
	private boolean isRunning=false;
	private Marker userLocation=null;
	private LocationInterface locationInterface;
	public DeviceLocation(Context activity) {
		this.activity = activity;

	}

	
	public DeviceLocation(Service trafficService) {
		// TODO Auto-generated constructor stub
		this.trafficService = trafficService;
	}


	/**
	 * start location listenerk
	 * @param none
	 * @return none
	 */
	public void start() {
	     if(locationManager==null)
	    	 if(activity==null)
	    		 locationManager = (LocationManager) trafficService
	 				.getSystemService(Context.LOCATION_SERVICE);
	    	 else
	    		 
	 		locationManager = (LocationManager) activity
	 				.getSystemService(Context.LOCATION_SERVICE);
	        try{
	     	   locationListener=new MyLocationListener();

	     	if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) 
	     	
	    	 		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
	    	 				MINIMUM_TIME_BETWEEN_UPDATES,
	    	 				MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,locationListener);
	     	else
	 		locationManager.requestLocationUpdates(
	 				LocationManager.NETWORK_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES,
	 				MINIMUM_DISTANCE_CHANGE_FOR_UPDATES, locationListener);
	     	
	        }catch(Exception ignored){

	        }
	        isRunning=true;
	}
	
	/**
	 * stop location listener
	 * @param none
	 * @return none
	 */
	public void stop() {
		if(locationManager!=null)
		locationManager.removeUpdates(locationListener);
		isRunning=false;
		locationManager=null;
    }
	
	/**
	 * used to check if location listner is running or not
	 * @param none
	 * @return true if running or else false
	 */
	public boolean isRunning() {
		return isRunning;
    }
	
	
	/**
	 * get current location longt and latit
	 * @param none
	 * @return location as Location
	 */
	public Location showCurrentLocation() {
		Location location=null;
		try{
		if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

			locationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, MINIMUM_TIME_BETWEEN_UPDATES,
					MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
					locationListener);
			
			 location = locationManager
					.getLastKnownLocation(LocationManager.GPS_PROVIDER);

		
		}
		else
		if (  locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
			locationManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER,
					MINIMUM_TIME_BETWEEN_UPDATES,
					MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
					locationListener);
			location = locationManager
					.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

		}


		
			if ( location==null && locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
				locationManager.requestLocationUpdates(
						LocationManager.NETWORK_PROVIDER,
						MINIMUM_TIME_BETWEEN_UPDATES,
						MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
						locationListener);
				location = locationManager
						.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

			}

			if(location!=null){

			    if (isBetterLocation(location, mLocation))
				 mLocation = location;
			}

		//Log.d("my_location",""+mLocation.getProvider()+" "+mLocation.getLatitude()+" "+mLocation.getLongitude());
		return mLocation;
		}catch(Exception e){
			Log.d("tag","error ="+ e);
	       }
		Log.d("my_location","null");
		return null;
	}

	public void setOnLocationFoundListner(LocationInterface locationInterface) {
		this.locationInterface=locationInterface;
	}

	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(final Location location) {

			Log.d("my_location","onLocationChanged"+location.getProvider()+" "+location.getLatitude()+" "+location.getLongitude());
			
			new Thread(new Runnable(){

				@Override
				public void run() {
					// TODO Auto-generated method stub
					if(location==null)
						 return;
					if (isBetterLocation(location, mLocation)){
						mLocation = location;
						if(userLocation!=null && activity!=null){
//							activity.runOnUiThread(new Runnable(){
//
//								@Override
//								public void run() {
//									// TODO Auto-generated method stub
//									userLocation.setPosition(new LatLng(mLocation.getLatitude(),mLocation.getLongitude()));
//									if(AppGlobal.isNetwork(activity))
//									userLocation.setTitle(Utility.getAddress(mLocation.getLatitude(), mLocation.getLongitude(),activity));
//								}
//
//							});
						
						
						}
						if(activity!=null){
									if(locationInterface!=null)
									locationInterface.onLocationFound(mLocation);




						}
						
					}
				}
				
				
			}).start();
			
			/*String message = String.format(
					"New Location \n Longitude: %1$s \n Latitude: %2$s \n Provider: %3$s",
					mLocation.getLongitude(), mLocation.getLatitude(), mLocation.getProvider());*/
			
		     

		}

		public void onStatusChanged(String s, int i, Bundle b) {

		}

		public void onProviderDisabled(String s) {

		}

		public void onProviderEnabled(String s) {

		}

	}
	
	/**
	 * get address using latitude nad logtitude
	 * 
	 * @param latitude
	 * @param longitude
	 * @return Address as String
	 */
	/*public String getAddress(double latitude, double longitude) {
		// TODO Auto-generated method stub

		try {
			Geocoder geocoder;
			List<Address> addresses;
			geocoder = new Geocoder(activity, Locale.getDefault());
			addresses = geocoder.getFromLocation(latitude, longitude, 1);

			return addresses.get(0).getAddressLine(0) + ","
					+ addresses.get(0).getAddressLine(1) + ",\n"
					+ addresses.get(0).getAddressLine(2);

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IndexOutOfBoundsException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "";
	}
	*/
	

	/**
	 * compare two location for better location
	 * @param location
	 * @param currentBestLocation
	 * @return location as Location
	 */
	private boolean isBetterLocation(Location location,
									 Location currentBestLocation) {
		if (currentBestLocation == null) {
			// A new location is always better than no location
			return true;
		}

		// Check whether the new location fix is newer or older
		long timeDelta = location.getTime() - currentBestLocation.getTime();
		boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
		boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
		boolean isNewer = timeDelta > 0;

		// If it's been more than two minutes since the current location, use
		// the new location
		// because the user has likely moved
		if (isSignificantlyNewer) {
			return true;
			// If the new location is more than two minutes older, it must be
			// worse
		} else if (isSignificantlyOlder) {
			return false;
		}

		// Check whether the new location fix is more or less accurate
		int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation
				.getAccuracy());
		boolean isLessAccurate = accuracyDelta > 0;
		boolean isMoreAccurate = accuracyDelta < 0;
		boolean isSignificantlyLessAccurate = accuracyDelta > 200;

		// Check if the old and new location are from the same provider
		boolean isFromSameProvider = isSameProvider(location.getProvider(),
				currentBestLocation.getProvider());

		// Determine location quality using a combination of timeliness and
		// accuracy
		if (isMoreAccurate) {
			return true;
		} else if (isNewer && !isLessAccurate) {
			return true;
		} else if (isNewer && !isSignificantlyLessAccurate
				&& isFromSameProvider) {
			return true;
		}
		return false;
	}

	/**
	 * chech if two provider are same
	 * @param provider1
	 * @param provider2
	 * @return true if same else false
	 */
	private boolean isSameProvider(String provider1, String provider2) {
		if (provider1 == null) {
			return provider2 == null;
		}
		return provider1.equals(provider2);
	}


	public void setMarker(Marker userLocation) {
		// TODO Auto-generated method stub
		this.userLocation=userLocation;
	}

	public Marker getMarker() {
		// TODO Auto-generated method stub
		return userLocation;
	}
	
	
}

