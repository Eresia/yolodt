import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;

public class Yolodt {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Folder folderToStudy = new Folder("res/"); //On balance le dossier à étudier
		ArrayList<Document>odtToStudy = folderToStudy.searchODT();
		for(Document d : odtToStudy){
			Document curr = new Document(d.getPath());
			curr.extractTitles();
			curr.getPrettyOutput();
		}
		
	}
}
 