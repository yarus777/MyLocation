package com.yarus.location;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.yarus.location.models.Coords;

public class CoordsActivity extends ActionBarActivity implements UrlLoadedListener {
	private String uid;
	private TextView latitude;
	private TextView longitude;
	private TextView status;
	private LocationManager locationManager;

	private LocationListener locationListener = new LocationListener() {
		@Override
		public void onLocationChanged(Location location) {
			updateLocation(location);
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
			updateLocation(locationManager.getLastKnownLocation(provider));
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coords);
		status =  (TextView) findViewById(R.id.textView2);
		
		latitude = (TextView) findViewById(R.id.Latitude);
		longitude = (TextView) findViewById(R.id.Longitude);
		
		locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
		uid = getIntent().getExtras().getString("UID");
	}

	@Override
	protected void onResume() {
		super.onResume();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				1000 * 10, 10, locationListener);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 1000 * 10, 10,
				locationListener);
	}

	@Override
	protected void onPause() {
		super.onPause();
		locationManager.removeUpdates(locationListener);
	}

	private void updateLocation(Location location) {
		if (location == null)
			return;
		String provider = location.getProvider();
		if (provider.equals(LocationManager.GPS_PROVIDER)
				|| provider.equals(LocationManager.NETWORK_PROVIDER)) {
			double lat = (double) Math.round(location.getLatitude() * 1000000) / 1000000;
			double lng = (double) Math.round(location.getLongitude() * 1000000) / 1000000;
			Coords coords = new Coords(lat, lng);

			showLocation(coords);
			sendLocationToServer(coords);
		}
	}

	private void sendLocationToServer(Coords coords) {
		try {
			JSONObject obj = new JSONObject();

			JSONObject coord = new JSONObject();
			coord.put("Lattitude", coords.getLattitude());
			coord.put("Longitude", coords.getLongitude());
			
			obj.put("Coords", coord);
			
			SimpleDateFormat formatUTC = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			formatUTC.setTimeZone(TimeZone.getTimeZone("UTC"));
			String date = formatUTC.format(new Date());
			
			obj.put("Time", date);
			obj.put("UID", uid);
			
			new PostRequest(this).execute("http://mylocation.somee.com/geo/set", obj.toString());
		}
		catch(Exception e){
			e.printStackTrace();
			Log.d(MainActivity.TAG, e.toString());
		}
	}

	private void showLocation(Coords coords) {
		latitude.setText(Double.toString(coords.getLattitude()));
		longitude.setText(Double.toString(coords.getLongitude()));
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.coords, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onJsonLoaded(String obj) {
		int rc = -1;
		JSONObject json;
		try {
			json = new JSONObject(obj);
			rc = Integer.parseInt(json.get("RC").toString());
			if (rc == 0) {
				String time = json.getString("Time").replaceAll("\\D", "");
				Log.d(MainActivity.TAG, time);
				Date date = new Date(Long.parseLong(time));
				status.setText("Location was updated at " + date.toString());
			} else if (rc == 1) {
				status.setText("Wrong login/password");
			} else if(rc == 2) {
				status.setText(json.getString("Msg"));
			} else {
				status.setText("Server is not responding");
			}
		} catch (JSONException e) {
			Log.d(MainActivity.TAG, e.toString());
			e.printStackTrace();
		}		
	}
}
