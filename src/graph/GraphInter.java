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

//Principal class for GUI
public class GraphInter extends JFrame {

	private JButton updateButton;
	private JButton searchButton;
	private JButton addButton;
	private JTextField keyWords;
	// FLORIAN HELPS : Utilisation JList pratique. Wesh
	private JList<String> results;
	private JTextArea errorArea;

	private JPanel mainPanel;

	private JPanel topPanel;
	private ArrayList<LayoutFolder> folders = new ArrayList<LayoutFolder>();

	private JPanel botPanel;

	private int nbFolderCreate;

	private final int sizeX = 700;
	private final int sizeY = 600;
	private final int margin = 5;
	private final int interMargin = 4;

	public GraphInter(String title) {
		super();

		buildFen(title);
		buildInter();

		/*
		 * ArrayList<Document> docs = new ArrayList<Document>(); docs.add(new
		 * Document("/home/eresia/Documents/S3/Test/tests/src/content.odt"));
		 * docs.add(new Document("machine")); printODT(docs);
		 */

	}

	// Window creation
	public void buildFen(String title) {
		setTitle(title);
		setSize(sizeX, sizeY);
		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	// Interface creation
	public void buildInter() {

		mainPanel = new JPanel();
		topPanel = new JPanel();
		botPanel = new JPanel();

		mainPanel.setLayout(new BorderLayout());
		topPanel.setLayout(new GridBagLayout());
		botPanel.setLayout(new GridBagLayout());
		mainPanel.add(topPanel, BorderLayout.NORTH);
		mainPanel.add(botPanel, BorderLayout.CENTER);

		updateButton = new JButton("Import/Update");
		updateButton.addActionListener(new ActionUpdate());
		searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionSearch());
		addButton = new JButton("+");
		addButton.addActionListener(new ActionAdd());
		keyWords = new JTextField();
		keyWords.addActionListener(new ActionUpdate());
		results = new JList<String>();
		results.setSelectionMode(ListSelectionModel.SINGLE_INTERVAL_SELECTION);
		results.setLayoutOrientation(JList.VERTICAL);
		results.addKeyListener(new ActionEnterList());
		results.addMouseListener(new ActionMouseList());
		errorArea = new JTextArea();
		errorArea.setEditable(false);

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

		LayoutFolder folderInit = new LayoutFolder();
		folderInit.addInPanel(topPanel, 0);
		updateTop(1);

		refresh();
	}

	// Update "+", "Update", "Search" buttons and "Key Words" area
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

	public void updateRemVisible() {

		if (folders.size() > 1) {
			folders.get(0).setRemVisible(true);
		} else if (folders.isEmpty()) {
			System.err.println("Not folders found, click on \"+\"");
		} else {
			folders.get(0).setRemVisible(false);
		}
	}

	public void printODT(HashMap<String, Folder> fold) {
		DefaultListModel<String> model = new DefaultListModel<String>();
		for(String key : fold.keySet()){
			for (Document d : fold.get(key).DocSorting()) {
				model.addElement(d.getPath());
			}
		}
		results.setModel(model);
		refresh();
	}

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

	public void updateFolders() {
		Storage bdd = new Storage(".");
		ArrayList<LayoutFolder> askFolders = (ArrayList<LayoutFolder>) folders.clone();
		clean(askFolders);
		for (LayoutFolder lf : askFolders) {
			Folder f = new Folder(lf.getText());
			bdd.writeFolder(f);
		}

	}

	public void searchInFolders(){
		HashMap<String, Folder> hmFolders = new HashMap<String, Folder>();
		if (readInBdd(hmFolders)){
			updateFolders();
			readInBdd(hmFolders);
		}
		
		String[] keyWordsString = keyWords.getText().split(" ");
		ArrayList<String> listKeyWords = new ArrayList<String>();
		int begin = 0;
		boolean hasOrOperator = true;
		if(keyWordsString[0].equals("+")){
			begin = 1;
		}
		else if(keyWordsString[0].equals("-")){
			hasOrOperator = false;
			begin = 1;
		}
		boolean isInQuote = false;
		String strInQuote = "";
		for(int i = begin; i < keyWordsString.length; i++){
			if(keyWordsString[i].charAt(0) == '\''){
				isInQuote = true;
			}
			if(isInQuote == true){
				if(keyWordsString[i].charAt(keyWordsString[i].length()) == '\''){
					isInQuote = false;
					strInQuote += keyWordsString[i].replaceAll("\'", "");
					listKeyWords.add(strInQuote);
				}
				else{
					strInQuote += keyWordsString[i].replaceAll("\'", "") + " ";
				}
			}
			else{
				listKeyWords.add(keyWordsString[i]);
			}
		}
		
		for(String key : hmFolders.keySet()){
			hmFolders.get(key).searchWords(listKeyWords, hasOrOperator);
		}
		printODT(hmFolders);
		
	}

