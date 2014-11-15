package info.sss.htl.adapter;

import info.androidhive.htl.R;
import info.sss.htl.DatabaseHandler;
import info.sss.htl.HomeFragment;
import info.sss.htl.InternetOprations;
import info.sss.htl.MainActivity;
import info.sss.htl.PagesFragment;
import info.sss.htl.PhotosFragment;
import info.sss.htl.UserFunctions;
import info.sss.htl.app.AppController;
import info.sss.htl.data.FeedItem;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.color;
import android.app.Activity;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.toolbox.ImageLoader;

public class FeedListAdapter extends BaseAdapter {	
	
	public static final String CategoryId="com.example.htl.MESSAGE";
	private Activity activity;
	private LayoutInflater inflater;
	private List<FeedItem> feedItems;
	private UserFunctions UF = new UserFunctions();
	private TextView like1count,like2count;
	private String bg=null;
	private String user_id="1";
	private int picId , cateid,pos ;
	private ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	protected String postid;
	protected String likeId;
	private TextView commentcount;
	
	public FeedListAdapter(Activity activity, List<FeedItem> feedItems) {
		this.activity = activity;
		this.feedItems = feedItems;
		
	}
	public FeedListAdapter(Activity activity, List<FeedItem> feedItems, int cateid) {
		this.activity = activity;
		this.feedItems = feedItems;
		this.cateid = cateid;
		
	}
	public List<FeedItem> getAllValues() {
	    return feedItems;
	}
	@Override
	public int getCount() {
		return feedItems.size();
	}

	@Override
	public Object getItem(int location) {
		return feedItems.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		pos = position;
		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.feed_item, null);

		if (imageLoader == null)
		imageLoader = AppController.getInstance().getImageLoader();
		TextView category_name = (TextView) convertView.findViewById(R.id.category_name);
		TextView name = (TextView) convertView.findViewById(R.id.feedname);
		TextView timestamp = (TextView) convertView.findViewById(R.id.feedtimestamp);
		TextView statusMsg = (TextView) convertView.findViewById(R.id.feedtxtStatusMsg);
		
		like1count = (TextView) convertView.findViewById(R.id.like1Count);
		like2count = (TextView) convertView.findViewById(R.id.like2Count);
		commentcount = (TextView) convertView.findViewById(R.id.commentCount);
		//TextView url = (TextView) convertView.findViewById(R.id.feedtxtUrl);
		//NetworkImageView profilePic = (NetworkImageView) convertView.findViewById(R.id.feedprofilePic);
		//FeedImageView feedImageView = (FeedImageView) convertView.findViewById(R.id.feedImage1);
		
		//Image icon buttons  
		final ImageButton ShowComment=(ImageButton) convertView.findViewById(R.id.homecomments);
		ShowComment.setImageResource(R.drawable.comment_icon);
		ShowComment.setTag("Default");
		final ImageButton onclicklike1 = (ImageButton) convertView.findViewById(R.id.like1);
		onclicklike1.setTag("one");
		onclicklike1.setImageResource(R.drawable.like1);
		final ImageButton onclicklike2 = (ImageButton) convertView.findViewById(R.id.like2);
		onclicklike2.setTag("Two");
		onclicklike2.setImageResource(R.drawable.like2);
		
		
		final FeedItem item = feedItems.get(position);
		String nameCaps = Character.toUpperCase(item.getName().charAt(0)) + item.getName().substring(1);
		name.setText(nameCaps);
		category_name.setText(item.getCategory()+ " by ");
		timestamp.setText(item.getTimeStamp());
		//item.getNoOflike1();
		//item.getNoOflike2();
		like1count.setText(String.valueOf(item.getNoOflike1()));
		like2count.setText(String.valueOf(item.getNoOflike2()));
		commentcount.setText(String.valueOf(item.getCommentCount()));
		// Chcek for empty status message
		if (!TextUtils.isEmpty(item.getStatus())) {
			statusMsg.setText(item.getStatus());
			statusMsg.setVisibility(View.VISIBLE);
		} else {
			// status is empty, remove from view
			statusMsg.setVisibility(View.GONE);
		}
		
		//set background of post 
		bg = item.getBackground();
		
		if(!bg.equals("0"))
		{	bg = bg.substring(0, bg.lastIndexOf('.'));
			picId = activity.getResources().getIdentifier(bg, "drawable", "info.androidhive.htl");
			statusMsg.setBackgroundResource(picId);
			statusMsg.setTextColor(Color.WHITE);
		}
		else
		{
		statusMsg.setTextColor(Color.BLACK);
		statusMsg.setBackgroundColor(color.white);
		}
		
