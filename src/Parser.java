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
	ArrayList<Title> titles;
	
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
		grepTitles();
	}
	
	public void grepTitles(){
		titles = new ArrayList<Title>();
		textP = root.getElementsByTagName("text:p");
		textH = root.getElementsByTagName("text:h");
		for(int i = 0; i<textP.getLength(); i++){
			Element title = (Element) textP.item(i);			
			if(title.getAttribute("text:style-name").equals("P1")){
				//System.out.println("height = 0");
				//System.out.println(title.getTextContent());
				titles.add(new Title(title.getTextContent(), 0));
				//Foutre dans l'AL : String & height=0
			}
			if(title.getAttribute("text:style-name").equals("Subtitle")){
				//System.out.println("height = 0");
				//System.out.println(title.getTextContent());
				titles.add(new Title(title.getTextContent(), 1));
				//Foutre dans l'AL : String & height=0
			}
			//System.out.println(textP.item(i).getTextContent());	
		}
		for (int i = 0; i < textH.getLength(); i++) {
			Element title = (Element) textH.item(i);
			int height = Integer.parseInt(title.getAttribute("text:outline-level"));
//			System.out.println(title.getAttribute("text:outline-level"));
//			System.out.println(textH.item(i).getTextContent());
			titles.add(new Title(title.getTextContent(), height+1));
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
	
		//TODO : La même avec les titres en p. ( En gros chopper un documentTitle. e du
		//choppe les titres des docs
		// VINCENT HELPS : getElementsByTagName marche récursivement, appelle le dans tout le office:document-content. Wesh
		// A stocker : Dans l'arrayList, le premier item c'est le titre principal, de hauteur 0, et après on met tout le reste. 
}
