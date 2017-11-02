package fmp.runSpinBackground;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;

public class RunOnPromptDOS {

	//it runs Spin command and return the result
	public String dirSpin;
	
	private String newline = System.getProperty("line.separator");
	
	public RunOnPromptDOS(String dirSpin){
		this.dirSpin = dirSpin;
	}
	
	//it runs a property on the Spin 
	public String runPropertyInSpin(String property, String fileName) throws IOException{
		String result = runSpinCommand("spin -search -ltl "+property+" "+fileName, false);
		return result;
	}
	
	//it return the trail regarding the last execution
	public String getReportLastRun(String fileName) throws IOException{
		String result = runSpinCommand("spin -t -p -l -g -v "+fileName, true);
		return result;
	}
	
	//it runs a command in the Spin
	//boolean complete = true --> return all string, complete = false --> return only the line with the execution result
	public String runSpinCommand(String action, boolean completeResult) throws IOException{
		String[] command = {"CMD", "/C", action};
		String result = "";
		StringBuffer stringAux = new StringBuffer();
	    ProcessBuilder probuilder = new ProcessBuilder( command );
	
	    //You can set up your work directory
	    probuilder.directory(new File(dirSpin));
	    
	    Process process = probuilder.start();
	    
	    //Read out dir output
	    InputStream is = process.getInputStream();
	    InputStreamReader isr = new InputStreamReader(is);
	    BufferedReader br = new BufferedReader(isr);
	    System.out.printf("Running %s is:\n", Arrays.toString(command));
	    String line;
	    if(completeResult){
	    	while ((line = br.readLine()) != null) {
	    		System.out.println(line);
	    		stringAux.append(br.readLine()+newline);
	    	}
	    	result = stringAux.toString();
	    }else{
	    	
	    	while ((line = br.readLine()) != null) {
	    		System.out.println(line);
		        if(line.matches("State-vector \\d+ byte, depth reached \\d+, errors: \\d+"))
		        	result = line;
		    }
	    }
	    
	    //Wait to get exit value
	    try {
	        int exitValue = process.waitFor();
	        //System.out.println("\n\nExit Value is " + exitValue);
	    } catch (InterruptedException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }
	  return result;
	}
}
