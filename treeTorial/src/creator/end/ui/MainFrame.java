package creator.end.ui;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.MatteBorder;
import javax.swing.filechooser.FileNameExtensionFilter;

import creator.end.api.KnowledgeNode;
import creator.end.parsing.KnowledgeNodeBeanPicker;
import creator.end.test.ExampleKnowledgeNode;

public class MainFrame extends JFrame {
	
	public static JPanel main_panel;
	public static ListCreator list_panel;
	public static NodeCreator node_panel;
	public static List<KnowledgeNode> node_list;
	public static MainFrame Creator_UI;
	public static KnowledgeNodeBeanPicker picker;
	public static String directory;
	public static final Color COLOR = new Color(214, 250, 255);
	
	public static void main(String[] args) {
			KnowledgeNode testNode = ExampleKnowledgeNode.generateNode();
		
			directory = getPath();
			System.out.println(directory);
			picker = new KnowledgeNodeBeanPicker();
			picker.setDataDirectory(directory);
			
			node_list = picker.getAllKnowledgeNodes();
			
			Creator_UI = new MainFrame("TreeTorials Creation Toolbox");
			Creator_UI.setSize(1024,768);
			Creator_UI.getContentPane().setBackground(COLOR);
			Creator_UI.setResizable(false);
			Creator_UI.setVisible(true);
			Creator_UI.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static String getPath() {
	    //JFileChooser chooser = new JFileChooser();
		JFileChooser chooser = new JFileChooser(); 
	    chooser.setCurrentDirectory(new java.io.File("."));
	    chooser.setDialogTitle("Select Working Directory");
	    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
	    //
	    // disable the "All files" option.
	    //
	    chooser.setAcceptAllFileFilterUsed(false);
	    //    
	    if (chooser.showOpenDialog(Creator_UI) == JFileChooser.APPROVE_OPTION) { 
	    		return ""+ chooser.getSelectedFile();
	      }
	    else {
	       return "";
	      }
	    
	    }
	
	public MainFrame(String s) {
		super(s);
		
		main_panel = new JPanel();
		main_panel.setLayout(new BoxLayout(main_panel, BoxLayout.X_AXIS));
		//main_panel.add(Box.createRigidArea(new Dimension(150, 500)));
		//
		node_panel = new NodeCreator();
		list_panel = new ListCreator(node_list, node_panel);
		node_panel.setBackground(COLOR);
		list_panel.setBackground(COLOR);
		Border a = BorderFactory.createEmptyBorder(10, 30, 10, 30);
		Border a2 = BorderFactory.createEmptyBorder(0, 0, 0, 0);
		Border b = BorderFactory.createLineBorder(Color.BLACK, 2);
		Border combined = BorderFactory.createCompoundBorder(b, a);
		Border combined2 = BorderFactory.createCompoundBorder(b, a2);
		
		list_panel.setBorder(combined2);
		node_panel.setBorder(combined);
		
		list_panel.setMaximumSize(new Dimension(250,1000));
		
		main_panel.add(list_panel);//, BorderLayout.WEST);
		main_panel.add(node_panel);//, BorderLayout.CENTER);
		
		add(main_panel);
	}
}
