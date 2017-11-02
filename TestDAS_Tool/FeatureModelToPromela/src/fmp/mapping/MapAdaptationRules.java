package fmp.mapping;

import fmp.contextEvolModel.AdaptationRuleWithCtxProposition;
import fmp.contextEvolModel.CKS;
import fmp.contextEvolModel.ContextProposition;
import fmp.contextModel.AdaptationRuleWithCtxFeature;
import fmp.featureModel.DSPL;
import fmp.featureModel.Feature;

public class MapAdaptationRules {

	private String newline = System.getProperty("line.separator");
	
	private DSPL dspl;
	
	private CKS cks;
	
	public MapAdaptationRules(DSPL dspl, CKS cks) {
		this.dspl = dspl;
		this.cks = cks;
	}
	
	public String getAdaptationRulestoPromelaCode(){
		StringBuffer adaptationRulesPromelaCode = new StringBuffer();
		adaptationRulesPromelaCode.append(appendController());
		adaptationRulesPromelaCode.append(appendAdaptationRules());
		return adaptationRulesPromelaCode.toString();				
	}
	
	// Append the Adaptation Rules Controller to the Promela Code
	public String appendController(){
		StringBuffer controllerPromelaCode = new StringBuffer();
		int numAdaptationRules = cks.getAdaptationRules().size();
		controllerPromelaCode.append("active proctype Controller() {"+newline+
										" do" +newline +
										"  :: buss?contextChanged -> ");
		
		for (int i = 1; i <= numAdaptationRules; i++){
			controllerPromelaCode.append("run AR"+i+"();");
		}
		controllerPromelaCode.append("numAnswers = 0;"+newline);
		controllerPromelaCode.append("  do" +newline+ 
				"	 :: (numAnswers != "+numAdaptationRules+") -> buss?done; numAnswers = numAnswers + 1;" +newline+
				"	 :: else -> break" +newline+
				"  od" +newline+
				"  buss!adapted" +newline+	
				" od"+ newline +
				"}");
		return controllerPromelaCode.toString();
	}
	
	// TODO PS:It does not treat the cases in which the adaptation rule require contextPropositions=false (e.g.,(isBtLow == true ) &&
	// hasPwScr=false) is a context trigguer not captured by this implementation. 
	public String appendAdaptationRules(){
		StringBuffer adaptationRulesPromelaCode = new StringBuffer();
		int idRule = 1;
		for (AdaptationRuleWithCtxProposition aRule : cks.getAdaptationRules()) {
			adaptationRulesPromelaCode.append(newline);
			adaptationRulesPromelaCode.append("proctype AR"+idRule+"() {" + newline+
							"if"+ newline);
			
			//context condition
			adaptationRulesPromelaCode.append(" :: (");
			for (ContextProposition ctxProposition : aRule.getContextRequired()) {
				adaptationRulesPromelaCode.append(ctxProposition.getName()+ " == true && ");
			}
			//Removing the last &&
			adaptationRulesPromelaCode.replace(adaptationRulesPromelaCode.length()-3, 
					adaptationRulesPromelaCode.length(), "");
			adaptationRulesPromelaCode.append(") -> ");
			
			//feature activation
			for (Feature feature : aRule.getToActiveFeatureList()) {
				adaptationRulesPromelaCode.append("buss!"+feature.getName()+"On;");
			}
			
			//feature deactivation
			for (Feature feature : aRule.getToDeactiveFeatureList()){
				adaptationRulesPromelaCode.append("buss!"+feature.getName()+"Off;");
			}

			adaptationRulesPromelaCode.append("buss!done");
			//(battery == 1 && hasPwSrc == false) -> buss!imageOff;buss!videoOff;buss!done

			adaptationRulesPromelaCode.append(newline+" :: else -> buss!done"+newline+
											"fi" +newline+
											"}");
			idRule++;
			adaptationRulesPromelaCode.append(newline);
			adaptationRulesPromelaCode.append(newline);
		}
		
		return adaptationRulesPromelaCode.toString();
	}
	
}
