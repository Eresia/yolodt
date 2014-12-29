package core;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Folder implements Serializable{

	private String path;
	private ArrayList<Document> docs = new ArrayList<Document>();

	public Folder(String path) {
		this.path = path;
		searchODT();
	}
	
	public String getPath(){
		return path;
	}
	
	//FONCTION IO*****************************************************************************************************************************
	
	//Creates a temporary folder onto the path of the file. 
	//We can enhance this by using Java's temporary folders.( see TODO ).  
	public File createTmp() { //TODO: voir avec createTempDirectory
		File folder;
		try {
			folder = new File(path + File.separator + "tmp" + File.separator); 
			if (!(folder.exists())) { 
				folder.mkdir();
			} else {
				deleteFolder(folder); //Avoid having 'dirty' temporary folders, especially in case of program's interruption.
				folder.mkdir();
			}
			return folder;
		} catch (Exception e) {
			System.err.println("Error creating temporary folder.");
			return null;
		}

	}
	
	
	public void searchODT(){  //Recursively constructs an ArrayList with all the files with the odt type.
		ArrayList<String> files = new ArrayList<String>();
		File folder = new File(path);
		listDocument(folder, files);
		for(String docAct : files){
			if(isODT(docAct)){
				docs.add(new Document(docAct));
			}
		}
		if(!docs.isEmpty()){
			createTmp(); //We create the temporary files here to avoid useless creating if the hashmap is empty.
		}
	}
	
	
	
	
	
	//Io Fonctions "lib" ***********************************************************************************************************************************

	//Recursively delete a Folder and all of its content.
	public void deleteFolder(File file) {
		if (file.isDirectory()) {
			for (int i = 0; i < file.list().length; i++) {
				deleteFolder(new File(file.getAbsolutePath() + File.separator
						+ file.list()[i]));
			}
			file.delete();
		} else {
			file.delete();
		}
	}
	
	//Creates an ArrayList from the folder's content.
	public void listDocument(File folder, ArrayList<String> files){
		if(!folder.isDirectory()){
			files.add(folder.getAbsolutePath());
		}
		else{
			for(int i = 0; i < folder.list().length; i++){
				listDocument(new File(folder.getAbsolutePath() + File.separator + folder.list()[i]), files);
			}
		}
	}
	
	//Checks if a file is an Open Document Text file.
	public boolean isODT(String path){
	
		try{
			Path pathTest = new File(path).toPath();
			//System.out.println(Files.probeContentType(pathTest));
			return Files.probeContentType(pathTest).equals("application/vnd.oasis.opendocument.text"); //Check for MIME-type, stronger than checking for file's extension.
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	//On Documents Fonctions ********************************************************************************************************************************
	
	public void searchWords(ArrayList<String> words, boolean hasOrOperator){
		for(Document d : docs){
			d.searchWords(words, hasOrOperator);
		}
	}
	
	public ArrayList<Document> DocSorting(){
		ArrayList<Document> docsSort = new ArrayList<Document>();
		for(Document docPlacing : docs){
			if(docPlacing.getWeight() != 0){
				if(docsSort.isEmpty()){
					docsSort.add(docPlacing);
				}
				else{
					for(Document d : docsSort){
						if(d.getWeight() < docPlacing.getWeight()){
							docsSort.add(docsSort.indexOf(d), docPlacing);
							break;
						}
					}
					if(!docsSort.contains(docPlacing)){
						docsSort.add(docPlacing);
					}
				}
			}
		}
		return docsSort;
	}
	
	//Others Fonctions ***********************************************************************************************************************************

	@Override
	public String toString() {
		return "Folder [path=" + path + ", docs=" + docs + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((docs == null) ? 0 : docs.hashCode());
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
		Folder other = (Folder) obj;
		if (docs == null) {
			if (other.docs != null)
				return false;
		} else if (!docs.equals(other.docs))
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}
	
	

}
