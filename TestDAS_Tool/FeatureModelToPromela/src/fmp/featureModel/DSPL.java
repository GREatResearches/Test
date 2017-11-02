package fmp.featureModel;

import java.util.LinkedList;

public class DSPL {

	private LinkedList<Feature> features = new LinkedList<Feature>();
	
	private LinkedList<RequireRule> requireRules = new LinkedList<RequireRule>();
	
	private LinkedList<ExcludeRule> excludeRules = new LinkedList<ExcludeRule>();
	
	private LinkedList<FeatureGroup> featureGroups = new LinkedList<FeatureGroup>();
	
	//The initial configuration of the DPSL (set of faetures true at launch time)
	private LinkedList<Feature> featuresInitialConfiguration = new LinkedList<Feature>();
	
	public LinkedList<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(LinkedList<Feature> features) {
		this.features = features;
	}

	public LinkedList<RequireRule> getRequireRules() {
		return requireRules;
	}

	public void setRequireRules(LinkedList<RequireRule> requireRules) {
		this.requireRules = requireRules;
	}

	public LinkedList<ExcludeRule> getExcludeRules() {
		return excludeRules;
	}

	public void setExcludeRules(LinkedList<ExcludeRule> excludeRules) {
		this.excludeRules = excludeRules;
	}

	
	
	public LinkedList<FeatureGroup> getFeatureGroups() {
		return featureGroups;
	}

	public void setFeatureGroups(LinkedList<FeatureGroup> featureGroups) {
		this.featureGroups = featureGroups;
	}
	
	public LinkedList<Feature> getInitialConfiguration() {
		return featuresInitialConfiguration;
	}

	public void setIntialConfiguration(LinkedList<Feature> featuresInitialConfiguration) {
		this.featuresInitialConfiguration = featuresInitialConfiguration;
	}
	
	public void print(){
		System.out.println("**********");
		System.out.println("FEATURES:");
		for (Feature feature : features) {
			feature.print();
		}
		
		System.out.println("**********");
		System.out.println("REQUIRE RULES:");
		for (RequireRule requireRule : requireRules) {
			//requireRule.print();
		}
		
		System.out.println("**********");
		System.out.println("EXCLUDE RULES:");
		for (ExcludeRule excludeRule : excludeRules) {
			//excludeRule.print();
		}
	}
}
