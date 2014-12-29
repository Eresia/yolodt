package main;
import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

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
		

	}

	
	
	
	
	
	// FONCTION STOCKAGE**********************************************************************************************************

	// Lis les objets présents dans "data.db" du path et les renvois sous un
	// ArrayList
	public static HashMap<String, Folder> readObject(String path, String fileName) {
		File folder = new File(path);
		ObjectInputStream ois;
		HashMap<String, Folder> fold = new HashMap<String, Folder>();
		try {
			ois = new ObjectInputStream(new BufferedInputStream(
					new FileInputStream(folder.getAbsolutePath()
							+ File.separator + fileName)));
			try {
				while (true) {
					fold.put((String) ois.readObject(), (Folder) ois.readObject());
				}
			} catch (EOFException e) {
				ois.close();
				return  fold;
			} catch (ClassNotFoundException e) {
				System.out.println("Class no exist (in read)");
				e.printStackTrace();
				return null;
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found (in read)");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.out.println("IOException (in read)");
			e.printStackTrace();
			return null;
		}
	}
	
	public static HashMap<String, Folder> readObject(String path) {
		return readObject(path, "data.db");
	}

	// Ecrit l'HashMap dans le "data.db"
	public static boolean writeObject(Folder fold, String path) {
		File folder = new File(path);
		boolean hasAnc = false;
		File ancDb = new File(folder.getAbsolutePath() + File.separator + "data.db");
		if(ancDb.exists()){
			ancDb.renameTo(ancDb = new File(folder.getAbsolutePath() + File.separator + "data.db-a"));
			hasAnc = true;
		}
		
		if (folder.isDirectory()) {
			try {
				FileOutputStream fos = new FileOutputStream(folder.getAbsolutePath() + File.separator + "data.db");
				ObjectOutputStream oos = new ObjectOutputStream(fos);
				if(hasAnc)
				{
					HashMap<String, Folder> ancData =  readObject(folder.getAbsolutePath(), "data.db-a");
					for(String key : ancData.keySet()){
						System.out.println("a");
						oos.writeObject(key);
						oos.writeObject(ancData.get(key));
					}
					System.out.println("b");
					ancDb.delete();
				}
				oos.writeObject(fold.getPath());
				oos.writeObject(fold);
				fos.close();
				oos.close();
				return true;
			} catch (FileNotFoundException e) {
				System.out.println("File not found");
				e.printStackTrace();
				return false;
			} catch (IOException e) {
				System.out.println("Error in Write");
				e.printStackTrace();
				return false;
			}
		} else {
			System.out.println(folder.getAbsolutePath() + " is a directory");
			return false;
		}
	}
}
