import java.io.Serializable;
import java.util.ArrayList;
import java.util.ListIterator;


public class Document implements Serializable{

	private static final long serialVersionUID = 1L;
	private String path;
	private int weight;
	private ArrayList<Title> titles;
	
	public Document(String path)	
	{
		this.path = path;
		this.weight = 0;
		//parse(path);
	}
	
	public void parse(String path){
		Parser p = new Parser(path);
		//titles = p.getText();
	}
	
	// LOUIS HELPS : Penser aux opérateurs. Wesh
	public void searchWords(ArrayList<String> words, boolean isOROp){
		boolean[] used = new boolean[words.size()];
		for(int i = 0 ; i < words.size(); i++){
			used[i] = false;
		}
		for(Title t : titles){
			ListIterator lI = words.listIterator();
			while(lI.hasNext()){
				int nextInd = lI.nextIndex();
				if(t.getTitle().contains((String)lI.next())){
					weight += 6 - t.getHeight();
					used[nextInd] = true;
				}
			}
		}
		if(!isOROp){
			for(int i = 0; i < words.size(); i++){
				if(used[i] == false){
					weight = 0;
				}
			}
		}
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
