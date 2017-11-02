package cfmts.ts.generator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import cfmts.graph.dfts.Graph_DFTS;
import cfmts.graph.dfts.Node_DFTS;
import cfmts.graph.dfts.Transition_DFTS;
import cfmts.ts.elements.TestCase;
import cfmts.ts.elements.TestSequence;
import cfmts.ts.elements.TestSequenceList;
import cfmts.ts.utilities.HashMapGenerator;
import fmp.contextEvolModel.AdaptationRuleWithCtxProposition;
import fmp.contextEvolModel.CKS;
import fmp.contextEvolModel.ContextProposition;
import fmp.contextModel.AdaptationRuleWithCtxFeature;
import fmp.contextModel.CFM;
import fmp.contextModel.ContextFeature;

//test each adaptation rule of the DSPL
public class TSForRuleLiveness {

	private HashMapGenerator mapGen = new HashMapGenerator();
	//Map the "ID-AdaptationRule" and the "DFTS transition" that cover id
	// The ID-AdaptationRule = position in the list adaptationRules  in CFM (Context Feature Model)
	//DFTS transition = 0 not covered yet
	private Map<Integer, Integer> mapAdaptationRulesToCoverIdTransition = new HashMap<>();
	
	// list not covered
	private ArrayList<Integer> mapAdaptationRulesNotCovered;
	int actualNumberOfAdaptationRulesCovered = 0;
	int numAdaptationRulesToCover;
	//private DSPL dspl;
	private CFM cfm;
	//private CKS cks;
	
	int tcId = 0;
	
	public TSForRuleLiveness(CFM cfm){
		this.cfm = cfm;
		//get the list of all adaptation rules
		int sizeAdaptationRulesList = cfm.getAdaptationRules().size();
		for (int i =0; i < sizeAdaptationRulesList; i++) {
			mapAdaptationRulesToCoverIdTransition.put(i, 0);
		}
	}
	
	public TestSequenceList generateTestSequence(Graph_DFTS dfts, double coverageRate, TestSequenceList testSeqList){
		int numAdapationRules = mapAdaptationRulesToCoverIdTransition.size();
		numAdaptationRulesToCover = (int) (numAdapationRules * coverageRate); 
		// keeps the current number of States covered
		
		//Coverage == 0
		if(numAdaptationRulesToCover == 0)
			return new TestSequenceList();
		
		Analysis:
			for (Entry entry : mapAdaptationRulesToCoverIdTransition.entrySet()) {
			TestSequence testSeq = new TestSequence();
			
				if((int) entry.getValue() == 0){ // => has not covered yet
				 	// Adaptation Rule R
					int  idAdapRuleAnalyzed = (int)entry.getKey();
					AdaptationRuleWithCtxFeature adapRuleAnalyzed = cfm.getAdaptationRules().get(idAdapRuleAnalyzed);
					
					// looks for the DFTS tansition that trigguer R
				    for (Node_DFTS nodeDFTS : dfts.getNodes()) {
						
				    	//Verifies the adaptation after the node
						for (Transition_DFTS transitionDFTS : nodeDFTS.getTransitions()) {
							Node_DFTS nextNode = transitionDFTS.getNewState();
							
							//verifiies if this transition trigguers the adaptation rule R
							if(verifyIfContextTrigguersRule(transitionDFTS.getCtxState().getAtiveContextPropositions(), 
									adapRuleAnalyzed.getContextRequired())){
								
								// new TC
					    	    actualNumberOfAdaptationRulesCovered++;
								tcId++;
	    			    	    TestCase tc = new TestCase(tcId, 
	    			    	    		mapGen.generateMapFromContextState(transitionDFTS.getCtxState()), 
	    			    	    		mapGen.generateMapFromFeaturesState(nextNode.getStringListOfAtiveFeature()));
	    			    	    tc.setIdTransitionDFTS(transitionDFTS.getId());
	    			    	    testSeq.addTestItem(tc);
	    			    	    
	    			    	    // update map
	    			    	    mapAdaptationRulesToCoverIdTransition.put(idAdapRuleAnalyzed,transitionDFTS.getId());
	    			    	    //  Verify if there are more adaptation rules covered
	    			    	    verifyOtherCoveredAdaptRules(transitionDFTS);
	    			    	    
	    			    	    if(actualNumberOfAdaptationRulesCovered >= numAdaptationRulesToCover){
	    			    	    	testSeqList.add(testSeq);
	    			    	    	break Analysis;
	    			    	    }
	    			    	    else{
	    			    	    	//try to keep the sequence looking for more adaptation rules to cover
	    			    	    	//it return testSequence updated
	    			    	    	updateMapAdaptRulesNotCovered();
	    			    	    	testSeq = walkDFTSForMore(nextNode, testSeq); 
	    			    	    }
							}						
						}							
								
									
					} // loop searching for DSPl nodes
				}
				if(testSeq.getTestCases().size() != 0)  // equals to zero means that if did not found any TC, for example, there is not in the CFM
					testSeqList.add(testSeq); // Add TestSequence to TestSequenceLSit
			}	
	
		return testSeqList;
		
	}
	
