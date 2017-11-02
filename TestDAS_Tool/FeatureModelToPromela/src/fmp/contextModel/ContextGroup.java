package fmp.contextModel;

import java.util.LinkedList;

// To indicate context propositions that can not assume true value at same time
public class ContextGroup {
	
	private LinkedList<ContextFeature> contextFeatureList = new LinkedList<ContextFeature>();
	
	private String name;
	
	public ContextGroup(String name){
		this.name = name;
	}
	
	// it indicates whether the constraint is a XOR, OR or None (for optional context feature subfeatures)
	private ContextGroupType type;

	public ContextGroupType getType() {
		return type;
	}

	public void setType(ContextGroupType type) {
		this.type = type;
	}

	public LinkedList<ContextFeature> getContextFeatureList() {
		return contextFeatureList;
	}

	public void setContextFeatureList(LinkedList<ContextFeature> contextFeatureList) {
		this.contextFeatureList = contextFeatureList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public void append(ContextFeature ctxFeature){
		this.contextFeatureList.add(ctxFeature);
		ctxFeature.setGroup(this);		
	}

	public void print(){
		System.out.println("NAME: "+name);
		System.out.println("Features:");
		for (ContextFeature contextFeature : contextFeatureList) {
			System.out.println(contextFeature.getName());
		}
	}
}
