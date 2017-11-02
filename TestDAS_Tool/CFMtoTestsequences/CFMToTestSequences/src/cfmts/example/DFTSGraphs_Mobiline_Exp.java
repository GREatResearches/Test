package cfmts.example;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import cfmts.graph.cks.Graph_CKS;
import cfmts.graph.cks.Node_CKS;
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
import fmp.featureModel.Feature;
import fmp.featureModel.FeatureGroup;
import fmp.featureModel.FeatureGroupType;
import fmp.featureModel.FeatureType;


public class DFTSGraphs_Mobiline_Exp {
	
	public static void main(String[] args) {
		
	}
	
	public void genTestSeq(String path){
		//--------------------------> FEATURE MODEL <-------------------/
		//GETS FROM FIXTURE
		
		// Features
		Feature documents = new Feature();
		documents.setName("Documents");
		documents.setFeatureType(FeatureType.Mandatory);
		
		Feature video = new Feature();
		video.setName("Video");
		video.setFeatureType(FeatureType.Optional);
		video.setFatherFeature(documents);
		
		Feature image = new Feature();
		image.setName("Image");
		image.setFeatureType(FeatureType.Optional);
		image.setFatherFeature(documents);
		
		Feature text = new Feature();
		text.setName("Text");
		text.setFeatureType(FeatureType.Mandatory);
		text.setFatherFeature(documents);
	
		Feature showEvents = new Feature();
		showEvents.setName("ShowEvents");
		showEvents.setFeatureType(FeatureType.Mandatory);		
		
		Feature allEvents = new Feature();
		allEvents.setName("allEvents");
		allEvents.setFeatureType(FeatureType.Group);
		allEvents.setFatherFeature(showEvents);
		
		Feature current = new Feature();
		current.setName("current");
		current.setFeatureType(FeatureType.Group);
		current.setFatherFeature(showEvents);
		
		FeatureGroup eventsGroup = new FeatureGroup();
		eventsGroup.append(allEvents);
		eventsGroup.append(current);
		eventsGroup.setGroupType(FeatureGroupType.OR_Group);
		
		//DSPL
		DSPL mobilineDspl = new DSPL();
			//DSPL features
		mobilineDspl.getFeatures().add(text);
		mobilineDspl.getFeatures().add(video);
		mobilineDspl.getFeatures().add(image);
		mobilineDspl.getFeatures().add(documents);
		mobilineDspl.getFeatures().add(showEvents);
		mobilineDspl.getFeatures().add(current);
		mobilineDspl.getFeatures().add(allEvents);
			
			//DSPL Initial Configuration - mandatory features and from group features
		mobilineDspl.getInitialConfiguration().add(documents);
		mobilineDspl.getInitialConfiguration().add(text);
		mobilineDspl.getInitialConfiguration().add(image);
		mobilineDspl.getInitialConfiguration().add(video);
		mobilineDspl.getInitialConfiguration().add(showEvents);
		mobilineDspl.getInitialConfiguration().add(allEvents);
			//DSPL Feature Groups
		mobilineDspl.getFeatureGroups().add(eventsGroup);		
		
		
		//--------------------------> CONTEXT MODEL <-------------------/
		//GETS FROM FIXTURE
		
		//Context Root
		ContextFeature root = new ContextFeature("Root Context");
		
		// Context Propositions
		ContextFeature isBtFull = new ContextFeature("BtFull");
		isBtFull.setContextType(ContextType.Group);
		ContextFeature isBtNormal = new ContextFeature("BtNormal");
		isBtNormal.setContextType(ContextType.Group);
		ContextFeature isBtLow = new ContextFeature("BtLow");
		isBtLow.setContextType(ContextType.Group);
		ContextFeature slowNetwork = new ContextFeature("slowNetwork");
		slowNetwork.setContextType(ContextType.Optional);		
		
		//Context Groups
		ContextGroup battery = new ContextGroup("Baterry");
		battery.setType(ContextGroupType.XOR);
		battery.append(isBtFull);
		battery.append(isBtNormal);
		battery.append(isBtLow);		
		
		ContextGroup network = new ContextGroup("Network"); // To the interleaving testing
		network.setType(ContextGroupType.NONE);
		network.append(slowNetwork);		
		
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
		
		//Adaptation Rule isBtFull => Video on, Image on
		AdaptationRuleWithCtxFeature rule5 = new AdaptationRuleWithCtxFeature();
		LinkedList<ContextFeature> contextTrigger5 = new LinkedList<ContextFeature>();
		contextTrigger5.add(isBtFull);		
		rule5.setContextRequired(contextTrigger5);
		
		LinkedList<Feature> toActiveFeature5 = new LinkedList<Feature>();
		toActiveFeature5.add(image);
		toActiveFeature5.add(video);
		rule5.setToActiveFeatureList(toActiveFeature5);
		
		//Adaptation Rule SlowNetwork => allEvents off
		AdaptationRuleWithCtxFeature rule6 = new AdaptationRuleWithCtxFeature();
		LinkedList<ContextFeature> contextTrigger6 = new LinkedList<ContextFeature>();
		contextTrigger6.add(slowNetwork);		
		rule6.setContextRequired(contextTrigger6);
		
		LinkedList<Feature> toDeactiveFeature6 = new LinkedList<Feature>();
		toDeactiveFeature6.add(allEvents);		
		rule6.setToDeactiveFeatureList(toDeactiveFeature6);
		
		// COMO é um OR, se ele desativou o outro, automaticamente ele ativa esse
		LinkedList<Feature> toActiveFeature6 = new LinkedList<Feature>();
		toActiveFeature6.add(current);		
		rule6.setToActiveFeatureList(toActiveFeature6);
		
		//Context Model
		CFM ctxModel = new CFM();
		ctxModel.setContextRoot(root);
		ctxModel.getContextGroups().add(battery);
		ctxModel.getContextGroups().add(network);
		ctxModel.getAdaptationRules().add(rule1);
		ctxModel.getAdaptationRules().add(rule3);
		ctxModel.getAdaptationRules().add(rule5);
		ctxModel.getAdaptationRules().add(rule6);
	
		
		//--------------------------> CKS <-------------------/
		//GETS FROM EXCEL
		 
		//Context Propositions
		ContextProposition isBtFullProp = new ContextProposition("BtFull");
		ContextProposition isBtNormalProp = new ContextProposition("BtNormal");
		ContextProposition isBtLowProp = new ContextProposition("BtLow");
		ContextProposition slowNetWork = new ContextProposition("slowNetwork");
		
		//Context Constraint
		ContextConstraint batteryLevel = new ContextConstraint();
		batteryLevel.getContextPropositionsList().add(isBtFullProp);
		batteryLevel.getContextPropositionsList().add(isBtNormalProp);
		batteryLevel.getContextPropositionsList().add(isBtLowProp);
		
		//Context States
		// S0
		Node_CKS ctxSt0 = new Node_CKS();
		ctxSt0.getAtiveContextPropositions().add(isBtFullProp);
		ctxSt0.setId(0);
		
		// S1
		Node_CKS ctxSt1 = new Node_CKS();
		ctxSt1.getAtiveContextPropositions().add(isBtFullProp);
		ctxSt1.getAtiveContextPropositions().add(slowNetWork);
		ctxSt1.setId(1);
		
		// S2
		Node_CKS ctxSt2 = new Node_CKS();
		ctxSt2.getAtiveContextPropositions().add(isBtNormalProp);
		
		ctxSt2.setId(2);
		
		// S3
		Node_CKS ctxSt3 = new Node_CKS();
		ctxSt3.getAtiveContextPropositions().add(isBtNormalProp);
		ctxSt3.getAtiveContextPropositions().add(slowNetWork);
		ctxSt3.setId(3);
		
		// S4
		Node_CKS ctxSt4 = new Node_CKS();
		ctxSt4.getAtiveContextPropositions().add(isBtLowProp);
		ctxSt4.setId(4);
		
		//S5
		Node_CKS ctxSt5 = new Node_CKS();
		ctxSt5.getAtiveContextPropositions().add(isBtLowProp);
		ctxSt5.getAtiveContextPropositions().add(slowNetWork);				
		ctxSt5.setId(5);

		// Transition Relation R (CKS)
		ctxSt0.addNextState(ctxSt1); //F -> F + S 
		ctxSt0.addNextState(ctxSt2); //F -> N
		ctxSt1.addNextState(ctxSt0); //F + S -> F
		ctxSt1.addNextState(ctxSt3); //F + S -> N + S
		ctxSt2.addNextState(ctxSt3); //N -> N + S
		ctxSt2.addNextState(ctxSt4); //N -> L
		ctxSt3.addNextState(ctxSt2); //N + S -> N
		ctxSt3.addNextState(ctxSt5); //N + S -> L + S
		ctxSt4.addNextState(ctxSt5); //L -> Ls + S 
		ctxSt5.addNextState(ctxSt4); //L + S  -> L
			
		Graph_CKS cksGraph = new Graph_CKS();
		cksGraph.getNodes().add(ctxSt0);
		cksGraph.getNodes().add(ctxSt1);
		cksGraph.getNodes().add(ctxSt2);
		cksGraph.getNodes().add(ctxSt3);
		cksGraph.getNodes().add(ctxSt4);
		cksGraph.getNodes().add(ctxSt5);
		
		
		/////// CODE PARA FIX!!!
					//GenerateDFTSGFromCKSG gen = new GenerateDFTSGFromCKSG(mobilineDspl, ctxModel, cksGraph);
					//it start analze by the first context state of CKS
					//gen.dephtFirstSearchGeneratingDFTSTrasitions(cksGraph.getNodes().get(0));
					//gen.printGraphDFTS();
					
					//Graph_DFTS dfts = gen.getGraph_DFTS(); //dfts

		//////--- To the EXP
		DFTS_For_Exp dftsGen = new DFTS_For_Exp();
		Graph_DFTS dfts = dftsGen.getMobilineExpGraph();
		/////-----
		
		//--------------------------> TEST SEQUENCES [C1 ]<-------------------/
		
		
		//[C1]: it generate the testSequence to cover ALL DFTS States
		    TSForConfigurationCorrectness tsForCC = new TSForConfigurationCorrectness();
		//1.0d= 100%
		// from scratch 
		    //TestSequenceList testSeqList = new TestSequenceList();
		    //TestSequence testSequence = tsForCC.generateTestSequence(dfts,0.2d);
		    //testSeqList.add(testSequence);
		    //printTestSequence(testSequence);
		    //System.out.println("###########");
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
			//TestSequenceList testSeqList = tsRlLiv.generateTestSequence(dfts, 1.0d, new TestSequenceList());
			//printTestSequenceList(testSeqList);
	
			//saveTestSequenceToJson(testSeqList, "D:/testSeq1.json");
			
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
			TestSequenceList testSeqList = new TestSequenceList();
			
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
			printTestSequenceList(testSeqList);
			
			saveTestSequenceToJson(testSeqList, path);	
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