	public boolean readInBdd(HashMap<String, Folder> hmFolders) {
		HashMap<String, Folder> bddTotal;
		Storage bdd = new Storage(".");
		bddTotal = bdd.readFolder();
		ArrayList<LayoutFolder> askFolders = (ArrayList<LayoutFolder>) folders.clone();
		clean(askFolders);
		for (String key : bddTotal.keySet()) {
			for (LayoutFolder lf : askFolders) {
				if (lf.getText().equals(key)) {
					hmFolders.put(key, new Folder(key));
					askFolders.remove(lf);
					break;
				}
			}
		}
		return !askFolders.isEmpty();
	}

	public void clean(ArrayList<LayoutFolder> askFolders) {
		String message = "";
		for (LayoutFolder lf : askFolders) {
			if (lf.getText().isEmpty()) {
				askFolders.remove(lf);
			}
			else{
				try{
					File rep = new File(lf.getText());
					if(!rep.exists()){
						message += lf.getText() + " n'existe pas";
						askFolders.remove(lf);
					}
					else{
						lf.setText(rep.getCanonicalPath());
					}
				}catch(IOException e){
					message += lf.getText() + " n'est pas un chemin valide";
					askFolders.remove(lf);
				}
			}
		}
		if(!message.isEmpty()){
			System.out.println(message);
		}
	}

	public void refresh() {
		setContentPane(mainPanel);
	}

	/*
	 * public void printError(String error){ errorArea.setText(error);
	 * refresh(); }
	 */

	public JTextArea getErrorArea() {
		return errorArea;
	}

	// Class to represent folder creation
	public class LayoutFolder {

		private JTextField folder;
		private JButton searchDirectory;
		private JButton rem;

		public LayoutFolder() {
			folder = new JTextField();
			folder.addActionListener(new ActionUpdate());
			searchDirectory = new JButton("Folder");
			searchDirectory.addActionListener(new ActionDirectory(folder));
			rem = new JButton("-");
			rem.addActionListener(new ActionRem(this));
		}

		public String getText() {
			return folder.getText();
		}
		
		public void setText(String text){
			folder.setText(text);
		}

		// Add the line to Window
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

		// Remove the line to Window
		public void removeInPanel(JPanel panel) {
			panel.remove(folder);
			panel.remove(searchDirectory);
			panel.remove(rem);
			folders.remove(this);
			updateRemVisible();
		}

		public void setRemVisible(boolean visible) {
			rem.setEnabled(visible);
		}

		public String toString() {
			return "JTextField : " + folder.toString() + "JButton dir: "
					+ searchDirectory.toString() + "\nJButton Rem : "
					+ rem.toString() + "\n";
		}
	}

	// ******************************ACTION
	// Class's******************************

	public class ActionAdd implements ActionListener {

		public void actionPerformed(ActionEvent arg0) {
			LayoutFolder newFolder = new LayoutFolder();
			newFolder.addInPanel(topPanel, nbFolderCreate);
			refresh();
		}

	}

	public class ActionRem implements ActionListener {

		private LayoutFolder folder;

		public ActionRem(LayoutFolder folder) {
			super();
			this.folder = folder;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (folders.size() > 1) {
				folder.removeInPanel(topPanel);
				refresh();
			}

		}

	}

	public class ActionDirectory implements ActionListener {

		private JTextField text;

		public ActionDirectory(JTextField text) {
			this.text = text;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser but = new JFileChooser();
			but.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			if (but.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
				text.setText(but.getSelectedFile().getAbsolutePath());
			}
		}

	}

	public class ActionSearch implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			searchInFolders();

		}

	}

	public class ActionUpdate implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			updateFolders();

		}

	}

	public class ActionEnterList implements KeyListener {

		@Override
		public void keyPressed(KeyEvent arg0) {
			if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
				openODT(results.getSelectedValue());
			}
		}

		@Override
		public void keyReleased(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void keyTyped(KeyEvent arg0) {
			// TODO Auto-generated method stub

		}
	}

	public class ActionMouseList implements MouseListener {

		@Override
		public void mouseClicked(MouseEvent arg0) {
			if (arg0.getClickCount() == 2) {
				openODT(results.getSelectedValue());
			}
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

}
