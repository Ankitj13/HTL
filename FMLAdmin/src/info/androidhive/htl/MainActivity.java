package info.androidhive.htl;

import info.androidhive.htl.adapter.FeedListAdapter;
import info.androidhive.htl.app.AppController;
import info.androidhive.htl.data.FeedItem;
import info.androidhive.listviewfeed.R;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

public class MainActivity extends Activity {
	private static final String TAG = MainActivity.class.getSimpleName();
	private ListView listView;
	private FeedListAdapter listAdapter;
	private List<FeedItem> feedItems;
	//private String URL_FEED = "http://api.androidhive.info/feed/feed.json";
	private String URL_FEED = "http://ssism.org/api/adminapi/getpostsForAdmin.php";
	private int rowcount;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		DatabaseHandler dbhndlr = new DatabaseHandler(MainActivity.this);
        rowcount = dbhndlr.getRowCount();
        
		listView = (ListView) findViewById(R.id.list);

		feedItems = new ArrayList<FeedItem>();

		listAdapter = new FeedListAdapter(this, feedItems);
		listView.setAdapter(listAdapter);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		// These two lines not needed,
		// just to get the look of facebook (changing background color & hiding the icon)
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3c8dbc")));
		getActionBar().setTitle("Hi to life");
		// We first check for cached request
		Cache cache = AppController.getInstance().getRequestQueue().getCache();
		Entry entry = cache.get(URL_FEED);
		if (entry != null) {
			// fetch the data from cache
			try {
				String data = new String(entry.data, "UTF-8");
				try {
					parseJsonFeed(new JSONObject(data));
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		} else {
			// making fresh volley request and getting json
			JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
					URL_FEED, null, new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							VolleyLog.d(TAG, "Response: " + response.toString());
							if (response != null) {
								parseJsonFeed(response);
							}
						}
					}, new Response.ErrorListener() {

						@Override
						public void onErrorResponse(VolleyError error) {
							VolleyLog.d(TAG, "Error: " + error.getMessage());
						}
					});

			// Adding request to volley request queue
			AppController.getInstance().addToRequestQueue(jsonReq);
		}

	}

	/**
	 * Parsing json reponse and passing the data to feed view list adapter
	 * */
	private void parseJsonFeed(JSONObject response) {
		try {
			JSONArray feedArray = response.getJSONArray("feed");
			//Log.i("values",feedArray.toString());
			for (int i = 0; i < feedArray.length(); i++) {
				JSONObject feedObj = (JSONObject) feedArray.get(i);
				
				FeedItem item = new FeedItem();
				item.setId(feedObj.getInt("id"));
				item.setName(feedObj.getString("username"));
				item.setCategory(feedObj.getString("category_name"));
				item.setStatus(feedObj.getString("postContent"));
				item.setTimeStamp(feedObj.getString("postTimeStamp"));
				item.setpostStatus(feedObj.getString("postStatus"));
				item.setNoOflike1(feedObj.getInt("NoOfLike1"));
				item.setNoOflike2(feedObj.getInt("NoOfLike2"));
				item.setBackground(feedObj.getString("postBG"));
				item.setCommentCount(feedObj.getInt("commentCount"));
				feedItems.add(item);
			}

			// notify data changes to list adapater
			listAdapter.notifyDataSetChanged();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		
		MenuItem logout = menu.findItem(R.id.action_logout);
		if(rowcount!=0){
			
			logout.setVisible(true);
		}
		else{
			logout.setVisible(false);
		}
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		
		// Handle action bar actions click
		switch (item.getItemId()) {
		   
		case R.id.action_logout:
			UserFunctions logout = new UserFunctions();
			Boolean check = logout.logoutUser(getApplicationContext());
			if(check.equals(true))
			{
				Intent LogoutIntent = new Intent(MainActivity.this,LoginActivity.class);
	            startActivity(LogoutIntent);
				Toast.makeText(this, "You are signout successfully.", Toast.LENGTH_LONG).show();
			}
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
