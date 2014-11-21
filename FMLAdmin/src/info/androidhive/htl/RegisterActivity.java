package info.androidhive.htl;

import info.androidhive.listviewfeed.R;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

import org.json.JSONException;
import org.json.JSONObject;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class RegisterActivity extends Activity {
	/**
	 * A dummy authentication store containing known user names and passwords.
	 * TODO: remove after connecting to a real authentication system.
	
	private static final String[] DUMMY_CREDENTIALS = new String[] {
			"foo@example.com:hello", "bar@example.com:world" };
 */
	/**
	 * The default email to populate the email field with.
	 */
	public static final String REGISTEREXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";
	public static final String ERRORFLAG = "com.example.android.MESSAGE";
	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserRegisterTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	
	private String mEmail;
	private String mPassword;
	private String user_Name;
	private String user_Confirm_Password;
	// UI references.
	private EditText userName;
	private EditText userConfirmPassword;
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mRegisterFormView;
	private View mRegisterStatusView;
	private TextView mRegisterStatusMessageView;
	TextView errorRegisterMsg;

	//Values for login 
		public static final String Groups="com.example.attendence.MESSAGE";
		private static String KEY_SUCCESS = "success";
		private static String KEY_ERROR = "error";
		private static String KEY_ERROR_MSG = "error_msg";
		private static String KEY_UID = "uid";
		private static String KEY_NAME = "name";
		private static String KEY_EMAIL = "email";
		private static String KEY_CREATED_AT = "created_at";
		JSONObject json_user;
		private static String IP;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_register);

		// Set up the login form.
		mEmail = getIntent().getStringExtra(REGISTEREXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.user_email);
		mEmailView.setText(mEmail);
		userName = (EditText) findViewById(R.id.user_name);
		userConfirmPassword = (EditText) findViewById(R.id.user_confirm_password);
		mPasswordView = (EditText) findViewById(R.id.user_password);
		errorRegisterMsg = (TextView) findViewById(R.id.error_register);
		
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.register || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mRegisterFormView = findViewById(R.id.login_form);
		mRegisterStatusView = findViewById(R.id.register_status);
		mRegisterStatusMessageView = (TextView) findViewById(R.id.register_status_message);

		findViewById(R.id.register_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();
					}
				});
		//code to get the local IP address 
		  try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        IP= inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception ex) {
            Log.e("IP Address", ex.toString());
        }
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);
		userName.setError(null);
		userConfirmPassword.setError(null);
		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();
		user_Name = userName.getText().toString();
		user_Confirm_Password = userConfirmPassword.getText().toString();
		
		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_RegisterRequired));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_RegisterPassword));
			focusView = mPasswordView;
			cancel = true;
		}  
		
		//check for a confirm password
		 if(TextUtils.isEmpty(user_Confirm_Password)){
			userConfirmPassword.setError(getString(R.string.error_incorrect_RegisterPassword));
			focusView = userConfirmPassword;
			cancel = true;
		} else if (!mPassword.equals(user_Confirm_Password)) {
			userConfirmPassword.setError(getString(R.string.error_incorrect_RegisterPassword));
			focusView = userConfirmPassword;
			cancel = true;
		}
			

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_RegisterRequired));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_RegisterEmail));
			focusView = mEmailView;
			cancel = true;
		}
		// Check for a valid user name.
		if (TextUtils.isEmpty(user_Name)) {
			userName.setError(getString(R.string.error_field_RegisterRequired));
			focusView = userName;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mRegisterStatusMessageView.setText(R.string.login_progress_signing_up);
			showProgress(true);
			mAuthTask = new UserRegisterTask();
			mAuthTask.execute((Void) null);
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mRegisterStatusView.setVisibility(View.VISIBLE);
			mRegisterStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mRegisterStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mRegisterFormView.setVisibility(View.VISIBLE);
			mRegisterFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mRegisterFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mRegisterStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {
		@Override
		protected Boolean doInBackground(Void... params) {
			
			// TODO: attempt authentication against a network service.
			UserFunctions userFunction = new UserFunctions();
            JSONObject json = userFunction.registerUser(user_Name, mEmail, mPassword, IP);
           
			try {
				if (json.getString(KEY_SUCCESS) != null) {
                    
                    String res = json.getString(KEY_SUCCESS); 
                    if(Integer.parseInt(res) == 1){
                        // user successfully registred
                        // Store user details in SQLite Database
                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                        JSONObject json_user = json.getJSONObject("user");
                         
                        // Clear all previous data in database
                        userFunction.logoutUser(getApplicationContext());
                        //Add user data into local db
                        db.addUser(json_user.getString(KEY_NAME), json_user.getString(KEY_EMAIL), json.getString(KEY_UID), json_user.getString(KEY_CREATED_AT));                        
                        // Launch Dashboard Screen
                        Intent dashboard = new Intent(getApplicationContext(), MainActivity.class);
                        // Close all views before launching Dashboard
                        dashboard.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(dashboard);
                        // Close Registration Screen
                        finish();
                    }else{
                    	Intent dashboard = new Intent(getApplicationContext(), LoginActivity.class);
                        // Close all views before launching Dashboard
                        dashboard.putExtra(ERRORFLAG, "Email already Exist.");
                        startActivity(dashboard);
                        // Close Registration Screen
                        finish();
                    	
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

			/*for (String credential : DUMMY_CREDENTIALS) {
				String[] pieces = credential.split(":");
				if (pieces[0].equals(mEmail)) {
					// Account exists, return true if the password matches.
					return pieces[1].equals(mPassword);
				}*/
			// TODO: register the new account here.
			
			return true;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {
				finish();
			} else {
				String Error="Email is already Exist.";
            	errorRegisterMsg.setText(Error);
			}
		}

		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
}
