package fmp.utilities;

import java.io.File;
import java.io.IOException;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class ReaderXLSFile {

	public void readXLSFile(String filename) throws BiffException, IOException{
		Workbook workbook = Workbook.getWorkbook(new File(filename));

		Sheet sheet = workbook.getSheet(0);
     
		int rows = sheet.getRows();
		
		int columns = sheet.getColumns();
		System.out.println(columns);
		
		System.out.println("Iniciando a leitura da planilha XLS:");

		for(int i = 0; i < rows; i++){
			for(int j = 0; j < columns; j++){
				Cell a1 = sheet.getCell(j, i);
				String as1 = a1.getContents();
				System.out.println("Linha "+(i+1)+" Coluna "+(j+1)+": " + as1);
			}
			
		}

		workbook.close();
	}
	
	
}




 

