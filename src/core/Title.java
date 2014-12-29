package core;
import java.io.Serializable;

/**
 * Class that represent a Title in ODT
 * @author ABADJI Julien & LEPESANT Bastien
 *
 */
@SuppressWarnings("serial")
public class Title  implements Serializable{

	private String title; //Text of the title
	private int height; //Title's level. Used for the search.

	/**
	 * Constructor
	 * @param title
	 * @param height
	 */
	public Title(String title, int height) {
		this.title = title;
		this.height = height;
	}

	/**
	 * Get the text of the title
	 * @return The text of the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Get the height of the title
	 * @return The height of the title
	 */
	public int getHeight() {
		return height;
	}

	@Override
	public String toString() {
		return "Title [title=" + title + ", height=" + height + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Title other = (Title) obj;
		if (height != other.height)
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		return true;
	}
	
	
}
