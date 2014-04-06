package creator.end.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeIndex {
	
	private String sURL;
	
	private Map<Integer,String> idsToFilename = new HashMap<Integer,String>();

	public String getURL() {
		return sURL;
	}

	public void setURL(String sURL) {
		this.sURL = sURL;
	}
	
	public void addFilename(int id, String filename){
		idsToFilename.put(id, filename);
	}
	
	public String getFilename(int id){
		return idsToFilename.get(id);
	}
	
	public void removeFilename(int id){
		idsToFilename.remove(idsToFilename);
	}
	
	public List<Integer> getAllIds(){
		return new ArrayList<Integer>(idsToFilename.keySet());
	}
	
	public void clearFilenames(){
		this.idsToFilename = new HashMap<Integer,String>();
	}

}
