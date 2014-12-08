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

public class FileDocument {

	/**
	 * @param args
	 * @throws IOException
	 */
	public static void main(String[] args) {

		// TEST IO (READ/WRITE OBJECT)
		
		/*File folder = openTmp("/home/eresia/Documents");
		ArrayList<Document> docs1 = new ArrayList<Document>();
		ArrayList<Document> docs2;
		for (int i = 0; i < 5; i++) {
			docs1.add(new Document("coucou " + Integer.toString(i)));
		}
		// System.out.println(docs1);
		if (writeObject(docs1, folder)) {
			docs2 = (ArrayList<Document>) readObject(folder);
			try {
				for (Document doc : docs2) {
					System.out.println(doc.toString());
				}
			} catch (NullPointerException e) {
				System.out.println("docs2 null");
			}
		}*/
		
		// TEST isODT
		
		/*String path = ("/home/eresia/Documents/S3/exemple.odt");
		System.out.println(isODT(path));*/
		
		// TEST recherche
		String path = ("/home/eresia/Documents/S3/testProgramme");
		System.out.println(searchODT(path));
	}

	public static ArrayList readObject(File folder) {
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

	public static boolean writeObject(ArrayList objs, File folder) {
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

	public static File openTmp(String path) {
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

	public static void deleteFolder(File file) {
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
	
	public static boolean isODT(String path){
	
		try{
			Path pathTest = new File(path).toPath();
			System.out.println(Files.probeContentType(pathTest));
			return Files.probeContentType(pathTest).equals("application/vnd.oasis.opendocument.text");
		}catch(IOException e){
			e.printStackTrace();
			return false;
		}
	}
	
	public static ArrayList<Document> searchODT(String path){
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
	
	public static void listDocument(File folder, ArrayList<String> docs){
		if(!folder.isDirectory()){
			docs.add(folder.getAbsolutePath());
		}
		else{
			for(int i = 0; i < folder.list().length; i++){
				listDocument(new File(folder.getAbsolutePath() + File.separator + folder.list()[i]), docs);
			}
		}
	}

}
