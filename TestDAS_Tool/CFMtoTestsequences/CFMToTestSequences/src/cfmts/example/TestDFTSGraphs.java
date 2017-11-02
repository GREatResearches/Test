package cfmts.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cfmts.graph.cks.Graph_CKS;
import cfmts.graph.cks.Node_CKS;
import cfmts.graph.dfts.GenerateDFTSGFromCKSG;
import cfmts.graph.dfts.Graph_DFTS;
import cfmts.ts.elements.TestCase;
import cfmts.ts.elements.TestSequence;
import cfmts.ts.elements.TestSequenceList;
import cfmts.ts.generator.TSForConfigurationCorrectness;
import cfmts.ts.generator.TSForFeatureLiveness;
import cfmts.ts.generator.TSForInterleavingCorrectness;
import cfmts.ts.generator.TSForRuleLiveness;
import cfmts.ts.generator.TSForVariationLiveness;
import fmp.contextEvolModel.ContextConstraint;
import fmp.contextEvolModel.ContextProposition;
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

public class TestDFTSGraphs {
	
	public static void main(String[] args) {
		//--------------------------> FEATURE MODEL <-------------------/
		//GETS FROM FIXTURE
		
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
		//GETS FROM FIXTURE
		
		//Context Root
		ContextFeature root = new ContextFeature("Root Context");
		
		// Context Propositions
		ContextFeature isBtFull = new ContextFeature("isBtFull");
		isBtFull.setContextType(ContextType.Group);
		ContextFeature isBtFullCharging = new ContextFeature("isBtFullCharging");
		isBtFullCharging.setContextType(ContextType.Group);
		ContextFeature isBtNormal = new ContextFeature("isBtNormal");
		isBtNormal.setContextType(ContextType.Group);
		ContextFeature isBtNormalCharging = new ContextFeature("isBtNormalCharging");
		isBtNormalCharging.setContextType(ContextType.Group);
		ContextFeature isBtLow = new ContextFeature("isBtLow");
		isBtLow.setContextType(ContextType.Group);
		ContextFeature isBtLowCharging = new ContextFeature("isBtLowCharging");
		isBtLowCharging.setContextType(ContextType.Group);		
		
		//Context Groups
		ContextGroup battery = new ContextGroup("Baterry");
		battery.setType(ContextGroupType.XOR);
		battery.append(isBtFull);
		battery.append(isBtFullCharging);
		battery.append(isBtNormal);
		battery.append(isBtNormalCharging);
		battery.append(isBtLow);		
		
		ContextGroup battery2 = new ContextGroup("Baterry"); // To the interleaving testing
		battery2.append(isBtLowCharging);		
		
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
			
		//Adaptation Rule isBtLow and HasPwSrc => Video off, Image on --> NOT SUPPORTED
		//Adaptation Rule isBtLowCharging => Video off, Image on
		AdaptationRuleWithCtxFeature rule2 = new AdaptationRuleWithCtxFeature();
		LinkedList<ContextFeature> contextTrigger2 = new LinkedList<ContextFeature>();
		contextTrigger2.add(isBtLowCharging);		
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
		
		//Adaptation Rule isBtNormal and HasPwSrc => Video on, Image on --> NOT SUPPORTED
		//Adaptation Rule isBtNormalCharging => Video off, Image on
		AdaptationRuleWithCtxFeature rule4 = new AdaptationRuleWithCtxFeature();
		LinkedList<ContextFeature> contextTrigger4 = new LinkedList<ContextFeature>();
		contextTrigger4.add(isBtNormalCharging);		
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
		
		//Adaptation Rule isBtFull => Video on, Image on
		AdaptationRuleWithCtxFeature rule6 = new AdaptationRuleWithCtxFeature();
		LinkedList<ContextFeature> contextTrigger6 = new LinkedList<ContextFeature>();
		contextTrigger6.add(isBtFullCharging);		
		rule6.setContextRequired(contextTrigger6);
		
		LinkedList<Feature> toActiveFeature6 = new LinkedList<Feature>();
		toActiveFeature6.add(image);
		toActiveFeature6.add(video);
		rule6.setToActiveFeatureList(toActiveFeature6);
		
		//Context Model
		CFM ctxModel = new CFM();
		ctxModel.setContextRoot(root);
		ctxModel.getContextGroups().add(battery);
		ctxModel.getContextGroups().add(battery2); // to the interleaving
		ctxModel.getAdaptationRules().add(rule1);
		ctxModel.getAdaptationRules().add(rule2);
		ctxModel.getAdaptationRules().add(rule3);
		ctxModel.getAdaptationRules().add(rule4);
		ctxModel.getAdaptationRules().add(rule5);
		ctxModel.getAdaptationRules().add(rule6);
		
		//--------------------------> CKS <-------------------/
		//GETS FROM EXCEL
		
		//Context Propositions
		ContextProposition isBtFullProp = new ContextProposition("isBtFull");
		ContextProposition isBtFullChagingProp = new ContextProposition("isBtFullCharging");
		ContextProposition isBtNormalProp = new ContextProposition("isBtNormal");
		ContextProposition isBtNormalChagingProp = new ContextProposition("isBtNormalCharging");
		ContextProposition isBtLowProp = new ContextProposition("isBtLow");
		ContextProposition isBtLowChagingProp = new ContextProposition("isBtLowCharging");
		
		//Context Constraint
		ContextConstraint batteryLevel = new ContextConstraint();
		batteryLevel.getContextPropositionsList().add(isBtFullProp);
		batteryLevel.getContextPropositionsList().add(isBtFullChagingProp);
		batteryLevel.getContextPropositionsList().add(isBtNormalProp);
		batteryLevel.getContextPropositionsList().add(isBtNormalChagingProp);	
		batteryLevel.getContextPropositionsList().add(isBtLowProp);
		batteryLevel.getContextPropositionsList().add(isBtLowChagingProp);	
		
		//Context States
		// S0
		Node_CKS ctxSt0 = new Node_CKS();
		ctxSt0.getAtiveContextPropositions().add(isBtFullProp);
		ctxSt0.setId(0);
		
		// S1
		Node_CKS ctxSt1 = new Node_CKS();
		ctxSt1.getAtiveContextPropositions().add(isBtFullChagingProp);
		ctxSt1.setId(1);
		
		// S2
		Node_CKS ctxSt2 = new Node_CKS();
		ctxSt2.getAtiveContextPropositions().add(isBtNormalProp);
			//To	 the test with testing criteria 3
			//ctxSt2.getAtiveContextPropositions().add(isBtLowChagingProp);
		ctxSt2.setId(2);
		
		// S3
		Node_CKS ctxSt3 = new Node_CKS();
		ctxSt3.getAtiveContextPropositions().add(isBtNormalChagingProp);
			//To the test with testing criteria 3
			//ctxSt3.getAtiveContextPropositions().add(isBtLowChagingProp);	
		ctxSt3.setId(3);
		
		// S4
		Node_CKS ctxSt4 = new Node_CKS();
		ctxSt4.getAtiveContextPropositions().add(isBtLowProp);
		ctxSt4.setId(4);
		
		//S5
		Node_CKS ctxSt5 = new Node_CKS();
		ctxSt5.getAtiveContextPropositions().add(isBtLowChagingProp);
			//	To the test with testing criteria 3
			//ctxSt5.getAtiveContextPropositions().add(isBtFullProp);	
		ctxSt5.setId(5);

		// Transition Relation R (CKS)
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
			
		Graph_CKS cksGraph = new Graph_CKS();
		cksGraph.getNodes().add(ctxSt0);
		cksGraph.getNodes().add(ctxSt1);
		cksGraph.getNodes().add(ctxSt2);
		cksGraph.getNodes().add(ctxSt3);
		cksGraph.getNodes().add(ctxSt4);
		cksGraph.getNodes().add(ctxSt5);
		
		//cksGraph.dephtFirstSearch(ctxSt0);
		//cksGraph.breadthFirstSearch(ctxSt0);
		
		GenerateDFTSGFromCKSG gen = new GenerateDFTSGFromCKSG(mobilineDspl, ctxModel, cksGraph);
		//it start analze by the first context state of CKS
		gen.dephtFirstSearchGeneratingDFTSTrasitions(cksGraph.getNodes().get(0));
		gen.printGraphDFTS();
		
		//--------------------------> TEST SEQUENCES [C1 ]<-------------------/
		Graph_DFTS dfts = gen.getGraph_DFTS(); //dfts
		
		//[C1]: it generate the testSequence to cover ALL DFTS States
		    TSForConfigurationCorrectness tsForCC = new TSForConfigurationCorrectness();
		//1.0d= 100%
		// from scratch 
		    //TestSequence testSequence = tsForCC.generateTestSequence(dfts,0.2d);
		    // printTestSequence(testSequence);
		    // System.out.println("###########");
		//Based on a previous one
		    //testSequence = tsForCC.identifyMissingCases(dfts, testSequence, 1.0d);
			//printTestSequence(testSequence);
		
		//--------------------------> TEST SEQUENCES [C2 ]<-------------------/
			TSForFeatureLiveness tsFtLiv = new TSForFeatureLiveness(mobilineDspl);
		//1.0d= 100%
		// from scratch 
		// Tem 5 features (A,B,C, Video,Image)... 0.5d > 2 (Image e Video)... 0.2d = 1 feature
			//ArrayList<TestSequence> testSeqList = tsFtLiv.generateTestSequence(dfts, 1.0d, new ArrayList<TestSequence>());
			//printTestSequenceList(testSeqList);
		
			//System.out.println("\n########################\nCompleting the Test Sequence \n ########################");
		//Based on a previous one
			//testSeqList = tsFtLiv.identifyMissingCases(dfts, testSeqList, 1.0d);
			//printTestSequenceList(testSeqList);
		
		//--------------------------> TEST SEQUENCES [C1 AND C2]<-------------------/
		//[C1]: it generate the testSequence to cover ALL DFTS States
		    //TSForConfigurationCorrectness tsForCC = new TSForConfigurationCorrectness();
		    //TestSequence testSequence = tsForCC.generateTestSequence(dfts,0.2d);
		    // printTestSequence(testSequence);
		     
		    //System.out.println("\n########################\nCompleting the Test Sequence \n ########################");
		    //TSForFeatureLiveness tsFtLiv = new TSForFeatureLiveness(mobilineDspl);
			// ArrayList<TestSequence> initialTestSeq = new ArrayList<TestSequence>();
			// initialTestSeq.add(testSequence);
			// ArrayList<TestSequence> testSeqList = tsFtLiv.generateTestSequence(dfts, 1.0d,initialTestSeq );
			// printTestSequenceList(testSeqList);
		
		//--------------------------> TEST SEQUENCES [C3]<-------------------/
			TSForInterleavingCorrectness tsIntCor = new TSForInterleavingCorrectness(ctxModel);
			//1.0d= 100%
			// from scratch 
			// Tem 5 features (A,B,C, Video,Image)... 0.5d > 2 (Image e Video)... 0.2d = 1 feature
				//ArrayList<TestSequence> testSeqList = tsIntCor.generateTestSequence(dfts, 0.0d, new ArrayList<TestSequence>());
				//printTestSequenceList(testSeqList);
		
			//System.out.println("\n########################\nCompleting the Test Sequence \n ########################");
			//Based on a previous one
				//testSeqList = tsIntCor.identifyMissingCases(dfts, testSeqList, 1.0d);
				//printTestSequenceList(testSeqList);
		
		//--------------------------> TEST SEQUENCES [C4]<-------------------/
			TSForRuleLiveness tsRlLiv = new TSForRuleLiveness(ctxModel);
		//1.0d= 100%
		// from scratch 
		// Tem 5 features (A,B,C, Video,Image)... 0.5d > 2 (Image e Video)... 0.2d = 1 feature
			TestSequenceList testSeqList = tsRlLiv.generateTestSequence(dfts, 1.0d, new TestSequenceList());
			printTestSequenceList(testSeqList);
	
			saveTestSequenceToJson(testSeqList, "D:/testSeq1.json");
			
		//System.out.println("\n########################\nCompleting the Test Sequence \n ########################");
		//Based on a previous one
			//testSeqList = tsRlLiv.identifyMissingCases(dfts, testSeqList, 1.0d);
			//printTestSequenceList(testSeqList);
			
		
		//--------------------------> TEST SEQUENCES [C5]<-------------------/
			TSForVariationLiveness tsVtLiv = new TSForVariationLiveness(mobilineDspl);
		//1.0d= 100%
		// from scratch 
		// Tem 5 features (A,B,C, Video,Image)... 0.5d > 2 (Image e Video)... 0.2d = 1 feature
			//ArrayList<TestSequence> testSeqList = tsVtLiv.generateTestSequence(dfts, 1.0d, new ArrayList<TestSequence>());
			//printTestSequenceList(testSeqList);
		
			//System.out.println("\n########################\nCompleting the Test Sequence \n ########################");
		//Based on a previous one
			//testSeqList = tsVtLiv.identifyMissingCases(dfts, testSeqList, 1.0d);
			//printTestSequenceList(testSeqList);

		//--------------------------> TEST SEQUENCES [ALL]<-------------------/
			/*ArrayList<TestSequence> testSeqList = new ArrayList<>(); 
			
			System.out.println("###########  C1 ############ ");
			TestSequence testSequence = tsForCC.generateTestSequence(dfts,1.0d);
		    printTestSequence(testSequence);
		    testSeqList.add(testSequence);
		    
		    System.out.println("###########  C2 ############ ");
		    testSeqList = tsFtLiv.identifyMissingCases(dfts, testSeqList, 1.0d);
			printTestSequenceList(testSeqList);
			
			System.out.println("###########  C3 ############ ");
			testSeqList = tsIntCor.identifyMissingCases(dfts, testSeqList, 1.0d);
			printTestSequenceList(testSeqList);
			
			System.out.println("###########  C4 ############ ");
			testSeqList = tsRlLiv.identifyMissingCases(dfts, testSeqList, 1.0d);
			printTestSequenceList(testSeqList);
			
			System.out.println("###########  C5 ############ ");
			testSeqList = tsVtLiv.identifyMissingCases(dfts, testSeqList, 1.0d);
			printTestSequenceList(testSeqList);*/			
	}
	
