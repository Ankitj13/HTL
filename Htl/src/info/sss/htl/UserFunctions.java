package info.sss.htl;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;
import android.content.Context;
import android.util.Log;

public class UserFunctions {

	private JSONParser jsonParser;
	
	private static String loginURL = "http://ssism.org/Api/";
	//for localhost on geny http://10.0.3.2/Admin/AndroidAttendancePHP/index.php  www.ssism.org
	private static String registerURL = "http://ssism.org/Api/";
	private static String getcommentBypostURL = "http://ssism.org/Api/getcommentsByPost.php";
	private static String getPostBycateIDURL = "http://ssism.org/Api/getpostByCateID.php";
	private static String likepostByIdURL = "http://ssism.org/Api/likepostById.php";
	private static String doCommentURL = "http://ssism.org/Api/Comment.php";
	private static String doPostURL = "http://ssism.org/Api/Post.php";
	private static String login_tag = "login";
	private static String register_tag = "register";
	
	// constructor
	public UserFunctions(){
		jsonParser = new JSONParser();
	}
	
	/**
	 * function make Login Request
	 * @param email
	 * @param password
	 * */
	public JSONObject loginUser(String email, String password){  Log.i("check login 222", "ohhhhhh");
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", login_tag));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		JSONObject json = jsonParser.getJSONFromUrl(loginURL, params);
		// return json
		 //Log.e("JSON", json.toString());
		return json;
	}
	
	/**
	 * function make Login Request
	 * @param name
	 * @param email
	 * @param password
	 * */
	public JSONObject registerUser(String name, String email, String password, String IP){
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("tag", register_tag));
		params.add(new BasicNameValuePair("name", name));
		params.add(new BasicNameValuePair("email", email));
		params.add(new BasicNameValuePair("password", password));
		params.add(new BasicNameValuePair("ipaddress", IP));
		// getting JSON Object
		JSONObject json = jsonParser.getJSONFromUrl(registerURL, params);
		// return json
		return json;
	}
/*
 * get comment by selected post id
 */
	public JSONObject getcommentByPostid(String postid){ 
	// Building Parameters
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	params.add(new BasicNameValuePair("postid", postid));
	
	JSONObject json = jsonParser.getJSONFromUrl(getcommentBypostURL, params);
	return json;
}
	/*
	 * get POST By selected Category 
	 */
		public JSONObject getPostBycateID(String cateid){ 
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("cateid", cateid));
		Log.i("cateid",cateid);
		JSONObject json = jsonParser.getJSONFromUrl(getPostBycateIDURL, params);
		return json;
	}
		/*
		 * get like post 
		 */
	public JSONObject likepostById(String likeid, String postid, String user_id, String localTime, String IP)
		{ 
			// Building Parameters
			List<NameValuePair> params = new ArrayList<NameValuePair>();
			params.add(new BasicNameValuePair("likeid", likeid));
			params.add(new BasicNameValuePair("postid", postid));
			params.add(new BasicNameValuePair("user_id", user_id));
			params.add(new BasicNameValuePair("likeAt", localTime));
			params.add(new BasicNameValuePair("ip", IP));
			
			JSONObject json = jsonParser.getJSONFromUrl(likepostByIdURL, params);
			return json;
		}
	/* 
	 * DO COMMENT FOR POST
	 */
public JSONObject doComment(String comment, String postid, String user_id, String localTime)
	{ 
		// Building Parameters
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("comemntText", comment));
		params.add(new BasicNameValuePair("postid", postid));
		params.add(new BasicNameValuePair("user_id", user_id));
		params.add(new BasicNameValuePair("comemntAt", localTime));
		params.add(new BasicNameValuePair("Tag", "InsertComment"));
		JSONObject json = jsonParser.getJSONFromUrl(getcommentBypostURL, params);
		return json;
	}
/* 
 * DO POST
 */
public JSONObject doPost(String user_id, String cateid, String postContent, String IP, String localTime, String postbg)
{ 
	// Building Parameters
	List<NameValuePair> params = new ArrayList<NameValuePair>();
	params.add(new BasicNameValuePair("postContent", postContent));
	params.add(new BasicNameValuePair("cateid", cateid));
	params.add(new BasicNameValuePair("user_id", user_id));
	params.add(new BasicNameValuePair("postAt", localTime));
	params.add(new BasicNameValuePair("ipaddress", IP));
	params.add(new BasicNameValuePair("postbg", postbg));
	JSONObject json = jsonParser.getJSONFromUrl(doPostURL, params);
	return json;
}


		/**
	 * Function get Login status
	 * */
	public boolean isUserLoggedIn(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		int count = db.getRowCount();
		if(count > 0){
			// user logged in
			return true;
		}
		return false;
	}
	
	/**
	 * Function to logout user
	 * Reset Database
	 * */
	public boolean logoutUser(Context context){
		DatabaseHandler db = new DatabaseHandler(context);
		db.resetTables();
		return true;
	}
}
