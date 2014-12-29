package main;

import graph.CustomErrStream;
import graph.GraphInter;

import java.util.HashMap;

import core.Folder;


/**
 * GUI main
 * @author ABADJI Julien & LEPESANT Bastien
 */
public class TestGUI {

	/**
	 * Graphic main
	 * @param args
	 */
	public static void main(String[] args) {
		
		//Window Creation
		GraphInter gui = new GraphInter("yolodt");
		
		//Error messages redirection to window
		try{
			System.setErr(new CustomErrStream(gui.getErrorArea()));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Windows Display
		gui.setVisible(true);
		
	}

}
