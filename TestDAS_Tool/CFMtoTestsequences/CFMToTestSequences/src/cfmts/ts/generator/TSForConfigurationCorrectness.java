package cfmts.ts.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;
import cfmts.graph.dfts.Graph_DFTS;
import cfmts.graph.dfts.Node_DFTS;
import cfmts.ts.elements.TestCase;
import cfmts.ts.elements.TestSequence;
import cfmts.ts.utilities.HashMapGenerator;
import fmp.contextEvolModel.ContextState;

// It pass in each DFTS state at least once
public class TSForConfigurationCorrectness {

	private HashMapGenerator mapGen = new HashMapGenerator();
	// it generate a list of test sequences	
	//coverageRate is a range 0 to 1 (30%  = 0.3)
	//just need to return only 1 sequence since the the adaptations are not history-based (premisse)
	public TestSequence generateTestSequence(Graph_DFTS dfts, double coverageRate){
		int numStates = dfts.getNodes().size();
		int numStatesToCover = (int) (numStates * coverageRate); 
		// keeps the current number of States covered
		int actualNumberOfStates = 0; 
				
		TestSequence testSeq = new TestSequence();
		

		//Coverage == 0
		if(numStatesToCover == 0)
			return testSeq;
		
		//--- DEPTH SEARCH
		Stack<Node_DFTS> stackDFTS = new Stack<>();
		for (int i = 0; i < dfts.getNodes().size(); i++) {
			dfts.getNodeById(i).setNotVisited();            
		}

		stackDFTS.push(dfts.getNodes().get(0));
		dfts.getNodes().get(0).setVisited();
		
		stackAnalysis:
		while (!stackDFTS.isEmpty()) {
		    	Node_DFTS currentDFTSNode = stackDFTS.pop();

		    	//get the transitions for other states
		        for(int i = 0; i < currentDFTSNode.getTransitions().size(); i++){
		        	
		        	Node_DFTS adj = currentDFTSNode.getTransitions().get(i).getNewState();
		            
		            if(!adj.isVisited()){
		            	stackDFTS.push(adj);
		                adj.setVisited();
		                
		                //creates a test case
			            actualNumberOfStates++;
			    	    TestCase tc = new TestCase(actualNumberOfStates, 
			    	    		mapGen.generateMapFromContextState(currentDFTSNode.getTransitions().get(i).getCtxState()), 
			    	    		mapGen.generateMapFromFeaturesState(adj.getStringListOfAtiveFeature()));
			    	    tc.setIdTransitionDFTS(currentDFTSNode.getTransitions().get(i).getId());
			    	    testSeq.addTestItem(tc);			    	    
			    	    if(actualNumberOfStates>=numStatesToCover)
			    	    	break stackAnalysis;
		            }                
		        }	        
		}
		return testSeq;
	}
	
	//Given a test Sequence, it analysis the sequence to verify which cases were already covered
	//useful to generate one sequence and then analyze other criteria
	public TestSequence identifyMissingCases(Graph_DFTS dfts, TestSequence testSequence, double coverageRate){
		int numStates = dfts.getNodes().size();
		//expected coverage
		int numStatesToCover = (int) (numStates * coverageRate); 
		//keeps the current number of States covered
		//Supposing that it did not get a transition more than once
		int actualNumberOfStates = testSequence.getTestCases().size(); 
		
		//base case, verify if it is already completed
		if(actualNumberOfStates >= numStatesToCover)
			return testSequence;
					
		//--- DEPTH SEARCH
		Stack<Node_DFTS> stackDFTS = new Stack<>();
		for (int i = 0; i < dfts.getNodes().size(); i++) {
			dfts.getNodeById(i).setNotVisited();            
		}

		stackDFTS.push(dfts.getNodes().get(0));
		dfts.getNodes().get(0).setVisited();
		
		stackAnalysis:
		while (!stackDFTS.isEmpty()) {
		    	Node_DFTS currentDFTSNode = stackDFTS.pop();

		    	//get the transitions for other states
		        for(int i = 0; i < currentDFTSNode.getTransitions().size(); i++){
		        	
		        	Node_DFTS adj = currentDFTSNode.getTransitions().get(i).getNewState();
		            
		            if(!adj.isVisited()){
		            	stackDFTS.push(adj);
		                adj.setVisited();
		                
		                //Verify if this transitions is already in the Test Sequence 
		                boolean transitionInTestSequence = false;
		                int transitionID = currentDFTSNode.getTransitions().get(i).getId();
		                for (TestCase tc : testSequence.getTestCases()) {
							if(tc.getIdTransitionDFTS() == transitionID){
								transitionInTestSequence = true;
								break;
							}
						}
		                
		                //New test case to be added
		                if(!transitionInTestSequence){
				            actualNumberOfStates++;
				    	    TestCase tc = new TestCase(actualNumberOfStates, 
				    	    		mapGen.generateMapFromContextState(currentDFTSNode.getTransitions().get(i).getCtxState()), 
				    	    		mapGen.generateMapFromFeaturesState(adj.getStringListOfAtiveFeature()));
				    	    tc.setIdTransitionDFTS(currentDFTSNode.getTransitions().get(i).getId());
				    	    testSequence.addTestItem(tc);			    	    
				    	    if(actualNumberOfStates>=numStatesToCover)
				    	    	break stackAnalysis;
		                }
		            }                
		        }	        
		}
		return testSequence;
	}
	
}
