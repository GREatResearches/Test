package cfmts.ts.generator;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import org.sat4j.minisat.SolverFactory;
import org.sat4j.reader.DimacsReader;
import org.sat4j.reader.ParseFormatException;
import org.sat4j.reader.Reader;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.IProblem;
import org.sat4j.specs.ISolver;
import org.sat4j.specs.TimeoutException;

import cfmts.graph.dfts.Graph_DFTS;
import cfmts.graph.dfts.Node_DFTS;
import cfmts.graph.dfts.Transition_DFTS;
import cfmts.ts.elements.TestCase;
import cfmts.ts.elements.TestSequence;
import cfmts.ts.elements.TestSequenceList;
import cfmts.ts.utilities.HashMapGenerator;
import fmp.contextEvolModel.ContextProposition;
import fmp.contextModel.AdaptationRuleWithCtxFeature;
import fmp.contextModel.CFM;
import fmp.contextModel.ContextFeature;
import fmp.contextModel.ContextGroup;
import fmp.contextModel.ContextGroupType;

//test each context state that activates more than one context = named IContextState
public class TSForInterleavingCorrectness {
	private HashMapGenerator mapGen = new HashMapGenerator();
	//Map the "ID-IContextState" and the "DFTS transition" that cover id
	//The ID-IContextState = position in the list IContextState
	//DFTS transition = 0 not covered yet
	private Map<Integer, Integer> mapIContextStateToCoverIdTransition = new HashMap<>();
	
	// list not covered
	private ArrayList<Integer> mapIContextStateNotCovered;
	
	
	private LinkedList<ArrayList<String>> IContextStateList = new LinkedList<>(); // list of IContext -> context that trigger more than one rule
	//String -> Context Name Integer-> ID/key of the Context 
	private Map<String, Integer> contextString = new HashMap<>();
	
	int actualNumberOfIContextStateCovered = 0;
	int numIContextStateToCover;
	//private DSPL dspl;
	private CFM cfm;
	//private CKS cks;
	
	int tcId = 0;
	int numClauses = 0;
	int numVariables = 0;
	
	public TSForInterleavingCorrectness(CFM cfm){
		this.cfm = cfm;
		
		//get the list of all context states that trigger more than one rule
		//creates mapIContextStateToCoverIdTransition
		identifyIContextState();
	}
	
