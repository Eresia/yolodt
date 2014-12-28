import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;

public class Parser {
	DocumentBuilder builder;
	Document document;
	Element root;
	NodeList rootNode;
	NodeList textH;//Text in text:h Elements
	NodeList textP; //Text in test:p Elements
	NodeList titleH; //Title in text:h Elements
	NodeList titleP; //Title in text:p Elements
	ArrayList<Title> titles; //Contains parsed titles
	
	public Parser(String toParse){
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try{
		builder = factory.newDocumentBuilder();
		document = builder.parse(new File(toParse));
		}
		catch(ParserConfigurationException e){
			e.printStackTrace();
		}
		catch(SAXException e){
			e.printStackTrace();
		}
		catch(IOException e){
			e.printStackTrace();
		} // Creating the factory and catching eventual errors.
		root = document.getDocumentElement(); // We'll navigate through this var
		grepTitles(); 
	}
	
	public void grepTitles(){
		titles = new ArrayList<Title>();
		textP = root.getElementsByTagName("text:p"); //We get all the "text:p" and "text:h" elements recursively
		textH = root.getElementsByTagName("text:h");
		for(int i = 0; i<textP.getLength(); i++){ //We compare each "text:p" field in the document to what we expect
			Element title = (Element) textP.item(i);			
			if(title.getAttribute("text:style-name").equals("P1")){ //P1 is the style-name value for grand titles
				titles.add(new Title(title.getTextContent(), 0)); /* When conditions meet, we store the interesting String on an ArrayList declared 
																   * previously, with a height value for later search purpose.*/
			}
			if(title.getAttribute("text:style-name").equals("Subtitle")){ //Same idea
				titles.add(new Title(title.getTextContent(), 1));
			}	
		}
		for (int i = 0; i < textH.getLength(); i++) {
			Element title = (Element) textH.item(i);
			int height = Integer.parseInt(title.getAttribute("text:outline-level")); /* Depending on the importance of the title, the field text:outline-level contains 
			 																		  * a number. Here we convert this number to an int, because getAttribute's type is
			 																		  * String.*/
			if(height<=4){//We're limited to 4 levels of titles.
				titles.add(new Title(title.getTextContent(), height+1)); //We add 1 here because of text:h's height.
			}
		}
	}
	public ArrayList<Title> getTitles(){
		return titles;
	}
	
	@Override
	public String toString(){
		String ret="";
		for(Title t: titles){
			ret+=(t.toString()+"\n");
		}
		return ret;
	} 
}
