package fmp.featureModel;

public class Feature {
	
	private Feature fatherFeature;
	
	private String name;
	
	// opcional, mandatory
	private FeatureType featureType;
	
	private FeatureGroup group;
	
	public Feature getFatherFeature() {
		return fatherFeature;
	}

	public void setFatherFeature(Feature fatherFeature) {
		this.fatherFeature = fatherFeature;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public FeatureType getFeatureType() {
		return featureType;
	}

	public void setFeatureType(FeatureType featureType) {
		this.featureType = featureType;
	}

	public FeatureGroup getGroup() {
		return group;
	}

	public void setGroup(FeatureGroup group) {
		this.group = group;
	}

	public void print(){
		System.out.println("Name: "+name);
		System.out.println("Type: " +featureType);
	}

}
