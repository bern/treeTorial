package creator.end.api;

import java.rmi.UnexpectedException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * Class for containing a "piece of knowledge". Contains dependencies and
 * tutorial for this piece of knowledge.
 * @author coriakin
 *
 */
public class KnowledgeNode {

	/**
	 * The name of the "piece of knowledge"
	 */
	private String name;
	
	/**
	 * Unique id for the node. Primary key for database.
	 */
	private int id = -1;
	
	/**
	 * The actual description. Secretly html.
	 */
	private String body;
	
	/**
	 * The things you need to know to know this.
	 */
	private Map<Integer,KnowledgeNode> dependencies = new HashMap<Integer,KnowledgeNode>();
	
	/**
	 * A brief description of why you need to know a given dependency.
	 */
	private Map<Integer,String> dependencyDescription = new HashMap<Integer,String>();
	
	/**
	 * Represents whether the user "knows" this topic. Defaults to true.
	 */
	boolean understood=true;
	
	boolean fullyPopulated = false;
	
	/**
	 * Returns unigue ID of the Node
	 */
	public int getId(){
		return id;
	}
	
	/**
	 * Returns general name of the node
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the node
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Gets the secret html of the body to display on the Webpage
	 */
	public String getBody() {
		return body;
	}
	
	/**
	 * Sets the secret html of the body of each KnowledgeNode
	 */
	public void setBody(String body) {
		this.body = body;
	}
	
	/**
	 * Sets the unique ID of each KnowledgeNode
	 */
	public void setId(int id) {
		this.id = id;
	}
	
	/**
	 * Returns a list of the immediate dependencies of this knowledge node.
	 */
	public List<KnowledgeNode> getDependencies(){
		Set<Entry<Integer,KnowledgeNode>> dSet = dependencies.entrySet();
	    List<KnowledgeNode> dependencyList = new ArrayList<KnowledgeNode>();
	    for(Entry<Integer,KnowledgeNode> entry : dSet){
	    	dependencyList.add(entry.getValue());
	    }
	    return dependencyList;
	}
	
	/**
	 * Adds a depency to the current KnowledgeNode
	 * @param node
	 */
	public void addDependency(KnowledgeNode node){
		dependencies.put(node.getId(), node);
	}
	
	/**
	 * Remove the given node from my dependencies
	 */
	public void removeDependency(KnowledgeNode node){
		//
		dependencies.remove(node);
	}
	
	/**
	 * Eliminates all dependencies and children.
	 */
	public void removeAllDependencies(){
		dependencies = new HashMap<Integer,KnowledgeNode>();
		//dependencyDescription = new HashMap<Integer,String>();
	}
	
	/**
	 * Adds a mouse over description for the node
	 * @param node
	 * @param description
	 */
	public void addDescription(KnowledgeNode node,String description){
		Integer key = node.getId();
		dependencyDescription.put(key, description);
	}
	
	/**
	 * Gets the mouse over description of each KnowledgeNode
	 * @param node
	 * @return
	 */
	public String getDescription(KnowledgeNode node){
		String description = dependencyDescription.get(node);
		return description;
	}
	/**
	 * Marks the KnowledgeNode as understood
	 */
	public void markUnderstood(){
		understood = true;
	}
	/**
	 * Marks the KnowledgeNode as confusing
	 */
	public void markNotUnderstood(){
		understood =  false;
	}
	/**
	 * Returns if the KnowledgeNode is understood or not
	 * @return
	 */
	public boolean isUnderstood(){
		return understood;
	}
	
	/**
	 * If all the necessary information is required, then write to an XML file
	 * @return
	 */
	public boolean databaseWriteReady(){
		boolean ready = true;
		
		/**
		 * Below are the conditions that need to be true to write to XML doc
		 */
		if(name == null) ready = false;
		if(id == -1) ready = false;
		if(body == null) ready = false;
		if(!dependencies.isEmpty()){
			for(Integer id : dependencies.keySet()){
				if(dependencyDescription.get(id) == null) ready = false;
			}
		}
		
		return ready;
	}
	
	public boolean isLeafNode(){
		if(dependencies.isEmpty()) return true;
		else return false;
	}

	public boolean isFullyPopulated() {
		return fullyPopulated;
	}

	public void setFullyPopulated(boolean fullyPopulated) {
		this.fullyPopulated = fullyPopulated;
	}
	
	public int hashCode(){
		return id;
	}
}
