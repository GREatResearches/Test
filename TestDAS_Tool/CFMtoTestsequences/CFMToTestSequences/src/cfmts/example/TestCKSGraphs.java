package cfmts.example;

import cfmts.graph.cks.Graph_CKS;
import cfmts.graph.cks.Node_CKS;
import fmp.contextEvolModel.CKS;
import fmp.contextEvolModel.ContextConstraint;
import fmp.contextEvolModel.ContextProposition;
import fmp.contextEvolModel.ContextState;

public class TestCKSGraphs {
	
	public static void main(String[] args) {
		//Context Propositions
		ContextProposition isBtFull = new ContextProposition("isBtFull");
		ContextProposition isBtNormal = new ContextProposition("isBtNormal");
		ContextProposition isBtLow = new ContextProposition("isBtLow");
		ContextProposition hasPwSource = new ContextProposition("hasPwSource");
		
		//Context Constraint
		ContextConstraint batteryLevel = new ContextConstraint();
		batteryLevel.getContextPropositionsList().add(isBtFull);
		batteryLevel.getContextPropositionsList().add(isBtNormal);
		batteryLevel.getContextPropositionsList().add(isBtLow);		
		
		//Context States
		// S0
		Node_CKS ctxSt0 = new Node_CKS();
		ctxSt0.getAtiveContextPropositions().add(isBtFull);
		ctxSt0.setId(0);
		
		// S1
		Node_CKS ctxSt1 = new Node_CKS();
		ctxSt1.getAtiveContextPropositions().add(isBtFull);
		ctxSt1.getAtiveContextPropositions().add(hasPwSource);
		ctxSt1.setId(1);
		
		// S2
		Node_CKS ctxSt2 = new Node_CKS();
		ctxSt2.getAtiveContextPropositions().add(isBtNormal);
		ctxSt2.setId(2);
		
		// S3
		Node_CKS ctxSt3 = new Node_CKS();
		ctxSt3.getAtiveContextPropositions().add(isBtNormal);
		ctxSt3.getAtiveContextPropositions().add(hasPwSource);
		ctxSt3.setId(3);
		
		// S4
		Node_CKS ctxSt4 = new Node_CKS();
		ctxSt4.getAtiveContextPropositions().add(isBtLow);
		ctxSt4.setId(4);
		
		//S5
		Node_CKS ctxSt5 = new Node_CKS();
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
		/*CKS cks = new CKS();
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
		
		cks.print();*/
		
		Graph_CKS cksGraph = new Graph_CKS();
		cksGraph.getNodes().add(ctxSt0);
		cksGraph.getNodes().add(ctxSt1);
		cksGraph.getNodes().add(ctxSt2);
		cksGraph.getNodes().add(ctxSt3);
		cksGraph.getNodes().add(ctxSt4);
		cksGraph.getNodes().add(ctxSt5);
		
		//cksGraph.dephtFirstSearch(ctxSt0);
		cksGraph.breadthFirstSearch(ctxSt0);
				
	}
}
