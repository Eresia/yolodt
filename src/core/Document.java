package core;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * The class that represent a ODT File
 * @author ABADJI Julien & LEPESANT Bastien
 *
 */
@SuppressWarnings("serial")
public class Document implements Serializable{

	private String path;
	private int weight;
	private ArrayList<Title> titles;
	
	/**
	 * Constructor
	 * @param path
	 * @throws IOException If the file is corrupt
	 */
	public Document(String path) throws IOException	
	{
		this.path = path;
		this.weight = 0;
		extractTitles();
	}
	/**
	 * Unzip and parse an ODT File
	 * @throws IOException If the File is corrupt
	 */
	public void extractTitles() throws IOException{ //This method handles the unzip, parse and delete temporary folders procedures.
		String tmpPath = Paths.get(path).getParent()+File.separator+"tmp"; 
		File tmpFile = new File(tmpPath);
		String xmlPath = tmpPath+File.separator+"content.xml"; //We need to manipulate paths in two different types, so we have 2 vars by path.
		File xmlFile = new File(xmlPath);
		unZip(path, tmpPath.toString());  //Unzips the content.xml onto the temporary folder.
		titles = parse(xmlPath); //Fills titles with the titles parsed.
		xmlFile.delete();
		tmpFile.delete(); //Deletes the temporary files. 
	}
	
	/**
	 * Parse the ODT File
	 * @param path
	 * @return The List of the titles
	 * @throws IOException If the File is corrupt
	 */
	public ArrayList<Title> parse(String path) throws IOException{ //Calls the Parser object.
		Parser p = new Parser(path);
		return p.getTitles();
	}
	
	/**
	 * Check operator about keywords
	 * @param words
	 * @return An ArrayList with informations
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public ArrayList parseSearch(ArrayList<String> words){
		boolean hasAndOperator=false; // LOUIS HELPS : Think to operator. Wesh
		String keyWords="";
		ArrayList parsedSearch = new ArrayList();
		if(words.get(0).equals("+")){ //If "+", it is AND operator
			hasAndOperator = true;
			words.remove(0);
		}else if(words.get(0).equals("-")){ //If "-" or nothing, it is OR operator
			words.remove(0);
		}
		parsedSearch.add(hasAndOperator);
		for(String word : words){
			keyWords+=word+" ";
		}
		Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(keyWords); //Group word between quotes
		while (m.find()){
		    parsedSearch.add(m.group(1).replace("\"", ""));
		}
		return parsedSearch;
	}
	
	/**
	 * Do search by keywords in Documents
	 * @param unparsedSearch
	 */
	public void searchWords(ArrayList<String> unparsedSearch){ 
		@SuppressWarnings("rawtypes")
		ArrayList parsedSearch = parseSearch(unparsedSearch); //Check operators informations
		boolean hasAndOperator = (boolean) parsedSearch.get(0);
		parsedSearch.remove(0);
		@SuppressWarnings("unchecked")
		ArrayList<String> words = (ArrayList<String>) parsedSearch;
		weight = 0;
		boolean[] used = new boolean[parsedSearch.size()]; //Table to check if one word is not used
		for(int i = 0 ; i < parsedSearch.size(); i++){
			used[i] = false;
		}
		for(Title t : titles){
			for(String w : words){
				if(t.getTitle().contains(w)){
					weight += 5 - t.getHeight(); //Affect a weight to document
					used[words.indexOf(w)] = true;
				}
			}
		}
		if(hasAndOperator){ //If it is AND operator
			for(int i = 0; i < words.size(); i++){
				if(used[i] == false){ //If one word is not used
					weight = 0; //The ODT has not weight
				}
			}
		}
	}
	
	/**
	 * Unzip the ODT
	 * @param zipFile
	 * @param outputFolder
	 */
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

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}
	
	/**
	 * Get the path
	 * @return The path
	 */
	public String getPath() {
		return path;
	}
	
	/**
	 * Get the weight
	 * @return The weight
	 */
	public int getWeight(){
		return weight;
	}
	
	/**
	 * Get the titles
	 * @return The titles
	 */
	public ArrayList<Title> getTitles(){
		return titles;
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
