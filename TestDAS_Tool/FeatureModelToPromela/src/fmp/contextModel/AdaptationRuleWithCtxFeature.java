package fmp.contextModel;

import java.util.LinkedList;

import fmp.featureModel.Feature;

public class AdaptationRuleWithCtxFeature {

	//Context that need to be true to trigger the rule
	private LinkedList<ContextFeature> contextRequired;
	
	private LinkedList<Feature> toActiveFeatureList = new LinkedList<Feature>();
	
	private LinkedList<Feature> toDeactiveFeatureList = new LinkedList<Feature>();

	public LinkedList<ContextFeature> getContextRequired() {
		return contextRequired;
	}

	public void setContextRequired(LinkedList<ContextFeature> contextRequired) {
		this.contextRequired = contextRequired;
	}

	public LinkedList<Feature> getToActiveFeatureList() {
		return toActiveFeatureList;
	}

	public void setToActiveFeatureList(LinkedList<Feature> toActiveFeatureList) {
		this.toActiveFeatureList = toActiveFeatureList;
	}

	public LinkedList<Feature> getToDeactiveFeatureList() {
		return toDeactiveFeatureList;
	}

	public void setToDeactiveFeatureList(LinkedList<Feature> toDeactiveFeatureList) {
		this.toDeactiveFeatureList = toDeactiveFeatureList;
	}
	
	public void print(){
		System.out.println("Context Trigguer:");
		for (ContextFeature contextFeature : contextRequired) {
			System.out.println(contextFeature.getName() + " ");			
		}
		if(toActiveFeatureList.size() > 0){
			System.out.println("Features to active:");
			for (Feature feature : toActiveFeatureList) {
				System.out.println(feature.getName() + " ");				
			}
		}
		if(toDeactiveFeatureList.size() > 0){
			System.out.println("Features to deactive:");
			for (Feature feature : toDeactiveFeatureList) {
				System.out.println(feature.getName() + " ");			
			}
		}
		
	}
}
