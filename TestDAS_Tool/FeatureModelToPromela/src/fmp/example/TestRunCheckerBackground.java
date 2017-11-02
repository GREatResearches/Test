package fmp.example;

import java.awt.EventQueue;
import java.io.BufferedReader;
import java.io.IOException;

import javax.swing.JFrame;

import fmp.runSpin.ui.RunSpinChecker;
import fmp.runSpinBackground.RunOnPromptDOS;

public class TestRunCheckerBackground {

	private JFrame frame;
	
	public static void main(String[] args) {
		//./spin -search -l -a -ltl pro21 MobileGuideDSPL_10_com_Messages_testando_Property_3_CameraReady.pml
		
		
		RunOnPromptDOS runCommand = new RunOnPromptDOS("C:\\Spin");
		try {
			String result = runCommand.runSpinCommand("spin -search -ltl pro41 MobileGuideDSPL_10_com_Messages_testando_Property_3_CameraReady.pml",false);
			
			String numberOfErros = result.substring(result.indexOf(":")+1, result.length());
			System.out.println("ERROS: "+numberOfErros);
			System.out.println();
			System.out.println(result);
			
			//System.out.println("TRAIL!!!");
			// it analyses the trail
			//String result1 = runCommand.runSpinCommand("spin -t -p -l -g -v MobileGuideDSPL_10_com_Messages_testando_Property_3_CameraReady.pml",true);
			//System.out.println(result1);
			
			
		} catch (IOException e) {
			System.out.println("ERROR");
			e.printStackTrace();
		}
		
	}
	
}
