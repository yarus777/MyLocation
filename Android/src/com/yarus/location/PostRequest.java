package com.yarus.location;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import android.os.AsyncTask;
import android.util.Log;

public class PostRequest extends AsyncTask<String, Void, String> {
	final String TAG = "States";
	private UrlLoadedListener listener;

	public PostRequest(UrlLoadedListener listener) {
		this.listener = listener;
	}

	@Override
	protected String doInBackground(String... args) {

		HttpClient client = new DefaultHttpClient();
		try {
			HttpPost post = new HttpPost(args[0]);

			List<NameValuePair> parms = new ArrayList<NameValuePair>();
			parms.add(new BasicNameValuePair("event", args[1]));
			post.setEntity(new UrlEncodedFormEntity(parms));
			HttpResponse response = client.execute(post);
			String responseText = EntityUtils.toString(response.getEntity());
			return responseText;
		} catch (Exception e) {
			Log.d(TAG, "exception " + e);
			e.printStackTrace();
			return null;
		} finally {
			client.getConnectionManager().shutdown();
		}
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		listener.onJsonLoaded(result);
	}

}
