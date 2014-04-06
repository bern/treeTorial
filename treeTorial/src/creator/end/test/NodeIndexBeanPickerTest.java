package creator.end.test;

import creator.end.api.NodeIndex;
import creator.end.parsing.NodeIndexBeanPicker;

public class NodeIndexBeanPickerTest {
	public static void main(String[] args){
		testLoadBasicBean();
		testWriteBasicBean();
	}
	
	private static void testLoadBasicBean() {
		String nodeIndexLocation = "Resources/nodeIndex.xml";
		NodeIndexBeanPicker niBeanPicker = new NodeIndexBeanPicker();
		niBeanPicker.setDataDirectory(nodeIndexLocation);
		niBeanPicker.parseDataDirectory();
		NodeIndex readNodeIndex = niBeanPicker.getNodeIndex();
	}
	
	private static void testWriteBasicBean(){
		NodeIndex nodeIndex = new NodeIndex();
		nodeIndex.setURL("Resources");
		nodeIndex.addFilename(12345, "someFile.xml");
		nodeIndex.addFilename(456757, "someFile2.xml");
		
		NodeIndexBeanPicker niBeanPicker = new NodeIndexBeanPicker();
		niBeanPicker.setDataDirectory("Resources/AutoGenNodeIndex.xml");
		
		
		
		
		niBeanPicker.writeNodeIndex(nodeIndex);
	}
}
