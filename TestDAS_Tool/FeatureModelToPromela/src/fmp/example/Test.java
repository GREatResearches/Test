package fmp.example;

import java.util.HashMap;
import java.util.LinkedList;

import fmp.contextEvolModel.AdaptationRuleWithCtxProposition;
import fmp.contextEvolModel.CKS;
import fmp.contextEvolModel.ContextProposition;
import fmp.contextEvolModel.ContextState;
import fmp.contextModel.AdaptationRuleWithCtxFeature;
import fmp.contextModel.ContextConstraintCtxModel;
import fmp.featureModel.DSPL;
import fmp.featureModel.ExcludeRule;
import fmp.featureModel.Feature;
import fmp.featureModel.FeatureGroup;
import fmp.featureModel.FeatureGroupType;
import fmp.featureModel.FeatureType;
import fmp.featureModel.RequireRule;
import fmp.mapping.Parser;
import fmp.utilities.GenerateFiles;

public class Test {

	public static void main(String[] args) {
		//Context Propositions
		ContextProposition isBtFull = new ContextProposition("isBtFull");
		ContextProposition isBtNormal = new ContextProposition("isBtNormal");
		ContextProposition isBtLow = new ContextProposition("isBtLow");
		ContextProposition hasPwSource = new ContextProposition("hasPwSource");
		
		//Context Constraint
		ContextConstraintCtxModel batteryLevel = new ContextConstraintCtxModel();
		batteryLevel.getContextPropositionsList().add(isBtFull);
		batteryLevel.getContextPropositionsList().add(isBtNormal);
		batteryLevel.getContextPropositionsList().add(isBtLow);		
		
		//Context States
		// S0
		ContextState ctxSt0 = new ContextState();
		ctxSt0.getAtiveContextPropositions().add(isBtFull);
		
		ctxSt0.setId(0);
		
		// S1
		ContextState ctxSt1 = new ContextState();
		ctxSt1.getAtiveContextPropositions().add(isBtFull);
		ctxSt1.getAtiveContextPropositions().add(hasPwSource);
		
		ctxSt1.setId(1);
		
		// S2
		ContextState ctxSt2 = new ContextState();
		ctxSt2.getAtiveContextPropositions().add(isBtNormal);
		
		ctxSt2.setId(2);
		
		// S3
		ContextState ctxSt3 = new ContextState();
		ctxSt3.getAtiveContextPropositions().add(isBtNormal);
		ctxSt3.getAtiveContextPropositions().add(hasPwSource);
		
		ctxSt3.setId(3);
		
		// S4
		ContextState ctxSt4 = new ContextState();
		ctxSt4.getAtiveContextPropositions().add(isBtLow);
		
		ctxSt4.setId(4);
		
		//S5
		ContextState ctxSt5 = new ContextState();
		ctxSt5.getAtiveContextPropositions().add(isBtLow);
		ctxSt5.getAtiveContextPropositions().add(hasPwSource);
		
		
		ctxSt5.setId(5);

		// Transition Relation R
		ctxSt0.addNextState(ctxSt1);
		ctxSt0.addNextState(ctxSt2);
		ctxSt1.addNextState(ctxSt0);
		ctxSt1.addNextState(ctxSt1); // self-loop
		ctxSt2.addNextState(ctxSt3);
		ctxSt2.addNextState(ctxSt4);
		ctxSt3.addNextState(ctxSt1);
		ctxSt3.addNextState(ctxSt2);
		ctxSt4.addNextState(ctxSt5);
		ctxSt5.addNextState(ctxSt4);
		ctxSt5.addNextState(ctxSt3);
		
		//C-KS [S, I, C, L, ->]
		CKS cks = new CKS();
		cks.getContextStates().add(ctxSt0);
		cks.getContextStates().add(ctxSt1);
		cks.getContextStates().add(ctxSt2);
		cks.getContextStates().add(ctxSt3);
		cks.getContextStates().add(ctxSt4);
		cks.getContextStates().add(ctxSt5);

		cks.setInitialState(ctxSt0);
		
		cks.getContextPropositions().add(isBtFull);
		cks.getContextPropositions().add(isBtNormal);
		cks.getContextPropositions().add(isBtLow);
		cks.getContextPropositions().add(hasPwSource);
		
		cks.getConstraints().add(batteryLevel);
		
		//cks.print();
		
		// Features
		Feature documents = new Feature();
		documents.setName("Documents");
		documents.setFeatureType(FeatureType.Mandatory);
		
		Feature video = new Feature();
		video.setName("Video");
		video.setFatherFeature(documents);
		video.setFeatureType(FeatureType.Group);
		
		Feature image = new Feature();
		image.setName("Image");
		image.setFatherFeature(documents);
		image.setFeatureType(FeatureType.Group);
		
		FeatureGroup documentsGroup = new FeatureGroup();
		documentsGroup.getFeatures().add(video);
		documentsGroup.getFeatures().add(image);
		documentsGroup.setGroupType(FeatureGroupType.OR_Group);
		
		// setting the Feature Group in the features attribute 
		video.setGroup(documentsGroup);
		image.setGroup(documentsGroup);
		
		Feature text = new Feature();
		text.setName("Text");
		text.setFeatureType(FeatureType.Mandatory);
		text.setFatherFeature(documents);
		
		//Adaptation Rule isBtLow => Video off, Image off
		AdaptationRuleWithCtxProposition rule1 = new AdaptationRuleWithCtxProposition();
		LinkedList<ContextProposition> contextTrigger1 = new LinkedList<ContextProposition>();
		contextTrigger1.add(isBtLow);
		rule1.setContextRequired(contextTrigger1);
		
		LinkedList<Feature> toDeactiveFeatures1 = new LinkedList<Feature>();
		toDeactiveFeatures1.add(image);
		toDeactiveFeatures1.add(video);
		rule1.setToDeactiveFeatureList(toDeactiveFeatures1);
		
		//Adaptation Rule isBtLow and HasPwSrc => Video off, Image on
		AdaptationRuleWithCtxProposition rule2 = new AdaptationRuleWithCtxProposition();
		LinkedList<ContextProposition> contextTrigger2 = new LinkedList<ContextProposition>();
		contextTrigger2.add(isBtLow);
		contextTrigger2.add(hasPwSource);
		rule2.setContextRequired(contextTrigger2);
		
		LinkedList<Feature> toActiveFeature2 = new LinkedList<Feature>();
		toActiveFeature2.add(image);
		rule2.setToActiveFeatureList(toActiveFeature2);
		
		LinkedList<Feature> toDeactiveFeatures2 = new LinkedList<Feature>();
		toDeactiveFeatures2.add(video);
		rule2.setToDeactiveFeatureList(toDeactiveFeatures2);
		
		//Adaptation Rule isBtNormal => Video off, Image on
		AdaptationRuleWithCtxProposition rule3 = new AdaptationRuleWithCtxProposition();
		LinkedList<ContextProposition> contextTrigger3 = new LinkedList<ContextProposition>();
		contextTrigger3.add(isBtNormal);		
		rule3.setContextRequired(contextTrigger3);
		
		LinkedList<Feature> toActiveFeature3 = new LinkedList<Feature>();
		toActiveFeature3.add(image);
		rule3.setToActiveFeatureList(toActiveFeature3);
		
		LinkedList<Feature> toDeactiveFeatures3 = new LinkedList<Feature>();
		toDeactiveFeatures3.add(video);
		rule3.setToDeactiveFeatureList(toDeactiveFeatures3);
		
		//Adaptation Rule isBtNormal and HasPwSrc => Video on, Image on
		AdaptationRuleWithCtxProposition rule4 = new AdaptationRuleWithCtxProposition();
		LinkedList<ContextProposition> contextTrigger4 = new LinkedList<ContextProposition>();
		contextTrigger4.add(isBtNormal);
		contextTrigger4.add(hasPwSource);
		rule4.setContextRequired(contextTrigger4);
		
		LinkedList<Feature> toActiveFeature4 = new LinkedList<Feature>();
		toActiveFeature4.add(image);
		toActiveFeature4.add(video);
		rule4.setToActiveFeatureList(toActiveFeature4);		
		
		//Adaptation Rule isBtFull => Video on, Image on
		AdaptationRuleWithCtxProposition rule5 = new AdaptationRuleWithCtxProposition();
		LinkedList<ContextProposition> contextTrigger5 = new LinkedList<ContextProposition>();
		contextTrigger5.add(isBtFull);		
		rule5.setContextRequired(contextTrigger5);
		
		LinkedList<Feature> toActiveFeature5 = new LinkedList<Feature>();
		toActiveFeature5.add(image);
		toActiveFeature5.add(video);
		rule5.setToActiveFeatureList(toActiveFeature5);
		
		//Require Rule
		//Example A requer B
		Feature A = new Feature();
		A.setFeatureType(FeatureType.Optional);
		A.setName("A");
		A.setFatherFeature(documents);
		
		Feature B = new Feature();
		B.setFeatureType(FeatureType.Optional);
		B.setName("B");
		B.setFatherFeature(documents);
		
		RequireRule reqRule = new RequireRule();
		reqRule.setFeatureClaimant(A);
		LinkedList<Feature> reqFeatureList = new LinkedList<Feature>();
		reqFeatureList.add(B);
		reqRule.setRequiredFeatureList(reqFeatureList);
		
		//Include Rule
		//Example A exclui C 
		Feature C = new Feature();
		C.setFeatureType(FeatureType.Optional);
		C.setName("C");
		C.setFatherFeature(documents);
		
		ExcludeRule excRule = new ExcludeRule();
		excRule.setFeatureClaimant(A);
		LinkedList<Feature> excFeatureList = new LinkedList<Feature>();
		excFeatureList.add(C);
		excRule.setRequiredFeatureList(excFeatureList);		
		
		//DSPL
		DSPL mobilineDspl = new DSPL();
	
		//DSPL adaptation rules
		// note in this test I added to cks just to validate 
		cks.getAdaptationRules().add(rule1);
		cks.getAdaptationRules().add(rule2);
		cks.getAdaptationRules().add(rule3);
		cks.getAdaptationRules().add(rule4);
		cks.getAdaptationRules().add(rule5);
		
		
		//DSPL features
		mobilineDspl.getFeatures().add(text);
		mobilineDspl.getFeatures().add(video);
		mobilineDspl.getFeatures().add(image);
		mobilineDspl.getFeatures().add(documents);
		mobilineDspl.getFeatures().add(A);
		mobilineDspl.getFeatures().add(B);
		mobilineDspl.getFeatures().add(C);	
			//DSPL Initial Configuration - mandatory features and from group features
		mobilineDspl.getInitialConfiguration().add(documents);
		mobilineDspl.getInitialConfiguration().add(text);
		mobilineDspl.getInitialConfiguration().add(image);		
			//DSPL Cross-tree Rules
		mobilineDspl.getExcludeRules().add(excRule);
		mobilineDspl.getRequireRules().add(reqRule);
			//DSPL Feature Groups
		mobilineDspl.getFeatureGroups().add(documentsGroup);
		
		mobilineDspl.print();
		
		Parser parser = new Parser(mobilineDspl,cks);
		HashMap<String,String> propertiesList = new HashMap<String,String>();
		String promelaCodeGenerated = parser.generatePromelaCodefromDSPL(propertiesList);
		System.out.println(promelaCodeGenerated);
		
		GenerateFiles gen = new GenerateFiles();
		gen.generatePMLFile(promelaCodeGenerated, "promelaCodeGenerated");
		
	}
}


