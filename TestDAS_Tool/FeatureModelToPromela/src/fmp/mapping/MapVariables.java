package fmp.mapping;

import java.util.LinkedList;

import fmp.contextEvolModel.CKS;
import fmp.contextEvolModel.ContextProposition;
import fmp.contextModel.AdaptationRuleWithCtxFeature;
import fmp.featureModel.DSPL;
import fmp.featureModel.Feature;

public class MapVariables {

	private String newline = System.getProperty("line.separator");
	
	private LinkedList<Feature> contextAwareFeatures;
	
	private DSPL dspl;
	
	private CKS cks;
	
	public MapVariables(LinkedList<Feature> contextAwareFeatures, CKS cks, DSPL dspl) {
		this.contextAwareFeatures = contextAwareFeatures;
		this.cks = cks;
		this.dspl = dspl;		
	}
	
	// Append the variables to the Promela Code
	public String getVariablestoPromelaCode(){
		StringBuffer variablesPromelaCode = new StringBuffer();
		variablesPromelaCode.append(appendContextVariables());
		variablesPromelaCode.append(newline);
		variablesPromelaCode.append(appendFeatureVariables());
		variablesPromelaCode.append(newline);
		variablesPromelaCode.append(appendMessageChannel());
		variablesPromelaCode.append(newline);
		variablesPromelaCode.append("int numAnswers = 0;" + newline);
		return variablesPromelaCode.toString();
	}
		
	// Append the context variables to the Promela Code
	// Example: bool isBtFull = false; -> Depende do CkS
	public String appendContextVariables(){
		StringBuffer contextVariablesPromelaCode = new StringBuffer();
		contextVariablesPromelaCode.append("bool ");
		for (ContextProposition contextProposition: cks.getContextPropositions()) {
			contextVariablesPromelaCode.append(contextProposition.getName() + " = false, ");	
		} 
		// changing the last char ',' to ';' 
		if(contextVariablesPromelaCode.length() > 0)
			contextVariablesPromelaCode.replace(contextVariablesPromelaCode.length()-2, contextVariablesPromelaCode.length(), ";");
		return contextVariablesPromelaCode.toString();
	}
	
	// Creating the variables of the model in the Model Checker
	// Example: bool hasPwSrc = false, image = false, video = false, mobileGuide=true, showDocs=true, text=true;
	public String appendFeatureVariables(){
		StringBuffer featureVariablesPromelaCode = new StringBuffer();
		featureVariablesPromelaCode.append("bool ");
		for (Feature feature : dspl.getFeatures()) {
			// it checks whether the feature is from the initial configuration or not
			if(dspl.getInitialConfiguration().contains(feature))
				featureVariablesPromelaCode.append(feature.getName() + " = true, ");
			else
				featureVariablesPromelaCode.append(feature.getName() + " = false, ");
		}
		// changing the last char ',' to ';' 
		if(featureVariablesPromelaCode.length() > 0)
			featureVariablesPromelaCode.replace(featureVariablesPromelaCode.length()-2, featureVariablesPromelaCode.length(), ";");
		return featureVariablesPromelaCode.toString();
	}
	
	// Append the Message Channel to the Promela Code
	// mtype = {imageOn, imageOff, videoOn, videoOff,contextChanged,done,adapted}
	public String appendMessageChannel(){
		StringBuffer msgChannelPromelaCode = new StringBuffer();
		msgChannelPromelaCode.append("mtype = {" );
		for (Feature ctxAwarefeature : contextAwareFeatures) {
			msgChannelPromelaCode.append(ctxAwarefeature.getName()+"On,"+ctxAwarefeature.getName()+"Off,");
		}
		msgChannelPromelaCode.append("contextChanged,done,adapted};");
		return msgChannelPromelaCode.toString();
	}
}
