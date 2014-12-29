package graph;
import java.io.FileOutputStream;
import java.io.PrintStream;

import javax.swing.JTextArea;

public class CustomErrStream extends PrintStream {

	private JTextArea outComponent;

	public CustomErrStream(JTextArea area) throws Exception {
		super(new FileOutputStream("./.flux"), true, "ISO-8859-1");
		outComponent = area;
	}

	@Override
	public void write(byte buff[], int off, int len) {
		addToConsole(new String(buff, off, len));
	}

	@Override
	public void write(int b) {
		addToConsole(new String(new char[] { (char) b }));
	}

	@Override
	public void println(String x) {
		addToConsole(x);
	}

	private void addToConsole(String message) {
		outComponent.setText(message);
		/*StyledDocument doc = outComponent.getStyledDocument();
		doc.getStyle("error");
		try {
			if (!message.endsWith("\n")) {
				println();
			}
			doc.insertString(doc.getLength(), message,
					doc.getStyle(StyleContext.DEFAULT_STYLE));
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		outComponent.setCaretPosition(doc.getLength() - message.length());*/
	}

}