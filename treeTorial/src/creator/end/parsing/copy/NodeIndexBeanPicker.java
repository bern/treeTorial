package creator.end.parsing.copy;

import javax.management.RuntimeErrorException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import creator.end.api.NodeIndex;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This class is responsible for parsing the NodeIndex
 * file into a nice java object. 
 * @author coriakin
 *
 */
public class NodeIndexBeanPicker {
	
	public static final String ELNAME_ROOT = "NodeIndex";
	public static final String ELNAME_DIRECTORY="directory";
	public static final String ELNAME_NODE = "Node";
	public static final String ELNAME_ID = "id";
	public static final String ELNAME_FILENAME = "filename";
	
	/**
	 * The directory where all of the xml for our tutorial's
	 *knowledge tree lives.
	 */
	private String dataDirectory;
	private boolean upToDate = false;
	
	
	private NodeIndex nodeIndex = null;
	
	public void setDataDirectory(String dataDirectory){
		this.dataDirectory = dataDirectory;
	}
	
	/**
	 * Parse the nodeIndex into a node index bean.
	 */
	public void parseDataDirectory(){
		try{
			File fXmlFile = new File(dataDirectory);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);
			
			this.nodeIndex = parseNodeIndexFromDoc(doc);
			upToDate = true;
		} catch (ParserConfigurationException e) {
			//TODO: write log file.
			e.printStackTrace();
			throw new RuntimeException("Could not parse nodeIndexFile.",e);
		} catch (SAXException e) {
			//TODO: write log file.
			e.printStackTrace();
			throw new RuntimeException("Could not parse nodeIndexFile.",e);
		} catch (IOException e) {
			//TODO: write log file.
			e.printStackTrace();
			throw new RuntimeException("Could not parse nodeIndexFile.",e);
		}
	}
	
	public NodeIndex getNodeIndex(){
		if(upToDate){
			return nodeIndex;
		}
		return null;
	}

	private NodeIndex parseNodeIndexFromDoc(Document doc) {
		Element rootElement = doc.getDocumentElement();
		
		//create base node index
		NodeIndex nodeIndexLocal = new NodeIndex();

		//get and add the directory to node index
	    String directory = getDirectoryFromRootElement(rootElement);
	    nodeIndexLocal.setURL(directory);
	    
	    //load the nodes into the nodeIndex
	    loadNodes(nodeIndexLocal,rootElement);
	    return nodeIndexLocal;
	}

	private void loadNodes(NodeIndex nodeIndexLocal, Element rootElement) {
		NodeList nodes = rootElement.getElementsByTagName(ELNAME_NODE);
		int numNodes = nodes.getLength();
		for(int i=0; i<numNodes; i++){
			loadNode(nodes.item(i),nodeIndexLocal);
		}
	}

	/**
	 * Loads id and filename into nodeIndexLocal
	 * @param node
	 * @param nodeIndexLocal
	 */
	private void loadNode(Node node, NodeIndex nodeIndexLocal) {
		String sId = ((Element)node).getElementsByTagName(ELNAME_ID).item(0).getTextContent();
		String sFilename = ((Element)node).getElementsByTagName(ELNAME_FILENAME).item(0).getTextContent();
		int id = Integer.parseInt(sId);
		nodeIndexLocal.addFilename(id, sFilename);
	}

	/**
	 * Takes in the root element from a nodeIndex.xml
	 * and returns directory.
	 * @param rootElement
	 * @return
	 */
	private String getDirectoryFromRootElement(Element rootElement) {
		NodeList directoryNodes = rootElement.getElementsByTagName(ELNAME_DIRECTORY);
		if(directoryNodes==null){
			throw new RuntimeException("nodeIndex.xml did not have a directory specified");
		}
		Node directoryNode = directoryNodes.item(0); //should be just 1!
		return directoryNode.getTextContent();
	}
	
	/**
	 * Writes a node index xml file to the directory which has been set.
	 * @param toWrite
	 * @return
	 */
	public boolean writeNodeIndex(NodeIndex toWrite){
		try{			
			DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
			
			// root elements
			Document doc = docBuilder.newDocument();
			Element rootElement = doc.createElement(ELNAME_ROOT);
			doc.appendChild(rootElement);
			
			addDirectoryNodeWrite(toWrite,rootElement,doc);
			addNodesWrite(toWrite,rootElement,doc);
			
			//Write out the document
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File(toWrite.getURL()+"/nodeIndex.xml"));
	 
			transformer.transform(source, result);
			
			return true;
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

	private void addNodesWrite(NodeIndex toWrite, Element rootElement,
			Document doc) {
				List<Integer> ids = toWrite.getAllIds();
				for(Integer id : ids){
					addNodeWrite(toWrite,rootElement,doc,id);
				}
	}

	private void addNodeWrite(NodeIndex toWrite, Element rootElement,
			Document doc, Integer id) {
		Element nodeElement = doc.createElement(ELNAME_NODE);
		rootElement.appendChild(nodeElement);
		addNodeContents(toWrite,nodeElement,doc,id);
	}

	private void addNodeContents(NodeIndex toWrite, Element nodeElement,
			Document doc, Integer id) {
		String filename = toWrite.getFilename(id);
		
		Element idElement = doc.createElement(ELNAME_ID);
		nodeElement.appendChild(idElement);
		idElement.appendChild(doc.createTextNode(id.toString()));
		
		Element filenameElement = doc.createElement(ELNAME_FILENAME);
		nodeElement.appendChild(filenameElement);
		filenameElement.appendChild(doc.createTextNode(filename));
	}

	/**
	 * Writes directory node to rootElement using toWrite 
	 * @param toWrite
	 * @param rootElement
	 */
	private void addDirectoryNodeWrite(NodeIndex toWrite, Element rootElement,Document doc) {
		// TODO Auto-generated method stub
		String directoryToWrite = toWrite.getURL();
		Element directoryElement = doc.createElement(ELNAME_DIRECTORY);
		rootElement.appendChild(directoryElement);
		
		directoryElement.appendChild(doc.createTextNode(directoryToWrite));
		
	}
	
	
}
