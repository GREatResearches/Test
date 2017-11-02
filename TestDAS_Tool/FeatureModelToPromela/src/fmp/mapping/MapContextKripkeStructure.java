package fmp.mapping;

import java.util.LinkedList;

import fmp.contextEvolModel.CKS;
import fmp.contextEvolModel.ContextProposition;
import fmp.contextEvolModel.ContextState;

public class MapContextKripkeStructure {
	
	private String newline = System.getProperty("line.separator");
	
	private CKS cks;

	public MapContextKripkeStructure(CKS cks) {
		// TODO Auto-generated constructor stub		
		this.cks = cks;
	}
	
	// Append the variables to the Promela Code
	public String getCKSPromelaCode(){
		StringBuffer cksToPromelaCode = new StringBuffer();
		cksToPromelaCode.append("active proctype ContextManager() {" + newline);
		cksToPromelaCode.append(appendInitialContext());
		cksToPromelaCode.append(appendContextEvolution());
		cksToPromelaCode.append("}"+newline);
		return cksToPromelaCode.toString();
	}

	// Example: battery = 3; hasPwSrc = false;
	public String appendInitialContext(){
		StringBuffer initialContextPromelaCode = new StringBuffer();
		for (ContextProposition contextPropostion : cks.getInitialState().getAtiveContextPropositions()) {
			initialContextPromelaCode.append(contextPropostion.getName() +"= true;");
		}
		initialContextPromelaCode.append("buss!contextChanged;buss?adapted" + newline);
		return initialContextPromelaCode.toString();
	}
	
	// Example: :: (battery == 2 && hasPwSrc == false) -> battery = battery - 1; buss!contextChanged;buss?adapted
	// PS: Avoid CS1 -> CS1 (same state), to avoid "(isBtFull=true && hasPwSource=true) -> buss!contextChanged;buss?adapted"
	public String appendContextEvolution(){
		StringBuffer contextEvolutionPromelaCode = new StringBuffer();
		contextEvolutionPromelaCode.append("do" + newline);
		for (ContextState contextState : cks.getContextStates()) {
			for(ContextState nextContextState : contextState.getNextStates()){
				
				// Current Context State
				contextEvolutionPromelaCode.append(":: (");
				for (int i = 0; i < contextState.getAtiveContextPropositions().size(); i++){
					contextEvolutionPromelaCode.append(contextState.getAtiveContextPropositions().get(i).getName() +" == true");
					if(!(i == contextState.getAtiveContextPropositions().size() - 1))
						contextEvolutionPromelaCode.append(" && ");
				}
				for (int i = 0; i < cks.getContextPropositions().size(); i++){
					if(!contextState.containCtxProposition(cks.getContextPropositions().get(i)))
						contextEvolutionPromelaCode.append(" && " + cks.getContextPropositions().get(i).getName() +" == false");
				}
				contextEvolutionPromelaCode.append(") -> ");
				
				// Next Context State
				// TODO spite of puting all, we can put just that set that changes
				LinkedList<ContextProposition> newActiveCtxPropositions = contextState.getNewActiveContextPropositions(nextContextState);
				for (int i = 0; i < newActiveCtxPropositions.size(); i++){
					contextEvolutionPromelaCode.append(newActiveCtxPropositions.get(i).getName() +" = true;");
				}
				LinkedList<ContextProposition> newInactiveCtxPropositions = contextState.getNewInactiveContextPropositions(nextContextState);
				for (int i = 0; i < newInactiveCtxPropositions.size(); i++){
					contextEvolutionPromelaCode.append(newInactiveCtxPropositions.get(i).getName() +" = false;");
				}
				contextEvolutionPromelaCode.append("buss!contextChanged;buss?adapted"+newline);
			}
		}
		
		contextEvolutionPromelaCode.append("od" + newline);
		return contextEvolutionPromelaCode.toString();
	}
	
}
