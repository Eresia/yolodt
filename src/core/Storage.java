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

		// Lis les objets présents dans "data.db" du path et les renvois sous un
		// ArrayList
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
						fold.put((String) ois.readObject(), (Folder) ois.readObject());
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
		
		public HashMap<String, Folder> readFolder() {
			return readFolder("data.db");
		}

		// Ecrit l'HashMap dans le "data.db"
		public boolean writeFolder(Folder fold) {
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
						HashMap<String, Folder> ancData =  readFolder("data.db-a");
						for(String key : ancData.keySet()){
							oos.writeObject(key);
							oos.writeObject(ancData.get(key));
						}
						ancDb.delete();
					}
					oos.writeObject(fold.getPath());
					oos.writeObject(fold);
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
