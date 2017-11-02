package fmp.example;

import java.awt.EventQueue;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import fmp.CFMtoCKS.ExtractorCks;
import fmp.contextEvolModel.AdaptationRuleWithCtxProposition;
import fmp.contextEvolModel.CKS;
import fmp.contextEvolModel.ContextProposition;
import fmp.contextEvolModel.ContextState;
import fmp.contextModel.AdaptationRuleWithCtxFeature;
import fmp.contextModel.CFM;
import fmp.contextModel.ContextFeature;
import fmp.contextModel.ContextGroup;
import fmp.contextModel.ContextGroupType;
import fmp.contextModel.ContextType;
import fmp.featureModel.DSPL;
import fmp.featureModel.ExcludeRule;
import fmp.featureModel.Feature;
import fmp.featureModel.FeatureGroup;
import fmp.featureModel.FeatureGroupType;
import fmp.featureModel.FeatureType;
import fmp.featureModel.RequireRule;
import fmp.mapping.Parser;
import fmp.runSpin.ui.RunSpinChecker;
import fmp.runSpinBackground.RunOnPromptDOS;
import fmp.utilities.GenerateFiles;

public class TestStepByStepWithUI {


	public static void main(String[] args) {
	// Step 1 - User modeling in the Fixture both Feature Model and Context Model
		//--------------------------> FEATURE MODEL <-------------------/
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
		documentsGroup.append(video);
		documentsGroup.append(image);
		documentsGroup.setGroupType(FeatureGroupType.OR_Group);
		
		Feature text = new Feature();
		text.setName("Text");
		text.setFeatureType(FeatureType.Mandatory);
		text.setFatherFeature(documents);
		
		//Dependency Rule
		//Example A require B
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
		
		//Exclude Rule
		//Example A excludes C 
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
		
		
		//--------------------------> CONTEXT MODEL <-------------------/
		//Context Root
		ContextFeature root = new ContextFeature("Root Context");
		
		// Context Propositions
		ContextFeature isBtFull = new ContextFeature("isBtFull");
		isBtFull.setContextType(ContextType.Group);
		ContextFeature isBtNormal = new ContextFeature("isBtNormal");
		isBtNormal.setContextType(ContextType.Group);
		ContextFeature isBtLow = new ContextFeature("isBtLow");
		isBtLow.setContextType(ContextType.Group);
		ContextFeature hasPwSource = new ContextFeature("hasPwSource");
		hasPwSource.setContextType(ContextType.Optional);
		
		//Context Groups
		ContextGroup battery = new ContextGroup("Baterry");
		battery.setType(ContextGroupType.XOR);
		battery.append(isBtFull);
		battery.append(isBtNormal);
		battery.append(isBtLow);
		
		ContextGroup source = new ContextGroup("Source");
		source.setType(ContextGroupType.NONE);
		source.append(hasPwSource);

		// Adaptation Rules
		
			//Adaptation Rule isBtLow => Video off, Image off
			AdaptationRuleWithCtxFeature rule1 = new AdaptationRuleWithCtxFeature();
			LinkedList<ContextFeature> contextTrigger1 = new LinkedList<ContextFeature>();
			contextTrigger1.add(isBtLow);
			rule1.setContextRequired(contextTrigger1);
			
			LinkedList<Feature> toDeactiveFeatures1 = new LinkedList<Feature>();
			toDeactiveFeatures1.add(image);
			toDeactiveFeatures1.add(video);
			rule1.setToDeactiveFeatureList(toDeactiveFeatures1);
				
			//Adaptation Rule isBtLow and HasPwSrc => Video off, Image on
			AdaptationRuleWithCtxFeature rule2 = new AdaptationRuleWithCtxFeature();
			LinkedList<ContextFeature> contextTrigger2 = new LinkedList<ContextFeature>();
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
			AdaptationRuleWithCtxFeature rule3 = new AdaptationRuleWithCtxFeature();
			LinkedList<ContextFeature> contextTrigger3 = new LinkedList<ContextFeature>();
			contextTrigger3.add(isBtNormal);		
			rule3.setContextRequired(contextTrigger3);
			
			LinkedList<Feature> toActiveFeature3 = new LinkedList<Feature>();
			toActiveFeature3.add(image);
			rule3.setToActiveFeatureList(toActiveFeature3);
			
			LinkedList<Feature> toDeactiveFeatures3 = new LinkedList<Feature>();
			toDeactiveFeatures3.add(video);
			rule3.setToDeactiveFeatureList(toDeactiveFeatures3);
			
			//Adaptation Rule isBtNormal and HasPwSrc => Video on, Image on
			AdaptationRuleWithCtxFeature rule4 = new AdaptationRuleWithCtxFeature();
			LinkedList<ContextFeature> contextTrigger4 = new LinkedList<ContextFeature>();
			contextTrigger4.add(isBtNormal);
			contextTrigger4.add(hasPwSource);
			rule4.setContextRequired(contextTrigger4);
				
			LinkedList<Feature> toActiveFeature4 = new LinkedList<Feature>();
			toActiveFeature4.add(image);
			toActiveFeature4.add(video);
			rule4.setToActiveFeatureList(toActiveFeature4);		
			
			//Adaptation Rule isBtFull => Video on, Image on
			AdaptationRuleWithCtxFeature rule5 = new AdaptationRuleWithCtxFeature();
			LinkedList<ContextFeature> contextTrigger5 = new LinkedList<ContextFeature>();
			contextTrigger5.add(isBtFull);		
			rule5.setContextRequired(contextTrigger5);
			
			LinkedList<Feature> toActiveFeature5 = new LinkedList<Feature>();
			toActiveFeature5.add(image);
			toActiveFeature5.add(video);
			rule5.setToActiveFeatureList(toActiveFeature5);
		
		//Context Model
		CFM ctxModel = new CFM();
		ctxModel.setContextRoot(root);
		ctxModel.getContextGroups().add(battery);
		ctxModel.getContextGroups().add(source);
		ctxModel.getAdaptationRules().add(rule1);
		ctxModel.getAdaptationRules().add(rule2);
		ctxModel.getAdaptationRules().add(rule3);
		ctxModel.getAdaptationRules().add(rule4);
		ctxModel.getAdaptationRules().add(rule5);

		//mobilineDspl.print();
		//ctxModel.print();
		
		// Step 2 - Identifying the ContextStates
		// Step 3 - Generating the XLS File with the contextStates
		ExtractorCks extractor = new ExtractorCks();
		CKS cks = extractor.getContextKripkeStructureFromContextModel(ctxModel,"GeneratedFile.xls");		
		
		// Step 4 - Read the XLS File with the context evolution completing the CKS
		// GeneratedFile2 is the same file generate before, but it is filled out
		extractor.getContextChangesFromXLS("GeneratedFile2.xls", cks.getContextStates());
		
		cks.print();
		
		// Step 5 - Parser the CKS to PML Code, and generate the PML file
		Parser parser = new Parser(mobilineDspl, cks);
		HashMap <String,String> propertiesList = new HashMap<String,String>();
		String promelaCodeGenerated = parser.generatePromelaCodefromDSPL(propertiesList);
		//System.out.println(promelaCodeGenerated);
		GenerateFiles gen = new GenerateFiles();
		gen.generatePMLFile(promelaCodeGenerated, "promelaCodeGenerated");
		
		// Step 6 - Run the PML Code
		// Step 7 - Show the results
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					String path = "C:/promelaCodeGenerated.pml";
					RunSpinChecker window = new RunSpinChecker(propertiesList, path); 
					//window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		
		RunOnPromptDOS runCommand = new RunOnPromptDOS("C:\\Spin");
		/*for (String property : propertiesList.keySet()) {
			try {
				System.out.println("--> Executing analysis of the property: "+property);
				String runResult = runCommand.runPropertyInSpin(property, "promelaCodeGenerated.pml");
				String numberOfErros = runResult.substring(runResult.indexOf(":")+1, runResult.length());
				if(!numberOfErros.equals("0")){
					// the result has errors
					String erroReport = runCommand.getReportLastRun("promelaCodeGenerated.pml");
					System.out.println("ERROR FOUND");
					System.out.println("## PRINTING ERROR REPORT ## ");					
					System.out.println(erroReport);					
				}else{
					System.out.println("NO ERROR FOUND!! \n");
				}
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}*/
				
	}

}
