import java.io.BufferedInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

public class Folder {

	private String path;

	public Folder(String path) {
		this.path = path;
	}
	
	public String getPath(){
		return path;
	}
	
	//FONCTION IO*****************************************************************************************************************************
	
	//Ouvre un dossier tmp dans le path (S'il esxiste déja on le supprime et on le recréé
	public File createTmp(String path) {
		File folder;

		try {
			folder = new File(path + File.separator + "tmp" + File.separator);
			if (!(folder.exists())) {
				folder.mkdir();
			} else {
				deleteFolder(folder);
				folder.mkdir();
			}
			return folder;
		} catch (Exception e) {
			System.out.println("Erreur sur openTmp");
			return null;
		}

	}
	
	//Construit un tableau avec tout les ODT présent dans le path
	public ArrayList<Document> searchODT(String path){
		ArrayList<String> docs = new ArrayList<String>();
		ArrayList<Document> odt = new ArrayList<Document>();
		File folder = new File(path);
		listDocument(folder, docs);
		for(String docAct : docs){
			if(isODT(docAct)){
				odt.add(new Document(docAct));
			}
		}
		
		
		return odt;
	}
	
	
	
	
	
	//FONCTION IO "lib" ***********************************************************************************************************************************
	
	//Supprime un fichier/dossier et tout ses sous dossiers
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
	
	//Créé un ArayList de tout les fichiers présent dans le dossier et ses sous dossiers
	public void listDocument(File folder, ArrayList<String> docs){
		if(!folder.isDirectory()){
			docs.add(folder.getAbsolutePath());
		}
		else{
			for(int i = 0; i < folder.list().length; i++){
				listDocument(new File(folder.getAbsolutePath() + File.separator + folder.list()[i]), docs);
			}
		}
	}
	
	//Check si un fichier est un ODT
	public boolean isODT(String path){
	
		try{
			Path pathTest = new File(path).toPath();
			System.out.println(Files.probeContentType(pathTest));
			return Files.probeContentType(pathTest).equals("application/vnd.oasis.opendocument.text");
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	//FONCTION STOCKAGE**********************************************************************************************************

	//Lis les objets présents dans "data.db" du path et les renvois sous un ArrayList
	public ArrayList readObject(String path) {
		File folder = new File(path);
		ObjectInputStream ois;
		ArrayList objs = new ArrayList();
		Object reading;
		try {
			ois = new ObjectInputStream(new BufferedInputStream(
					new FileInputStream(folder.getAbsolutePath()
							+ File.separator + "data.db")));
			try {
				while (true) {
					reading = ois.readObject();
					objs.add(reading);
				}
			} catch (EOFException e) {
				ois.close();
				return objs;
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
	
	//Ecrit les objets de l'ArrayList dans le data.db du path
	public boolean writeObject(ArrayList objs, String path) {
		File folder = new File(path);
		ObjectOutputStream oos;
		if (folder.isDirectory()) {
			try {
				oos = new ObjectOutputStream(new FileOutputStream(
						folder.getAbsolutePath() + File.separator + "data.db"));
				for (Object obj : objs) {
					oos.writeObject(obj);
				}
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
