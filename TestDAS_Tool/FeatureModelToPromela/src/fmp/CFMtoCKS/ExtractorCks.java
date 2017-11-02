package fmp.CFMtoCKS;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.text.StyledEditorKit.ForegroundAction;

import fmp.contextEvolModel.AdaptationRuleWithCtxProposition;
import fmp.contextEvolModel.CKS;
import fmp.contextEvolModel.ContextProposition;
import fmp.contextEvolModel.ContextState;
import fmp.contextModel.AdaptationRuleWithCtxFeature;
import fmp.contextModel.CFM;
import fmp.contextModel.ContextFeature;
import fmp.contextModel.ContextGroup;
import fmp.contextModel.ContextGroupType;
import fmp.utilities.WriterXLSFile;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

// it extracts a C-KS from a XLS file
public class ExtractorCks {
	
	public void  getContextChangesFromXLS(String xlsFileName, LinkedList<ContextState> contextStates){
		try {
			Workbook workbook = Workbook.getWorkbook(new File(xlsFileName));

			Sheet sheet = workbook.getSheet(0);
	     
			int rows = sheet.getRows();
			int columns = sheet.getColumns();
			
			//it starts from 2 to avoid 'id' cell and 'ContextState' cell
			//it gets the context changes
			for(int i = 2; i < rows; i++){
				for(int j = 2; j < columns; j++){
					Cell a1 = sheet.getCell(j, i);
					String as1 = a1.getContents();
					if(as1.equals("x")){
						contextStates.get(i-2).addNextState(contextStates.get(j-2));
					}
				}				
			}
			workbook.close();			
		} catch (BiffException  e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}catch (IOException e) {
			System.out.println("ERROR in the file reading: "+e.getMessage());
		}
	}
	
	// with it needs to extract the context states from the xls
	/*public ContextState getContextPropositions(String contextStateString){
		ContextState ctxState = new ContextState();
		String[] tokens = contextStateString.split(",");
		for (String contextPropositionName : tokens) {
			ContextProposition ctxProp = new ContextProposition(contextPropositionName);
			ctxState.getAtiveContextPropositions().add(ctxProp);
		}
		return ctxState;		
	}*/
	
	//it generates the cks from a CFM
	public CKS getContextKripkeStructureFromContextModel(CFM cfm, String fileName){
		CKS cks = new CKS();
		//C-KS [S, I, C, L, ->]
		
		//set C
		for (ContextGroup contextGroup : cfm.getContextGroups()) {
			for (ContextFeature ctxFeature : contextGroup.getContextFeatureList()) {
				ContextProposition ctxProposition = new ContextProposition(ctxFeature.getName());
				cks.getContextPropositions().add(ctxProposition);
			}			
		}
		
		//set S
		//optional Context Feature Group
		//XOR Context Feature Group
		//OR Context Feature Group
		CtxStateGenerator gen = new CtxStateGenerator();
		//creating an empty ctxState list
		LinkedList<ContextState> ctxStateList = new LinkedList<ContextState>();
		//it identifies the contextStates
		for (ContextGroup contextGroup : cfm.getContextGroups()) {
			gen.generateContexStateFromContextGroup(contextGroup, ctxStateList);	
		}
		cks.setContextStates(ctxStateList);
				
		//initial I: the initial context state
		//By DEFAULT it is taken here the first context state as the initial context state
		if(ctxStateList.size() > 0)
			cks.setInitialState(ctxStateList.getFirst());
		
		//it gets the adaptation Rules from the Context Model
		for (AdaptationRuleWithCtxFeature ruleCtxFeature : cfm.getAdaptationRules()) {
			AdaptationRuleWithCtxProposition ruleCtxProposition = new AdaptationRuleWithCtxProposition();
			for (ContextFeature contextFeature : ruleCtxFeature.getContextRequired()) {
				int indexCtxProp = cks.getIndexCtxProp(contextFeature.getName());
				if(indexCtxProp != -1){
					ruleCtxProposition.getContextRequired().add(cks.getContextPropositions().get(indexCtxProp));
				}
			}
			ruleCtxProposition.setToDeactiveFeatureList(ruleCtxFeature.getToDeactiveFeatureList());
			ruleCtxProposition.setToActiveFeatureList(ruleCtxFeature.getToActiveFeatureList());
			cks.getAdaptationRules().add(ruleCtxProposition);
			
		}		
		
		//->
		//it Generates the excel file
		WriterXLSFile writer = new WriterXLSFile();
		try {
			writer.writeContextStatesIntoXLSFile(fileName,ctxStateList);
		} catch (RowsExceededException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BiffException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (WriteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return cks;
	}
	
	

}
