package fmp.mapping;

import java.util.LinkedList;

import fmp.featureModel.Feature;

public class MapActuators {
	
	private String newline = System.getProperty("line.separator");
	
	private LinkedList<Feature> contextAwareFeatures;
	
	public MapActuators(LinkedList<Feature> contextAwareFeatures){
		this.contextAwareFeatures = contextAwareFeatures;
	}
	
	// Creating the actuators to active/deactive for the context aware features
	public String getActuatorstoPromelaCode(){
		StringBuffer actuatorsPromelaCode = new StringBuffer();
		for (Feature feature : contextAwareFeatures) {
			// Identifying the optional Features
		  actuatorsPromelaCode.append(
				newline+
				"active proctype "+feature.getName()+"Actuator() {" + newline +
				"do"  + newline +
					":: atomic{ buss?"+feature.getName()+"On -> "+ feature.getName() + " = true }"  + newline +
					":: atomic{ buss?"+feature.getName()+"Off -> " + feature.getName() + " = false }"  + newline +
				"od"  + newline +	
				"}"+
				newline);		  
		}
		return actuatorsPromelaCode.toString();
	}
		
}
