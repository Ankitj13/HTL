package info.sss.htl;

import info.androidhive.htl.R;
import info.sss.htl.adapter.FeedListAdapter;
import info.sss.htl.data.FeedItem;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class PagesFragment extends Fragment {
	int cateID;
	private ListView listView;
	private FeedListAdapter listAdapter;
	private List<FeedItem> feedItems;
	public PagesFragment(int ID){cateID=ID;}
	private TextView Message;
	View rootView;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		getActivity().getActionBar().show();
        rootView = inflater.inflate(R.layout.fragment_pages, container, false);
         
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        listView = (ListView) rootView.findViewById(R.id.postByCategoryview);

		feedItems = new ArrayList<FeedItem>();

		listAdapter = new FeedListAdapter(getActivity(), feedItems , cateID);
		Message =(TextView) rootView.findViewById(R.id.Message);
		Message.setText("There is no post related to that category.");
		listView.setAdapter(listAdapter);
		InternetOprations netcheck = new InternetOprations(getActivity());

		// Check if Internet present
		if (!netcheck.isConnectingToInternet()) {
			// Internet Connection is not present
			Toast.makeText(getActivity(), "No Connection! Try Again.", Toast.LENGTH_LONG).show();// stop executing code by return
		}else{
		new GetfeedsByCategory().execute();
		}
		 return rootView;
    }
	
	/**
	 * Background Async Task to Get complete product details
	 * */
	class GetfeedsByCategory extends AsyncTask<String, String, String> {

		private ProgressDialog pDialog;
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(getActivity());
			pDialog.setMessage("loading...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			Log.i("show", "dialog");
			pDialog.show();
		}

		/**
		 * Getting product details in background thread
		 * */
		protected String doInBackground(String... params) {

			// updating UI from Background Thread
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					UserFunctions uf = new UserFunctions();
			 		JSONObject response = uf.getPostBycateID(String.valueOf(cateID));
			 		try {
			 			
						JSONArray feedArray = response.getJSONArray("PostsByCateID");
						
						for (int i = 0; i < feedArray.length(); i++) {
							JSONObject feedObj = (JSONObject) feedArray.get(i);
							
							if(feedObj.getInt("success")==1)
							{
							FeedItem item = new FeedItem();
							item.setId(feedObj.getInt("id"));
							item.setName(feedObj.getString("username"));
							item.setCategory(feedObj.getString("category_name"));
							item.setStatus(feedObj.getString("postContent"));
							item.setTimeStamp(feedObj.getString("postTimeStamp"));
							item.setNoOflike1(feedObj.getInt("NoOfLike1"));
							item.setNoOflike2(feedObj.getInt("NoOfLike2"));
							item.setBackground(feedObj.getString("postBG"));
							item.setCommentCount(feedObj.getInt("commentCount"));
							feedItems.add(item);
							}
							else
							{
							Message.setVisibility(rootView.VISIBLE);
							}
							}

					}
			 		catch (JSONException e) {
						e.printStackTrace();
					}
					
				}
			});

			return null;
		}

		/**
		 * After completing background task Dismiss the progress dialog
		 * **/
		protected void onPostExecute(String file_url) {
			// dismiss the dialog once got all details
			// notify data changes to list adapater
			listAdapter.notifyDataSetChanged();
			pDialog.dismiss();
		}
	}
	
}
