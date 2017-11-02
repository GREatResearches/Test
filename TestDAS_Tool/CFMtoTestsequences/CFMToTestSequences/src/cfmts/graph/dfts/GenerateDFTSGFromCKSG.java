package cfmts.graph.dfts;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import cfmts.graph.cks.Graph_CKS;
import cfmts.graph.cks.Node_CKS;
import fmp.contextEvolModel.AdaptationRuleWithCtxProposition;
import fmp.contextEvolModel.ContextProposition;
import fmp.contextModel.AdaptationRuleWithCtxFeature;
import fmp.contextModel.CFM;
import fmp.contextModel.ContextFeature;
import fmp.featureModel.DSPL;
import fmp.featureModel.Feature;

public class GenerateDFTSGFromCKSG {

	// it generates the DFTS GRATH from the CKS Graph
	private DSPL dspl;
	private CFM cfm;
	private Graph_DFTS dftsG;
	private Graph_CKS cksG;
	private Node_DFTS initialNode;
	private int transitionsId = 1;
	
	public GenerateDFTSGFromCKSG(DSPL dspl, CFM cfm, Graph_CKS cksG) {
		// TODO Auto-generated constructor stub
		this.dspl = dspl;
		this.cfm = cfm;
		this.cksG = cksG;
		
		dftsG = new Graph_DFTS();
		this.initialNode = new Node_DFTS();
		this.initialNode.setId(0);
		if(dspl.getInitialConfiguration()!= null){
			// TODO trying with a "blank" initial node
			//this.initialNode.setActiveFeatures(dspl.getInitialConfiguration());
		}
		dftsG.getNodes().add(initialNode); // add the initial node to the DFTS graph
	}
	
	//it makes the breadth search on CKS Graph and generates the DFTS graph
	public void dephtFirstSearchGeneratingDFTSTrasitions(Node_CKS origin) {
	    Stack<Node_CKS> stack = new Stack<>();
	    Stack<Node_DFTS> stackDFTS = new Stack<>();
	    for (int i = 0; i < cksG.getNodes().size(); i++) {
	    	cksG.getNodeById(i).setNotVisited();            
	    }

	    stack.push(origin);
	    origin.setVisited();
	    Node_DFTS resultingDFTSNode = generateTransitionOnDFTSGraph(origin,initialNode);
	    stackDFTS.push(resultingDFTSNode);
	    
	    while (!stack.isEmpty()) {
	    	System.out.println(stack);
	        Node_CKS v = stack.pop();	
	        Node_DFTS currentDFTSNode = stackDFTS.pop();

	        for(int i = 0; i <v.getNextStates().size(); i++){
	            Node_CKS adj = (Node_CKS) v.getNextStates().get(i);
	            adj.setFather(v);
	            if(!adj.isVisited()){
	                //System.out.println(v.getId() + " >> "+adj.getId());
	                stack.push(adj);
	                adj.setVisited();
	                resultingDFTSNode = generateTransitionOnDFTSGraph(adj,currentDFTSNode);
	                stackDFTS.push(resultingDFTSNode);
	            }                
	        }	        
	   }
	}
	    
	/*public void breadthFirstSearchGeneratingDFTSTrasitions(Node_CKS origin) {
		Queue<Node_CKS> queue = new LinkedList<Node_CKS>();
		
	    for (int i = 0; i < cksG.getNodes().size(); i++) {
	    	cksG.getNodeById(i).setNotVisited();            
	    }

	    queue.add(origin);
	    origin.setVisited();
	    Node_DFTS resultingDFTSNode = generateTransitionOnDFTSGraph(origin,initialNode);

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
	                generateTransitionOnDFTSGraph(adj,resultingDFTSNode); //TODO
	            }                
	        }	        
	    }
	}*/
	
