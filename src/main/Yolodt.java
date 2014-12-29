package main;
import java.io.File;
import java.io.IOException;
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
		ArrayList<String> argsList = new ArrayList<String>(Arrays.asList(args)); //For more convinience, we convert the arguments' array onto an ArrayList.
		if(args[0].equals("-d")){ // -d means adding a directory to the database.
			Storage storage = new Storage("."); //We open the storage object to the current path
			argsList.remove(0); //Since we don't need the -d argument now, we remove it from the list.
			for(String s: argsList){ //The for loop makes the program handle multiple path additions.
				File f = new File(s); 
				if(!f.isDirectory()){ //We check if the file is a directory, and we notice to the user if it's not.
					System.err.println("Warning : "+s+" is not a directory : ignored.");
				}else{
					Folder folderToAdd = new Folder(s); //We construct a Folder object, who will call the unzipping and parsing methods for each odt in the path.
					System.out.println(folderToAdd); //We let the user know what was added to the database.
					storage.writeFolder(folderToAdd); //We write the Folder object onto the database
				}
			}
		}
		else if(args[0].equals("-f")){ // -f means we want to have informations on a specified odt.
				argsList.remove(0);
				if(argsList.size()>1 || argsList.size()<=0){ //We check for an invalid number of arguments, and we notice the user.
					System.err.println("Erreur : Nombre de paramètres incorrect");
				}else{
					Document d;
					try {
						d = new Document(argsList.get(0)); //try...catch, since it's IO.
						System.out.println(d); //We print the informations.
					} catch (IOException e) {
						System.err.println("Error : Can't open "+argsList.get(0));
						e.printStackTrace();
					}
				}
				
				
		}else if(args[0].equals("-w")){ // -w means we want to search.
				argsList.remove(0);
				Storage storage = new Storage(".");
				HashMap<String, Folder> collection = storage.readFolder(); //The Hashmap of all the folders we have on the database.
				for(String key: collection.keySet()){ // We iterate over the Folder objects.
					ArrayList<Document> findingsByFolder = new ArrayList<Document>(); //Here we have an ArrayList of all the Document of the Folder.
					collection.get(key).searchWords(argsList); //We call our search method, who will give a weight on each Document. 
					findingsByFolder = collection.get(key).docSort(); //We sort our Documents by their weight = interestingness.
					if(!findingsByFolder.isEmpty()){
						for(Document finding : findingsByFolder){
							System.out.println(finding);  //We print our findings.
						}
					}else{ //If we find nothing, we say it to the user :(
							System.err.println("No documents containing the word(s) " + argsList.toString());
					}
				}
			}else{
			System.err.println(
					"Error : Invalid arguments.\n"
					+ "Usage :\n"
					+ " -d <dir1> <dir2> ... : Add directories to database \n "
					+ "-f <file> : Read file informations \n "
					+ "-w <words> : Search onto the database\n");
		}
}
}