	public static void printTestSequenceList(TestSequenceList testSeqList){
		System.out.println("\n\n#### \n PRINTING THE TEST LIST \n ###");
		System.out.println();
		for (TestSequence testSequence : testSeqList.getTestSequences()) {
			System.out.println();
			System.out.println();
			System.out.println(" ------ NEW TEST SEQUENCE ---- ");
			printTestSequence(testSequence);
		}
	}
	
	public static void printTestSequence(TestSequence testSequence){
		System.out.println("#### \n PRINTING THE TEST SEQUENCE \n ###");
		for (TestCase tc : testSequence.getTestCases()) {
			System.out.println("---------------------------------------------------");
			System.out.println("Transition ID: "+tc.getIdTransitionDFTS());
			System.out.println("TC ID: "+tc.getId());
			
			System.out.println("Context State:");
			for(HashMap.Entry<String, Boolean> entry : tc.getContextState().entrySet()) {
			    String key = entry.getKey();
			    Boolean value = entry.getValue();
			    System.out.println(key+" : "+ value);
			}
			
			System.out.println("Expected Features Status:");
			for(HashMap.Entry<String, Boolean> entry : tc.getExpectedFeatures().entrySet()) {
			    String key = entry.getKey();
			    Boolean value = entry.getValue();
			    System.out.println(key+" : "+ value);
			}			

		}
	}
	
	public static void saveTestSequenceToJson(TestSequenceList testSequenceList, String absoluteFilePath) {
        //Gson gson = new GsonBuilder().create();
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
        String jsonString = gson.toJson(testSequenceList);
        try {
            FileWriter fileWriter = new FileWriter(absoluteFilePath);
            fileWriter.write(jsonString);
            System.out.println("File saved successfully");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
}