	//TODO
	// One NODE_CKS generates only one transition linked to one NODE_DFTS
	// Now (19.04), focus only in the adaptable features and  in the active features
	public Node_DFTS generateTransitionOnDFTSGraph(Node_CKS cksGNode, Node_DFTS currentDFTSNode){
		//1 -  for each context active in the Node look for the active Features
		
		// list of active features as responde to all active contexts in cksGNode
		LinkedList<Feature> activeFeaturesByTheCKSN = new LinkedList<>(); 
		for (ContextProposition activeContext : cksGNode.getAtiveContextPropositions()){
			//it will fill the list activefeatures
			searchForActivatedFeaturesByTheContext(activeContext, activeFeaturesByTheCKSN);
		}
		
		//2 - creating the transition and a new DFTS node when necessary
		Transition_DFTS transition = new Transition_DFTS();
		transition.setId(transitionsId);
		transitionsId++;
		transition.setCtxState(cksGNode);
		
		//verifying whether the DFTS Graph has the corresponding Node_DFTS or not 
		Node_DFTS dftsNode = dftsG.searchForDftsNode(activeFeaturesByTheCKSN);
		//TODO
		//Always we have a context state that activates none feature => it is created a new DFTS Node with empty active feature list
		if(dftsNode == null){
			//it should be created a new DFTS Node
			dftsNode = new Node_DFTS();
			dftsNode.setId(dftsG.getNodes().size());
			dftsNode.setActiveFeatures(activeFeaturesByTheCKSN);	
			dftsG.getNodes().add(dftsNode);
		}		
		transition.setNewState(dftsNode);	
		
		//3 - Link the transition with the analyzed NODE_DFTS
		currentDFTSNode.getTransitions().add(transition);
		return dftsNode;
	}
	
	//works only with one context as trigger (A -> activate feature 1, 2..)
	//TODO currently, it does not consider require and exclude rules
	public void searchForActivatedFeaturesByTheContext(ContextProposition activeContext,LinkedList<Feature> activeFeaturesList){
		//Checks if the context trigguer a rules
		//TODO spite of getting the list of context trigger, I consider here just one context max
		for (AdaptationRuleWithCtxFeature aRule : cfm.getAdaptationRules()) {
			
			//NOTE: This part do not consider more than one context for a rule. E.g. Normal + Source Source => ...  
			if(aRule.getContextRequired()!= null){
				//gets only the first because deals with only 1 Ctx to * Features
				ContextFeature ctxProp = aRule.getContextRequired().getFirst();
				if(ctxProp.getName().equals(activeContext.getName())){
					//return the features activated by the context
					for (Feature feature : aRule.getToActiveFeatureList()) {
						//it adds to the featuresList if it did not have that feature before
						if(!featureListContainsFeature(activeFeaturesList,feature)){
							activeFeaturesList.add(feature);
						}						
					}								
				}
			}
		}		
	}
	
	//TODO
	//adaptation that deactivates features
	public void searchForDeactivatedFeaturesByTheContext(){
		//melhor dentro da outra função feita pois fica uma analise só!!
		// precisa dessa função? considerando que o modelo é correto pelo model checking
	}
	
	public boolean featureListContainsFeature(LinkedList<Feature> featureList, Feature feature){
		for (Feature featureInList : featureList) {
			if(featureInList.getName().equals(feature.getName()))
				return true;
		}
		return false;
	}
	
	public Graph_DFTS getGraph_DFTS(){
		return this.dftsG;
	}
	
	public void printGraphDFTS(){
		System.out.println("PRINTING THE GRAPH");
		for (Node_DFTS node : dftsG.getNodes()) {
			System.out.println("---------------------");
			System.out.println("Node ID: "+node.getId());
			System.out.print("Active features: [");
			for (Feature feature : node.getActiveFeatures()) {
				System.out.print(" "+ feature.getName());
			}
			System.out.print("]");
			System.out.println("\n");
			if(node.getTransitions().size() > 0)
				System.out.println("Transitions:");
			for (Transition_DFTS transition : node.getTransitions()) {
				System.out.println("Transition ID: "+transition.getId());
				System.out.println("Context Trigger: "+transition.getCtxState().getAtiveContextPropositionsIntoAString());
				System.out.println("Next System State ID: "+transition.getNewState().getId());
				System.out.println("");
			}	
			
		}
	}
	
}
