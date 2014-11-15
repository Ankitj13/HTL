package info.sss.htl;


import info.androidhive.htl.R;
import info.sss.htl.adapter.FeedListAdapter;
import info.sss.htl.adapter.NavDrawerListAdapter;
import info.sss.htl.model.NavDrawerItem;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	// nav drawer title
	private CharSequence mDrawerTitle;

	// used to store app title
	private CharSequence mTitle;

	// slide menu items
	private String[] navMenuTitles;
	private TypedArray navMenuIcons;

	private ArrayList<NavDrawerItem> navDrawerItems;
	private NavDrawerListAdapter adapter;
	private int rowcount;
	private String user_name;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		DatabaseHandler dbhndlr = new DatabaseHandler(MainActivity.this);
        HashMap<String,String> user = dbhndlr.getUserDetails();
		user_name = user.get("name");
		rowcount = dbhndlr.getRowCount();
		
		//Getting user value after successful login by user
		Intent intent = getIntent();
	    String UserId= intent.getStringExtra(LoginActivity.Groups);
	    Integer cateid = intent.getIntExtra(FeedListAdapter.CategoryId,0);
	    Log.i("cateid",String.valueOf(cateid));
	    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
		if(UserId != null)
		{  
			Toast.makeText(this, "Your logged in successfully as "+UserId,Toast.LENGTH_LONG).show(); 
		}
	    mTitle = mDrawerTitle = getTitle();

		// load slide menu items
		navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);

		// nav drawer icons from resources
		navMenuIcons = getResources()
				.obtainTypedArray(R.array.nav_drawer_icons);

		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.list_slidermenu);

		navDrawerItems = new ArrayList<NavDrawerItem>();

		// adding Categoty Items to nav drawer items to array
		// Home
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIcons.getResourceId(0, 0)));
		// LOVE
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIcons.getResourceId(1, 0)));
		// Animals
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIcons.getResourceId(2, 0)));
		// Money
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1)));
		//navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIcons.getResourceId(3, -1), true, "22"));
		// Kids
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIcons.getResourceId(4, -1)));
		// Work
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIcons.getResourceId(5, -1)));
		//Health
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[6], navMenuIcons.getResourceId(6, -1)));
		// Intimacy
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[7], navMenuIcons.getResourceId(7, -1)));
		// Miscellaneous
		navDrawerItems.add(new NavDrawerItem(navMenuTitles[8], navMenuIcons.getResourceId(8, -1)));

		// Recycle the typed array
		navMenuIcons.recycle();

		mDrawerList.setOnItemClickListener(new SlideMenuClickListener());

		// setting the nav drawer list adapter
		adapter = new NavDrawerListAdapter(getApplicationContext(),
				navDrawerItems);
		mDrawerList.setAdapter(adapter);

		// enabling action bar app icon and behaving it as toggle button
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, //nav menu toggle icon
				R.string.app_name, // nav drawer open - description for accessibility
				R.string.app_name // nav drawer close - description for accessibility
		) {
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>"+mTitle+"</font>"));
				//getActionBar().setTitle(mTitle);
				// calling onPrepareOptionsMenu() to show action bar icons
				invalidateOptionsMenu();
			}

			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>"+mDrawerTitle+"</font>"));
				//getActionBar().setTitle(mDrawerTitle);
				// calling onPrepareOptionsMenu() to hide action bar icons
				invalidateOptionsMenu();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		if (savedInstanceState == null) {
			// on first time display view for first nav item
			
				displayView(cateid);
			
			
		}
		
     }
	
	/**
	 * Slide menu item click listener
	 * */
	private class SlideMenuClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// display view for selected nav drawer item
			InternetOprations netcheck = new InternetOprations(MainActivity.this);
			// Check if Internet present
			if (!netcheck.isConnectingToInternet()) {
				// Internet Connection is not present
				Toast.makeText(MainActivity.this, "No Connection! Try Again.", Toast.LENGTH_LONG).show();// stop executing code by return
			}else{
			displayView(position);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		MenuItem profile = menu.findItem(R.id.action_profile);
		MenuItem login = menu.findItem(R.id.action_login);
		MenuItem logout = menu.findItem(R.id.action_logout);
		if(rowcount!=0){
			profile.setVisible(true);
			login.setVisible(false);
			logout.setVisible(true);
			profile.setTitle(user_name);
		}
		else{
			logout.setVisible(false);
			login.setVisible(true);
			profile.setVisible(false);
		}
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// toggle nav drawer on selecting action bar app icon/title
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Handle action bar actions click
		switch (item.getItemId()) {
		case R.id.action_post:
			/*//check Login
			if(rowcount!=0)
			{*/
			 //create activity for post message
				Intent post = new Intent(MainActivity.this,Submit_postActivity.class);
	            startActivity(post);
	            finish();
			/*}
			else
			{
				Toast.makeText(this, "Please signin first.", Toast.LENGTH_LONG).show();
				Intent intent = new Intent(MainActivity.this,LoginActivity.class);
	            startActivity(intent);
	            finish();
			}*/
            return true;
		case R.id.action_login:
			 // create intent to perform web search for this planet
            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            return true;
            
		case R.id.action_logout:
			UserFunctions logout = new UserFunctions();
			Boolean check = logout.logoutUser(getApplicationContext());
			if(check.equals(true))
			{
				Intent LogoutIntent = new Intent(MainActivity.this,MainActivity.class);
	            startActivity(LogoutIntent);
				Toast.makeText(this, "You are signout successfully.", Toast.LENGTH_LONG).show();
			}
			
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	/* *
	 * Called when invalidateOptionsMenu() is triggered
	 */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// if nav drawer is opened, hide the action items
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		menu.findItem(R.id.action_post).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	/**
	 * Diplaying fragment view for selected nav drawer list item
	 * */
	private void displayView(int position) {
		// update the main content by replacing fragments
		Fragment fragment = null;
		switch (position) {
		case 0:
			fragment = new HomeFragment();
			break;
		case 1:
			fragment = new PagesFragment(1);
			break;
		case 2:
			fragment = new PagesFragment(2);
			break;
		case 3:
			fragment = new PagesFragment(3);
			break;
		case 4:
			fragment = new PagesFragment(4);
			break;
		case 5:
			fragment = new PagesFragment(5);
			break;
		case 6:
			fragment = new PagesFragment(6);
			break;
		case 7:
			fragment = new PagesFragment(7);
			break;
		case 8:
			fragment = new PagesFragment(8);
			break;
		
		default:
			break;
		}

		if (fragment != null) {
			FragmentManager fragmentManager = getFragmentManager();
			fragmentManager.beginTransaction()
					.replace(R.id.frame_container, fragment).commit();

			// update selected item and title, then close the drawer
			mDrawerList.setItemChecked(position, true);
			mDrawerList.setSelection(position);
			setTitle(navMenuTitles[position]);
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			// error in creating fragment
			Log.e("MainActivity", "Error in creating fragment");
		}
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(Html.fromHtml("<font color='#ffffff'>"+mTitle+"</font>"));
	
	}

	/**
	 * When using the ActionBarDrawerToggle, you must call it during
	 * onPostCreate() and onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
}
