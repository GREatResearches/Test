package fmp.featureModel;

import java.util.LinkedList;

public class FeatureGroup {

	private LinkedList<Feature> features = new LinkedList<Feature>();
	
	private FeatureGroupType groupType;

	public LinkedList<Feature> getFeatures() {
		return features;
	}

	public void setFeatures(LinkedList<Feature> features) {
		this.features = features;
	}

	public FeatureGroupType getGroupType() {
		return groupType;
	}

	public void setGroupType(FeatureGroupType groupType) {
		this.groupType = groupType;
	}
	
	public void append(Feature feature){
		features.add(feature);
		feature.setGroup(this);
		
	}
}
