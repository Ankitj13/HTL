package info.sss.htl.adapter;

import info.androidhive.htl.R;
import info.sss.htl.PhotosFragment;
import info.sss.htl.app.AppController;
import info.sss.htl.data.FeedItem;

import java.util.List;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

public class CommentOnPostAdapter extends BaseAdapter  {
	private Activity activity;
	private LayoutInflater inflater;
	private List<FeedItem> feedItems1;
	ImageLoader imageLoader = AppController.getInstance().getImageLoader();

	public CommentOnPostAdapter(Activity activity, List<FeedItem> feedItems1) {
		this.activity = activity;
		this.feedItems1 = feedItems1;
	}

	@Override
	public int getCount() {
		return feedItems1.size();
	}

	@Override
	public Object getItem(int location) {
		return feedItems1.get(location);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View commentView, ViewGroup parent) {

		if (inflater == null)
			inflater = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (commentView == null)
			commentView = inflater.inflate(R.layout.comment_onpost, null);

		if (imageLoader == null)
			imageLoader = AppController.getInstance().getImageLoader();
		
		TextView commentByUsername=(TextView) commentView.findViewById(R.id.commentby);
		TextView commentContent=(TextView) commentView.findViewById(R.id.commentContent);
		TextView commentAt=(TextView) commentView.findViewById(R.id.commentat);
		
		NetworkImageView profilePic = (NetworkImageView) commentView.findViewById(R.id.commentprofilePic);
		final FeedItem item = feedItems1.get(position);
		commentByUsername.setText(item.getCommentBy());
		commentAt.setText(item.getCommentAt());
		commentContent.setText(item.getCommentContent());
		
		// user profile pic
		profilePic.setImageUrl(item.getProfilePic(), imageLoader);

		return commentView;
	}

}
