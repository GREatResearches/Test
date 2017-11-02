package cfmts.graph.dfts;

import java.util.ArrayList;
import java.util.LinkedList;

import cfmts.graph.dfts.Node_DFTS;
import fmp.featureModel.Feature;

public class Graph_DFTS {
	// By default, the initial node has id = 0
	ArrayList<Node_DFTS> nodes = new ArrayList<>();
	
	public ArrayList<Node_DFTS> getNodes() {
		return nodes;
	}

	public Node_DFTS getNodeById(int nodeId) {
		return nodes.get(nodeId);
	}
	
	public void setNodes(ArrayList<Node_DFTS> nodes) {
		this.nodes = nodes;
	}
	
	//NOTE: it only work with features with different names
	public Node_DFTS searchForDftsNode(LinkedList<Feature> activeFeatures){
		for (Node_DFTS node_DFTS : nodes) {
			//first compares the size, since the matched node should have the same number of active features
			boolean featureIsActiveInNode= false;
			if (node_DFTS.getActiveFeatures().size() == activeFeatures.size()){
				for (Feature feature : activeFeatures) {
					featureIsActiveInNode = false;
					for (Feature activeFeaturesInNode : node_DFTS.getActiveFeatures()) {
						if(feature.getName().equals(activeFeaturesInNode.getName())){
							featureIsActiveInNode = true;
							break; //if breaks the for and go to the next feature
						}
					}
					if(!featureIsActiveInNode)
						break; // it breaks the for and go to the next node_DFTS 
				}
				//it already has the DFTS node
				if(featureIsActiveInNode)
					return node_DFTS;
			}
		}
		//it did not find a correspondent node_DFTS
		return null;
	}
}
