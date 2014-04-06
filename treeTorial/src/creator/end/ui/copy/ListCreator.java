package creator.end.ui.copy;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.MouseInputAdapter;

import creator.end.api.ApiUtils;
import creator.end.api.KnowledgeNode;
import creator.end.parsing.KnowledgeNodeBeanPicker;

public class ListCreator extends JPanel implements ListSelectionListener{
	//
	private static final long serialVersionUID = 1L;
	public static JList node_list;
	public static NodeCreator node_panel;
	public static Button save_tree;
	public static Button create_leaf;
	public static BufferedImage treeTorialLogo;
	public static Button remove_leaf;
	public static Button add_dep;
	public static DefaultListModel list_manager;
	public static KnowledgeNode n;
	public static List<KnowledgeNode> nodes;
	public static int storeIndex;
	public static JPanel savePanel;
	public static boolean setChildren = false;
	
	public ListCreator(List<KnowledgeNode> nodes, NodeCreator node_panel) {		
		
		setLayout(new BorderLayout());
		
		this.node_panel = node_panel;
		this.nodes = nodes;
		
		save_tree = new Button("Save Tree");
		save_tree.addActionListener(new ListManager("tree"));
		
		list_manager = new DefaultListModel();
		
		for(int i = 0; i < nodes.size(); i++) {
			list_manager.addElement((String)(nodes.get(i).getName()));
		}
		
		node_list = new JList(list_manager);
		
        node_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        node_list.setSelectedIndex(0);
        node_list.addListSelectionListener(this);
        node_list.setVisibleRowCount(5);
        JScrollPane listScrollPane = new JScrollPane(node_list);
        //
        node_list.addMouseListener(new MyMouseAdaptor());
        
        create_leaf = new Button("Create Leaf");
        remove_leaf = new Button("Remove Leaf");
        add_dep = new Button("Add Children");
        
        create_leaf.addActionListener(new ListManager("create"));
        add_dep.addActionListener(new ListManager("dep"));
        remove_leaf.addActionListener(new ListManager("remove"));
        
        //Create a panel that uses BoxLayout.
        JPanel buttonPane = new JPanel();
        buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(create_leaf);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(add_dep);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(remove_leaf);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
        
		try {
			treeTorialLogo = ImageIO.read(new File("Images/treetoriallogo.png"));
		} catch (Exception e) {}
        
        savePanel = new JPanel(new BorderLayout());
        savePanel.setBackground(MainFrame.COLOR);
        buttonPane.setBackground(MainFrame.COLOR);
        savePanel.setBorder(BorderFactory.createEmptyBorder(10,5,5,5));
        savePanel.add(new JLabel(new ImageIcon("Images/treetoriallogo.png")), BorderLayout.CENTER);
        savePanel.add(save_tree, BorderLayout.SOUTH);
        add(savePanel, BorderLayout.NORTH);
        savePanel.repaint();
        add(listScrollPane, BorderLayout.CENTER);
        add(buttonPane, BorderLayout.SOUTH);
        
        //node_list.setDragEnabled(true);
		
        node_panel.updateFields(nodes.get(0));
        
		setBackground(Color.WHITE);
	}
	
    protected void paintComponent(Graphics g) {
        //so this is a weird method, but basically this is how we get our images drawn to the screen
        //we can move our images by modifying the x and y fields (p2y, borderx, etc.)
        //since they're static, this is pretty easy :D
        super.paintComponent(g);
        g.drawImage(treeTorialLogo, 100, 100, savePanel);
    }
	
	public class ListManager implements ActionListener {
		
		public String btnName;
		
		public ListManager(String n) {
			btnName = n;
		}
		
		public void actionPerformed(ActionEvent e) {
			if(btnName.equals("create")) {
				KnowledgeNode empty = new KnowledgeNode();
				empty.setName("Empty Leaf");
				empty.setBody("There's nothing here. Add some information!");
				empty.setId(ApiUtils.getUID());
				nodes.add(empty);
				list_manager.addElement((String)(nodes.get(nodes.size()-1).getName()));
				if(setChildren) {
					ListCreator.setChildren = false;
					ListCreator.add_dep.setLabel("Add Children");
				}
			}
			else if(btnName.equals("remove")) {
				System.out.println("SELECTED INDEX TO REMOVE "+node_list.getSelectedIndex());
				int x = node_list.getSelectedIndex();
				list_manager.remove(x);
				nodes.remove(x);
				if(setChildren) {
					ListCreator.setChildren = false;
					ListCreator.add_dep.setLabel("Add Children");
				}
			}
			else if(btnName.equals("dep")) {
				if(!setChildren) {
					setChildren = true;
					storeIndex = node_list.getSelectedIndex();
					add_dep.setLabel("Click to Add");
				}
				else {
					setChildren = false;
					add_dep.setLabel("Add Children");
				}	
			}
			else if(btnName.equals("tree")) {
				KnowledgeNodeBeanPicker picker = new KnowledgeNodeBeanPicker();
				picker.setDataDirectory(MainFrame.directory);
				picker.writeKnowledgeNodesToDatabase(nodes);
			}
		}
	}

	 private class MyMouseAdaptor extends MouseInputAdapter {
	        private boolean mouseDragging = false;
	        private int dragSourceIndex;
	        
	        @Override
	        public void mousePressed(MouseEvent e) {
	        }

	        @Override
	        public void mouseReleased(MouseEvent e) {
            	String nodeName = nodes.get(node_list.getSelectedIndex()).getName();
            	if(!setChildren) {
            		//MainFrame.picker.populateNode(nodes.get(node_list.getSelectedIndex()));
            		node_panel.updateFields(nodes.get(node_list.getSelectedIndex()));
            	}
            	else {
            		nodes.get(storeIndex).addDependency(nodes.get(node_list.getSelectedIndex()));
            		node_panel.updateFields(nodes.get(storeIndex));
            	}
            		
	            //mouseDragging = false;
	        }

	        @Override
	        public void mouseDragged(MouseEvent e) {
	        	
	        }

	    }

	
	
	public void valueChanged(ListSelectionEvent e) {
	     if (e.getValueIsAdjusting() == false) {
	
	         if (node_list.getSelectedIndex() == -1) {
	         //No selection, disable fire button.
	             //fireButton.setEnabled(false);
	            } else {
	         //Selection, enable the fire button.
	             //fireButton.setEnabled(true);
	            	//System.out.println("Selected");
	            	//String nodeName = nodes.get(node_list.getSelectedIndex()).getName();
	            	//node_panel.updateFields(nodes.get(node_list.getSelectedIndex()));
	         }
	     }
	 }
}
