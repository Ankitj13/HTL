package info.sss.htl;


import info.androidhive.htl.R;
import info.sss.htl.adapter.CommentOnPostAdapter;
import info.sss.htl.app.AppController;
import info.sss.htl.data.FeedItem;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.color;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class PhotosFragment extends Fragment {
	int ID, rowcount;
	public PhotosFragment(){}
	private ListView listView;
	private CommentOnPostAdapter listAdapter;
	public List<FeedItem> feedItems1;
	private TextView docomment, close;
	private TextView postlike1count, postlike2count;
	private ImageButton like1, like2;
	private TextView name, timestamp, category_name, statusMsg, postCommentCount;
	private View rootView;
	private UserFunctions uf;
	private EditText commentText;
	private String postid, currentDateandTime, likeId, user_id="1", profilepic, commentByUserName;
	public JSONObject jscount, fetchdata;
	private JSONArray feedArray;
	private String bg;
	private int picId;
	public FeedItem item;
	public JSONObject feedObj;
	JSONObject response;
	private ImageButton comments;
	public String comment;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
		getActivity().getActionBar().hide();
		
       rootView = inflater.inflate(R.layout.fragment_photos, container, false);
       InternetOprations checkNET = new InternetOprations(getActivity());
       boolean isON = checkNET.isConnectingToInternet();
       
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		currentDateandTime = sdf.format(new Date());
		//GET VALUES FROM LOCAL DATABASE TO CHECK THAT USER IS LOGIN???
		DatabaseHandler dbhndlr = new DatabaseHandler(getActivity());
		int rowcount = dbhndlr.getRowCount();
		if(rowcount != 0)
		{
			HashMap<String,String> user = dbhndlr.getUserDetails();
			user_id = user.get("uid");
			profilepic = user.get("user_image");
			commentByUserName = user.get("name");
		}else{
			commentByUserName = "other";
			profilepic = "http://demo.ssism.org/assets/img/profile_pics/avatar.png";
		};
        ID = getArguments().getInt("CID"); 
        postid = String.valueOf(ID);
        
        listView = (ListView) rootView.findViewById(R.id.commentView);

 		feedItems1 = new ArrayList<FeedItem>();

 		listAdapter = new CommentOnPostAdapter(getActivity(), feedItems1);
 		
 		listView.setAdapter(listAdapter);
 		
 		uf = new UserFunctions();
 		response = uf.getcommentByPostid(String.valueOf(ID));
 		InternetOprations netcheck = new InternetOprations(getActivity());

			// Check if Internet present
			if (!netcheck.isConnectingToInternet()) {
				// Internet Connection is not present
				Toast.makeText(getActivity(), "No Connection! Try Again.", Toast.LENGTH_LONG).show();// stop executing code by return
			}else{Log.i("check","comemt");
				new Getfeeds().execute();
				
			}
        
        //GETTING VALUS OF WIDGETS OF THAT FRAGMENT
        
        close= (TextView) rootView.findViewById(R.id.Close_fragment);
        category_name = (TextView) rootView.findViewById(R.id.category_name1);
 		name = (TextView) rootView.findViewById(R.id.PostBy);
 		timestamp = (TextView) rootView.findViewById(R.id.PostAt);
 		statusMsg = (TextView) rootView.findViewById(R.id.feedtxtStatusMsg1);
 		postlike1count = (TextView) rootView.findViewById(R.id.postlike1Count);
 		postlike2count = (TextView) rootView.findViewById(R.id.postlike2Count);
 		commentText = (EditText) rootView.findViewById(R.id.editcomment);
 		docomment = (TextView) rootView.findViewById(R.id.postComment);
 		like1 = (ImageButton) rootView.findViewById(R.id.like1img);
 		like2 = (ImageButton) rootView.findViewById(R.id.like2img);
 		comments = (ImageButton) rootView.findViewById(R.id.comments);
 		postCommentCount = (TextView) rootView.findViewById(R.id.postCommentCount);
 		like1.setTag("one");
 		like2.setTag("two");
 		comments.setTag("comment");
 		commentText.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				listAdapter.notifyDataSetChanged();
				
			}
		});
 		//TO CLOSE THE FRAGMENT CLICK ON "X" 
 		 close.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				getActivity().getFragmentManager().popBackStack();
			}
		});
         //TO POST COMMENT ON A PARTICULER POST
         docomment.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String text = commentText.getText().toString();
				if(text.equals("")){
				Toast.makeText(getActivity(), "Please write something.", Toast.LENGTH_LONG).show();	
				}
				else{
					InternetOprations netcheck = new InternetOprations(getActivity());
					// Check if Internet present
					if (!netcheck.isConnectingToInternet()) {
						// Internet Connection is not present
						Toast.makeText(getActivity(), "No Connection! Try Again.", Toast.LENGTH_LONG).show();// stop executing code by return
					}else{	
				comment = commentText.getText().toString();
				Docomment dc = new Docomment();
				
				 dc.doInBackground("hello");
				
				if(comments.getTag().equals("comment")){
					comments.setImageResource(R.drawable.comment_icon_click);
					comments.setTag("comment");
					//listAdapter.notifyDataSetChanged();
					commentText.setText("");
					//feedItems1.clear();
					}
				}
					}
				}
		});
         like1.setOnClickListener(new OnClickListener() {
  			
  			@Override
  			public void onClick(View arg0) {
  				// TODO Auto-generated method stub
  				
  					likeId = "1";
  					InternetOprations netcheck = new InternetOprations(getActivity());

  					// Check if Internet present
  					if (!netcheck.isConnectingToInternet()) {
  						// Internet Connection is not present
  						Toast.makeText(getActivity(), "No Connection! Try Again.", Toast.LENGTH_LONG).show();// stop executing code by return
  					}else{
  						Dolike dl = new Dolike();
  	  					
  	  					Boolean val = dl.doInBackground("hello");
  						
  						if(val){
  						like1.setImageResource(R.drawable.like1_click);
  						for (int i = 0; i < feedArray.length(); i++) {
							try {int count = 0;
								feedObj = (JSONObject) feedArray.get(i);
								count = feedObj.getInt("likeCount");
								postlike1count.setText(String.valueOf(count));
								like1.setImageResource(R.drawable.like1);
								listAdapter.notifyDataSetChanged();
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
  						//
  						//feedItems1.get(0).getNoOflike1();
  						
  						}
  						}
  					}
  			}	
  		});
         like2.setOnClickListener(new OnClickListener() {
  			
  			@Override
  			public void onClick(View arg0) {
  				// TODO Auto-generated method stub
  				
  					likeId = "2";
  					InternetOprations netcheck = new InternetOprations(getActivity());

  					// Check if Internet present
  					if (!netcheck.isConnectingToInternet()) {
  						// Internet Connection is not present
  						Toast.makeText(getActivity(), "No Connection! Try Again.", Toast.LENGTH_LONG).show();// stop executing code by return
  					}else{
  						Dolike dl = new Dolike();
  	  					
  	  					Boolean val = dl.doInBackground("hello");
  						
  						if(val){
  						like2.setImageResource(R.drawable.like2_click);
  						for (int i = 0; i < feedArray.length(); i++) {
							try {int count = 0;
								feedObj = (JSONObject) feedArray.get(i);
								count = feedObj.getInt("likeCount");
								Log.i("likeed count", count+"");
								postlike2count.setText(String.valueOf(count));
								like2.setImageResource(R.drawable.like2);
								listAdapter.notifyDataSetChanged();
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
  						//
  						//feedItems1.get(0).getNoOflike1();
  						
  						}
  						}
  					}
  				
  			}
  		});
        
 		
 		return rootView;
    }
	
	/**
	 * Background Async Task to Get complete feed details
	 * */
	class Getfeeds extends AsyncTask<String, String, String> {

		
		private ProgressDialog Getfeeds;
		
		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			Getfeeds = new ProgressDialog(getActivity());
			Getfeeds.setMessage("Getting feeds. Please wait...");
			Getfeeds.setIndeterminate(false);
			Getfeeds.setCancelable(false);
			Log.i("show", "dialog");
			Getfeeds.show();
			//Getfeeds.show(getActivity(),"Loading feeds","Please Wait...",true);
		}

		/**
		 * Getting product details in background thread
		 * */
		protected String doInBackground(String... params) {

			// updating UI from Background Thread
			getActivity().runOnUiThread(new Runnable() {
				public void run() {
					// Check for success tag
					
					try {
						
						feedArray = response.getJSONArray("commentByPosts");
						
						for (int i = 0; i < feedArray.length(); i++) {
							feedObj = (JSONObject) feedArray.get(i);
							item = new FeedItem();
							if(i==0)
							{	
							String nameCaps = Character.toUpperCase(feedObj.getString("Postbyuser").charAt(0)) 
																+ feedObj.getString("Postbyuser").substring(1);
							name.setText(nameCaps);
							category_name.setText(feedObj.getString("postcategory")+ " by ");
							timestamp.setText(feedObj.getString("postTimeStamp"));
							postlike1count.setText(feedObj.getString("NoOfLike1"));
							postlike2count.setText(feedObj.getString("NoOfLike2"));
							postCommentCount.setText(feedObj.getString("commentCount"));
							item.setId(feedObj.getInt("postId"));
							item.setBackground(feedObj.getString("postBG"));
							// Chcek for empty status message
							if (!TextUtils.isEmpty(feedObj.getString("postContentText"))) {
								statusMsg.setText(feedObj.getString("postContentText"));
								statusMsg.setVisibility(View.VISIBLE);
							} else {
								// status is empty, remove from view
								statusMsg.setVisibility(View.GONE);
							}
							//set background of post 
							bg = feedObj.getString("postBG");
							
							if(!bg.equals("0"))
							{	bg = bg.substring(0, bg.lastIndexOf('.'));
								picId = getActivity().getResources().getIdentifier(bg, "drawable", "info.androidhive.htl");
								statusMsg.setBackgroundResource(picId);
								statusMsg.setTextColor(Color.WHITE);
							}
							else
							{
							statusMsg.setTextColor(Color.BLACK);
							statusMsg.setBackgroundColor(color.white);
							}
							}
							else
							{	
								item.setCommentContent(feedObj.getString("commentText"));
								item.setCommentBy(feedObj.getString("commentByUserName"));
								item.setCommentAt(feedObj.getString("commentTimeStamp"));
								item.setProfilePic(feedObj.getString("profilePic"));
								feedItems1.add(item);
							}
							
						}

						
					} catch (JSONException e) {
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
			Getfeeds.dismiss();
		}
	}
	/**
	 * Background Async Task to Get complete product details
	 * */
	class Dolike extends AsyncTask<String, String, Boolean> {

		public int Success;
		public int liked=0;
		
		/**
		 * Getting product details in background thread
		 * */
		protected Boolean doInBackground(String... params) {

			// updating UI from Background Thread
			
					// Check for success tag
					InternetOprations getip = new InternetOprations(getActivity());
					
					jscount = uf.likepostById(likeId, postid, user_id, currentDateandTime, getip.getIPAddress());
  					try {
  						feedArray = jscount.getJSONArray("likePostCount");
  						fetchdata =(JSONObject) feedArray.get(0);
  						if(fetchdata.getInt("success") == 1){
							Log.i("ids",""+fetchdata.getInt("likeCount"));
							// We first check for cached request
							
							if(fetchdata.getString("operation").equals("Update")){
								if(fetchdata.getString("Status").equals("liked"))
								{
									liked=1;
								}
							}
						Success=1;
						}
						else
						{
							Success=0;
						}
  					} catch (JSONException e) {
  						// TODO Auto-generated catch block
  						e.printStackTrace();
  					}
					
  					if(Success==1) {return true; } else { return false;}
		}
		
	}
	/**
	 * Background Async Task to Get complete feed details
	 * */
	class Docomment extends AsyncTask<String, String, String> {

		public JSONObject fetchdata;
		public JSONArray feedArray;
		public FeedItem item1;
		/**
		 * Getting product details in background thread
		 * */
		protected String doInBackground(String... params) {
			
			jscount = uf.doComment(comment, postid, user_id, currentDateandTime);
  					try {int a=0;
  						feedArray = jscount.getJSONArray("commentByPosts");
  						//fetchdata =(JSONObject) feedArray.get(1);
  						//feedItems1.clear();
  						/*for (int i = 1; i < feedArray.length(); i++) {
  							fetchdata = (JSONObject) feedArray.get(i);
							
							Log.i("val aaja",fetchdata.toString());
							Log.i("a",a+"");
							FeedItem item = new FeedItem();
							item.setCommentContent(fetchdata.getString("comment"));
							item.setCommentBy(fetchdata.getString("commentByUserName"));
							item.setCommentAt(fetchdata.getString("commentTimeStamp"));
							item.setProfilePic(fetchdata.getString("profilePic"));
							feedItems1.add(item);
							a++;*/
  						String[] time = currentDateandTime.split(" ");
  						FeedItem item = new FeedItem();
						item.setCommentContent(comment);
						item.setCommentBy(commentByUserName);
						item.setCommentAt(time[1]);
						item.setProfilePic(profilepic);
					
							feedItems1.add(item);
							//}
						//}
  						//listAdapter.notifyDataSetChanged();
  					}
  					catch (JSONException e) {
  						// TODO Auto-generated catch block
  						e.printStackTrace();
  					}
			return null;
		}

	}

	/*public void doRefresh()
	{
			AppController.getInstance().getRequestQueue().getCache().clear();
			PhotosFragment HF = new PhotosFragment();
		    android.app.FragmentManager fragmentManager = getActivity().getFragmentManager();
		    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
		    Bundle args = new Bundle();
		    args.putInt("CID", ID);
		    HF.setArguments(args);
		    fragmentTransaction.replace(R.id.frame_container,HF);
		    fragmentTransaction.commit();
		
	}*/
}
