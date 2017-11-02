package fmp.utilities;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Locale;

import fmp.contextEvolModel.ContextState;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.BorderLineStyle;
import jxl.read.biff.BiffException;
import jxl.format.Alignment;
import jxl.format.Border; 
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.biff.RowsExceededException;

public class WriterXLSFile {

	public void writeContextStatesIntoXLSFile(String filename, LinkedList<ContextState> ctxStateList) throws BiffException, IOException, RowsExceededException, WriteException{
		 File file = new File(filename);
        WorkbookSettings wbSettings = new WorkbookSettings();
        wbSettings.setLocale(new Locale("en", "EN"));
        WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);

        workbook.createSheet("ContextEvolution", 0);
        WritableSheet excelSheet = workbook.getSheet(0);
     
        //adding context States
        addLabel(excelSheet, 0, 0, "Id");
        addLabel(excelSheet, 1, 1, "Context State");
        for (ContextState contextState : ctxStateList) {
        	//Ids
        	addNumber(excelSheet, 0, contextState.getId()+2, contextState.getId());
        	addNumber(excelSheet, contextState.getId()+2, 0, contextState.getId());
        	
        	//ContextStates
        	addLabel(excelSheet, 1, contextState.getId()+2, contextState.getAtiveContextPropositionsIntoAString());
        	addLabel(excelSheet, contextState.getId()+2, 1, contextState.getAtiveContextPropositionsIntoAString());
        	
		}
        addBorderAllCells(excelSheet, ctxStateList.size()+2);
        
        workbook.write();
        workbook.close();
        
	}
	
	private void addLabel(WritableSheet sheet, int row, int column, String s)
           throws RowsExceededException, WriteException {
		
		WritableCellFormat cellFormat = new WritableCellFormat();
		cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		cellFormat.setWrap(true);
		
		//Adjusting the cell column width
		int col = column;
		String aux[] = s.split(",");
		int widthInChars = 0;
		for (String token : aux) {
			if(token.length() > widthInChars)
				widthInChars = token.length();			
		}
		widthInChars++;
		sheet.setColumnView(col, widthInChars);
		
		Label label;
	    label = new Label(column, row, s, cellFormat);
	    sheet.addCell(label);
	}
	
	private void addNumber(WritableSheet sheet, int row, int column,
            Integer num) throws WriteException, RowsExceededException {
		WritableCellFormat cellFormat = new WritableCellFormat();
		cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
		cellFormat.setAlignment(Alignment.CENTRE);
	    		
		Number number;
	    number = new Number(column, row, num,cellFormat);
	    sheet.addCell(number);
	}
	
	private void addBorderAllCells(WritableSheet sheet, Integer num) throws WriteException, RowsExceededException {
		
		WritableCellFormat cellFormat = new WritableCellFormat();
		cellFormat.setBorder(Border.ALL, BorderLineStyle.THIN);
	    
		for(int i = 2; i < num; i++){
			for(int j = 2; j < num; j++){
				Label label;
			    label = new Label(i, j, "", cellFormat);
			    sheet.addCell(label);
			}
		}
	}
}