	public TestSequenceList generateTestSequence(Graph_DFTS dfts, double coverageRate, TestSequenceList testSeqList){
		numIContextStateToCover = (int) (IContextStateList.size() * coverageRate); 
		// keeps the current number of States covered
		
		//Coverage == 0
		if(numIContextStateToCover == 0)
			return new TestSequenceList();
		
		Analysis:
			for (Entry entry : mapIContextStateToCoverIdTransition.entrySet()) {
			TestSequence testSeq = new TestSequence();
			
				if((int) entry.getValue() == 0){ // => has not covered yet
				 	// IContextState IC
					int  idIContextStateAnalyzed = (int)entry.getKey();
					ArrayList<String> iContextStateAnalyzed = IContextStateList.get(idIContextStateAnalyzed);
					
					// looks for the DFTS tansition that cover the IContextState
				    for (Node_DFTS nodeDFTS : dfts.getNodes()) {
						
				    	//Verifies the adaptation after the node
						for (Transition_DFTS transitionDFTS : nodeDFTS.getTransitions()) {
							Node_DFTS nextNode = transitionDFTS.getNewState();
							
							//verifies if this transition cover the Context IC
							if(verifyIfContextSatisfyIContextState(transitionDFTS.getCtxState().getAtiveContextPropositions(), 
									iContextStateAnalyzed)){
								
								// new TC
					    	    actualNumberOfIContextStateCovered++;
								tcId++;
	    			    	    TestCase tc = new TestCase(tcId, 
	    			    	    		mapGen.generateMapFromContextState(transitionDFTS.getCtxState()), 
	    			    	    		mapGen.generateMapFromFeaturesState(nextNode.getStringListOfAtiveFeature()));
	    			    	    tc.setIdTransitionDFTS(transitionDFTS.getId());
	    			    	    testSeq.addTestItem(tc);
	    			    	    
	    			    	    // update map
	    			    	    mapIContextStateToCoverIdTransition.put(idIContextStateAnalyzed,transitionDFTS.getId());
	    			    	    //  Verify if there are more adaptation rules covered
	    			    	    verifyOtherCoveredIContextState(transitionDFTS);
	    			    	    
	    			    	    if(actualNumberOfIContextStateCovered >= numIContextStateToCover){
	    			    	    	testSeqList.add(testSeq);
	    			    	    	break Analysis;
	    			    	    }
	    			    	    else{
	    			    	    	//try to keep the sequence looking for more adaptation rules to cover
	    			    	    	//it return testSequence updated
	    			    	    	updateMapIContextStateNotCovered();
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
	// Verify if other IContextState can be covered following the sequence If yes -> add to test sequence. 
	// Otherwise, go to first cycle again and create a new TS
	// update map
	//TODO
	public TestSequence walkDFTSForMore(Node_DFTS currentNodeAtTestSequence, TestSequence testSeq){
		TestCase tc1 = null;
		Node_DFTS nextNode = null;
		DeepAnalysis: for (Integer entryUpdatedList : mapIContextStateNotCovered) {
			//verify if one of them are in the currentStateAtTS
			int  idIContextStateAnalyzed = entryUpdatedList;
			ArrayList<String> iContextStateAnalyzed = IContextStateList.get(idIContextStateAnalyzed);
			
			for (Transition_DFTS transitionDFTS : currentNodeAtTestSequence.getTransitions()) {
				nextNode = transitionDFTS.getNewState();
				//Feature F is OFF in the Next State
				if(verifyIfContextSatisfyIContextState(transitionDFTS.getCtxState().getAtiveContextPropositions(), 
						iContextStateAnalyzed)){
					// new TC
					actualNumberOfIContextStateCovered++;
					tcId++;
		    	    tc1 = new TestCase(tcId, 
		    	    		mapGen.generateMapFromContextState(transitionDFTS.getCtxState()), 
		    	    		mapGen.generateMapFromFeaturesState(nextNode.getStringListOfAtiveFeature()));
		    	    tc1.setIdTransitionDFTS(transitionDFTS.getId());
		    	    testSeq.addTestItem(tc1);
		    	    
		    	    // update map
		    	    mapIContextStateToCoverIdTransition.put(idIContextStateAnalyzed,transitionDFTS.getId());
		    	    //  Verify if there are more IContextState covered
		    	    verifyOtherCoveredIContextState(transitionDFTS);
		    	    break DeepAnalysis;
				}
			}
		
		}
	    if(tc1 != null){
	    	updateMapIContextStateNotCovered();
	        return walkDFTSForMore(nextNode, testSeq); // TODO ver se precisa retornar isso, ou se já altera direto
	    }
	    else
	     return testSeq;		
	}
	
	//update the list of adaptation rules not covered
	public void updateMapIContextStateNotCovered(){
		mapIContextStateNotCovered = new ArrayList<>();
		for (Entry entry : mapIContextStateToCoverIdTransition.entrySet()) {
			if((int)entry.getValue() == 0 ){
				mapIContextStateNotCovered.add((int)entry.getKey());				 
			}
		}
	}	
		
	// Given the transition, it verifies if it cover others IContextState
	private void verifyOtherCoveredIContextState(Transition_DFTS transitionDFTS){
		updateMapIContextStateNotCovered();
		for (Integer entry : mapIContextStateNotCovered) {
			int  idIContextSatateAnalyzed = (int) entry;
			
			if(verifyIfContextSatisfyIContextState(transitionDFTS.getCtxState().getAtiveContextPropositions(), 
					IContextStateList.get(idIContextSatateAnalyzed))){
				//update Main Map Feature - Transition ID
				mapIContextStateToCoverIdTransition.put(idIContextSatateAnalyzed, transitionDFTS.getId());
				actualNumberOfIContextStateCovered++;
			}
								
		}
	}
	
	// Given the context String list, it verifies if it cover others IContextState
	private void verifyOtherCoveredIContextStateWithString(LinkedList<String> currentContext, int idTransition){
		updateMapIContextStateNotCovered();
		for (Integer entry : mapIContextStateNotCovered) {
			int  idIContextStateAnalyzed = entry;
			ArrayList<String> iContextStateAnalyzed = IContextStateList.get(idIContextStateAnalyzed);
			
			if(verifyIfContextSatisfyIContextStateWithString(currentContext,iContextStateAnalyzed)){
				//update Main Map Feature - Transition ID
				mapIContextStateToCoverIdTransition.put(idIContextStateAnalyzed, idTransition);
				actualNumberOfIContextStateCovered++;
			}
								
		}
	}
		
		
	//Given a test Sequence, it analysis the sequence to verify which cases were already covered
	//useful to generate one sequence and then analyze other criteria
	public TestSequenceList identifyMissingCases(Graph_DFTS dfts, TestSequenceList testSeqList, double coverageRate){
		//The list mapIcontextStateToCoverIdTransition is already filled by the class constructor
		for (Entry	entry : mapIContextStateToCoverIdTransition.entrySet()) {
			
			int  idIContextStateAnalyzed = (int) entry.getKey();
			ArrayList<String> iContextStateAnalyzed = IContextStateList.get(idIContextStateAnalyzed);
			
			if((int) entry.getValue() == 0){ // => has not covered yet
				
				//Verifying if this adaptation rule was already covered by the TestSequence given as input
				// Updating the mapIContextStateToCoverIdTransition
				for (TestSequence testSeq : testSeqList.getTestSequences()) {
					LinkedList<String> currentContextStringList = null;
					
					for (TestCase testCase : testSeq.getTestCases()) {
						currentContextStringList = testCase.getContextStringList();
						
						// verifies if it satisfies  the transition
						if(verifyIfContextSatisfyIContextStateWithString(currentContextStringList, 
								iContextStateAnalyzed)){
								//update Main Map Feature - Transition ID - Fix 13.07
								if(mapIContextStateToCoverIdTransition.get(idIContextStateAnalyzed) == 0)
									actualNumberOfIContextStateCovered++;
								mapIContextStateToCoverIdTransition.put(idIContextStateAnalyzed, testCase.getIdTransitionDFTS());
								
								
							    //Verify if there are more adaptation rules covered
								verifyOtherCoveredIContextStateWithString(currentContextStringList, testCase.getIdTransitionDFTS());
						}
					}		
				}
			}// End first IF (value ==0)
			
		}
		
		numIContextStateToCover = (int) (IContextStateList.size() * coverageRate); 
		
		//Base case, already covered
		if(actualNumberOfIContextStateCovered >= numIContextStateToCover)
			return testSeqList;
	
		//TODO (27.05)
		// Run walk to continue the same test sequence?... Ele está criando novas test sequence mesmo podendo continuar uma já existente
		return generateTestSequence(dfts, coverageRate,testSeqList);
	}
	
	//given the CFM, and adaptation rules, it identifies whether the rules can be trigguered togueter
	//Do not need to combine all, because two are enough
	public void identifyIContextState(){
		// Identify all context variables - creating the list of all context Variables
		int idContext = 1;
		for (ContextGroup ctxGroup : cfm.getContextGroups()) {
			for (ContextFeature ctxFeature : ctxGroup.getContextFeatureList()) {
				contextString.put(ctxFeature.getName(),idContext);
				numVariables++;
				idContext++;
			}
			if(ctxGroup.getType() == ContextGroupType.OR)
				numClauses++; // each ctxGroup should generate one clause
			else if (ctxGroup.getType() == ContextGroupType.XOR){
				int count = 0;
				for(int i =0; i < ctxGroup.getContextFeatureList().size();i++){
					for(int j=i+1; j < ctxGroup.getContextFeatureList().size();j++){
						count++;
					}
				}
				numClauses = numClauses + count;
			}
		} 
		System.out.println("clauses"+ numClauses);
		// Identify context trigger tuples that can happen at the same time
		// add clauses and apply the solver
		// If can be at the same time then add to the mapIContextStateNotCovered
		if (cfm.getAdaptationRules().size()>=2){
			for(int i=0; i<cfm.getAdaptationRules().size(); i++){
				for(int j=i+1; j<cfm.getAdaptationRules().size(); j++){
					ArrayList<String> IContextState = generateIContextState(cfm.getAdaptationRules().get(i), cfm.getAdaptationRules().get(j));
					
					//write CNF file
					writeCNFFile("testFile2", IContextState);
					//TODO
					boolean result  = verifySatisfatibility("testFile2");
					if(result){
						//If satisfactible ==> add to IContextState 
						printIContextState(IContextState);
						IContextStateList.add(IContextState);
						//the size-1 of the ICOntextStateList = ID of the last element added 
						mapIContextStateToCoverIdTransition.put(IContextStateList.size()-1, 0);
					}
				}
			}
		}// else -> If there is only 1 adaptation rule => there is not interleaving
		
	}
	
	// Given two CtxFeature, it generate a String with the context required to both
	public ArrayList<String> generateIContextState(AdaptationRuleWithCtxFeature rule1, AdaptationRuleWithCtxFeature rule2){
		ArrayList<String> requiredCtxToRules = new ArrayList<>();
		for (ContextFeature ctxProp1 : rule1.getContextRequired()) {
			requiredCtxToRules.add(ctxProp1.getName());
		}
		for (ContextFeature ctxProp2 : rule2.getContextRequired()) {
			requiredCtxToRules.add(ctxProp2.getName());
		}
		return requiredCtxToRules;
	}
	
	public void writeCNFFile(String fileName, ArrayList<String> IContextState){

		try {
	      /* Open the file */
	      FileOutputStream fos   = new FileOutputStream("D:/"+fileName+".cnf");
	      OutputStreamWriter osw = new OutputStreamWriter(fos);
	      BufferedWriter bw      = new BufferedWriter(osw);

	      //System.out.println("size"+IContextState.size());
	      
	      //Indicando a quantidade de variáveis e clásulas
	      bw.write("p cnf "+ numVariables +" "+(numClauses+IContextState.size()));
	      bw.newLine();
	      StringBuffer st;
	      
	      // Clauses related to the context group
	      for (ContextGroup ctxGroup : cfm.getContextGroups()) {
	    	  if(ctxGroup.getType() == ContextGroupType.OR){
	    		  st = new StringBuffer();
	    		  for (ContextFeature ctxFeature : ctxGroup.getContextFeatureList()) {
	    			  st.append(contextString.get(ctxFeature.getName())+" ");
	    		  }
	    		  st.append("0");
	    		  bw.write(st.toString());
	    		  bw.newLine();
	    	  } else if(ctxGroup.getType() == ContextGroupType.XOR){ // 3 vs 2 /
	    		  for(int i =0; i < ctxGroup.getContextFeatureList().size();i++){
						for(int j=i+1; j < ctxGroup.getContextFeatureList().size();j++){
							st = new StringBuffer();
							st.append("-"+contextString.get(ctxGroup.getContextFeatureList().get(i).getName()) +" "+"-"+contextString.get(ctxGroup.getContextFeatureList().get(j).getName())+" ");
							st.append("0");
				    		bw.write(st.toString());
				    		bw.newLine();
						}
					}
	    		  
	    	  }
	      }
	      	      
	      //Clauses related to the IContextState
	      for (String string : IContextState) {
	    	    st = new StringBuffer();
				st.append(contextString.get(string) +" ");
				st.append("0");
	    		bw.write(st.toString());
	    		bw.newLine();
	      }
	      
	      
	      /* Close the file */
	      bw.close();
	    }catch (IOException e) {
	      e.printStackTrace();
	    }
		
	}
	
	public boolean verifySatisfatibility(String fileName){
		//path "D:/Dropbox/Arquivos - GREat/Experimentos_qualificacao_SBSE/dados_experimento/LPSD_cnf.cnf"
		boolean status = false;
		//Aplicando o SAT solver
		ISolver solver = SolverFactory.newDefault();
        solver.setTimeout(3600); // 1 hour timeout
        Reader reader = new DimacsReader(solver);
        // CNF filename is given on the command line 
        //System.out.println("vai ler");
        try {
        	FileInputStream in = new FileInputStream("D:/"+fileName+".cnf");
        	
            IProblem problem = reader.parseInstance(in);
            //System.out.println("Ok, leu o arquivo");
            if (problem.isSatisfiable()) {
            	System.out.println("Satisfiable !");
            	System.out.println(reader.decode(problem.model()));		            	
            } else {
                System.out.println("Unsatisfiable !");
            }
            return problem.isSatisfiable();
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
        	System.out.println("File not found");
        } catch (ParseFormatException e) {
            // TODO Auto-generated catch block
        	System.out.println("Mal formatado");
        } catch (IOException e) {
            // TODO Auto-generated catch block
        	System.out.println("Io error");
        } catch (ContradictionException e) {
            System.out.println("Unsatisfiable (trivial)!");
        } catch (TimeoutException e) {
            System.out.println("Timeout, sorry!");      
        }
        return false;
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
	
	
	// It could use also Context Features from  CFM, but the Graph is using Context Proposition, then I used them
	public boolean verifyIfContextSatisfyIContextStateWithString(LinkedList<String> currentContext, ArrayList<String> requiredIContextState){
		if((currentContext == null) | (currentContext.size() == 0) | (requiredIContextState == null) || (requiredIContextState.size() == 0))
			return false; //
		
		boolean match = true;
	
		for (String contextRequired : requiredIContextState) { // first FOR
			if(!match)
				break;
			else
				match = false; 			
			for (String actualContext : currentContext) {
				if(contextRequired.equals(actualContext)){
					match = true;
					continue; // go to first FOR
				}
			}
		}
		return match;		
	}
		
	// It could use also Context Features from  CFM, but the Graph is using Context Proposition, then I used them
	public boolean verifyIfContextSatisfyIContextState(LinkedList<ContextProposition> currentContext, ArrayList<String> requiredIContextState){
		if((currentContext == null) | (currentContext.size() == 0) | (requiredIContextState == null) || (requiredIContextState.size() == 0))
			return false; //
		
		boolean match = true;
	
		for (String contextRequired : requiredIContextState) { // first FOR
			if(!match)
				break;
			else
				match = false; 			
			for (ContextProposition actualContext : currentContext) {
				if(contextRequired.equals(actualContext.getName())){
					match = true;
					continue; // go to first FOR
				}
			}
		}
		return match;		
	}
		
	public void printIContextState(ArrayList<String> IContextState){
		System.out.println("Context State");
		for (String string : IContextState) {
			System.out.print(string+" ");
		}
		System.out.println();
	}
	
}
