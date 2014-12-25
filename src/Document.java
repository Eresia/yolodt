import java.io.Serializable;
import java.util.ArrayList;


public class Document implements Serializable{

	private static final long serialVersionUID = 1L;
	private String path;
	private ArrayList<Title> titles = new ArrayList<Title>();
	
	public Document(String path)
	{
		this.path = path;
		parse(path);
	}
	
	public void parse(String path){
		
	}
	
	public String getPath() {
		return path;
	}
	
	public ArrayList<Title> getTitles(){
		return titles;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String toString()
	{
		return "path : " + path + "\n Titles : " + titles.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((titles == null) ? 0 : titles.hashCode());
		result = prime * result + ((path == null) ? 0 : path.hashCode());
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
		Document other = (Document) obj;
		if (titles == null) {
			if (other.titles != null)
				return false;
		} else if (!titles.equals(other.titles))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
}
