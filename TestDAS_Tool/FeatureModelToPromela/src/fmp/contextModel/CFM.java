package fmp.contextModel;

import java.util.LinkedList;

// it represents the Context Model
// root -> Context Group -> Context features
public class CFM {

	LinkedList<ContextGroup> contextGroups = new LinkedList<ContextGroup>();
	
	private LinkedList<AdaptationRuleWithCtxFeature> adaptationRules = new LinkedList<AdaptationRuleWithCtxFeature>();
	
	ContextFeature contextRoot;

	public LinkedList<ContextGroup> getContextGroups() {
		return contextGroups;
	}

	public void setContextGroups(LinkedList<ContextGroup> contextGroups) {
		this.contextGroups = contextGroups;
	}

	public ContextFeature getContextRoot() {
		return contextRoot;
	}

	public void setContextRoot(ContextFeature contextRoot) {
		this.contextRoot = contextRoot;
	}
	
	public LinkedList<AdaptationRuleWithCtxFeature> getAdaptationRules() {
		return adaptationRules;
	}

	public void setAdaptationRules(LinkedList<AdaptationRuleWithCtxFeature> adaptationRules) {
		this.adaptationRules = adaptationRules;
	}
	
	public void print(){
		System.out.println("**** CONTEXT MODEL *****");
		System.out.println("ROOT: "+contextRoot.getName());
		
		System.out.println("**********");
		System.out.println("CONTEXT GROUPS:");
		for (ContextGroup ctxGroup : contextGroups) {
			ctxGroup.print();
			System.out.println();
		}
		
		System.out.println("**********");
		System.out.println("ADAPTATION RULES:");
		for (AdaptationRuleWithCtxFeature adaptationRule : adaptationRules) {
			adaptationRule.print();
			System.out.println();
		}
	}
}
