package creator.end.parsing.copy;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import creator.end.api.KnowledgeNode;
import creator.end.api.NodeIndex;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;

public class KnowledgeNodeBeanPicker {
	
	public static final String ELNAME_NAME = "name";
	public static final String ELNAME_ID = "id";
	public static final String ELNAME_DEPENDENCIES = "dependencies";
	public static final String ELNAME_DEPENDENCY = "dependency";
	public static final String ELNAME_DEPENDENCYID = "id";
	public static final String ELNAME_DESCRIPTION = "description";
	public static final String ELNAME_BODY = "body";
	public static final String ELNAME_ROOTNAME = "KnowledgeNode";

	String dataDirectory;
	NodeIndex nodeIndex = null;
	
	/**
	 * Will return a list of all the knowledge nodes in the
	 * database. The knowledge nodes returned will be
	 * partial knowledge nodes! They will all appear as
	 * leaves. To get a full knowledge node, use the
	 * method populateNode(KnowledgeNode node)
	 */
	public List<KnowledgeNode> getAllKnowledgeNodes(){
		List<KnowledgeNode> allNodes = new ArrayList<KnowledgeNode>();
		
		initializeNodeIndex(false);
		
		
		List<Integer> allIds = nodeIndex.getAllIds();
		for(int id : allIds){
			allNodes.add(getKnowledgeNodeAsLeaf(id));
		}
		
		//Actually, this is inefficient, but we'll
		//just load everything in right away...
		int numberOfNodes = allNodes.size();
		for(int i=0; i<numberOfNodes; i++){
			this.populateNode(allNodes.get(i),allNodes);
		}
		
		//fun stuff...
		return allNodes;
	}
	
	private void initializeNodeIndex(boolean update) {
		if((!update) && (nodeIndex!=null)){
			return;
		}
		NodeIndexBeanPicker niBeanPicker = new NodeIndexBeanPicker();
		
		niBeanPicker.setDataDirectory(dataDirectory+"/nodeIndex.xml");
		niBeanPicker.parseDataDirectory();
		nodeIndex = niBeanPicker.getNodeIndex();
	}
	
	private void validateNodeIndexExists(){
		if(nodeIndex==null){
			throw new RuntimeException("NodeIndex has not been initialized yet!");
		}
	}

	public KnowledgeNode getKnowledgeNodeAsLeaf(int id){
		validateNodeIndexExists();
		
		String filename = nodeIndex.getFilename(id);
		String filepath = nodeIndex.getURL()+"/"+filename;
		
		return getKnowledgeNodeAsLeaf(filename);
	}

