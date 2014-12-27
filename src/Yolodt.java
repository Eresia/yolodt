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
		File tmp = folderToStudy.openTmp(folderToStudy.getPath());
		ArrayList<Document> odtToStudy = folderToStudy.searchODT(folderToStudy.getPath()); //On choppe les ODT dedans
		Iterator i = odtToStudy.iterator();
		while(i.hasNext()){
			UnZip uz = new Un
			System.out.println(i.next());
		}
	}
}
