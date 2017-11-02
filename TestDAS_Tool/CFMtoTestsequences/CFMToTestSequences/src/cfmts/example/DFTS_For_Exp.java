package cfmts.example;

import java.util.LinkedList;

import cfmts.graph.cks.Node_CKS;
import cfmts.graph.dfts.Graph_DFTS;
import cfmts.graph.dfts.Node_DFTS;
import cfmts.graph.dfts.Transition_DFTS;
import fmp.contextEvolModel.ContextConstraint;
import fmp.contextEvolModel.ContextProposition;
import fmp.featureModel.Feature;
import fmp.featureModel.FeatureType;

//generate the DFTS graphs for the experiments
//TODO
//13.07.21017 Bug on function: GenerateDFTSGFromCKSG gen = new GenerateDFTSGFromCKSG(mobilineDspl, ctxModel, cksGraph);... is
//not generating all transitions
public class DFTS_For_Exp {
	Graph_DFTS mobiline = new Graph_DFTS();
	Graph_DFTS smartHome = new Graph_DFTS();
		
	public Graph_DFTS getMobilineExpGraph(){
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
		
		//Context States
		ContextProposition isBtFullProp = new ContextProposition("BtFull");
		ContextProposition isBtNormalProp = new ContextProposition("BtNormal");
		ContextProposition isBtLowProp = new ContextProposition("BtLow");
		ContextProposition slowNetWork = new ContextProposition("slowNetwork");
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
		
		//----------- DFTS -----------------------------//
		int transitionsId = 0;
		
		Transition_DFTS transition0 = new Transition_DFTS();
		transition0.setId(transitionsId);
		transitionsId++;
		transition0.setCtxState(ctxSt0);
	
		Transition_DFTS transition1 = new Transition_DFTS();
		transition1.setId(transitionsId);
		transitionsId++;
		transition1.setCtxState(ctxSt1);
		
		Transition_DFTS transition2 = new Transition_DFTS();
		transition2.setId(transitionsId);
		transitionsId++;
		transition2.setCtxState(ctxSt0);
		
		Transition_DFTS transition3 = new Transition_DFTS();
		transition3.setId(transitionsId);
		transitionsId++;
		transition3.setCtxState(ctxSt2);
		
		Transition_DFTS transition4 = new Transition_DFTS();
		transition4.setId(transitionsId);
		transitionsId++;
		transition4.setCtxState(ctxSt3);
		
		Transition_DFTS transition5 = new Transition_DFTS();
		transition5.setId(transitionsId);
		transitionsId++;
		transition5.setCtxState(ctxSt3);
		
		Transition_DFTS transition6 = new Transition_DFTS();
		transition6.setId(transitionsId);
		transitionsId++;
		transition6.setCtxState(ctxSt2);
		
		Transition_DFTS transition7 = new Transition_DFTS();
		transition7.setId(transitionsId);
		transitionsId++;
		transition7.setCtxState(ctxSt4);
	
		Transition_DFTS transition8 = new Transition_DFTS();
		transition8.setId(transitionsId);
		transitionsId++;
		transition8.setCtxState(ctxSt5);
		
		Transition_DFTS transition9 = new Transition_DFTS();
		transition9.setId(transitionsId);
		transitionsId++;
		transition9.setCtxState(ctxSt4);
		
		Transition_DFTS transition10 = new Transition_DFTS();
		transition10.setId(transitionsId);
		transitionsId++;
		transition10.setCtxState(ctxSt5);
		
		int idNode = 0;
		Node_DFTS	dftsNode0 = new Node_DFTS();
		dftsNode0.setId(idNode);
		idNode++;
		dftsNode0.setActiveFeatures(new LinkedList<Feature>());	
		
		Node_DFTS	dftsNode1 = new Node_DFTS();
		dftsNode1.setId(idNode);
		idNode++;
		LinkedList<Feature> activeFeatures1 = new LinkedList<Feature>();
		activeFeatures1.add(image);
		activeFeatures1.add(video);		
		dftsNode1.setActiveFeatures(activeFeatures1);	
		
		Node_DFTS	dftsNode2 = new Node_DFTS();
		dftsNode2.setId(idNode);
		idNode++;
		LinkedList<Feature> activeFeatures2 = new LinkedList<Feature>();
		activeFeatures2.add(image);
		activeFeatures2.add(video);
		activeFeatures2.add(current);
		dftsNode2.setActiveFeatures(activeFeatures2);	
		
		Node_DFTS	dftsNode3 = new Node_DFTS();
		dftsNode3.setId(idNode);
		idNode++;
		LinkedList<Feature> activeFeatures3 = new LinkedList<Feature>();
		activeFeatures3.add(image);
		dftsNode3.setActiveFeatures(activeFeatures3);
		
		Node_DFTS	dftsNode4 = new Node_DFTS();
		dftsNode4.setId(idNode);
		idNode++;
		LinkedList<Feature> activeFeatures4 = new LinkedList<Feature>();
		activeFeatures4.add(image);
		activeFeatures4.add(current);
		dftsNode4.setActiveFeatures(activeFeatures4);
		
		Node_DFTS	dftsNode5 = new Node_DFTS();
		dftsNode5.setId(idNode);
		idNode++;
		LinkedList<Feature> activeFeatures5 = new LinkedList<Feature>();
		dftsNode5.setActiveFeatures(activeFeatures5);
		
		Node_DFTS	dftsNode6 = new Node_DFTS();
		dftsNode6.setId(idNode);
		idNode++;
		LinkedList<Feature> activeFeatures6 = new LinkedList<Feature>();
		activeFeatures6.add(current);
		dftsNode6.setActiveFeatures(activeFeatures6);
		
		transition0.setNewState(dftsNode1);
		transition1.setNewState(dftsNode2);
		transition2.setNewState(dftsNode1);
		transition3.setNewState(dftsNode3);
		transition4.setNewState(dftsNode4);
		transition5.setNewState(dftsNode4);
		transition6.setNewState(dftsNode3);
		transition7.setNewState(dftsNode5);
		transition8.setNewState(dftsNode6);
		transition9.setNewState(dftsNode5);
		transition10.setNewState(dftsNode6);
		
		dftsNode0.getTransitions().add(transition0);
		
		dftsNode1.getTransitions().add(transition1);
		dftsNode1.getTransitions().add(transition3);
		
		dftsNode2.getTransitions().add(transition2);
		dftsNode2.getTransitions().add(transition4);
		
		dftsNode3.getTransitions().add(transition5);
		dftsNode3.getTransitions().add(transition7);
		
		dftsNode4.getTransitions().add(transition6);
		dftsNode4.getTransitions().add(transition8);
		
		dftsNode5.getTransitions().add(transition9);
				
		dftsNode6.getTransitions().add(transition10);
		
		mobiline.getNodes().add(dftsNode0);
		mobiline.getNodes().add(dftsNode1);
		mobiline.getNodes().add(dftsNode2);
		mobiline.getNodes().add(dftsNode3);
		mobiline.getNodes().add(dftsNode4);
		mobiline.getNodes().add(dftsNode5);
		mobiline.getNodes().add(dftsNode6);
		
		printGraphDFTS(mobiline);
		
		return mobiline;
	}
	
