import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//Classe principale de l'interface grapique
public class GraphInter extends JFrame{
	
	private JButton updateButton;
	private JButton searchButton;
	private JButton addButton;
	private JTextField keyWords;
	private JTextArea results;
	
	private JPanel mainPanel;
	
	private JPanel topPanel;
	private ArrayList<LayoutFolder> folders = new ArrayList<LayoutFolder>();
	
	private JPanel botPanel;
	
	private int nbFolderCreate;
	
	private int sizeX = 700;
	private int sizeY = 600;
	private int margin = 5;
	private int interMargin = 4;
	
	
	public GraphInter(String title){
		super();
		
		buildFen(title);
		buildInter();
	}
	
	//Création de la fenêtre
	public void buildFen(String title){
		setTitle(title);
		setSize(sizeX, sizeY);
		setLocationRelativeTo(null);
		setResizable(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	//Création de l'interface
	public void buildInter(){
		
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
		results = new JTextArea();
		results.setEditable(false);
		
		GridBagConstraints pos = new GridBagConstraints();
		pos.insets.left = margin; pos.insets.bottom = margin;
		pos.gridy = 0; pos.gridx = 0;
		pos.fill = GridBagConstraints.BOTH;
		pos.weightx = 1;
		pos.weighty = 1;
		pos.gridwidth = 2;
		botPanel.add(results, pos);
			
		LayoutFolder folderInit = new LayoutFolder();		
		folderInit.addInPanel(topPanel, 0);
		updateTop(1);
		
		setContentPane(mainPanel);
	}
	
	//Mise à jour des boutons "+", "Update", "Search" et de la zone "Mot clés"
	public void updateTop(int posY){
		GridBagConstraints pos = new GridBagConstraints();
		
		topPanel.remove(addButton);
		topPanel.remove(updateButton);
		topPanel.remove(keyWords);
		topPanel.remove(searchButton);
		
		pos.insets.left = margin; pos.insets.right = 0;
		pos.gridy = posY; pos.gridx = 0;
		pos.weightx = 0.9;
		pos.gridwidth = 2;
		pos.fill = GridBagConstraints.HORIZONTAL;
		topPanel.add(addButton, pos);
		pos.insets.left = interMargin; pos.insets.right = margin;
		pos.gridx = 2;
		pos.weightx = 0.1;
		pos.gridwidth = 1;
		topPanel.add(updateButton, pos);
		pos.insets.left = margin; pos.insets.right = 0;
		pos.gridy = posY + 1; pos.gridx = 0;
		pos.weightx = 0.9;
		pos.gridwidth = 2;
		topPanel.add(keyWords, pos);
		pos.insets.left = interMargin; pos.insets.right = margin;
		pos.gridx = 2;
		pos.weightx = 0.1;
		pos.gridwidth = 1;
		topPanel.add(searchButton, pos);
	}
	
	//Classe représentant une demande de dossier (créée quand appuis sur le boutton "+")
	public class LayoutFolder {
		
		private JTextField folder;
		private JButton searchDirectory;
		private JButton rem; 
		
		public LayoutFolder(){
			folder = new JTextField();
			searchDirectory = new JButton("Folder");
			searchDirectory.addActionListener(new ActionDirectory(folder));
			rem = new JButton("-");
			rem.addActionListener(new ActionRem(this));
		}
		
		public String getFolder(){
			return folder.getText();
		}
		
		//Ajoute la ligne dans la fenêtre
		public void addInPanel(JPanel panel, int posY){
			GridBagConstraints pos = new GridBagConstraints();
			folders.add(this);
			nbFolderCreate++;
			pos.fill = GridBagConstraints.HORIZONTAL;
			pos.insets.left = margin; 
			pos.gridy = posY;
			pos.gridx = 0;
			pos.weightx = 0.8;
			panel.add(folder, pos);
			pos.insets.left = interMargin; pos.insets.right = 0;
			pos.gridx = 1;
			pos.weightx = 0.1;
			panel.add(searchDirectory, pos);
			pos.insets.right = margin;
			pos.gridx = 2;
			pos.weightx = 0.1;
			panel.add(rem, pos);
			updateTop(posY + 1);
		}
		
		//Suprimer la ligne de la fenêtre
		public void removeInPanel(JPanel panel){
			panel.remove(folder);
			panel.remove(searchDirectory);
			panel.remove(rem);
			folders.remove(this);
		}
		
		public String toString(){
			return "JTextField : " + folder.toString() + "JButton dir: " + searchDirectory.toString() + "\nJButton Rem : " + rem.toString();
		}
	}
	
	//******************************Classes ACTION******************************
	
	public class ActionAdd implements ActionListener{

		public void actionPerformed(ActionEvent arg0) {
			LayoutFolder newFolder = new LayoutFolder();
			newFolder.addInPanel(topPanel, nbFolderCreate);
			setContentPane(mainPanel);
		}
		
	}
	
	public class ActionRem implements ActionListener{
		
		private LayoutFolder folder;
		public ActionRem(LayoutFolder folder){
			super();
			this.folder = folder;
		}

		@Override
		public void actionPerformed(ActionEvent arg0) {
			if(folders.size() > 1){
				folder.removeInPanel(topPanel);
				setContentPane(mainPanel);
			}
			
		}
		
	}
	
	public class ActionDirectory implements ActionListener{

		private JTextField text;
		
		public ActionDirectory(JTextField text){
			this.text = text;
		}
		
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JFileChooser but = new JFileChooser();
			but.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			if(but.showOpenDialog(null) == JFileChooser.APPROVE_OPTION){
				text.setText(but.getSelectedFile().getAbsolutePath());
			}
		}
		
	}
	
	public class ActionSearch implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			//A Finir (Provoque la recherche)
			
		}
		
	}
	
	public class ActionUpdate implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent arg0) {
			//A finir (Provoque la mise à jour)
			
		}
		
	}

}
