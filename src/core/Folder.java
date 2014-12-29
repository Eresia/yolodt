package core;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

/**
 * Class to represent a folder and his documents
 * @author ABADJI Julien & LEPESANT Bastien
 *
 */
@SuppressWarnings("serial")
public class Folder implements Serializable {

	private String path;
	private ArrayList<Document> docs = new ArrayList<Document>();

	/**
	 * Constructor
	 * @param path
	 */
	public Folder(String path) {
		File f = new File(path);
		
		try {
			this.path = f.getCanonicalPath();
		} catch (IOException e) {
			System.err.println("Path not correct");;
		}
		
		searchODT();
	}

	/**
	 * Get the path of the folder
	 * @return The path of the folder
	 */
	public String getPath() {
		return path;
	}

	// FONCTION
	// IO*****************************************************************************************************************************

	/**
	 * Creates a temporary folder onto the path of the file.
	 * @return the folder created
	 */
	public File createTmp() {
		File folder;
		try {
			folder = new File(path + File.separator + "tmp" + File.separator);
			if (!(folder.exists())) {
				folder.mkdir();
			} else {
				deleteFolder(folder); // Avoid having 'dirty' temporary folders,
										// especially in case of program's
										// interruption.
				folder.mkdir();
			}
			return folder;
		} catch (Exception e) {
			System.err.println("Error creating temporary folder.");
			return null;
		}

	}

	/**
	 * Make ArrayList of ODT in folder and create temporary folder
	 */
	public void searchODT() {
		
		File folder = new File(path);
		listDocument(folder, docs);

		if (!docs.isEmpty()) {
			createTmp(); // creating if the hashmap is not empty.
		}
	}

	// Io Fonctions "lib"
	// ***********************************************************************************************************************************

	/**
	 * Recursively delete a Folder and all of its content.
	 * @param file
	 */
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

	/**
	 * Recursively constructs an ArrayList with all the files with the odt type
	 * @param folder
	 * @param files
	 */
	public void listDocument(File folder, ArrayList<Document> files) {
		if (folder.canRead() && !folder.isHidden()) {
			if (!folder.isDirectory()) {
				if (isODT(folder.getPath())) {
					try{
						files.add(new Document(folder.getPath()));
					//If the ODT is Corrompt or it is a bad extension of file, it is ignored
					}catch(IOException e){
						System.err.println("Corrompt ODT");
					}
				}
			} else {
				for (int i = 0; i < folder.list().length; i++) {
					listDocument(new File(folder.getPath() + File.separator
							+ folder.list()[i]), files);
				}
			}
		}
	}

	/**
	 * Checks if a file is an Open Document Text file.
	 * @param path
	 * @return If the File is an ODT file
	 */
	public boolean isODT(String path) {

		try {
			Path pathTest = new File(path).toPath();
			// System.out.println(Files.probeContentType(pathTest));
			return Files.probeContentType(pathTest).equals(
					"application/vnd.oasis.opendocument.text"); // Check for
																// MIME-type,
																// stronger than
																// checking for
																// file's
																// extension.
		} catch (IOException e) {
			System.err.println(e.getMessage());
			return false;
		}
	}

	// On Documents Fonctions
	// ********************************************************************************************************************************

	/**
	 * Make the search by keywords in all Documents
	 * @param words
	 */
	public void searchWords(ArrayList<String> words) {
		for (Document d : docs) {
			d.searchWords(words);
		}
	}

	/**
	 * Sort all Documents by their weights
	 * @return The List of sorting documents
	 */
	public ArrayList<Document> docSort() {
		ArrayList<Document> docsSort = new ArrayList<Document>();
		for (Document docPlacing : docs) {
			if (docPlacing.getWeight() != 0) { //If Document have a null weight, it is ignored
				if (docsSort.isEmpty()) { //If the List is empty, make it on the first place
					docsSort.add(docPlacing);
				} else {
					for (Document d : docsSort) {
						if (d.getWeight() < docPlacing.getWeight()) { //Place the Document in right place
							docsSort.add(docsSort.indexOf(d), docPlacing);
							break;
						}
					}
					if (!docsSort.contains(docPlacing)) {
						docsSort.add(docPlacing);
					}
				}
			}
		}
		return docsSort;
	}
	
	/**
	 * Get the List of the documents
	 * @return The List of the documents
	 */
	public ArrayList<Document> getDocuments(){
		return docs;
	}
	

	// Others Fonctions
	// ***********************************************************************************************************************************


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