	// RecursiveFunction
	// Verify if other adaptation Rules can be covered following the sequence If yes -> add to test sequence. 
	// Otherwise, go to first cycle again and create a new TS
	// update map	
	public TestSequence walkDFTSForMore(Node_DFTS currentNodeAtTestSequence, TestSequence testSeq){
		// analysis the updated list of features not covered
		TestCase tc1 = null;
		Node_DFTS nextNode = null;
		DeepAnalysis: for (Integer entryUpdatedList : mapAdaptationRulesNotCovered) {
			//verify if one of them are in the currentStateAtTS
			int  idAdapRuleAnalyzed = entryUpdatedList;
			AdaptationRuleWithCtxFeature adapRuleAnalyzed = cfm.getAdaptationRules().get(idAdapRuleAnalyzed);
			
			for (Transition_DFTS transitionDFTS : currentNodeAtTestSequence.getTransitions()) {
				nextNode = transitionDFTS.getNewState();
				//Feature F is OFF in the Next State
				if(verifyIfContextTrigguersRule(transitionDFTS.getCtxState().getAtiveContextPropositions(), 
						adapRuleAnalyzed.getContextRequired())){
					// new TC
					actualNumberOfAdaptationRulesCovered++;
					tcId++;
		    	    tc1 = new TestCase(tcId, 
		    	    		mapGen.generateMapFromContextState(transitionDFTS.getCtxState()), 
		    	    		mapGen.generateMapFromFeaturesState(nextNode.getStringListOfAtiveFeature()));
		    	    tc1.setIdTransitionDFTS(transitionDFTS.getId());
		    	    testSeq.addTestItem(tc1);
		    	    
		    	    
		    	    // update map
		    	    mapAdaptationRulesToCoverIdTransition.put(idAdapRuleAnalyzed,transitionDFTS.getId());
		    	    //  Verify if there are more adaptationRules covered
		    	    verifyOtherCoveredAdaptRules(transitionDFTS);
		    	    break DeepAnalysis;
				}
			}
		
		}
	    if(tc1 != null){
	    	updateMapAdaptRulesNotCovered();
	        return walkDFTSForMore(nextNode, testSeq); // TODO ver se precisa retornar isso, ou se já altera direto
	    }
	    else
	     return testSeq;		
	}

	// Given the transition, it verifies if it cover others adaptation rules
	private void verifyOtherCoveredAdaptRules(Transition_DFTS transitionDFTS){
		updateMapAdaptRulesNotCovered();
		for (Integer entry : mapAdaptationRulesNotCovered) {
			int  idAdapRuleAnalyzed = (int) entry;
			AdaptationRuleWithCtxFeature adapRuleAnalyzed = cfm.getAdaptationRules().get(idAdapRuleAnalyzed);
			
			if(verifyIfContextTrigguersRule(transitionDFTS.getCtxState().getAtiveContextPropositions(), 
					adapRuleAnalyzed.getContextRequired())){
				//update Main Map Feature - Transition ID
				mapAdaptationRulesToCoverIdTransition.put(idAdapRuleAnalyzed, transitionDFTS.getId());
				actualNumberOfAdaptationRulesCovered++;
			}
								
		}
	}
	
