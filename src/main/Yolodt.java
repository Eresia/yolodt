package main;
import core.Folder;

public class Yolodt {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Folder folderToStudy = new Folder("./src/"); // On balance le dossier à
													// étudier
		/*
		 * ArrayList<Document> odtToStudy = folderToStudy.searchODT();
		 * for(Document d : odtToStudy){ Document curr = new
		 * Document(d.getPath()); curr.extractTitles(); curr.getPrettyOutput();
		 * }
		 */
		
		
		//TEST IO
		/*HashMap<String, Folder> test = new HashMap<String, Folder>();
		HashMap<String, Folder> test2;
		test.put("coucou", new Folder("coucou"));
		test.put("coucou25", new Folder("coucou25"));
		test.put("coucou50", new Folder("coucou50"));
		
		writeObject(new Folder("coucou2"), "./src");
		writeObject(new Folder("coucou254"), "./src");
		writeObject(new Folder("coucou509"), "./src");
		test2 = (HashMap<String, Folder>) readObject("./src");
		
		System.out.println(test.toString());
		System.out.println(test2.toString());*/
		
		//TEST TRI DOCUMENT
		/*folderToStudy.addDocument("1", new Document("1"), 2);
		folderToStudy.addDocument("2", new Document("2"), 8);
		folderToStudy.addDocument("3", new Document("3"), 1);
		folderToStudy.addDocument("4", new Document("4"), 3);
		
		System.out.println(folderToStudy.getDocuments());
		System.out.println(folderToStudy.DocSorting());*/
		
	}

}
