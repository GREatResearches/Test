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

public class DFTSGraphs_SmartHome_Exp {
	
	public static void main(String[] args) {
		
	}
	
	public void genTestSeq(String path){
		//--------------------------> FEATURE MODEL <-------------------/
				//GETS FROM FIXTURE
				
				// Features
				Feature security = new Feature();
				security.setName("Security");
				security.setFeatureType(FeatureType.Mandatory);
				
				Feature callPolice = new Feature();
				callPolice.setName("CallPolice");
				callPolice.setFeatureType(FeatureType.Optional);
				callPolice.setFatherFeature(security);
				
				Feature presenceIlusion = new Feature();
				presenceIlusion.setName("PresenceIlusion");
				presenceIlusion.setFeatureType(FeatureType.Optional);
				presenceIlusion.setFatherFeature(security);
				
				Feature alarm = new Feature();
				alarm.setName("Alarm");
				alarm.setFeatureType(FeatureType.Optional);
				alarm.setFatherFeature(security);
			
				Feature temperatureControl = new Feature();
				temperatureControl.setName("TemperatureControl");
				temperatureControl.setFeatureType(FeatureType.Mandatory);		
				
				Feature fromAir = new Feature();
				fromAir.setName("FromAir");
				fromAir.setFeatureType(FeatureType.Group);
				fromAir.setFatherFeature(temperatureControl);
				
				Feature fromWindows = new Feature();
				fromWindows.setName("FromWindows");
				fromWindows.setFeatureType(FeatureType.Group);
				fromWindows.setFatherFeature(temperatureControl);
				
				FeatureGroup temperatureGroup = new FeatureGroup();
				temperatureGroup.append(fromAir);
				temperatureGroup.append(fromWindows);
				temperatureGroup.setGroupType(FeatureGroupType.OR_Group);
				
				//DSPL
				DSPL smartHomeDspl = new DSPL();
					//DSPL features
				smartHomeDspl.getFeatures().add(callPolice);
				smartHomeDspl.getFeatures().add(presenceIlusion);
				smartHomeDspl.getFeatures().add(alarm);
				smartHomeDspl.getFeatures().add(security);
				smartHomeDspl.getFeatures().add(temperatureControl);
				smartHomeDspl.getFeatures().add(fromAir);
				smartHomeDspl.getFeatures().add(fromWindows);
					
					//DSPL Initial Configuration - mandatory features and from group features
				smartHomeDspl.getInitialConfiguration().add(security);
				smartHomeDspl.getInitialConfiguration().add(callPolice);		
				smartHomeDspl.getInitialConfiguration().add(temperatureControl);
				smartHomeDspl.getInitialConfiguration().add(fromAir);
					//DSPL Feature Groups
				smartHomeDspl.getFeatureGroups().add(temperatureGroup);		
				
				
				//--------------------------> CONTEXT MODEL <-------------------/
				//GETS FROM FIXTURE
				
				//Context Root
				ContextFeature root = new ContextFeature("Root Context");
				
				// Context Propositions
				ContextFeature threat = new ContextFeature("Threat");
				threat.setContextType(ContextType.Optional);
				ContextFeature attempt = new ContextFeature("Attempt");
				attempt.setContextType(ContextType.Optional);
				ContextFeature highPower = new ContextFeature("HighPowerConsumption");
				highPower.setContextType(ContextType.Group);
				ContextFeature lowPower = new ContextFeature("LowPowerConsumption");
				lowPower.setContextType(ContextType.Group);		
				
				//Context Groups
				ContextGroup PowerConsumption = new ContextGroup("PowerConsumption");
				PowerConsumption.setType(ContextGroupType.XOR);
				PowerConsumption.append(lowPower);
				PowerConsumption.append(highPower);		
				
				ContextGroup robbery = new ContextGroup("Robbery"); // To the interleaving testing
				robbery.setType(ContextGroupType.NONE);
				robbery.append(threat);
				robbery.append(attempt);
				
				// Adaptation Rules
				
				//Adaptation Rule Threat => PresenceIllustion ON
				AdaptationRuleWithCtxFeature rule1 = new AdaptationRuleWithCtxFeature();
				LinkedList<ContextFeature> contextTrigger1 = new LinkedList<ContextFeature>();
				contextTrigger1.add(threat);
				rule1.setContextRequired(contextTrigger1);
				
				LinkedList<Feature> toActiveFeatures1 = new LinkedList<Feature>();
				toActiveFeatures1.add(presenceIlusion);		
				rule1.setToDeactiveFeatureList(toActiveFeatures1);
					
				//Adaptation Rule Attempt => CallPolice on, FromWindows off, Alarm on
				AdaptationRuleWithCtxFeature rule3 = new AdaptationRuleWithCtxFeature();
				LinkedList<ContextFeature> contextTrigger3 = new LinkedList<ContextFeature>();
				contextTrigger3.add(attempt);		
				rule3.setContextRequired(contextTrigger3);
				
				LinkedList<Feature> toActiveFeature3 = new LinkedList<Feature>();
				toActiveFeature3.add(callPolice);
				toActiveFeature3.add(alarm);
				rule3.setToActiveFeatureList(toActiveFeature3);
				
				//Adaptation Rule High Power => FromWindonws on, FromAir off
				AdaptationRuleWithCtxFeature rule5 = new AdaptationRuleWithCtxFeature();
				LinkedList<ContextFeature> contextTrigger5 = new LinkedList<ContextFeature>();
				contextTrigger5.add(highPower);		
				rule5.setContextRequired(contextTrigger5);
				
				LinkedList<Feature> toActiveFeature5 = new LinkedList<Feature>();
				toActiveFeature5.add(fromWindows);		
				rule5.setToActiveFeatureList(toActiveFeature5);
				
				LinkedList<Feature> toDeactiveFeature5 = new LinkedList<Feature>();
				toDeactiveFeature5.add(fromAir);		
				rule5.setToDeactiveFeatureList(toDeactiveFeature5);
				
				//Adaptation Rule LowPower => FromAir on
				AdaptationRuleWithCtxFeature rule6 = new AdaptationRuleWithCtxFeature();
				LinkedList<ContextFeature> contextTrigger6 = new LinkedList<ContextFeature>();
				contextTrigger6.add(lowPower);		
				rule6.setContextRequired(contextTrigger6);
				
				LinkedList<Feature> toActiveFeature6 = new LinkedList<Feature>();
				toActiveFeature6.add(fromAir);		
				rule6.setToActiveFeatureList(toActiveFeature6);
				
				//Context Model
				CFM ctxModel = new CFM();
				ctxModel.setContextRoot(root);
				ctxModel.getContextGroups().add(robbery);
				ctxModel.getContextGroups().add(PowerConsumption);
				ctxModel.getAdaptationRules().add(rule1);
				ctxModel.getAdaptationRules().add(rule3);
				ctxModel.getAdaptationRules().add(rule5);
				ctxModel.getAdaptationRules().add(rule6);
				
				//--------------------------> CKS <-------------------/
				//GETS FROM EXCEL
				
				//Context Propositions
				ContextProposition threatProp = new ContextProposition("Threat");
				ContextProposition attemptProp = new ContextProposition("Attempt");
				ContextProposition highPowerProp = new ContextProposition("HighPowerConsumption");
				ContextProposition lowPowerProp = new ContextProposition("LowPowerConsumption");
				
				//Context Constraint
				ContextConstraint powerConsumption = new ContextConstraint();
				powerConsumption.getContextPropositionsList().add(highPowerProp);
				powerConsumption.getContextPropositionsList().add(lowPowerProp);
				
				//Context States
				// S0
				Node_CKS ctxSt0 = new Node_CKS();
				ctxSt0.getAtiveContextPropositions().add(threatProp);
				ctxSt0.getAtiveContextPropositions().add(attemptProp);
				ctxSt0.getAtiveContextPropositions().add(highPowerProp);
				ctxSt0.setId(0);
				
				// S1
				Node_CKS ctxSt1 = new Node_CKS();
				ctxSt1.getAtiveContextPropositions().add(threatProp);
				ctxSt1.getAtiveContextPropositions().add(attemptProp);
				ctxSt1.getAtiveContextPropositions().add(lowPowerProp);
				ctxSt1.setId(1);
				
				// S2
				Node_CKS ctxSt2 = new Node_CKS();
				ctxSt2.getAtiveContextPropositions().add(threatProp);
				ctxSt2.getAtiveContextPropositions().add(highPowerProp);		
				ctxSt2.setId(2);
				
				// S3
				Node_CKS ctxSt3 = new Node_CKS();
				ctxSt3.getAtiveContextPropositions().add(threatProp);
				ctxSt3.getAtiveContextPropositions().add(lowPowerProp);
				ctxSt3.setId(3);
				
				// S4
				Node_CKS ctxSt4 = new Node_CKS();
				ctxSt4.getAtiveContextPropositions().add(highPowerProp);
				ctxSt4.setId(4);
				
				//S5
				Node_CKS ctxSt5 = new Node_CKS();
				ctxSt5.getAtiveContextPropositions().add(lowPowerProp);						
				ctxSt5.setId(5);

				// Transition Relation R (CKS)
				ctxSt0.addNextState(ctxSt1); //TAH -> TAL
				ctxSt0.addNextState(ctxSt2); //TAH -> TN
				ctxSt1.addNextState(ctxSt0); //TAL -> TAH
				ctxSt1.addNextState(ctxSt3); //TAL -> TL
				ctxSt2.addNextState(ctxSt0); //TH -> TAH
				ctxSt2.addNextState(ctxSt3); //TH -> TL
				ctxSt2.addNextState(ctxSt4); //TH -> H
				ctxSt3.addNextState(ctxSt1); //TL -> TAL
				ctxSt3.addNextState(ctxSt2); //TL -> TH
				ctxSt3.addNextState(ctxSt5); //TL -> L
				ctxSt4.addNextState(ctxSt2); //H -> TH
				ctxSt4.addNextState(ctxSt5); //H -> L
				ctxSt5.addNextState(ctxSt4); //L -> H
				ctxSt5.addNextState(ctxSt3); //L -> TL
					
				Graph_CKS cksGraph = new Graph_CKS();
				cksGraph.getNodes().add(ctxSt0);
				cksGraph.getNodes().add(ctxSt1);
				cksGraph.getNodes().add(ctxSt2);
				cksGraph.getNodes().add(ctxSt3);
				cksGraph.getNodes().add(ctxSt4);
				cksGraph.getNodes().add(ctxSt5);
				
							////// CODE PARA FIX!!!
								//GenerateDFTSGFromCKSG gen = new GenerateDFTSGFromCKSG(smartHomeDspl, ctxModel, cksGraph);
								//it start analze by the first context state of CKS
								//gen.dephtFirstSearchGeneratingDFTSTrasitions(cksGraph.getNodes().get(0));
								//gen.printGraphDFTS();
				
								//Graph_DFTS dfts = gen.getGraph_DFTS(); //dfts
				//////--- To the EXP
				DFTS_For_Exp dftsGen = new DFTS_For_Exp();
				Graph_DFTS dfts = dftsGen.getSmartHomeExpGraph();	
				//////
				
				
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
					TSForFeatureLiveness tsFtLiv = new TSForFeatureLiveness(smartHomeDspl);
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
					TSForVariationLiveness tsVtLiv = new TSForVariationLiveness(smartHomeDspl);
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
