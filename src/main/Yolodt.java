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
//		ArrayList<String> argsList = new ArrayList<String>(Arrays.asList(args));
//		if(args[0].equals("add")){
//			Storage storage = new Storage(".");
//			argsList.remove(0);
//			for(String s: argsList){
//				File f = new File(s);
//				if(!f.isDirectory()){
//					System.out.println("Warning : "+s+" is not a directory : ignored.");
//				}else{
//					Folder folderToAdd = new Folder(s);
//					System.out.println(folderToAdd);
//					storage.writeFolder(folderToAdd);
//				}
//			}	
//		}else{
//			if(args[0].equals("search")){
//				argsList.remove(0);
//				Storage storage = new Storage(".");
//				HashMap<String, Folder> collection = storage.readFolder();
////				for(String key: collection.keySet()){
////					collection.get(key).parseSearch(argsList);
////				}
				Document d = new Document("res/content.odt");
				ArrayList<String> test = new ArrayList<String>();
				test.add("Coucou");
				test.add("\"Bonjour");
				test.add("Monsieur\"");
				test.add("lol");
				System.out.println(d.parseSearch(test).get(2));
				System.out.println(d.parseSearch(test).get(3));
			//}
		//}
	}
}