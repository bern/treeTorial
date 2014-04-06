package creator.end.api.copy;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.LinkedList;

//Validates functions 
public class MapValidator {
	
	/**
	 * If the child that is being added to the parent is already in the dependency list 
	 * of the parent, a cycle will be created.  
	 * @param child
	 * @param parent
	 * @return
	 */
	public static boolean validateAddDependency(KnowledgeNode child, KnowledgeNode parent){
		//Get list of all dependencies in the parent KnowledgeNode
		 Queue<KnowledgeNode> queue = new LinkedList<KnowledgeNode>(); 
		 List<KnowledgeNode> dependencyList = new ArrayList<KnowledgeNode>();
		
		 KnowledgeNode current = parent;
		 queue.add(current);
		 
		 do{
			 current = queue.remove();
			 if(current.getId() == child.getId()) return false;
			 if(!current.isLeafNode()){
				 dependencyList = current.getDependencies();
				 for(int i = 0; i < dependencyList.size(); i++){
					 queue.add(dependencyList.get(i));
				 }
			 }
		 }
		 while(!queue.isEmpty());
		 return true;
	}

}