	// Given the context String list, it verifies if it cover others adaptation rules
	private void verifyOtherCoveredAdaptRulesWithString(LinkedList<String> currentContext, int idTransition){
		updateMapAdaptRulesNotCovered();
		for (Integer entry : mapAdaptationRulesNotCovered) {
			int  idAdapRuleAnalyzed = (int) entry;
			AdaptationRuleWithCtxFeature adapRuleAnalyzed = cfm.getAdaptationRules().get(idAdapRuleAnalyzed);
			
			if(verifyIfContextStringListTrigguersRule(currentContext,adapRuleAnalyzed.getContextRequired())){
				//update Main Map Feature - Transition ID
				mapAdaptationRulesToCoverIdTransition.put(idAdapRuleAnalyzed, idTransition);
				actualNumberOfAdaptationRulesCovered++;
			}
								
		}
	}
	
	
	//Given a test Sequence, it analysis the sequence to verify which cases were already covered
	//useful to generate one sequence and then analyze other criteria
	public TestSequenceList identifyMissingCases(Graph_DFTS dfts, TestSequenceList testSeqList, double coverageRate){
		//The list mapFeaturesToCoverIdTransition is already filled by the class constructor
		for (Entry	entry : mapAdaptationRulesToCoverIdTransition.entrySet()) {
			int  idAdapRuleAnalyzed = (int) entry.getKey();
			AdaptationRuleWithCtxFeature adapRuleAnalyzed = cfm.getAdaptationRules().get(idAdapRuleAnalyzed);
			
			if((int) entry.getValue() == 0){ // => has not covered yet
				
				//Verifying if this adaptation rule was already covered by the TestSequence given as input
				// Updating the mapAdaptationRulesToCoverIdTransition
				for (TestSequence testSeq : testSeqList.getTestSequences()) {
					LinkedList<String> currentContextStringList = null;
					
					for (TestCase testCase : testSeq.getTestCases()) {
						currentContextStringList = testCase.getContextStringList();
						
						// verifies if it satisfies  the transition
						if(verifyIfContextStringListTrigguersRule(currentContextStringList, 
								adapRuleAnalyzed.getContextRequired())){
								//update Main Map Feature - Transition ID
							    //update Main Map Feature - Transition ID Fix 13.07
								if(mapAdaptationRulesToCoverIdTransition.get(idAdapRuleAnalyzed) == 0)
									actualNumberOfAdaptationRulesCovered++;
								mapAdaptationRulesToCoverIdTransition.put(idAdapRuleAnalyzed, testCase.getIdTransitionDFTS());
								
								
							    //Verify if there are more adaptation rules covered
								verifyOtherCoveredAdaptRulesWithString(currentContextStringList, testCase.getIdTransitionDFTS());
						}
					}		
				}
			}// End first IF (value ==0)
			
		}
		
		int numAdapationRules = mapAdaptationRulesToCoverIdTransition.size();
		numAdaptationRulesToCover = (int) (numAdapationRules * coverageRate); 
		
		//Base case, already covered
		if(actualNumberOfAdaptationRulesCovered >= numAdaptationRulesToCover)
			return testSeqList;
	
		//TODO (27.05)
		// Run walk to continue the same test sequence?... Ele está criando novas test sequence mesmo podendo continuar uma já existente
		return generateTestSequence(dfts, coverageRate,testSeqList);		
	}
				
	//update the list of adaptation rules not covered
	public void updateMapAdaptRulesNotCovered(){
		mapAdaptationRulesNotCovered = new ArrayList<>();
		for (Entry entry : mapAdaptationRulesToCoverIdTransition.entrySet()) {
			if((int)entry.getValue() == 0 ){
				mapAdaptationRulesNotCovered.add((int)entry.getKey());				 
			}
		}
	}	
	
	// It could use also Context Features from  CFM, but the Graph is using Context Proposition, then I used them
	public boolean verifyIfContextTrigguersRule(LinkedList<ContextProposition> currentContext, LinkedList<ContextFeature> requiredContextToRule){
		if((currentContext == null) | (currentContext.size() == 0) | (requiredContextToRule == null) || (requiredContextToRule.size() == 0))
			return false; //
		
		boolean match = true;
	
		for (ContextFeature contextRequired : requiredContextToRule) { // first FOR
			if(!match)
				break;
			else
				match = false; 			
			for (ContextProposition actualContext : currentContext) {
				if(contextRequired.getName().equals(actualContext.getName())){
					match = true;
					continue; // go to first FOR
				}
			}
		}
		return match;		
	}
	
	// It could use also Context Features from  CFM, but the Graph is using Context Proposition, then I used them
	public boolean verifyIfContextStringListTrigguersRule(LinkedList<String> currentContext, LinkedList<ContextFeature> requiredContextToRule){
		if((currentContext == null) | (currentContext.size() == 0) | (requiredContextToRule == null) || (requiredContextToRule.size() == 0))
			return false; //
		
		boolean match = true;
	
		for (ContextFeature contextRequired : requiredContextToRule) { // first FOR
			if(!match)
				break;
			else
				match = false; 			
			for (String actualContext : currentContext) {
				if(contextRequired.getName().equals(actualContext)){
					match = true;
					continue; // go to first FOR
				}
			}
		}
		return match;		
	}
	
}
