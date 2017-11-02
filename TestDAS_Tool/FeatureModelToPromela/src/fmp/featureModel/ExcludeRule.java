package fmp.featureModel;

import java.util.LinkedList;

import fmp.contextEvolModel.ContextProposition;

public class ExcludeRule {
	private Feature featureClaimant;
	
	private LinkedList<Feature> excludedFeatureList;
	
	public Feature getFeatureClaimant() {
		return featureClaimant;
	}

	public void setFeatureClaimant(Feature featureClaimant) {
		this.featureClaimant = featureClaimant;
	}

	public LinkedList<Feature> getRequiredFeatureList() {
		return excludedFeatureList;
	}

	public void setRequiredFeatureList(LinkedList<Feature> requiredFeatureList) {
		this.excludedFeatureList = requiredFeatureList;
	}
	
	public void print(){
		System.out.println("Feature Trigguer: " + featureClaimant);
		
		System.out.println("Excluded Features:");
		for (Feature feature : excludedFeatureList) {
				System.out.println(feature.getName() + " ");			
		}
		
	}
}
