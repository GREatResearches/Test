package fmp.featureModel;

import java.util.LinkedList;

public class RequireRule {

	private Feature featureClaimant;
	
	private LinkedList<Feature> requiredFeatureList;
	
	public Feature getFeatureClaimant() {
		return featureClaimant;
	}

	public void setFeatureClaimant(Feature featureClaimant) {
		this.featureClaimant = featureClaimant;
	}

	public LinkedList<Feature> getRequiredFeatureList() {
		return requiredFeatureList;
	}

	public void setRequiredFeatureList(LinkedList<Feature> requiredFeatureList) {
		this.requiredFeatureList = requiredFeatureList;
	}
	
	public void print(){
		System.out.println("Feature Trigguer: " + featureClaimant);
		
		System.out.println("Required Features:");
		for (Feature feature : requiredFeatureList) {
				System.out.println(feature.getName() + " ");			
		}
		
	}
	
}
