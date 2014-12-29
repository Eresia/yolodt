package main;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import core.Document;
import core.Folder;
import core.Storage;

public class Yolodt {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		ArrayList<String> argsList = new ArrayList<String>(Arrays.asList(args));
		if(args[0].equals("-d")){
			Storage storage = new Storage(".");
			argsList.remove(0);
			for(String s: argsList){
				File f = new File(s);
				if(!f.isDirectory()){
					System.out.println("Warning : "+s+" is not a directory : ignored.");
				}else{
					Folder folderToAdd = new Folder(s);
					System.out.println(folderToAdd);
					storage.writeFolder(folderToAdd);
				}
			}
		}
		else if(args[0].equals("-f")){
				argsList.remove(0);
				if(argsList.size()>1 || argsList.size()<=0){
					System.out.println("Erreur : Nombre de paramètres incorrect");
				}
				System.out.println(new Document(argsList.get(0)));
				
				
		}else if(args[0].equals("-w")){
				argsList.remove(0);
				Storage storage = new Storage(".");
				HashMap<String, Folder> collection = storage.readFolder();
				for(String key: collection.keySet()){
					ArrayList<Document> findingsByFolder = new ArrayList<Document>();
					collection.get(key).searchWords(argsList);
					findingsByFolder = collection.get(key).docSort();
					if(!findingsByFolder.isEmpty()){
						for(Document finding : findingsByFolder){
							System.out.println(finding);
						}
					}
				}
			}
		}
}