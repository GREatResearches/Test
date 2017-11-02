package fmp.example;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import fmp.contextEvolModel.ContextProposition;
import fmp.contextEvolModel.ContextState;
import fmp.contextModel.ContextConstraintCtxModel;
import fmp.utilities.WriterXLSFile;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

public class TestCreatingXLSFile {

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
		ctxSt1.getAtiveContextPropositions().add(isBtNormal);
		
		ctxSt1.setId(1);
		
		// S2
		ContextState ctxSt2 = new ContextState();
		ctxSt2.getAtiveContextPropositions().add(isBtLow);
		
		ctxSt2.setId(2);
				
		// S3
		ContextState ctxSt3 = new ContextState();
		ctxSt3.getAtiveContextPropositions().add(isBtFull);
		ctxSt3.getAtiveContextPropositions().add(hasPwSource);
		
		ctxSt3.setId(3);
		
		// S4
		ContextState ctxSt4 = new ContextState();
		ctxSt4.getAtiveContextPropositions().add(isBtNormal);
		ctxSt4.getAtiveContextPropositions().add(hasPwSource);
		
		ctxSt4.setId(4);
		
		//S5
		ContextState ctxSt5 = new ContextState();
		ctxSt5.getAtiveContextPropositions().add(isBtLow);
		ctxSt5.getAtiveContextPropositions().add(hasPwSource);
		
		ctxSt5.setId(5);
		
		LinkedList<ContextState> ctxStates = new LinkedList<ContextState>();
		ctxStates.add(ctxSt0);
		ctxStates.add(ctxSt1);
		ctxStates.add(ctxSt2);
		ctxStates.add(ctxSt3);
		ctxStates.add(ctxSt4);
		ctxStates.add(ctxSt5);
		
		
		WriterXLSFile writer = new WriterXLSFile();
		try {
			writer.writeContextStatesIntoXLSFile("test.xls",ctxStates);
			
			// Code Block To OPEN a XLS FILE
			File f = new File("test.xls");
			Runtime.getRuntime().exec("cmd /c start "+f.getAbsolutePath()) ;
	          
			
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
			e.printStackTrace();
		}
	}
}
