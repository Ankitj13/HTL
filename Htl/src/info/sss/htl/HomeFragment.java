package info.sss.htl;


import info.androidhive.htl.R;
import info.sss.htl.adapter.FeedListAdapter;
import info.sss.htl.app.AppController;
import info.sss.htl.data.FeedItem;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

public class HomeFragment extends Fragment {
	private static final String TAG = MainActivity.class.getSimpleName();
	private ListView listView;
	public static FeedListAdapter listAdapter;
	public static List<FeedItem> feedItems;
	private String URL_FEED = "http://ssism.org/api/getposts.php";//http://ssism.org/api/getposts.php;
	public HomeFragment(){}
	int val;
	private ProgressDialog pDialog1;
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
           Bundle savedInstanceState) {
		getActivity().getActionBar().show();
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        listView = (ListView) rootView.findViewById(info.androidhive.htl.R.id.list_view);

		feedItems = new ArrayList<FeedItem>();

		listAdapter = new FeedListAdapter(getActivity(), feedItems);
		
		listView.setAdapter(listAdapter);

		// These two lines not needed,
		// just to get the look of facebook (changing background color & hiding the icon)
		getActivity().getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3c8dbc")));
		getActivity().setTitleColor(R.color.white);
		/*final SwipeRefreshLayout swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipe_container);
		 
		swipeLayout.setColorScheme(android.R.color.holo_blue_bright,
				android.R.color.holo_green_light,
				android.R.color.holo_orange_light,
				android.R.color.holo_red_light);
 
		swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
					@Override
					public void onRefresh() {
						new Handler().postDelayed(new Runnable() {
							@Override
							public void run() {
								swipeLayout.setRefreshing(false);
								AppController.getInstance().getRequestQueue().getCache().clear();
								// making fresh volley request and getting json
								JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
										URL_FEED, null, new Response.Listener<JSONObject>() {

											@Override
											public void onResponse(JSONObject response) {
												Log.i("responce", "that one is there");
												VolleyLog.d(TAG, "Response: " + response.toString());
												if (response != null) {
													parseJsonFeed(response);
													// notify data changes to list adapater
													
													listAdapter.notifyDataSetChanged();
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
						}, 5000);
						
					}
				});
		listView.setOnScrollListener(new AbsListView.OnScrollListener() {
		        @Override
		        public void onScrollStateChanged(AbsListView absListView, int i) {
		        	absListView.setTop(i);
		        }
		 
		        @Override
		        public void onScroll(AbsListView absListView, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		        	absListView.setTop(firstVisibleItem);
		        	if (firstVisibleItem == 0)
		                	swipeLayout.setEnabled(true);
		                else
		                	swipeLayout.setEnabled(false);
		        }
		    });*/
		
		//getActionBar().setIcon(
		//		   new ColorDrawable(getResources().getColor(android.R.color.transparent)));
       // We first check for cached request
		/*Cache cache = AppController.getInstance().getRequestQueue().getCache();
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

		} else {*/
			
			// making fresh volley request and getting json
			JsonObjectRequest jsonReq = new JsonObjectRequest(Method.GET,
					URL_FEED, null, new Response.Listener<JSONObject>() {

						@Override
						public void onResponse(JSONObject response) {
							Log.i("responce", "that one is there");
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
		//}
		
        return rootView;
    }
	
	/**
	 * Parsing json reponse and passing the data to feed view list adapter
	 * */
	private void parseJsonFeed(JSONObject response) {
		pDialog1 = new ProgressDialog(getActivity());
		pDialog1.setMessage("Loading...");
		pDialog1.setIndeterminate(true);
		pDialog1.setCancelable(true);
		pDialog1.show();
		try {
			JSONArray feedArray = response.getJSONArray("feed");

			Log.i("json", feedArray.toString());
			
			for (int i = 0; i < feedArray.length(); i++) {
				JSONObject feedObj = (JSONObject) feedArray.get(i);

				FeedItem item = new FeedItem();
				item.setId(feedObj.getInt("id"));
				item.setName(feedObj.getString("username"));
				item.setCategory(feedObj.getString("category_name"));
				item.setTimeStamp(feedObj.getString("postTimeStamp"));
				item.setStatus(feedObj.getString("postContent"));
				item.setNoOflike1(feedObj.getInt("NoOfLike1"));
				item.setNoOflike2(feedObj.getInt("NoOfLike2"));
				item.setBackground(feedObj.getString("postBG"));
				item.setCommentCount(feedObj.getInt("commentCount"));
				feedItems.add(item);	
			}
			// notify data changes to list adapater
			listAdapter.notifyDataSetChanged();
			Log.i(String.valueOf(listAdapter.getCount()),"count");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		pDialog1.dismiss();
	}
	
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.i("Inside resu", "Hello");
	}
	
}
