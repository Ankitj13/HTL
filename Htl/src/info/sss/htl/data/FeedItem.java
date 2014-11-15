package info.sss.htl.data;

public class FeedItem {
	private int id,NoOfLike1,NoOfLike2, commentcount;
	private String name, status, image, profilePic, timeStamp, url, category, commentBy, commentAt, commentCotent;
	private String background, like1IP, like2IP;
	public FeedItem() {
	}

	public FeedItem(int id, String name, String image, String status,
			String profilePic, String timeStamp, String url, String category, String commentBy,
			String commentAt, String commentCotent,int NoOfLike1,int NoOfLike2,String background, String like1IP, String like2IP, int commentcount)
	{
		super();
		this.id = id;
		this.name = name;
		this.image = image;
		this.status = status;
		this.profilePic = profilePic;
		this.timeStamp = timeStamp;
		this.url = url;
		this.category = category;
		this.commentBy = commentBy;
		this.commentAt = commentAt;
		this.commentCotent = commentCotent;
		this.NoOfLike1 = NoOfLike1;
		this.NoOfLike2 = NoOfLike2;
		this.background = background;
		this.like1IP = like1IP;
		this.like2IP = like2IP;
		this.commentcount = commentcount;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImge() {
		return image;
	}

	public void setImge(String image) {
		this.image = image;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
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

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
	public String getCommentBy() {
		return commentBy;
	}

	public void setCommentBy(String commentBy) {
		this.commentBy = commentBy;
	}
	public String getCommentAt() {
		return commentAt;
	}

	public void setCommentAt(String commentAt) {
		this.commentAt = commentAt;
	}
	public String getCommentContent() {
		return commentCotent;
	}
	public void setCommentContent(String commentContent) {
		this.commentCotent = commentContent;
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
	public void setlike1IP(String like1IP) {
		this.like1IP = like1IP;
	}
	public String getlike1IP()
	{ 
		return like1IP;
	}
	public void setlike2IP(String like2IP) {
		this.like2IP = like2IP;
	}
	public String getlike2IP()
	{ 
		return like2IP;
	}
	
	public void setCommentCount(int commentcount) {
		this.commentcount = commentcount;
	}
	
	public int getCommentCount() {
		return commentcount;
	}
}