		ShowComment.setOnClickListener(new OnClickListener() {
			
			private ProgressDialog pDialog1;

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InternetOprations checkIPO = new InternetOprations(activity);
				if(checkIPO.isConnectingToInternet()){
				ProgressDialog pDialog1=ProgressDialog.show(activity,"Loading feeds","Please Wait...",true);
				activity.runOnUiThread(new Runnable() {
					public void run() {
						Log.i("click"," comment yahi he kya??");
						if(ShowComment.getTag().equals("Default")){
							ShowComment.setImageResource(R.drawable.comment_icon_click);
							ShowComment.setTag("Default");
						}
						PhotosFragment PF = new PhotosFragment();
					    android.app.FragmentManager fragmentManager = activity.getFragmentManager();
					    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
					    Bundle args = new Bundle();
					    args.putInt("CID", item.getId());
					    PF.setArguments(args);
					    fragmentTransaction.replace(R.id.frame_container,PF);
					    fragmentTransaction.addToBackStack(null);
					    fragmentTransaction.commit();
					    ShowComment.setTag("Default");	
					}
				});
				pDialog1.cancel();
			    pDialog1.dismiss();
				}	
				else{
					Toast.makeText(activity, "No Connection! Try Again.", Toast.LENGTH_LONG).show();
					return;
				}	
			}
			
		});
		
		onclicklike1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				InternetOprations netcheck = new InternetOprations(activity);

				// Check if Internet present
				if (!netcheck.isConnectingToInternet()) {
					// Internet Connection is not present
					Toast.makeText(activity, "No Connection! Try Again.", Toast.LENGTH_LONG).show();// stop executing code by return
					return;
				}else{
				
				Log.i("Click on ","LIke one i get it");
				DatabaseHandler dbhndlr = new DatabaseHandler(activity);
				int rowcount = dbhndlr.getRowCount();
				
				if(rowcount != 0)
				{
					HashMap<String,String> user = dbhndlr.getUserDetails();
					user_id = user.get("uid");
				}
					likeId = "1";
					postid = String.valueOf(item.getId());
					// **Edited to apply image update at click**
					dolike dl = new dolike();
					
					Boolean val = dl.doInBackground("hello");
					
					if(val){int count;
						//if(dl.liked==1)count = item.getNoOflike1()+1;else count = item.getNoOflike1()-1;
						onclicklike1.setImageResource(R.drawable.like1_click);
						
						HomeFragment.feedItems.get(position).setNoOflike1(dl.liked);
						like1count.setText(String.valueOf(dl.liked));
                        HomeFragment.listAdapter.notifyDataSetChanged();
					}
				}
			}
		});
		
		onclicklike2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				// Check if Internet present
				InternetOprations checkIPO = new InternetOprations(activity);
				if (!checkIPO.isConnectingToInternet()) {
					// Internet Connection is not present
					Toast.makeText(activity, "No Connection! Try Again.", Toast.LENGTH_LONG).show();// stop executing code by return
					return;
				}else{
				DatabaseHandler dbhndlr = new DatabaseHandler(activity);
				int rowcount = dbhndlr.getRowCount();
				if(rowcount != 0)
				{
					HashMap<String,String> user = dbhndlr.getUserDetails();
					user_id = user.get("uid");
				}
				likeId = "2";
				postid = String.valueOf(item.getId());
				Log.i("pos",String.valueOf(position));
				// **Edited to apply image update at click**
				dolike dl = new dolike();
				
				Boolean val = dl.doInBackground("hello");
				
				if(val){int count;
					//if(dl.liked==1)count = item.getNoOflike2()+1; else count = item.getNoOflike2()-1;
					onclicklike2.setImageResource(R.drawable.like1_click);
					HomeFragment.feedItems.get(position).setNoOflike2(dl.liked);
					like2count.setText(String.valueOf(dl.liked));
                    HomeFragment.listAdapter.notifyDataSetChanged();
				}
				}    //internet check
			}
		});
		
		return convertView;
	}

	/**
	 * Background Async Task to Get complete product details
	 * */
	class dolike extends AsyncTask<String, String, Boolean> {
		
		public int Success;
		public int liked=0;
	/**
		 * Getting product details in background thread
		 * */
		protected Boolean doInBackground(String... params) {

			// Check for success tag
					InternetOprations getip = new InternetOprations(activity);
					
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
					String currentDateandTime = sdf.format(new Date());
					JSONObject jscount = UF.likepostById(likeId, postid, user_id, currentDateandTime, getip.getIPAddress());
					try {
						JSONArray feedArray = jscount.getJSONArray("likePostCount");
						JSONObject fetchdata =(JSONObject) feedArray.get(0);
						Log.i("fetchdata",fetchdata.toString());
						if(fetchdata.getInt("success") == 1){
							Log.i("ids",postid+","+user_id+","+fetchdata.getInt("likeCount"));
							// We first check for cached request
							
							/*if(fetchdata.getString("operation").equals("Update")){
								if(fetchdata.getString("Status").equals("liked"))
								{
									liked=fetchdata.getInt("likeCount");
								}
							}*/
							liked=fetchdata.getInt("likeCount");
						Success=1;
						}
						else
						{
							Success=0;
						}
					}
					
					catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(Success==1) {return true; } else { return false;}
					}
		
	}
	
	private void dorefresh() {
		// TODO Auto-generated method stub
		//AppController.getInstance().getRequestQueue().getCache().clear();
		if(cateid>0){Log.i("pages","dialog");
			PagesFragment F = new PagesFragment(cateid);
			android.app.FragmentManager fragmentManager = activity.getFragmentManager();
		    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		    fragmentTransaction.replace(R.id.frame_container,F);
		    fragmentTransaction.addToBackStack(null);
		    fragmentTransaction.commit();
		    }
			else
			{Log.i("home","dialog");
			HomeFragment F = new HomeFragment();
		    android.app.FragmentManager fragmentManager = activity.getFragmentManager();
		    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		    fragmentTransaction.replace(R.id.frame_container,F);
		    fragmentTransaction.addToBackStack(null);
		    fragmentTransaction.commit();
				}
	}
	
}
