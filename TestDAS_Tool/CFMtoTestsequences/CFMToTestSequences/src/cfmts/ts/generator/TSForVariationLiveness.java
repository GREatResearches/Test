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
import fmp.featureModel.DSPL;
import fmp.featureModel.Feature;
import fmp.featureModel.FeatureType;

//test a transition F[on] -> F[off], for every non-mandatory feature
public class TSForVariationLiveness {
	private HashMapGenerator mapGen = new HashMapGenerator();
	//Map between Features Non mandatory (that we had to cover) and the transitions
	private Map<String, Integer> mapFeatureToCoverIdTransition = new HashMap<>();
	
	// list not covered
	//Changed to array list --> not need to be a Map with value (21.05) copied from TSForFeatureLiveness
	private ArrayList<String> mapFeatureNotCovered;
	int actualNumberOfFeaturesCovered = 0;
	int numFeaturesToCover;
	private DSPL dspl;
	
	int tcId = 0;
	
	public TSForVariationLiveness(DSPL dspl){
		this.dspl = dspl;
		//get the list of all non-mandatory features
		for (Feature feature : dspl.getFeatures()) {
			if(!feature.getFeatureType().equals(FeatureType.Mandatory))
				mapFeatureToCoverIdTransition.put(feature.getName(), 0);
		}
	}
		
