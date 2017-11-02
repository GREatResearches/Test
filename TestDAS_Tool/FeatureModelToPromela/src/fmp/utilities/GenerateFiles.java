package fmp.utilities;

import java.io.IOException;
import java.io.PrintWriter;

public class GenerateFiles {

	public void generatePMLFile(String pmlFile, String fileName){
		try{
		    PrintWriter writer = new PrintWriter(fileName + ".pml", "UTF-8");
		    //writer.println("The first line");		    
		    writer.println(pmlFile);
		    writer.close();
		} catch (IOException e) {
		   // do something
		}
	}
	
	public void generateTXTFile(String txtFile, String fileName){
		try{
		    PrintWriter writer = new PrintWriter(fileName + ".txt", "UTF-8");
		    //writer.println("The first line");		    
		    writer.println(txtFile);
		    writer.close();
		} catch (IOException e) {
		   // do something
		}
	}
	
}
