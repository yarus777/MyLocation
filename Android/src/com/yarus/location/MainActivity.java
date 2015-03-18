package com.yarus.location;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends ActionBarActivity implements
		UrlLoadedListener {

	private EditText userEdit;
	private EditText pwdEdit;
	private TextView status;
	public final static String TAG = "States";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		userEdit = (EditText) findViewById(R.id.editText1);
		pwdEdit = (EditText) findViewById(R.id.editText2);
		status = (TextView) findViewById(R.id.textView3);
	}

	public void onRegisterClick(View view) {

	}

	public void onLoginClick(View view) {
		JSONObject userJson = new JSONObject();
		try {
			userJson.put("username", userEdit.getText().toString());
			userJson.put("passHash", pwdEdit.getText().toString());
		} catch (JSONException e) {
			e.printStackTrace();
		}

		new PostRequest(this).execute("http://mylocation.somee.com/auth/login",
				userJson.toString());
	}

	public void onCoordsActivityClick(View view) {

	}

	@Override
	public void onJsonLoaded(String obj) {
		int rc = -1;
		JSONObject json;
		try {
			json = new JSONObject(obj);
			rc = Integer.parseInt(json.get("RC").toString());
			if (rc == 0) {
				String uid = json.get("UID").toString();
				Intent intent = new Intent(this, CoordsActivity.class);
				intent.putExtra("UID", uid);
				startActivity(intent);
			} else if (rc == 1) {
				status.setText("Wrong login/password");
			} else if(rc == 2) {
				status.setText(json.getString("Msg"));
			} else {
				status.setText("Server is not responding");
			}
		} catch (JSONException e) {
			Log.d(TAG, e.toString());
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
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
}
