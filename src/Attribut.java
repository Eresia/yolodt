public class Attribut {

	private String title;
	private int hight;

	public Attribut(String title, int hight) {
		this.title = title;
		this.hight = hight;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getHight() {
		return hight;
	}

	public void setHight(int hight) {
		this.hight = hight;
	}
	
	@Override
	public String toString() {
		return "Attribut [title=" + title + ", hight=" + hight + "]";
	}

}
