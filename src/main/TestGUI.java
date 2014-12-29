package main;

import graph.CustomErrStream;
import graph.GraphInter;


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
			e.printStackTrace();
		}
		
		//Windows Display
		gui.setVisible(true);
		
	}

}
