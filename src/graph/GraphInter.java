package graph;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;

import core.Document;
import core.Folder;
import core.Storage;

/**
 * Class for GUI
 * @author ABADJI Julien & LEPESANT Bastien
 */
@SuppressWarnings("serial")
public class GraphInter extends JFrame {

	//Buttons
	private JButton updateButton;
	private JButton searchButton;
	private JButton addButton;
	
	//KeyWords text area
	private JTextField keyWords;

	//Principal panel
	private JPanel mainPanel;

	//Top panel
	private JPanel topPanel;
	private ArrayList<LayoutFolder> folders = new ArrayList<LayoutFolder>();

	//Bot panel
	private JPanel botPanel;
	private JList<String> results; // FLORIAN HELPS : Thanks for JList idea. Wesh
	private JTextArea errorArea;

	private int nbFolderCreate;

	private final int sizeX = 700;
	private final int sizeY = 600;
	private final int margin = 5;
	private final int interMargin = 4;

	/**
	 * Window creation
	 * @param title
	 */
	public GraphInter(String title) {
		super();

		buildFen(title);
		buildInter();
	}

	/**
	 * Window creation
	 * @param title
	 */
	public void buildFen(String title) {
		setTitle(title);
		setSize(sizeX, sizeY);
		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	/**
	 * Interface creation
	 */
	public void buildInter() {

		//Panels initialisation
		mainPanel = new JPanel();
		topPanel = new JPanel();
		botPanel = new JPanel();

		//Layout initialisation
		mainPanel.setLayout(new BorderLayout());
		topPanel.setLayout(new GridBagLayout());
		botPanel.setLayout(new GridBagLayout());
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(botPanel, BorderLayout.CENTER);

		//Buttons initialisation
		updateButton = new JButton("Import/Update");
		updateButton.addActionListener(new ActionUpdate());
		searchButton = new JButton("Search/Dtb");
		searchButton.addActionListener(new ActionSearch());
		addButton = new JButton("+");
		addButton.addActionListener(new ActionAdd());
		
		//keyWords Text initialisation
		keyWords = new JTextField();
		keyWords.addActionListener(new ActionUpdate());
		
		//Results and error text area initialisation
		results = new JList<String>();
		results.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		results.setLayoutOrientation(JList.VERTICAL);
		results.addKeyListener(new ActionEnterList());
		results.addMouseListener(new ActionMouseList());
		errorArea = new JTextArea();
		errorArea.setEditable(false);

		//BotPanel place
		GridBagConstraints pos = new GridBagConstraints();
		pos.insets.left = margin;
		pos.insets.bottom = interMargin;
		pos.insets.right = margin;
		pos.gridy = 0;
		pos.gridx = 0;
		pos.fill = GridBagConstraints.BOTH;
		pos.weightx = 1;
		pos.weighty = 0.9;
		// pos.gridwidth = 2;
		botPanel.add(results, pos);
		pos.insets.bottom = margin;
		pos.gridy = 1;
		pos.weighty = 0.1;
		botPanel.add(errorArea, pos);

		//TopPanel place
		LayoutFolder folderInit = new LayoutFolder();
		folderInit.addInPanel(topPanel, 0);
		updateTop(1);

		//Window display
		refresh();
	}

	/**
	 * Update "+", "Update", "Search" buttons and "Key Words" area
	 * @param posY
	 */
	public void updateTop(int posY) {
		
		GridBagConstraints pos = new GridBagConstraints();

		topPanel.remove(addButton);
		topPanel.remove(updateButton);
		topPanel.remove(keyWords);
		topPanel.remove(searchButton);

		pos.insets.left = margin;
		pos.insets.right = 0;
		pos.gridy = posY;
		pos.gridx = 0;
		pos.weightx = 0.9;
		pos.gridwidth = 2;
		pos.fill = GridBagConstraints.HORIZONTAL;
		topPanel.add(addButton, pos);
		pos.insets.left = interMargin;
		pos.insets.right = margin;
		pos.gridx = 2;
		pos.weightx = 0.1;
		pos.gridwidth = 1;
		topPanel.add(updateButton, pos);
		pos.insets.left = margin;
		pos.insets.right = 0;
		pos.gridy = posY + 1;
		pos.gridx = 0;
		pos.weightx = 0.9;
		pos.gridwidth = 2;
		topPanel.add(keyWords, pos);
		pos.insets.left = interMargin;
		pos.insets.right = margin;
		pos.gridx = 2;
		pos.weightx = 0.1;
		pos.gridwidth = 1;
		topPanel.add(searchButton, pos);
	}

	/**
	 * If the first button "-" can be click
	 */
	public void updateRemVisible() {

		//If there are more one LayoutFolder
		if (folders.size() > 1) {
			folders.get(0).setRemVisible(true);//Can click on "-"
			
			//Else if there are no Layout (not possible)
		} else if (folders.isEmpty()) {
			System.err.println("Not folders found, click on \"+\"");//Error Message
			
			//Else set button to can't click
		} else {
			folders.get(0).setRemVisible(false);
		}
	}

	/**
	 * Print SearchResult
	 * @param fold
	 */
	public void printODT(HashMap<String, Folder> fold) {
		
		//List construction
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(String key : fold.keySet()){
			for (Document d : fold.get(key).docSort()) {
				model.addElement(d.getPath());
			}
		}
		//Update display
		results.setModel(model);
		refresh();
	}

	/**
	 * Open an odt with the default program on OS
	 * @param path
	 */
	public void openODT(String path) {
		
		if (Desktop.isDesktopSupported()) {
			if (Desktop.getDesktop().isSupported(Desktop.Action.OPEN)) {
				try {
					Desktop.getDesktop().open(new File(path));
				} catch (IOException e) {
					System.err.println(e.getMessage());
				} catch (IllegalArgumentException e) {
					System.err.println(e.getMessage());
				}
			} else {
				System.err.println("Function not supported by your OS");
			}
		} else {
			System.err.println("Function not supported by your OS");
		}
	}
	
	/**
	 * Display the dtb in JList
	 */
	public void displayDtb(){
		Storage dtb = new Storage(".");
		HashMap<String, Folder> fold = dtb.readFolder();
		if(fold.isEmpty() || fold == null){
			System.err.println("Database is Empty");
		}
		//List construction
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(String key : fold.keySet()){
			for (Document d : fold.get(key).getDocuments()) {
				model.addElement(d.getPath());
			}
		}
		//Update display
		results.setModel(model);
		refresh();
	}
	
	/**
	 * Display ODT in Folders asked
	 */
	public void displayFolders(){
		
		HashMap<String, Folder> fold = new HashMap<String, Folder>();
		@SuppressWarnings("unchecked")
		ArrayList<LayoutFolder> askFolders = (ArrayList<LayoutFolder>) folders.clone();
		
		readInDtb(askFolders, fold);
		if(fold.isEmpty() || fold == null){
			System.err.println("Database don't knows these folders");
		}
		//List construction
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(String key : fold.keySet()){
			for (Document d : fold.get(key).getDocuments()) {
				model.addElement(d.getPath());
			}
		}
		//Update display
		results.setModel(model);
		refresh();
	}
	
	/**
	 * Search keywords in dtb
	 */
	public void searchInDtb(){
		Storage dtb = new Storage(".");
		HashMap<String, Folder> fold = dtb.readFolder();
		
		//Split String to words
		String[] keyWordsString = keyWords.getText().split(" ");
		
		//Transform table to ArrayList
		ArrayList<String> listKeyWords = new ArrayList<String>();
		for(int i = 0; i < keyWordsString.length; i++){
			listKeyWords.add(keyWordsString[i]);
		}
		
		//Make the keywords search
		for(String key : fold.keySet()){
			fold.get(key).searchWords(listKeyWords);
		}
		
		printODT(fold);
	}

	/**
	 * Update Action
	 * @param foldersToUpdate
	 */
	public void updateFolders(ArrayList<LayoutFolder> foldersToUpdate) {
		//Dtb creation
		Storage dtb = new Storage(".");
		
		//Do a copy of folders
		@SuppressWarnings("unchecked")
		ArrayList<LayoutFolder> askFolders = (ArrayList<LayoutFolder>) foldersToUpdate.clone();
		
		//Clean empty and false path
		clean(askFolders);
		
		//Write folders on database
		for (LayoutFolder lf : askFolders) {
			Folder f = new Folder(lf.getText());
			dtb.writeFolder(f);
		}
		
		//Display successful action
		System.err.println("End of Update");

	}
	
	/**
	 * Search Action
	 */
	public void searchInFolders(){
		
		HashMap<String, Folder> hmFolders = new HashMap<String, Folder>();
		@SuppressWarnings("unchecked")
		ArrayList<LayoutFolder> askFolders = (ArrayList<LayoutFolder>) folders.clone();
		
		//Read dtb and check if all folder are knows
		if (readInDtb(askFolders, hmFolders)){
			updateFolders(askFolders); //If it isn't update not find folders
			readInDtb(askFolders, hmFolders); //And read a second time
		}
		
		//Split String to words
		String[] keyWordsString = keyWords.getText().split(" ");
		
		//Transform table to ArrayList
		ArrayList<String> listKeyWords = new ArrayList<String>();
		for(int i = 0; i < keyWordsString.length; i++){
			listKeyWords.add(keyWordsString[i]);
		}
		
		//Make the keywords search
		for(String key : hmFolders.keySet()){
			hmFolders.get(key).searchWords(listKeyWords);
		}
		
		//Send results to JList
		printODT(hmFolders);
		
		//Search is successful 
		System.err.println("End of search");
		
	}

	/**
	 * Read folders in dtb
	 * @param askFolders
	 * @param hmFolders
	 * @return If folders are all knows
	 */
	public boolean readInDtb(ArrayList<LayoutFolder> askFolders, HashMap<String, Folder> hmFolders) {
		
		HashMap<String, Folder> dtbTotal;
		Storage dtb = new Storage(".");
		
		//Read IO
		dtbTotal = dtb.readFolder();
		
		//Clean empty and false path
		clean(askFolders);
		
		//Check if folders are all knows
		for (String key : dtbTotal.keySet()) {
			for (LayoutFolder lf : askFolders) {
				if (lf.getText().equals(key)) {
					hmFolders.put(key, new Folder(key));
					askFolders.remove(lf);
					break;
				}
			}
		}
		
		//Return if folders are all knows
		return !askFolders.isEmpty();
	}

	/**
	 * Clean empty and false path
	 * @param askFolders
	 */
	public void clean(ArrayList<LayoutFolder> askFolders) {
		
		String message = "";
		@SuppressWarnings("unchecked")
		ArrayList<LayoutFolder> askFoldersCopy = (ArrayList<LayoutFolder>) askFolders.clone();
		
		for (LayoutFolder lf : askFolders) {
			//If the path is empty, remove it
			if (lf.getText().isEmpty()) {
				askFoldersCopy.remove(lf);
			}
			else{
				try{
					File rep = new File(lf.getText());
					//If the path is not a folder or a file, remove it
					if(!rep.exists()){
						message += lf.getText() + " n'existe pas\n";
						askFoldersCopy.remove(lf);
					}
					else{
						lf.setText(rep.getCanonicalPath());
					}
				}catch(IOException e){
					message += lf.getText() + " n'est pas un chemin valide\n";
					askFoldersCopy.remove(lf);
				}
			}
		}
		askFolders = askFoldersCopy;
		
		//If error messages wrote
		if(!message.isEmpty()){
			//print them
			System.err.println(message);
		}
	}

	/**
	 * Refresh window Display
	 */
	public void refresh() {
		setContentPane(mainPanel);
	}

	/**
	 * Get zone of error messages
	 * @return The error messages area
	 */
	public JTextArea getErrorArea() {
		return errorArea;
	}

	/**
	 * Class to represent folder creation
	 * @author ABADJI Julien & LEPESANT Bastien
	 */
	public class LayoutFolder {

		private JTextField folder;
		private JButton searchDirectory;
		private JButton rem;

		/**
		 * Constructor
		 */
		public LayoutFolder() {
			folder = new JTextField();
			folder.addActionListener(new ActionUpdate());
			searchDirectory = new JButton("Folder");
			searchDirectory.addActionListener(new ActionDirectory(folder));
			rem = new JButton("-");
			rem.addActionListener(new ActionRem(this));
		}

		/**
		 * Get the text in JTextField
		 * @return The text in JTextField 
		 */
		public String getText() {
			return folder.getText();
		}
		
		/**
		 * Set the text in JTextField
		 * @param text
		 */
		public void setText(String text){
			folder.setText(text);
		}

		/**
		 * Add the line to Window
		 * @param panel
		 * @param posY
		 */
		public void addInPanel(JPanel panel, int posY) {
			GridBagConstraints pos = new GridBagConstraints();
			folders.add(this);
			nbFolderCreate++;
			pos.fill = GridBagConstraints.HORIZONTAL;
			pos.insets.left = margin;
			pos.gridy = posY;
			pos.gridx = 0;
			pos.weightx = 0.8;
			panel.add(folder, pos);
			pos.insets.left = interMargin;
			pos.insets.right = 0;
			pos.gridx = 1;
			pos.weightx = 0.1;
			panel.add(searchDirectory, pos);
			pos.insets.right = margin;
			pos.gridx = 2;
			pos.weightx = 0.1;
			panel.add(rem, pos);
			updateTop(posY + 1);
			updateRemVisible();
		}

		/**
		 * Remove the line to Window
		 * @param panel
		 */
		public void removeInPanel(JPanel panel) {
			panel.remove(folder);
			panel.remove(searchDirectory);
			panel.remove(rem);
			folders.remove(this);
			updateRemVisible();
		}

		/**
		 * Do the Rem Button Visible
		 * @param visible
		 */
		public void setRemVisible(boolean visible) {
			rem.setEnabled(visible);
		}

		/**
		 * ToString
		 */
		public String toString() {
			return "JTextField : " + folder.toString() + "JButton dir: "
					+ searchDirectory.toString() + "\nJButton Rem : "
					+ rem.toString() + "\n";
		}
	}

	// ******************************ACTION Class's******************************

	/**
	 * Action of "+" button
	 * @author ABADJI Julien & LEPESANT Bastien
	 *
	 */
	public class ActionAdd implements ActionListener {

		/**
		 * Add Layout Folder
		 */
		public void actionPerformed(ActionEvent arg0) {
			LayoutFolder newFolder = new LayoutFolder();
			newFolder.addInPanel(topPanel, nbFolderCreate);
			refresh();
		}

	}

	/**
	 * Action of "-" button
	 *@author ABADJI Julien & LEPESANT Bastien
	 */
	public class ActionRem implements ActionListener {

		private LayoutFolder folder;

		/**
		 * Constructor
		 * @param folder
		 */
		public ActionRem(LayoutFolder folder) {
			super();
			this.folder = folder;
		}

		/**
		 * Remove Layout Folder if it is not the last
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (folders.size() > 1) {
				folder.removeInPanel(topPanel);
				refresh();
			}

		}

	}

	/**
	 * Action of the "Folder" Button
	 * @author ABADJI Julien & LEPESANT Bastien
	 *
	 */
	public class ActionDirectory implements ActionListener {

		private JTextField text;

		/**
		 * Constructor
		 * @param text
		 */
		public ActionDirectory(JTextField text) {
			this.text = text;
		}

		/**
		 * Open File Explorator of the OS
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser but = new JFileChooser();
			but.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			if (but.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				text.setText(but.getSelectedFile().getAbsolutePath());
			}
		}

	}

	/**
	 * Action of Search/Dtb button
	 * @author ABADJI Julien & LEPESANT Bastien
	 *
	 */
	public class ActionSearch implements ActionListener {

		/**
		 * Call fonctions of dtb dependant to text zones
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			boolean allIsEmpty = true;
			for(LayoutFolder lf : folders){
				if (!lf.getText().isEmpty()){
					allIsEmpty = false;
				}
			}
			
			if(keyWords.getText().isEmpty()){
				if(allIsEmpty){
					displayDtb();
				}
				else{
					displayFolders();
				}
			}
			else{
				if(!allIsEmpty){
					searchInFolders();
				}else{
					searchInDtb();
				}
			}

		}

	}

	/**
	 * Action of Update button
	 * @author ABADJI Julien & LEPESANT Bastien
	 *
	 */
	public class ActionUpdate implements ActionListener {

		/**
		 * Call updateFolders
		 */
		@Override
		public void actionPerformed(ActionEvent arg0) {
			updateFolders(folders);

		}

	}

	/**
	 * Action if enter to JList
	 * @author ABADJI Julien & LEPESANT Bastien
	 *
	 */
	public class ActionEnterList implements KeyListener {

		/**
		 * Open the file
		 */
		@Override
		public void keyPressed(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				openODT(results.getSelectedValue());
			}
		}

		/**
		 * Not used
		 */
		@Override
		public void keyReleased(KeyEvent arg0) {

		}

		/**
		 * Not used
		 */
		@Override
		public void keyTyped(KeyEvent arg0) {

		}
	}

	/**
	 * Action if double click to JList
	 * @author ABADJI Julien & LEPESANT Bastien
	 *
	 */
	public class ActionMouseList implements MouseListener {

		/**
		 * Open the file
		 */
		@Override
		public void mouseClicked(MouseEvent arg0) {
			if (arg0.getClickCount() == 2) {
				openODT(results.getSelectedValue());
			}
		}

		/**
		 * Not used
		 */
		@Override
		public void mouseEntered(MouseEvent arg0) {

		}

		/**
		 * Not used
		 */
		@Override
		public void mouseExited(MouseEvent arg0) {

		}

		/**
		 * Not used
		 */
		@Override
		public void mousePressed(MouseEvent arg0) {

		}

		/**
		 * Not used
		 */
		@Override
		public void mouseReleased(MouseEvent arg0) {

		}

	}

	@Override
	public String toString() {
		return "GraphInter [updateButton=" + updateButton + ", searchButton="
				+ searchButton + ", addButton=" + addButton + ", keyWords="
				+ keyWords + ", mainPanel=" + mainPanel + ", topPanel="
				+ topPanel + ", folders=" + folders + ", botPanel=" + botPanel
				+ ", results=" + results + ", errorArea=" + errorArea
				+ ", nbFolderCreate=" + nbFolderCreate + ", sizeX=" + sizeX
				+ ", sizeY=" + sizeY + ", margin=" + margin + ", interMargin="
				+ interMargin + "]";
	}
	
	

}
