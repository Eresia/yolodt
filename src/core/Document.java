package core;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.ListIterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;


public class Document implements Serializable{

	private String path;
	private int weight;
	private ArrayList<Title> titles;
	
	public Document(String path)	
	{
		this.path = path;
		this.weight = 0;
		//parse(path);
	}
	public void extractTitles(){ //This method handles the unzip, parse and delete temporary folders procedures.
		String tmpPath = Paths.get(path).getParent()+File.separator+"tmp"; 
		File tmpFile = new File(tmpPath);
		String xmlPath = tmpPath+File.separator+"content.xml"; //We need to manipulate paths in two different types, so we have 2 vars by path.
		File xmlFile = new File(xmlPath);
		
		unZip(path, tmpPath.toString());  //Unzips the content.xml onto the temporary folder.
		titles = parse(xmlPath); //Fills titles with the titles parsed.
		xmlFile.delete();
		tmpFile.delete(); //Deletes the temporary files. 
	}
	public ArrayList<Title> parse(String path){ //Calls the Parser object.
		Parser p = new Parser(path);
		return p.getTitles();
	}
	
	// LOUIS HELPS : Penser aux opérateurs. Wesh
	public void searchWords(ArrayList<String> words, boolean hasOrOperator){ 
		weight = 0;
		boolean[] used = new boolean[words.size()];
		for(int i = 0 ; i < words.size(); i++){
			used[i] = false;
		}
		for(Title t : titles){
			ListIterator<String> li = words.listIterator();
			while(li.hasNext()){
				int nextInd = li.nextIndex();
				if(t.getTitle().contains((String)li.next())){
					weight += 6 - t.getHeight();
					used[nextInd] = true;
				}
			}
		}
		if(!hasOrOperator){
			for(int i = 0; i < words.size(); i++){
				if(used[i] == false){
					weight = 0;
				}
			}
		}
	}
	public void unZip(String zipFile, String outputFolder) { //Unzips ONLY the 'content.xml' on the odt file.
		byte[] buffer = new byte[1024];
		try {
			// create output directory is not exists
			File folder = new File(outputFolder);
			if (!folder.exists()) {
				folder.mkdir();
			}
			// get the zip file content
			ZipInputStream zis = new ZipInputStream(
					new FileInputStream(zipFile));
			// get the zipped file list entry
			ZipEntry ze = zis.getNextEntry();
			while (ze != null) {
				if (ze.getName().equals("content.xml")) { //Search for content.xml, and unzip it.
					FileOutputStream fos;
					File newFile = new File(outputFolder + File.separator + ze.getName());
					System.out.println("file unzip : " + newFile.getAbsoluteFile());
					fos = new FileOutputStream(newFile);
					int len;
					while ((len = zis.read(buffer)) > 0) { //Write.
						fos.write(buffer, 0, len);
					}
				fos.close();
				}
				ze = zis.getNextEntry();
			}

			zis.closeEntry();
			zis.close(); //We close what we opened.

			//System.out.println("Done");

		} catch (IOException ex) {
			ex.printStackTrace();
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
	
	
	
	@Override
	public String toString() {
		return "Document [path=" + path + ", weight=" + weight + ", titles="
				+ titles + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		result = prime * result + ((titles == null) ? 0 : titles.hashCode());
		result = prime * result + weight;
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
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		if (titles == null) {
			if (other.titles != null)
				return false;
		} else if (!titles.equals(other.titles))
			return false;
		if (weight != other.weight)
			return false;
		return true;
	}

	
}
