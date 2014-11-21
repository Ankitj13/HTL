package info.androidhive.htl.data;

public class FeedItem {
	
	private int id,NoOfLike1,NoOfLike2, commentcount;
	private String username,category_name, status, image, profilePic, timeStamp, postStatus, background;

	public FeedItem() {
	}

	public FeedItem(int id, String username, String category_name,String image, String status,
			String profilePic, String timeStamp, String postStatus, int NoOfLike1,int NoOfLike2,String background, int commentcount) {
		super();
		this.id = id;
		this.username = username;
		this.category_name=category_name;
		this.image = image;
		this.status = status;
		this.profilePic = profilePic;
		this.timeStamp = timeStamp;
		this.postStatus = postStatus;
		this.NoOfLike1 = NoOfLike1;
		this.NoOfLike2 = NoOfLike2;
		this.background = background;
		this.commentcount = commentcount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return username;
	}

	public void setName(String username) {
		this.username = username;
	}
	public String getCategory() {
		return category_name;
	}

	public void setCategory(String category_name) {
		this.category_name = category_name;
	}
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getImge() {
		return image;
	}

	public void setImge(String image) {
		this.image = image;
	}

	

	public String getProfilePic() {
		return profilePic;
	}

	public void setProfilePic(String profilePic) {
		this.profilePic = profilePic;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getpostStatus() {
		return postStatus;
	}

	public void setpostStatus(String postStatus) {
		this.postStatus = postStatus;
	}
	public int getNoOflike1() {
		return NoOfLike1;
	}

	public void setNoOflike1(int NoOfLike1) {
		this.NoOfLike1 = NoOfLike1;
	}
	
	public int getNoOflike2() {
		return NoOfLike2;
	}

	public void setNoOflike2(int NoOfLike2) {
		this.NoOfLike2 = NoOfLike2;
	}
	public String getBackground() {
		return background;
	}
	public void setBackground(String Background) {
		this.background = Background;
	}
	public void setCommentCount(int commentcount) {
		this.commentcount = commentcount;
	}
	public int getCommentCount() {
		return commentcount;
	}
}
