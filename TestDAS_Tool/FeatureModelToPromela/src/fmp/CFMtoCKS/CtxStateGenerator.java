package fmp.CFMtoCKS;

import java.util.LinkedList;

import fmp.contextEvolModel.ContextProposition;
import fmp.contextEvolModel.ContextState;
import fmp.contextModel.ContextFeature;
import fmp.contextModel.ContextGroup;
import fmp.contextModel.ContextGroupType;

public class CtxStateGenerator {

	//TODO BUG 13.07.... it is not generating the xls correctly (test SmartHome)
	public void generateContexStateFromContextGroup(ContextGroup ctxGroup, LinkedList<ContextState> ctxStateList){
		LinkedList<ContextState> auxList;
		if(ctxGroup.getType().equals(ContextGroupType.NONE)){
			//sub-features are optional one
			if(ctxStateList.size() == 0){
			//there is no previous context states				
				for (ContextFeature ctxFeature : ctxGroup.getContextFeatureList()) {
					//CtxState with Optional CtxFeature
					ContextProposition ctxProposition = new ContextProposition(ctxFeature.getName());
					ContextState newContextStateWithCtxFeature = new ContextState();
					newContextStateWithCtxFeature.getAtiveContextPropositions().add(ctxProposition);
					newContextStateWithCtxFeature.setId(ctxStateList.size());
					ctxStateList.add(newContextStateWithCtxFeature);
					
					//CtxState without Optional CtxFeature 
					ContextState newContextStateWithoutCtxFeature = new ContextState();					
					newContextStateWithoutCtxFeature.setId(ctxStateList.size());
					ctxStateList.add(newContextStateWithoutCtxFeature);
				}
			}else{
				// there are previous context states
				// Exemple: Group with CtxFeatureA => Context States with and without CtxFeatureA
				// It keeps the existing ctxStates without CtxFeatureA and creates a new one with CtxFeatureA
				auxList = new LinkedList<ContextState>();
				int lastFreeId = ctxStateList.size();
				for (ContextState contextState : ctxStateList) {
					for (ContextFeature ctxFeature : ctxGroup.getContextFeatureList()) {
						//it creates a new context state to have the context feature optional true
						ContextState newContextState = new ContextState();
						newContextState.copyAtiveContextPropositions(contextState.getAtiveContextPropositions());
						newContextState.setId(lastFreeId);
						lastFreeId++;
						//it add the context proposition as active	
						ContextProposition ctxProposition = new ContextProposition(ctxFeature.getName());
						newContextState.getAtiveContextPropositions().add(ctxProposition);
						auxList.add(newContextState);
					}
				}
				for (ContextState newContextState : auxList) {
					ctxStateList.add(newContextState);
				}
			}
			
		}else if(ctxGroup.getType().equals(ContextGroupType.OR)){
			if(ctxStateList.size() == 0){
				//there is no previous context states
				if(ctxGroup.getContextFeatureList().size() > 0 )
					recursiveXORNAnalysis(ctxGroup, ctxStateList, null, 0);
			}else{
				//there are previous context states
				auxList = new LinkedList<ContextState>();
				for (ContextState contextState : ctxStateList) {
					auxList.add(contextState);
				}
				//here it is used the auxList because the recursive function edits the ctxStateList
				for (ContextState contextState : auxList) {
					if(ctxGroup.getContextFeatureList().size() > 0 )
						recursiveXORNAnalysis(ctxGroup, ctxStateList, contextState, 0);
				}
			}
			
		}else if(ctxGroup.getType().equals(ContextGroupType.XOR)){
			if(ctxStateList.size() == 0){
				//there is no previous context states
				for (ContextFeature ctxFeature : ctxGroup.getContextFeatureList()) {
					//CtxState with Optional CtxFeature
					ContextProposition ctxProposition = new ContextProposition(ctxFeature.getName());
					ContextState newContextStateWithCtxFeature = new ContextState();
					newContextStateWithCtxFeature.getAtiveContextPropositions().add(ctxProposition);
					newContextStateWithCtxFeature.setId(ctxStateList.size());
					ctxStateList.add(newContextStateWithCtxFeature);
				}
			}else{
				//there are previous context states
				// Exemple: Group with CtxFeatureA => Context States with and without CtxFeatureA
				// It keeps the existing ctxStates without CtxFeatureA and creates a new one with CtxFeatureA
				auxList = new LinkedList<ContextState>();
				int lastFreeId = ctxStateList.size();
				for (ContextState contextState : ctxStateList) {
					// set in the current states context to active the first feature of the XOR group
					ContextProposition firstCtxProposition = new ContextProposition(ctxGroup.getContextFeatureList().get(0).getName());
					contextState.getAtiveContextPropositions().add(firstCtxProposition);
					
					//it creates new context states for each of the ctxFeatures from the context group
					
					for(int i = 1; i < ctxGroup.getContextFeatureList().size(); i++){
						ContextState newContextState = new ContextState();
						newContextState.copyAtiveContextPropositions(contextState.getAtiveContextPropositions());
						newContextState.setId(lastFreeId);
						lastFreeId++;
						
						//Remove the last ctxFeature added -> firstCtxProposition 
						//This is necessary because is a XOR, only one of the ctxFEatures can be active
						newContextState.getAtiveContextPropositions().removeLast();
						
						// Add the new proposition to be active
						ContextProposition ctxProposition = new ContextProposition(ctxGroup.getContextFeatureList().get(i).getName());
						contextState.getAtiveContextPropositions().add(ctxProposition);	
						auxList.add(newContextState);
					}					
				}
				for (ContextState newContextState : auxList) {
					ctxStateList.add(newContextState);
				}
			}
			
		}
		
	}
	

	//It generates context states considering the XOR group
	public void recursiveXORNAnalysis(ContextGroup ctxGroup, LinkedList<ContextState> ctxStateList, ContextState referenceState, int currentPosition){
		
		//Base Case
		//End of the analysis
		if(currentPosition >= ctxGroup.getContextFeatureList().size())
			return;
		
		//Recursive Case
		for (int i = currentPosition; i < ctxGroup.getContextFeatureList().size(); i++) {
				ContextProposition ctxProposition = new ContextProposition(ctxGroup.getContextFeatureList().get(i).getName());
				ContextState newContextStateWithCtxFeature = new ContextState();
				if(referenceState != null)
					newContextStateWithCtxFeature.copyAtiveContextPropositions(referenceState.getAtiveContextPropositions());
				newContextStateWithCtxFeature.getAtiveContextPropositions().add(ctxProposition);
				newContextStateWithCtxFeature.setId(ctxStateList.size());
				ctxStateList.add(newContextStateWithCtxFeature);
				
				//recursiveCall
				recursiveXORNAnalysis(ctxGroup, ctxStateList, newContextStateWithCtxFeature, i+1);
		}
	}
}
