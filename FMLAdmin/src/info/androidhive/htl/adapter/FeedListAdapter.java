package info.androidhive.htl.adapter;

import info.androidhive.htl.InternetOprations;
import info.androidhive.htl.UserFunctions;
import info.androidhive.htl.app.AppController;
import info.androidhive.htl.data.FeedItem;
import info.androidhive.listviewfeed.R;

import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.color;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.toolbox.ImageLoader;

public class FeedListAdapter extends BaseAdapter {	
	//private int PostId;
	private Activity activity;
	private LayoutInflater inflater;
	private List<FeedItem> feedItems;
	private static ToggleButton swt;
	private UserFunctions UF = new UserFunctions();
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();
	private String bg;
	private int picId;
	protected String PostId;
	
	private TextView like1count,like2count,commentcount;

	public FeedListAdapter(Activity activity, List<FeedItem> feedItems) {
		this.activity = activity;
		this.feedItems = feedItems;
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
	public View getView( int position, View convertView, ViewGroup parent) {

		
		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (convertView == null)
			convertView = inflater.inflate(R.layout.feed_item, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();

		TextView username = (TextView) convertView.findViewById(R.id.feedname);
		TextView category_name = (TextView) convertView.findViewById(R.id.category_name);
		TextView Status = (TextView) convertView.findViewById(R.id.feedtxtStatusMsg);
		TextView timestamp = (TextView) convertView
				.findViewById(R.id.feedtimestamp);
		swt = (ToggleButton) convertView.findViewById(R.id.Button);
		like1count = (TextView) convertView.findViewById(R.id.like1Count);
		like2count = (TextView) convertView.findViewById(R.id.like2Count);
		commentcount = (TextView) convertView.findViewById(R.id.commentCount);
		
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
		
		
		username.setText(item.getName());
		category_name.setText(item.getCategory()+ " by ");
		Status.setText(item.getStatus());
		timestamp.setText(item.getTimeStamp());
		
		like1count.setText(String.valueOf(item.getNoOflike1()));
		like2count.setText(String.valueOf(item.getNoOflike2()));
		commentcount.setText(String.valueOf(item.getCommentCount()));
		
		
		//set background of post 
				bg = item.getBackground();
				
				if(!bg.equals("0"))
				{	bg = bg.substring(0, bg.lastIndexOf('.'));
					picId = activity.getResources().getIdentifier(bg, "drawable", "info.androidhive.listviewfeed");
					Status.setBackgroundResource(picId);
					Status.setTextColor(Color.WHITE);
				}
				else
				{
					Status.setTextColor(Color.BLACK);
					Status.setBackgroundColor(color.white);
				}
		if(item.getpostStatus().equals("Active"))
		{
			swt.setChecked(true);
		}
		else
		{
			swt.setChecked(false);
		}	
		swt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				InternetOprations ip = new InternetOprations(activity);
				if(ip.isConnectingToInternet())
				{
					PostId = String.valueOf(item.getId());
					Log.i("PostId", PostId);
					MakePostStatus mkp = new MakePostStatus();
					Boolean result = mkp.doInBackground(null);
					Toast.makeText(activity, "Post Status Updated.", Toast.LENGTH_LONG).show();
					if(result){
						if(item.getpostStatus().equals("Active"))
						{
							Log.i("Status if ",item.getpostStatus());
							swt.setChecked(false);
						}
						else
						{
							Log.i("Status else ",item.getpostStatus());
							swt.setChecked(true);
						}	
						
						
					}
				}
				else
				{
					Toast.makeText(activity, "Please check Net connection.", Toast.LENGTH_LONG).show();
				}
					
			}
		});
		
		return convertView;
	}
	/**
	 * Background Async Task to Get complete product details
	 * */
	class MakePostStatus extends AsyncTask<String, String, Boolean> {
		
		public int Success;
	/**
		 * Getting product details in background thread
		 * */
		protected Boolean doInBackground(String... params) {

			// Check for success tag
			
	        Log.i("postid",PostId);
					JSONObject jscount = UF.ActivePost(PostId);
					try {
						JSONArray feedArray = jscount.getJSONArray("Post");
						JSONObject fetchdata =(JSONObject) feedArray.get(0);
						Log.i("fetchdata",fetchdata.toString());
						
						if(fetchdata.getInt("success") == 1){Success=1;}else{Success=0;}
						
						}
					
					catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					if(Success==1) {return true; } else { return false;}
					}
		
	}
	
}