	private KnowledgeNode getKnowledgeNodeAsLeaf(String filename) {
		try{
			//get the doc loaded up. Overhead, overhead...
			File fXmlFile = new File(dataDirectory+"/"+filename);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			Element rootElement = doc.getDocumentElement();
			
			//The thing that will be returned.
			KnowledgeNode knowledgeNode = new KnowledgeNode();
			
			String name = getNameFromRootNode(rootElement);
			int id = getIdFromRootNode(rootElement);
			String body = getBodyFromRootNode(rootElement);
			
			knowledgeNode.setName(name);
			knowledgeNode.setId(id);
			knowledgeNode.setBody(body);
			loadDependencies(rootElement,knowledgeNode);
			
			return knowledgeNode;
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not parse knowledgeNode.",e);
		} catch (SAXException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not parse knowledgeNode.",e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not parse knowledgeNode.",e);
		}
	}

	private void loadDependencies(Element rootElement,
			KnowledgeNode knowledgeNode) {
		Node dependenciesNode = rootElement.getElementsByTagName(ELNAME_DEPENDENCIES).item(0);
		if(dependenciesNode==null){return;}
		NodeList dependencyNodes = ((Element)dependenciesNode).getElementsByTagName(ELNAME_DEPENDENCY);
		if(dependencyNodes==null){return;}
		int numDependencies = dependencyNodes.getLength();

		Node dependencyNode;
		for(int i=0;i<numDependencies;i++){
			dependencyNode = dependencyNodes.item(i);
			loadDependencyNode(dependencyNode,knowledgeNode);
		}
	}

	private void loadDependencyNode(Node dependencyNode,
			KnowledgeNode knowledgeNode) {
		KnowledgeNode dependencyKnowledgeNode = new KnowledgeNode();
		String sId = ((Element)dependencyNode).getElementsByTagName(ELNAME_DEPENDENCYID).item(0).getTextContent();
		int id = Integer.parseInt(sId);
		String description = "";
		
		NodeList descriptionsNodes = ((Element)dependencyNode).getElementsByTagName(ELNAME_DESCRIPTION);
		if(descriptionsNodes!=null){
			Node descriptionNode = descriptionsNodes.item(0);
			if(descriptionNode!=null){
				description = descriptionNode.getTextContent();
			}
		}

	
		dependencyKnowledgeNode.setId(id);
		knowledgeNode.addDescription(dependencyKnowledgeNode, description);
		knowledgeNode.addDependency(dependencyKnowledgeNode);
	}
	
	/**
	 * Takes a "stub" knowledgeNode with only empty children
	 * and turns it into a fully populated directed graph.
	 * @param node
	 */
	public void populateNode(KnowledgeNode node,List<KnowledgeNode> allNodesList){
		if(node.isFullyPopulated()){return;}
		
		Map<Integer,KnowledgeNode> idToCompleteKnowledgeNodes = createIdsToCompleteKnowledgeNodes(allNodesList);
		
		//get the dependencies of the knowledgeNode
		List<KnowledgeNode> dependencies = node.getDependencies();
		node.removeAllDependencies();
		for(KnowledgeNode dependencyNode : dependencies){
			KnowledgeNode completeKnowledgeNode = idToCompleteKnowledgeNodes.get(dependencyNode.getId());
			populateNode(completeKnowledgeNode,allNodesList);
			node.addDependency(completeKnowledgeNode);
		}
		node.setFullyPopulated(true);		
	}

	private Map<Integer, KnowledgeNode> createIdsToCompleteKnowledgeNodes(
			List<KnowledgeNode> allNodesList) {
		Map<Integer,KnowledgeNode> idsToCompleteKnowledgeNodes = new HashMap<Integer,KnowledgeNode>();
		for(KnowledgeNode knowledgeNode : allNodesList){
			idsToCompleteKnowledgeNodes.put(knowledgeNode.getId(),knowledgeNode);
		}
		return idsToCompleteKnowledgeNodes;
	}

	private String getBodyFromRootNode(Element rootElement) {
		return rootElement.getElementsByTagName(ELNAME_BODY).item(0).getTextContent();
	}

	private String getNameFromRootNode(Element rootElement) {
		return rootElement.getElementsByTagName(ELNAME_NAME).item(0).getTextContent();
	}
	
	private int getIdFromRootNode(Element rootElement){
		String sId = rootElement.getElementsByTagName(ELNAME_ID).item(0).getTextContent();
		return Integer.parseInt(sId);
	}

	public String getDataDirectory() {
		return dataDirectory;
	}

	public void setDataDirectory(String dataDirectory) {
		this.dataDirectory = dataDirectory;
	}
	
	///////////////////////////////////////////////////
	//Write out the xml for the knowledge nodes      //
	///////////////////////////////////////////////////
	
	public void writeKnowledgeNodesToDatabase(List<KnowledgeNode> allKnowledgeNodes){
		this.initializeNodeIndex(false);
		validateNodeIndexExists();
		
		//make sure we don't write anybody out twice.
		Set<KnowledgeNode> uniqueKns = new HashSet<KnowledgeNode>(allKnowledgeNodes);
		
		//create node index file.
		nodeIndex.clearFilenames();
		for(KnowledgeNode kn : uniqueKns){
			String filename = kn.getName();
			filename = filename.replaceAll("[^A-Za-z0-9]", "");
			filename+=".xml";
			nodeIndex.addFilename(kn.getId(), filename);
		
			//now go through and actually write the kns
			writeKnowledgeNode(kn,filename);
		}
		NodeIndexBeanPicker niBeanPicker = new NodeIndexBeanPicker();
		niBeanPicker.setDataDirectory(dataDirectory+"/nodeIndex.xml");
		nodeIndex.setURL(dataDirectory);
		niBeanPicker.writeNodeIndex(nodeIndex);
		
	}

	/**
	 * Takes in a knowledge node and writes it to a file
	 * with the given name.
	 * @param kn
	 */
	private void writeKnowledgeNode(KnowledgeNode toWrite,String filename) {
				
		try{			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement(ELNAME_ROOTNAME);
			doc.appendChild(rootElement);
			
			addNameElement(toWrite,rootElement,doc);
			addIdElement(toWrite,rootElement,doc);
			addBodyElement(toWrite,rootElement,doc);
			addDependenciesElement(toWrite,rootElement,doc);
			
			
			//Write out the document
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(dataDirectory+"/"+filename));
	 
			transformer.transform(source, result);
			
		} catch (ParserConfigurationException e) {
			// TODO write log
			e.printStackTrace();
			throw new RuntimeException("Could not write NodeIndex file",e);
		} catch (TransformerException e) {
			// TODO write log
			e.printStackTrace();
			throw new RuntimeException("Could not write NodeIndex file",e);
		}
	}

	

	private void addDependenciesElement(KnowledgeNode toWrite,
			Element rootElement, Document doc) {
		Node dependenciesNode = doc.createElement(ELNAME_DEPENDENCIES);
		rootElement.appendChild(dependenciesNode);
		
		List<KnowledgeNode> dependencies = toWrite.getDependencies();
		for(KnowledgeNode knDep : dependencies){
			String description = toWrite.getDescription(knDep);
			addDependencyElement(knDep,description,dependenciesNode,doc);
		}
	}

	private void addDependencyElement(KnowledgeNode knDep,String description,
			Node dependenciesNode, Document doc) {
		Node depNode = doc.createElement(ELNAME_DEPENDENCY);
		dependenciesNode.appendChild(depNode);
		
		Node idNode = doc.createElement(ELNAME_DEPENDENCYID);
		depNode.appendChild(idNode);
		String sId = (new Integer(knDep.getId())).toString();
		idNode.appendChild(doc.createTextNode(sId));
		
		if(description!=null){			
			Node descriptionNode = doc.createElement(ELNAME_DESCRIPTION);
			depNode.appendChild(descriptionNode);
			descriptionNode.appendChild(doc.createTextNode(description));
		}
	}

	private void addBodyElement(KnowledgeNode toWrite, Element rootElement,
			Document doc) {
		Node bodyNode = doc.createElement(ELNAME_BODY);
		rootElement.appendChild(bodyNode);
		bodyNode.appendChild(doc.createTextNode(toWrite.getBody()));
		
	}

	private void addIdElement(KnowledgeNode toWrite, Element rootElement,
			Document doc) {
		Node idNode = doc.createElement(ELNAME_ID);
		rootElement.appendChild(idNode);
		String sId = (new Integer(toWrite.getId())).toString();
		idNode.appendChild(doc.createTextNode(sId));
	}

	private void addNameElement(KnowledgeNode toWrite, Element rootElement,
			Document doc) {
		Node nameNode = doc.createElement(ELNAME_NAME);
		rootElement.appendChild(nameNode);
		nameNode.appendChild(doc.createTextNode(toWrite.getName()));		
	}
	
	
}
