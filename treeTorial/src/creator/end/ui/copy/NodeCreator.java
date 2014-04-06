package creator.end.ui.copy;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.text.JTextComponent;

import creator.end.api.KnowledgeNode;

import java.util.ArrayList;

public class NodeCreator extends JPanel {
	
	public static JLabel node_name_label;
	public static JPanel title_panel;
	public static JTextField node_name;
	public static JLabel node_body_label;
	public static JTextArea node_body;
	public static JLabel node_pred_label;
	public static ArrayList<JLabel> node_pred;
	public static JPanel node_pred_panel;
	public static Button save_leaf;
	public static Button generate_xml;
	public static JPanel title_panel_ina_panel;
	public static GridBagConstraints c;
	public static BufferedImage treeTorialLogo;
	public static JComboBox<String> pred_list;
	
	public NodeCreator() {
		
		setLayout(new GridBagLayout());
		c = new GridBagConstraints(); 
		
		title_panel = new JPanel();
		title_panel.setLayout(new GridLayout(1,2));
		
		try {
			treeTorialLogo = ImageIO.read(new File("Images/treetoriallogo.png"));
		} catch (Exception e) {}
		
		JPanel name_panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		node_name_label = new JLabel("Name: ");
		node_name = new JTextField("");
		name_panel.add(node_name_label);
		name_panel.add(node_name);
		title_panel.add(name_panel);
		title_panel_ina_panel = new JPanel();
		//JLabel x = new JLabel(" ");
		//title_panel_ina_panel.add(x);
		//title_panel.add(title_panel_ina_panel);
		//title_panel_ina_panel.repaint();
		add(title_panel);
		
		node_body_label = new JLabel("Body: ");
		node_body = new JTextArea(500,500);
		node_body.setLineWrap(true);
		JScrollPane scrollPane = new JScrollPane(node_body); 
		node_body.setText("N/A");
		
		/*JPanel buttonPane = new JPanel();
		save_leaf = new JButton("Save Leaf");
		generate_xml = new JButton("Generate XML");
		buttonPane.setLayout(new BoxLayout(buttonPane,
                                           BoxLayout.LINE_AXIS));
        buttonPane.add(save_leaf);
        buttonPane.add(Box.createHorizontalStrut(5));
        buttonPane.add(new JSeparator(SwingConstants.VERTICAL));
        buttonPane.add(Box.createHorizontalStrut(5));
        //buttonPane.add(employeeName);
        buttonPane.add(generate_xml);
        //buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));*/
		
        JPanel buttonPane = new JPanel(new FlowLayout(FlowLayout.LEFT));
        save_leaf = new Button("Save Leaf");
        save_leaf.addActionListener(new NodeManager("save"));
       // generate_xml = new Button("Generate XML");
        //generate_xml.addActionListener(new NodeManager("gen"));
        buttonPane.add(save_leaf);
        buttonPane.setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
		
		node_pred_label = new JLabel("Predecessors: ");
		name_panel.setBackground(MainFrame.COLOR);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		add(name_panel,c);
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(10,0,0,0);
		c.gridx = 0;
		c.gridy = 1;
		add(node_body_label,c);
		
		//c.gridwidth = 200;
		//c.gridheight = 100;
		c.ipady = 450;
		c.ipadx = 575;
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 2;
		add(scrollPane,c);
		
		pred_list = new JComboBox<String>();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(13,0,0,0);
		c.ipady = 0;
		c.gridx = 0;
		c.gridy = 3;
		add(node_pred_label,c);
		
		node_pred_panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
		//node_pred_panel.setLayout(new BoxLayout(node_pred_panel, BoxLayout.X_AXIS));
		//node_pred_panel.setPreferredSize(new Dimension(200, 200));
		
		node_pred = new ArrayList<JLabel>();
		
		for(int i = 0; i < node_pred.size(); i++) {
			Border a = BorderFactory.createEmptyBorder(5, 5, 5, 5);
			Border b = BorderFactory.createLineBorder(Color.BLACK, 1);
			Border combined = BorderFactory.createCompoundBorder(b, a);
			node_pred.get(i).setBorder(combined);
			node_pred_panel.add(node_pred.get(i));
		}
		
		c.gridx = 0;
		c.gridy = 4;
		c.ipadx = 600;
		c.insets = new Insets(0,0,0,0);
		c.fill = GridBagConstraints.HORIZONTAL;
		node_pred_panel.add(pred_list);
		//node_pred_panel.add(pred_desc);
		add(node_pred_panel,c);
		//
		buttonPane.setBackground(MainFrame.COLOR);
		c.ipadx = 50;
		c.gridx = 0;
		c.gridy = 5;
		c.insets = new Insets(10,0,0,0);
		add(buttonPane,c);
	}
	
