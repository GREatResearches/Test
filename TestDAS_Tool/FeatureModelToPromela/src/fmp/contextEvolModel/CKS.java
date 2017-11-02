package fmp.contextEvolModel;

import java.util.LinkedList;

import fmp.contextModel.AdaptationRuleWithCtxFeature;
import fmp.contextModel.ContextConstraintCtxModel;

public class CKS {

	//CKS[S, I, C, L, ->]
	
	// List of the context states of the C-KS :: S
	private LinkedList<ContextState> contextStates = new LinkedList<ContextState>();
	
	// Initial state of the C-KS :: I
	// 24.01: it assumes only one initial state
	private ContextState initialState;
	
	// Set of context propositions of the C-KS
	private LinkedList<ContextProposition> contextPropositions = new LinkedList<ContextProposition>();
	
	// List of constraints that affect the context propositions of the C-KS :: (kind of) L 
	private LinkedList<ContextConstraintCtxModel> constraints = new LinkedList<ContextConstraintCtxModel>();
	
	private LinkedList<AdaptationRuleWithCtxProposition> adaptationRules = new LinkedList<AdaptationRuleWithCtxProposition>();

	// The relation -> is implemented within the contextSate object 
	
	public ContextState getInitialState() {
		return initialState;
	}

	public void setInitialState(ContextState initialState) {
		this.initialState = initialState;
	}

	public LinkedList<ContextConstraintCtxModel> getConstraints() {
		return constraints;
	}

	public void setConstraints(LinkedList<ContextConstraintCtxModel> constraints) {
		this.constraints = constraints;
	}

	public LinkedList<ContextState> getContextStates() {
		return contextStates;
	}

	public void setContextStates(LinkedList<ContextState> contextStates) {
		this.contextStates = contextStates;
	}

	public LinkedList<ContextProposition> getContextPropositions() {
		return contextPropositions;
	}

	public void setContextPropositions(LinkedList<ContextProposition> contextPropositions) {
		this.contextPropositions = contextPropositions;
	}
	 
	public LinkedList<AdaptationRuleWithCtxProposition> getAdaptationRules() {
		return adaptationRules;
	}

	public void setAdaptationRules(LinkedList<AdaptationRuleWithCtxProposition> adaptationRules) {
		this.adaptationRules = adaptationRules;
	}
	
	public int getIndexCtxProp(String ctxFeatureName){
		for (int i = 0; i < contextPropositions.size(); i++){
			if(contextPropositions.get(i).getName().equals(ctxFeatureName)){
				return i;
			}
		}
		return -1;
	}

	public void print(){
		System.out.println("Initial State:");
		initialState.print();
		System.out.println();
		System.out.println("Full List of States:");
		System.out.println("*******************");
		for (ContextState contextState : contextStates) {
			contextState.print();
			System.out.println("*******************");
		}
		System.out.println("Full List of Adaptation Rules:");
		for (AdaptationRuleWithCtxProposition rule : adaptationRules) {
			rule.print();
			System.out.println();
		}
	}
}
