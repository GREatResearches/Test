package fmp.contextEvolModel;

import java.util.ArrayList;
import java.util.LinkedList;

public class ContextState {
	
	//Context Propositions that are true at the state
	private int id;
	
	private LinkedList<ContextProposition> activeContextPropositions = new LinkedList<ContextProposition>();
	
	private LinkedList<ContextState> nextStates = new LinkedList<ContextState>();

	public LinkedList<ContextProposition> getAtiveContextPropositions() {
		return activeContextPropositions;
	}
	
	public String getAtiveContextPropositionsIntoAString() {
		StringBuffer activeContexts = new StringBuffer();
		for(int i = 0; i < activeContextPropositions.size(); i++){
			activeContexts.append(activeContextPropositions.get(i).getName());
			if(i+1 < activeContextPropositions.size())
				activeContexts.append(",\n");
		}
		return activeContexts.toString();
	}
	
	//it returns a String list of the active Contexts
	public ArrayList<String> getStringListOfAtiveContext() {
		ArrayList<String> activeContextsList = new ArrayList<String>();
		for (ContextProposition ctxProp : activeContextPropositions) {
			activeContextsList.add(ctxProp.getName());
		}
		return activeContextsList;
	}
	
	public boolean containCtxProposition(ContextProposition ctxProposition){
		boolean result = false;
		for (ContextProposition contextProposition : activeContextPropositions) {
			if(contextProposition.getName().equals(ctxProposition.getName())){
				result = true;
				break;
			}
		}		
		return result;
	}
			
	public void copyAtiveContextPropositions(LinkedList<ContextProposition> contextPropositions) {
		this.activeContextPropositions = new LinkedList<ContextProposition>();
		for (ContextProposition contextProposition : contextPropositions) {
			activeContextPropositions.add(contextProposition);
		}
	}

	public void setAtiveContextPropositions(LinkedList<ContextProposition> contextPropositions) {
		this.activeContextPropositions = contextPropositions;
	}

	public LinkedList<ContextState> getNextStates() {
		return nextStates;
	}

	public void setNextStates(LinkedList<ContextState> nextStates) {
		this.nextStates = nextStates;
	}
	
	public void addNextState(ContextState nextState) {
		this.nextStates.add(nextState);
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	// it return the context propositions that were false in the current state, but are true in the next context state  
	public LinkedList<ContextProposition> getNewActiveContextPropositions(ContextState nextState){
		LinkedList<ContextProposition> newActiveCtxPropositions = new LinkedList<>();
		for (ContextProposition newContextProposition : nextState.getAtiveContextPropositions()) {
			boolean isContextActivePreviously = false;
			for(ContextProposition currentContextProposition : this.getAtiveContextPropositions()){
				if(currentContextProposition.getName().equals(newContextProposition.getName()))
					isContextActivePreviously = true; // it indicates that it was active before					
			}
			if(!isContextActivePreviously)
				newActiveCtxPropositions.add(newContextProposition);
		}
		return newActiveCtxPropositions;
	}
	
	// it return the context propositions that were true in the current state, but are false in the next context state
	public LinkedList<ContextProposition> getNewInactiveContextPropositions(ContextState nextState){
		LinkedList<ContextProposition> newInactiveCtxPropositions = new LinkedList<>();
		for(ContextProposition currentContextProposition : this.getAtiveContextPropositions()){
			boolean isContextInactiveNext = false;
			for (ContextProposition newContextProposition : nextState.getAtiveContextPropositions()) {
				if(currentContextProposition.getName().equals(newContextProposition.getName()))
					isContextInactiveNext = true;					
			}
			if(!isContextInactiveNext)
			newInactiveCtxPropositions.add(currentContextProposition);
		}
		return newInactiveCtxPropositions;
	}
	
	public void print(){
		
		System.out.println("Propositions of the State S" + id);
		for (ContextProposition contextProposition : activeContextPropositions) {
			System.out.println(contextProposition.getName());
		}		
		System.out.println("Number of next state: "+ nextStates.size());
		System.out.print("Next States:");
		for (ContextState contextState : nextStates) {
			System.out.print(" S"+contextState.getId());
		}
		System.out.println();	
	}
}