	public TestSequenceList generateTestSequence(Graph_DFTS dfts, double coverageRate, TestSequenceList testSeqList){
			
		int numFeaturesNonMandatory = mapFeatureToCoverIdTransition.size();
		numFeaturesToCover = (int) (numFeaturesNonMandatory * coverageRate); 
		// keeps the current number of States covered
		
		//Coverage == 0
		if(numFeaturesToCover == 0)
			return new TestSequenceList();
		
		Analysis:
			for (Entry entry : mapFeatureToCoverIdTransition.entrySet()) {
			TestSequence testSeq = new TestSequence();
			
				if((int) entry.getValue() == 0){ // => has not covered yet
			 	// Feature F
				String featureAnalyzed = (String) entry.getKey();
				
					// looks for the DFTS state where F is ON
					for (Node_DFTS nodeDFTS : dfts.getNodes()) {
						//TODO
						if(nodeDFTS.getArrivalTransitions().size() == 0 ) // First Node (empty) and without arrival Transitions
							continue;
						// Feature F is ON in the State
						if(featureActiveListHasFeature(nodeDFTS.getActiveFeatures(), featureAnalyzed)){
							// NodeDFTS = S1
							for (Transition_DFTS transitionDFTS : nodeDFTS.getTransitions()) {
								Node_DFTS nextNode = transitionDFTS.getNewState();
								//Feature F is OFF in the Next State
								if(!featureActiveListHasFeature(nextNode.getActiveFeatures(), featureAnalyzed)){
									// new TC
									// If It is the first of test sequence state => add previous transition: Ctx -> S1
									// It needs this to assure that the state before the test case will be S1
									tcId++;
									TestCase tc1 = new TestCase(tcId, 
		    			    	    		mapGen.generateMapFromContextState(nodeDFTS.getArrivalTransitions().get(0).getCtxState()), 
		    			    	    		mapGen.generateMapFromFeaturesState(nodeDFTS.getStringListOfAtiveFeature()));
		    			    	    tc1.setIdTransitionDFTS(nodeDFTS.getArrivalTransitions().get(0).getId());
		    			    	    testSeq.addTestItem(tc1);
									// If It is not the first of test sequence state => just add the transition analyzed : S1, Ctx -> S2
									actualNumberOfFeaturesCovered++;
									tcId++;
		    			    	    TestCase tc2 = new TestCase(tcId, 
		    			    	    		mapGen.generateMapFromContextState(transitionDFTS.getCtxState()), 
		    			    	    		mapGen.generateMapFromFeaturesState(nextNode.getStringListOfAtiveFeature()));
		    			    	    tc2.setIdTransitionDFTS(transitionDFTS.getId());
		    			    	    testSeq.addTestItem(tc2);
		    			    	    
		    			    	    // update map
		    			    	    mapFeatureToCoverIdTransition.put(featureAnalyzed,transitionDFTS.getId());
		    			    	    //  3 Verify if there are more features ON -> OFF from S1 -> S2.
		    			    	    verifyOtherCoveredFeatures(nodeDFTS, nextNode, transitionDFTS);
		    			    	    //TODO add to the Test Seq List
		    			    	    if(actualNumberOfFeaturesCovered>=numFeaturesToCover){
		    			    	    	testSeqList.add(testSeq);
		    			    	    	break Analysis;
		    			    	    }
		    			    	    else{
		    			    	    	//try to keep the sequence looking for more features to cover
		    			    	    	//it return testSequence updated
		    			    	    	updateMapFeatureNotCovered();
		    			    	    	testSeq = walkDFTSForMore(nextNode, testSeq);
		    			    	    }
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
		// 4 Verify who is false at S2 and follow the transitions looking for new features ON -> OFF. If yes -> add to test sequence. 
		// Otherwise, go to first cycle again and create a new TS
		// update map	
		public TestSequence walkDFTSForMore(Node_DFTS currentNodeAtTestSequence, TestSequence testSeq){

			// analysis the updated list of features not covered
			TestCase tc3 = null;
			Node_DFTS nextNode = null;
			DeepAnalysis: for (String entryUpdatedList : mapFeatureNotCovered) {
				//verify if one of them are in the currentStateAtTS
				String featureAnalyzed = entryUpdatedList;
				// Feature F = ON in current State
				if(featureActiveListHasFeature(currentNodeAtTestSequence.getActiveFeatures(), featureAnalyzed)){
					
					for (Transition_DFTS transitionDFTS : currentNodeAtTestSequence.getTransitions()) {
						nextNode = transitionDFTS.getNewState();
						//Feature F is OFF in the Next State
						if(!featureActiveListHasFeature(nextNode.getActiveFeatures(), featureAnalyzed)){
							// new TC
							actualNumberOfFeaturesCovered++;
							tcId++;
				    	    tc3 = new TestCase(tcId, 
				    	    		mapGen.generateMapFromContextState(transitionDFTS.getCtxState()), 
				    	    		mapGen.generateMapFromFeaturesState(nextNode.getStringListOfAtiveFeature()));
				    	    tc3.setIdTransitionDFTS(transitionDFTS.getId());
				    	    testSeq.addTestItem(tc3);
				    	    
				    	    
				    	    // update map
				    	    mapFeatureToCoverIdTransition.put(featureAnalyzed,transitionDFTS.getId());
				    	    //  3 Verify if there are more features ON -> OFF from S1 -> S2.
				    	    verifyOtherCoveredFeatures(currentNodeAtTestSequence, nextNode, transitionDFTS);
				    	    break DeepAnalysis;
						}
					}
					
				}
			}
		    if(tc3 != null){
		    	updateMapFeatureNotCovered();
		        return walkDFTSForMore(nextNode, testSeq); // TODO ver se precisa retornar isso, ou se já altera direto
		    }
		    else
		     return testSeq;		
		}
		
		// Given the current State, the next State and the transition, it verifies other Features covered
		private void verifyOtherCoveredFeatures(Node_DFTS currentNode, Node_DFTS nextNode, Transition_DFTS transitionDFTS){
			updateMapFeatureNotCovered();
			for (String entry : mapFeatureNotCovered) {
				String featureAnalyzed = entry;
				if(featureActiveListHasFeature(currentNode.getActiveFeatures(), featureAnalyzed)){ // in S1 F is ON
					if(!featureActiveListHasFeature(nextNode.getActiveFeatures(), featureAnalyzed)){ // in S2 F is OFF
						//update Main Map Feature - Transition ID
						mapFeatureToCoverIdTransition.put(featureAnalyzed, transitionDFTS.getId());
						actualNumberOfFeaturesCovered++;
					}
				}		
			}
		}
		
		// Given the current State, the next State and the transition, it verifies other Features covered
			private void verifyOtherCoveredFeaturesWithStrings(LinkedList<String> currentNodeString, LinkedList<String> nextNodeString, int transitionDFTSId){
				updateMapFeatureNotCovered();
				for (String entry : mapFeatureNotCovered) {
					String featureAnalyzed = entry;
					if(featureActiveStringListHasFeature(currentNodeString, featureAnalyzed)){ // in S1 F is ON
						if(!featureActiveStringListHasFeature(nextNodeString, featureAnalyzed)){ // in S2 F is OFF
							//update Main Map Feature - Transition ID
							mapFeatureToCoverIdTransition.put(featureAnalyzed, transitionDFTSId);
							actualNumberOfFeaturesCovered++;
						}
					}		
				}
		}
		
		//Given a test Sequence, it analysis the sequence to verify which cases were already covered
		//useful to generate one sequence and then analyze other criteria
		public TestSequenceList identifyMissingCases(Graph_DFTS dfts, TestSequenceList testSeqList, double coverageRate){
			
			//The list mapFeaturesToCoverIdTransition is already filled by the class constructor
			for (Entry	entry : mapFeatureToCoverIdTransition.entrySet()) {
				String featureAnalyzed = entry.getKey().toString();
				
				if((int) entry.getValue() == 0){ // => has not covered yet
					
					//Verifying if this feature was already covered by the TestSequence given as input
					// Updating the mapFeatyresToCoverIdTransition
					for (TestSequence testSeq : testSeqList.getTestSequences()) {
						LinkedList<String> currentFeatureStatusStringList = null;
						for (TestCase testCase : testSeq.getTestCases()) {
							// First TestCase of the List (i.e, in the Sequence we have: NULL->transition->Feature Status)
							if(currentFeatureStatusStringList == null ){
								currentFeatureStatusStringList = testCase.getExpectedFeaturesStringList();
								continue;
							}
								
							
							if(featureActiveStringListHasFeature(currentFeatureStatusStringList, featureAnalyzed)){ // in S1 F is ON
								if(!featureActiveStringListHasFeature(testCase.getExpectedFeaturesStringList(), featureAnalyzed)){ // in S2 F is OFF
									//update Main Map Feature - Transition ID
									//update Main Map Feature - Transition ID Fix 13.07
									if(mapFeatureToCoverIdTransition.get(featureAnalyzed) == 0)
										actualNumberOfFeaturesCovered++;
									mapFeatureToCoverIdTransition.put(featureAnalyzed, testCase.getIdTransitionDFTS());
									
									
								    //Verify if there are more features ON -> OFF from S1 -> S2.
									verifyOtherCoveredFeaturesWithStrings(currentFeatureStatusStringList, testCase.getExpectedFeaturesStringList(), testCase.getIdTransitionDFTS());
								}
							} 
							currentFeatureStatusStringList = testCase.getExpectedFeaturesStringList();
						}		
					}
				}// End first IF (value ==0)
				
			}
			
			int numFeaturesNonMandatory = mapFeatureToCoverIdTransition.size();
			numFeaturesToCover = (int) (numFeaturesNonMandatory * coverageRate); 
			//Base case, already covered
			if(actualNumberOfFeaturesCovered >= numFeaturesToCover)
				return testSeqList;
			
			//Here the  actualNumberOfFeaturesCovered and the mapFeatureToCoverIdTransition are already updated
			//TODO (27.05)
			// Run walk to continue the same test sequence?... Ele está criando novas test sequence mesmo podendo continuar uma já existente
			return generateTestSequence(dfts, coverageRate,testSeqList);		
		}
		
		//update the lit of feature not covered
		public void updateMapFeatureNotCovered(){
			mapFeatureNotCovered = new ArrayList<>();
			for (Entry entry : mapFeatureToCoverIdTransition.entrySet()) {
				if((int)entry.getValue() == 0 ){
					mapFeatureNotCovered.add((String) entry.getKey());				 
				}
			}
		}
		
		//return true if the list of features contains a given feature
		public boolean featureActiveListHasFeature(LinkedList<Feature> featuresList, String featureName){
			boolean result = false;
			for (Feature feature : featuresList) {
				if(feature.getName().equals(featureName))
					return true;
			}
			return result;
		}
		
		//return true if the list of String with features Names contains a given feature
		public boolean featureActiveStringListHasFeature(LinkedList<String> featuresStringList, String featureName){
			boolean result = false;
			for (String feature : featuresStringList) {
				if(feature.equals(featureName))
					return true;
			}
			return result;
		}
			
}