	public class NodeManager implements ActionListener {
		
		public String btnName;
		
		public NodeManager(String n) {
			btnName = n;
		}
		
		public void actionPerformed(ActionEvent e) {
			if(btnName.equals("save")) {
				ListCreator.nodes.get(ListCreator.node_list.getSelectedIndex()).setName(node_name.getText());
				ListCreator.nodes.get(ListCreator.node_list.getSelectedIndex()).setBody(node_body.getText());
				int length = ListCreator.nodes.size();
				int index = ListCreator.node_list.getSelectedIndex();
				
				for(int i = index; i < length; i++) {
					ListCreator.list_manager.remove(index);
				}
				
				for(int i = index; i < length; i++) {
					ListCreator.list_manager.addElement(ListCreator.nodes.get(i).getName());
				}
				if(ListCreator.setChildren) {
					ListCreator.setChildren = false;
					ListCreator.add_dep.setLabel("Add Children");
				}
			}
			else if(btnName.equals("gen")) {
			}
		}
	}
	
	public void updateFields(KnowledgeNode node) {
		//System.out.println("what happened"+node.getName());
		node_name.setText(node.getName());
		node_body.setText(node.getBody());
		
		int length = node.getDependencies().size();
		
		node_pred.clear();
		pred_list.removeAllItems();
		node_pred_panel.removeAll();
		
		node_pred_panel.setBackground(MainFrame.COLOR);
		
		//try{Thread.sleep(1000);}catch(Exception e){}
		
		boolean goodToAdd = true;
		
		/*for(int i = 0; i < length; i++) {
			goodToAdd = true;
			for(int j = 0; j < node_pred.size(); j++) {
				if(node_pred.get(j).getText().equals(node.getDependencies().get(i).getName())) {
					goodToAdd = false;
				}
			}
			if(goodToAdd) {
				//node_pred.add(new JLabel(node.getDependencies().get(i).getName()));
				pred_list.addItem((node.getDependencies().get(i).getName()));
			}
		}*/
		
		for(int i = 0; i < length; i++) {
			goodToAdd = true;
			for(int j = 0; j < pred_list.getItemCount(); j++) {
				if(pred_list.getItemAt(j).equals(node.getDependencies().get(i).getName())) {
					goodToAdd = false;
				}
			}
			if(goodToAdd) {
				//node_pred.add(new JLabel(node.getDependencies().get(i).getName()));
				pred_list.addItem((node.getDependencies().get(i).getName()));
			}
		}
		
		node_pred_panel.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		for(int i = node_pred.size()-1; i >= 0; i--) {
			Border a = BorderFactory.createEmptyBorder(5, 5, 5, 5);
			Border b = BorderFactory.createLineBorder(Color.BLACK, 1);
			Border combined = BorderFactory.createCompoundBorder(b, a);
			//node_pred.get(i).setBorder(combined);
			//node_pred_panel.add(node_pred.get(i));
		}
		
		c = new GridBagConstraints();
		
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 4;
		node_pred_panel.add(pred_list);
		node_pred_panel.updateUI();
		this.add(node_pred_panel,c);
	}
}
