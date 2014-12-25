public class Title {

	private String title;
	private int height;

	public Title(String title, int height) {
		this.title = title;
		this.height = height;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	@Override
	public String toString() {
		return "Title [title=" + title + ", height=" + height + "]";
	}

}
