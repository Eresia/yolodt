import java.io.File;
import java.io.IOException;

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
	NodeList textH;//Titles in text:h Elements
	NodeList textP; //Titles in test:p Elements
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
		} // On crée tout le bordel pour parser.
		root = document.getDocumentElement(); //On choppe la racine.
		rootNode = root.getChildNodes(); //Les enfants de la racine sémignon.
	}
	
	public void setTextH(){
		textH = root.getElementsByTagName("text:h");
		for (int i = 0; i < textH.getLength(); i++) {
			Element title = (Element) textH.item(i);
			System.out.println(title.getAttribute("text:outline-level"));
			System.out.println(textH.item(i).getTextContent());	
		}
	}
	public void getTextH(){
		
	}
		//TODO : La même avec les titres en p. ( En gros chopper un documentTitle. e du
		//choppe les titres des docs
		// VINCENT HELPS : getElementsByTagName marche récursivement, appelle le dans tout le office:document-content. Wesh
		// A stocker : Dans l'arrayList, le premier item c'est le titre principal, de hauteur 0, et après on met tout le reste. 
}
