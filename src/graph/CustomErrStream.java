package graph;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;

/**
 * Class that redirect error messages flux to window
 * @author ABADJI Julien & LEPESANT Bastien
 *
 */
public class CustomErrStream extends PrintStream {

	private JTextArea outComponent;

	/**
	 * Constructor
	 * @param area
	 * @throws Exception If it is impossible to access to flux
	 */
	public CustomErrStream(JTextArea area) throws Exception {
		super(new FileOutputStream("./.flux"), true, "ISO-8859-1");
		outComponent = area;
	}

	/**
	 * Print a String in flux
	 * @param message
	 */
	@Override
	public void println(String message) {
		outComponent.setText(message);
	}

	@Override
	public String toString() {
		return "CustomErrStream [outComponent=" + outComponent + "]";
	}
	
	

}