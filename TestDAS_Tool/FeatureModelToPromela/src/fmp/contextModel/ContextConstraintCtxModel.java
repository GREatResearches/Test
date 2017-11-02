package fmp.contextModel;

import java.util.LinkedList;

import fmp.contextEvolModel.ContextProposition;
import fmp.featureModel.Feature;

// To indicate context propositions that can not assume true value at same time
// It is only for XOR relationships!! because for OR, does not affect the context evol model

//TODO - Usar para restrição entre contextFeatures
public class ContextConstraintCtxModel {
	
	private LinkedList<ContextProposition> contextPropositionsList = new LinkedList<ContextProposition>();
	
	private String type = "NONE";
	
	public void setToRequire() {
		  type="REQUIRE";
	}
	
	public void setToExclude() {
		  type="EXCLUDE";
	}

	public LinkedList<ContextProposition> getContextPropositionsList() {
		return contextPropositionsList;
	}

	public void setContextPropositionsList(LinkedList<ContextProposition> contextPropositionsList) {
		this.contextPropositionsList = contextPropositionsList;
	}

	
}
