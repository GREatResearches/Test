package cfmts.graph.cks;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

public class Graph_CKS {

	ArrayList<Node_CKS> nodes = new ArrayList<>();

	public ArrayList<Node_CKS> getNodes() {
		return nodes;
	}

	public Node_CKS getNodeById(int nodeId) {
		return nodes.get(nodeId);
	}
	
	public void setNodes(ArrayList<Node_CKS> nodes) {
		this.nodes = nodes;
	}
	
	public void breadthFirstSearch(Node_CKS origin) {
		Queue<Node_CKS> queue = new LinkedList<Node_CKS>();
		
	    for (int i = 0; i < this.getNodes().size(); i++) {
	        this.getNodeById(i).setNotVisited();            
	    }

	    queue.add(origin);
	    origin.setVisited();

	    while (!queue.isEmpty()) {
	    	System.out.println(queue);
	        Node_CKS v = queue.remove();	        

	        for(int i = 0; i <v.getNextStates().size(); i++){
	            Node_CKS adj = (Node_CKS) v.getNextStates().get(i);
	            adj.setFather(v);
	            if(!adj.isVisited()){
	                //System.out.println(v.getId() + " >> "+adj.getId());
	                queue.add(adj);
	                adj.setVisited();
	            }                
	        }	        
	    }
	}
	
	public void dephtFirstSearch(Node_CKS origin) {
	    Stack<Node_CKS> stack = new Stack<>();
	    for (int i = 0; i < this.getNodes().size(); i++) {
	        this.getNodeById(i).setNotVisited();            
	    }

	    stack.push(origin);
	    origin.setVisited();

	    while (!stack.isEmpty()) {
	    	System.out.println(stack);
	        Node_CKS v = stack.pop();	        

	        for(int i = 0; i <v.getNextStates().size(); i++){
	            Node_CKS adj = (Node_CKS) v.getNextStates().get(i);
	            adj.setFather(v);
	            if(!adj.isVisited()){
	                //System.out.println(v.getId() + " >> "+adj.getId());
	                stack.push(adj);
	                adj.setVisited();
	            }                
	        }	        
	    }
	}
 
		 
}
