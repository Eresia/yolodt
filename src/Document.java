import java.io.Serializable;
import java.util.ArrayList;


public class Document implements Serializable{

	private static final long serialVersionUID = 1L;
	private String path;
	private ArrayList<Attribute> attributes = new ArrayList<Attribute>();
	
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
	
	public ArrayList<Attribute> getAttribute(){
		return attributes;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public String toString()
	{
		return "path : " + path + "\n Attributes : " + attributes.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((attributes == null) ? 0 : attributes.hashCode());
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
		if (attributes == null) {
			if (other.attributes != null)
				return false;
		} else if (!attributes.equals(other.attributes))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
}
