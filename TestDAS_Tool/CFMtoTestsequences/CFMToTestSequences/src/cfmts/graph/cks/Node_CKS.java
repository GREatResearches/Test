package cfmts.graph.cks;

import cfmts.graph.dfts.GenerateDFTSGFromCKSG;
import fmp.contextEvolModel.ContextProposition;
import fmp.contextEvolModel.ContextState;

public class Node_CKS extends ContextState{

	boolean visited;
	
	//set the father in the search process
	//TODO can be removed?
	Node_CKS father = null;
	
	public Node_CKS(){
		setNotVisited();
	}
	
	public boolean isVisited() {
		return visited;
	}
	
	public void setVisited() {
		this.visited = true;
		
	}
	
	public void setNotVisited() {
		this.visited = false;
	}
	
	public Node_CKS getFather() {
		return father;
	}

	public void setFather(Node_CKS father) {
		this.father = father;
	}

	public void printNode(){
		System.out.println("Node: " + this.getId());
		System.out.print("[ ");
		for (ContextProposition contextProposition : this.getAtiveContextPropositions()) {
			System.out.print(contextProposition.getName()+" ");
		}
		System.out.println("]");
	}
	
	public String toString(){
		return String.valueOf(this.getId());
	}
	
}