	public Graph_DFTS getSmartHomeExpGraph(){
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
		
		//-------------------CKS
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
		
		//------------------DFTS --------------------------
		int transitionsId = 0;
		
		Transition_DFTS transition0 = new Transition_DFTS();
		transition0.setId(transitionsId);
		transitionsId++;
		transition0.setCtxState(ctxSt0);
		
		Transition_DFTS transition1 = new Transition_DFTS();
		transition1.setId(transitionsId);
		transitionsId++;
		transition1.setCtxState(ctxSt1);
		
		Transition_DFTS transition2 = new Transition_DFTS();
		transition2.setId(transitionsId);
		transitionsId++;
		transition2.setCtxState(ctxSt0);
		
		Transition_DFTS transition3 = new Transition_DFTS();
		transition3.setId(transitionsId);
		transitionsId++;
		transition3.setCtxState(ctxSt2);
		
		Transition_DFTS transition4 = new Transition_DFTS();
		transition4.setId(transitionsId);
		transitionsId++;
		transition4.setCtxState(ctxSt0);
		
		Transition_DFTS transition5 = new Transition_DFTS();
		transition5.setId(transitionsId);
		transitionsId++;
		transition5.setCtxState(ctxSt3);
		
		Transition_DFTS transition6 = new Transition_DFTS();
		transition6.setId(transitionsId);
		transitionsId++;
		transition6.setCtxState(ctxSt1);
		
		Transition_DFTS transition7 = new Transition_DFTS();
		transition7.setId(transitionsId);
		transitionsId++;
		transition7.setCtxState(ctxSt3);
		
		Transition_DFTS transition8 = new Transition_DFTS();
		transition8.setId(transitionsId);
		transitionsId++;
		transition8.setCtxState(ctxSt2);
		
		Transition_DFTS transition9 = new Transition_DFTS();
		transition9.setId(transitionsId);
		transitionsId++;
		transition9.setCtxState(ctxSt4);
		
		Transition_DFTS transition10 = new Transition_DFTS();
		transition10.setId(transitionsId);
		transitionsId++;
		transition10.setCtxState(ctxSt2);
		
		Transition_DFTS transition11 = new Transition_DFTS();
		transition11.setId(transitionsId);
		transitionsId++;
		transition11.setCtxState(ctxSt5);
		
		Transition_DFTS transition12 = new Transition_DFTS();
		transition12.setId(transitionsId);
		transitionsId++;
		transition12.setCtxState(ctxSt3);
		
		Transition_DFTS transition13 = new Transition_DFTS();
		transition13.setId(transitionsId);
		transitionsId++;
		transition13.setCtxState(ctxSt5);
		
		Transition_DFTS transition14 = new Transition_DFTS();
		transition14.setId(transitionsId);
		transitionsId++;
		transition14.setCtxState(ctxSt4);
		
		int idNode = 0;
		Node_DFTS	dftsNode0 = new Node_DFTS();
		dftsNode0.setId(idNode);
		idNode++;
		dftsNode0.setActiveFeatures(new LinkedList<Feature>());	
		
		Node_DFTS	dftsNode1 = new Node_DFTS();
		dftsNode1.setId(idNode);
		idNode++;
		LinkedList<Feature> activeFeatures1 = new LinkedList<Feature>();
		activeFeatures1.add(callPolice);
		activeFeatures1.add(presenceIlusion);
		activeFeatures1.add(alarm);
		activeFeatures1.add(fromWindows);
		dftsNode1.setActiveFeatures(activeFeatures1);	
		
		Node_DFTS	dftsNode2 = new Node_DFTS();
		dftsNode2.setId(idNode);
		idNode++;
		LinkedList<Feature> activeFeatures2 = new LinkedList<Feature>();
		activeFeatures2.add(callPolice);
		activeFeatures2.add(presenceIlusion);
		activeFeatures2.add(alarm);
		activeFeatures2.add(fromAir);
		dftsNode2.setActiveFeatures(activeFeatures2);
		
		Node_DFTS	dftsNode3 = new Node_DFTS();
		dftsNode3.setId(idNode);
		idNode++;
		LinkedList<Feature> activeFeatures3 = new LinkedList<Feature>();
		activeFeatures3.add(presenceIlusion);		
		activeFeatures3.add(fromWindows);
		dftsNode3.setActiveFeatures(activeFeatures3);
		
		Node_DFTS	dftsNode4 = new Node_DFTS();
		dftsNode4.setId(idNode);
		idNode++;
		LinkedList<Feature> activeFeatures4 = new LinkedList<Feature>();
		activeFeatures4.add(presenceIlusion);
		activeFeatures4.add(fromAir);
		dftsNode4.setActiveFeatures(activeFeatures4);
		
		Node_DFTS	dftsNode5 = new Node_DFTS();
		dftsNode5.setId(idNode);
		idNode++;
		LinkedList<Feature> activeFeatures5 = new LinkedList<Feature>();
		activeFeatures5.add(fromWindows);
		dftsNode5.setActiveFeatures(activeFeatures5);
		
		Node_DFTS	dftsNode6 = new Node_DFTS();
		dftsNode6.setId(idNode);
		idNode++;
		LinkedList<Feature> activeFeatures6 = new LinkedList<Feature>();
		activeFeatures6.add(fromAir);
		dftsNode6.setActiveFeatures(activeFeatures6);
		
		
		transition0.setNewState(dftsNode1);
		transition1.setNewState(dftsNode2);
		transition2.setNewState(dftsNode1);
		transition3.setNewState(dftsNode3);
		transition4.setNewState(dftsNode1);
		transition5.setNewState(dftsNode4);
		transition6.setNewState(dftsNode2);
		transition7.setNewState(dftsNode4);
		transition8.setNewState(dftsNode3);
		transition9.setNewState(dftsNode5);
		transition10.setNewState(dftsNode3);
		transition11.setNewState(dftsNode6);
		transition12.setNewState(dftsNode4);
		transition13.setNewState(dftsNode6);
		transition14.setNewState(dftsNode5);
		
		dftsNode0.getTransitions().add(transition0);
		
		dftsNode1.getTransitions().add(transition1);
		dftsNode1.getTransitions().add(transition3);
		
		dftsNode2.getTransitions().add(transition2);
		dftsNode2.getTransitions().add(transition5);
		
		dftsNode3.getTransitions().add(transition4);
		dftsNode3.getTransitions().add(transition7);
		dftsNode3.getTransitions().add(transition9);
		
		dftsNode4.getTransitions().add(transition6);
		dftsNode4.getTransitions().add(transition8);
		dftsNode4.getTransitions().add(transition11);
		
		dftsNode5.getTransitions().add(transition10);
		dftsNode5.getTransitions().add(transition13);
				
		dftsNode6.getTransitions().add(transition12);
		dftsNode6.getTransitions().add(transition14);
		
		smartHome.getNodes().add(dftsNode0);
		smartHome.getNodes().add(dftsNode1);
		smartHome.getNodes().add(dftsNode2);
		smartHome.getNodes().add(dftsNode3);
		smartHome.getNodes().add(dftsNode4);
		smartHome.getNodes().add(dftsNode5);
		smartHome.getNodes().add(dftsNode6);
		
		printGraphDFTS(smartHome);
		
		return smartHome;

	}
	
	public void printGraphDFTS(Graph_DFTS dftsG){
		System.out.println("PRINTING THE GRAPH");
		for (Node_DFTS node : dftsG.getNodes()) {
			System.out.println("---------------------");
			System.out.println("Node ID: "+node.getId());
			System.out.print("Active features: [");
			for (Feature feature : node.getActiveFeatures()) {
				System.out.print(" "+ feature.getName());
			}
			System.out.print("]");
			System.out.println("\n");
			if(node.getTransitions().size() > 0)
				System.out.println("Transitions:");
			for (Transition_DFTS transition : node.getTransitions()) {
				System.out.println("Transition ID: "+transition.getId());
				System.out.println("Context Trigger: "+transition.getCtxState().getAtiveContextPropositionsIntoAString());
				System.out.println("Next System State ID: "+transition.getNewState().getId());
				System.out.println("");
			}	
			
		}
	}
}
