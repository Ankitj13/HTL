package info.sss.htl;

import info.androidhive.htl.R;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class Submit_postActivity  extends FragmentActivity
implements CategoryDialogFragment.NoticeDialogListener {
	
	TextView showCategory;
	private String cateName;
	EditText post;
	private TextView submitPost;
	private String currentDateandTime;
	private int cateid;
	String user_id="1", postContent;
	private String IP ;
	private int rowcount,i=0;
	private ImageButton clickbg;
	private String bg="NA";
	private ProgressDialog pDialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_submit_post);
		//enable back option for activity....
		ActionBar actionbar= getActionBar();
		actionbar.setDisplayHomeAsUpEnabled(true);
		//set color of action bar......
		getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3c8dbc")));
		
		
		
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		//get detalis of loggedin user......
		DatabaseHandler dbhndlr = new DatabaseHandler(this);
		rowcount = dbhndlr.getRowCount();
		if(rowcount != 0)
		{
			HashMap<String,String> user = dbhndlr.getUserDetails();
			user_id = user.get("uid");
		}
		
		//set local time
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		currentDateandTime = sdf.format(new Date());
			//call dialog box.......	
		CategoryDialogFragment diglog = new CategoryDialogFragment();
		diglog.show(getFragmentManager(), "post");
		
		showCategory = (TextView) findViewById(R.id.CategoryName);
		post = (EditText) findViewById(R.id.post_contentText);
		submitPost = (TextView) findViewById(R.id.doPost);
		clickbg = (ImageButton) findViewById(R.id.changebg);
				
		//code to get the local IP address 
		  try {
          for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
              NetworkInterface intf = en.nextElement();
              for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                  InetAddress inetAddress = enumIpAddr.nextElement();
                  if (!inetAddress.isLoopbackAddress()) {
                      IP = inetAddress.getHostAddress().toString();
                  }
              }
          }
      } catch (Exception ex) {
          Log.e("IP Address", ex.toString());
      }
		 
		  clickbg.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				i++;
				//set background of post 
					bg="bg"+String.valueOf(i);
					Log.i("bg",bg);
					int picid = getResources().getIdentifier(bg, "drawable", "info.androidhive.slidingmenu");
					post.setBackgroundResource(picid);
					post.setTextColor(Color.WHITE);
					if(i>10){i=0;post.setTextColor(R.color.like_text);}
				/*}
				else
				{
				statusMsg.setTextColor(Color.BLACK);
				statusMsg.setBackgroundColor(color.white);
				}*/
			}
		});
		submitPost.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				postContent=post.getText().toString().trim();
				 Log.e("postContent", postContent);
				 submitPost.setBackgroundColor(Color.parseColor("#3c8dbc"));
				 submitPost.setTextColor(Color.WHITE);
				if (TextUtils.isEmpty(postContent)) 
				{
				Toast.makeText(Submit_postActivity.this, "Please share something before post.", Toast.LENGTH_LONG).show();
				}
				else
				{
					new SubmitPost().execute();
				} 
				
			//need to make code for post succcess and send back to home page......@11/10 by ankit
			}
		});
	}

	 // The dialog fragment receives a reference to this Activity through the
    // Fragment.onAttach() callback, which it uses to call the following methods
    // defined by the NoticeDialogFragment.NoticeDialogListener interface
    public void onDialogPositiveClick(DialogFragment dialog ,int value) {
        // User touched the dialog's positive button
        cateid=value;
        String[] category = getResources().getStringArray(R.array.categories);
        for(int i=0;i<category.length;i++)
        {
        	if(category[i]==category[value]) cateName = category[i];
        }
        showCategory.setText(cateName);
     }

    public void onDialogNegativeClick(DialogFragment dialog , int value) {
        // User touched the dialog's negative button
       
    }
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.submit_post, menu);
		return true;
	}
	/**
	 * Background Async Task to Get complete product details
	 * */
	class SubmitPost extends AsyncTask<String, String, String> {

		protected JSONObject feedObj;


		/**
		 * Before starting background thread Show Progress Dialog
		 * */
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(Submit_postActivity.this);
			pDialog.setMessage("Post submiting. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		/**
		 * Getting product details in background thread
		 * */
		protected String doInBackground(String... params) {

			// updating UI from Background Thread
			runOnUiThread(new Runnable() {
				public void run() {
					// Check for success tag
					
					try {
						if(bg.equals("NA"))bg="";else bg=bg+".jpg";
						UserFunctions calldopost = new UserFunctions();
						JSONObject json = calldopost.doPost(user_id, String.valueOf(cateid+1), postContent, IP, currentDateandTime, bg);
						JSONArray feedArray = json.getJSONArray("doPost");
						feedObj = (JSONObject) feedArray.get(0);
						
						
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
			try {
				if(feedObj.getInt("success")==1)
				{
				Toast.makeText(Submit_postActivity.this, "Your post will be launched soon.Thanks for posting.", Toast.LENGTH_LONG).show();
				Intent i=new Intent(Submit_postActivity.this,MainActivity.class);
				startActivity(i);
				finish();
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			pDialog.dismiss();
		}
	}
}
