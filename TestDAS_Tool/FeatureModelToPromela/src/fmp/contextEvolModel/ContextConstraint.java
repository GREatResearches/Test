package fmp.contextEvolModel;

import java.util.LinkedList;

import fmp.contextEvolModel.ContextProposition;
import fmp.featureModel.Feature;

// To indicate context propositions that can not assume true value at same time
// It is only for XOR relationships!! because for OR, does not affect the context evol model

//TODO - Usar para restrição entre contextFeatures
public class ContextConstraint {
	
	public ContextConstraint() {
		// TODO Auto-generated constructor stub
	}
	
	private LinkedList<ContextProposition> contextPropositionsList = new LinkedList<ContextProposition>();
	
	public LinkedList<ContextProposition> getContextPropositionsList() {
		return contextPropositionsList;
	}

	public void setContextPropositionsList(LinkedList<ContextProposition> contextPropositionsList) {
		this.contextPropositionsList = contextPropositionsList;
	}

	
}
