package fmp.mapping;

import java.util.HashMap;
import java.util.LinkedList;

import fmp.contextEvolModel.AdaptationRuleWithCtxProposition;
import fmp.contextEvolModel.CKS;
import fmp.contextModel.AdaptationRuleWithCtxFeature;
import fmp.featureModel.DSPL;
import fmp.featureModel.Feature;
import fmp.featureModel.FeatureType;

public class Parser {

	private String newline = System.getProperty("line.separator");
	
	private DSPL dspl;
	
	private CKS cks;
	
	private LinkedList<Feature> contextAwareFeatures = new LinkedList<Feature>();
	
	public Parser(DSPL dspl, CKS cks){
		this.dspl = dspl;
		this.cks = cks;
	}
	
	public String generatePromelaCodefromDSPL(HashMap<String,String> propertiesList){
		StringBuilder promelaCode = new StringBuilder();

		//identifying the context-aware features (features that are (de)activated by a adaptation rule)
		getContextAwareFeatures();
		
		// Variables (and MessageChannel)
		MapVariables vars = new MapVariables(contextAwareFeatures, cks, dspl);
		promelaCode.append(vars.getVariablestoPromelaCode());
		
		// Message Channel
		promelaCode.append(newline+"chan buss = [0] of {mtype};"+newline);
		
		// Actuators
		MapActuators acts = new MapActuators(contextAwareFeatures);
		promelaCode.append(acts.getActuatorstoPromelaCode()+newline);

		//Context Manager
		MapContextKripkeStructure CtxEvolModel = new MapContextKripkeStructure(cks);
		promelaCode.append(CtxEvolModel.getCKSPromelaCode() +newline);
		
		// Controller
		// Adaptations Rules
		MapAdaptationRules adapRules = new MapAdaptationRules(dspl,cks);
		promelaCode.append(adapRules.getAdaptationRulestoPromelaCode());
		
		// LTL properties - all
		LTLProperties ltlProperties = new LTLProperties(contextAwareFeatures,dspl,cks,propertiesList);
		promelaCode.append(ltlProperties.getLTLPropertiestoPromelaCode());
		
		return promelaCode.toString();
	}
	
	// Get the context aware features from the dspl
	public void getContextAwareFeatures(){
		for (AdaptationRuleWithCtxProposition adaptationRule : cks.getAdaptationRules()) {
			for (Feature feature : adaptationRule.getToActiveFeatureList()) {
				//if the feature is not in the list, then add it
				if(!contextAwareFeatures.contains(feature))
					contextAwareFeatures.add(feature);
			}
			for (Feature feature : adaptationRule.getToDeactiveFeatureList()) {
				////if the feature is not in the list, then add it
				if(!contextAwareFeatures.contains(feature))
					contextAwareFeatures.add(feature);
			}
		}
	}
	
	
	

	// C-KS
	public void contextEvolutionModel(){
		
	}

	// for each adaptation rule and one for trigger all rules after a context change
	public void adaptationRules(){
		
	}

	
	
}
