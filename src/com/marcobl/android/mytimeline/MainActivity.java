package com.marcobl.android.mytimeline;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {

	private ListView listaTweet;
	private TweetListAdapter adapter;
	private JSONArray array;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		listaTweet = (ListView) findViewById(R.id.listTweet);
		
		new GetTimelineTask().execute();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	public static JSONArray getJSONobjectFromURL(String url) throws IOException {

		try {

			// Building the request

			AndroidHttpClient httpClient = AndroidHttpClient
					.newInstance("Android");

			URI uri = new URI(url);

			HttpGet getRequest = new HttpGet();

			getRequest.setURI(uri);

			// getting the response

			HttpResponse httpResponse = httpClient.execute(getRequest);

			final int statusCode = httpResponse.getStatusLine().getStatusCode();

			// check the response if it's ok

			if (statusCode != HttpStatus.SC_OK) {

				httpClient.close();

				return new JSONArray();

			}

			final HttpEntity entity = httpResponse.getEntity();

			String data = EntityUtils.toString(entity, HTTP.UTF_8);

			entity.consumeContent();

			httpClient.close();

			return new JSONArray(data);

		} catch (URISyntaxException e) {

			e.printStackTrace();

			throw new IOException("Error internet connection");

		} catch (ClientProtocolException e) {

			e.printStackTrace();

			throw new ClientProtocolException("Protocol error");

		} catch (IOException e) {

			e.printStackTrace();

			throw new IOException("Error IO" + e.getMessage());

		} catch (JSONException e) {

			e.printStackTrace();

			throw new IOException("JSON Error");

		}

	}

	public class GetTimelineTask extends AsyncTask<Void, Void, Boolean> {

		private ProgressDialog dialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(MainActivity.this);
			dialog.setMessage("Obteniendo Timeline");
			dialog.setIndeterminate(true);
			dialog.setCancelable(false);
			dialog.show();

		}

		@Override
		protected Boolean doInBackground(Void... params) {

			Boolean error = false;
			try {
				array = getJSONobjectFromURL("http://orleonsoft.com/apps/twitter/app.php");
				if (array!=null) {
					adapter = new TweetListAdapter(array, getLayoutInflater());
					
					
				} else {
					error = true;
				}
			} catch (IOException e) {
				error = true;
				e.printStackTrace();
			}
			
			return error;
		}

		@Override
		protected void onPostExecute(Boolean error) {
			// TODO Auto-generated method stub
			super.onPostExecute(error);
			dialog.dismiss();
			if (error) {
				Toast.makeText(MainActivity.this, "Error obteniendo el Timeline", Toast.LENGTH_SHORT);
				
			}else{
				listaTweet.setAdapter(adapter);
			}
		}
	}

}
