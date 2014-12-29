package core;

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

public class Storage {
	
	private String path;
	
	public Storage(String path){
		this.path = path;
	}
	
	// FONCTION STOCKAGE**********************************************************************************************************

		// Read HashMap in fileName
		public HashMap<String, Folder> readFolder(String fileName) {
			File folder = new File(path);
			ObjectInputStream ois;
			HashMap<String, Folder> fold = new HashMap<String, Folder>();
			try {
				ois = new ObjectInputStream(new BufferedInputStream(
						new FileInputStream(folder.getAbsolutePath()
								+ File.separator + fileName)));
				try {
					while (true) {
						fold = (HashMap<String, Folder>) ois.readObject();
					}
				} catch (EOFException e) {
					ois.close();
					return  fold;
				} catch (ClassNotFoundException e) {
					System.err.println("Class no exist (in read)");
					return null;
				}
			} catch (FileNotFoundException e) {
				return new HashMap<String, Folder>();
			} catch (IOException e) {
				System.err.println("IOException (in read)");
				return null;
			}
		}
		
		//Read HashMap in "data.db"
		public HashMap<String, Folder> readFolder() {
			return readFolder("data.db");
		}

		// Write the HashMap in "data.db"
		public boolean writeFolder(Folder fold) {
			File folder = new File(path);
			HashMap<String, Folder> data = readFolder();
			data.put(fold.getPath(), fold);
			if (folder.isDirectory()) {
				try {
					FileOutputStream fos = new FileOutputStream(folder.getAbsolutePath() + File.separator + "data.db");
					ObjectOutputStream oos = new ObjectOutputStream(fos);
					oos.writeObject(data);
					fos.close();
					oos.close();
					return true;
				} catch (FileNotFoundException e) {
					System.err.println("File not found");
					e.printStackTrace();
					return false;
				} catch (IOException e) {
					System.err.println("Error in Write");
					e.printStackTrace();
					return false;
				}
			} else {
				System.err.println(folder.getAbsolutePath() + " is a directory");
				return false;
			}
		}

}
