package br.ufc.great;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;

public final class ControlUtils {
	
	public static TestSequences testSequences;
	
	public static ControlSetup initializeControlSetup() {
		ControlSetup controlSetup = null;
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
		try {
			// TODO - Editar para que o path não seja hardcoded!
            controlSetup = gson.fromJson(new FileReader("C:\\Users\\Erick\\Documents\\control_setup.json"), ControlSetup.class);
        } catch(FileNotFoundException e) {
            System.out.println(e);
        }
		
		return controlSetup;
	}
	
	public static TestSequences getTestSequences() {
		if (testSequences == null) {
			Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
			ControlSetup controlSetup = ControlSetup.getInstance();
			
			try {
				testSequences = gson.fromJson(new FileReader(controlSetup.getTestSequencePath()), TestSequences.class);
				testSequences.initialize();
	        } catch(FileNotFoundException e) {
	            System.out.println(e);
	        }
		}
		return testSequences;
	}
    
    public static void saveTestItemToJson(TestCase testItem, String absoluteFilePath) {
    	Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
        String jsonString = gson.toJson(testItem);
        try {
            FileWriter fileWriter = new FileWriter(absoluteFilePath);
            fileWriter.write(jsonString);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void saveTestSequenceToJson(TestSequences testSequence, String absoluteFilePath) {
    	Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
        String jsonString = gson.toJson(testSequence);
        try {
            FileWriter fileWriter = new FileWriter(absoluteFilePath);
            fileWriter.write(jsonString);
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // TODO - DEBUG
//    public static void printDebugTestItem() {
//    	Gson gson = new GsonBuilder().create();
//    	
//    	TestItem testItem = new TestItem(555);
//    	
//    	Map<String,Boolean> features = new HashMap<String,Boolean>();
//    	features.put("feature1", true);
//    	features.put("feature2", false);
//    	
//    	testItem.setmContextChange(new ContextChange("Context1", "555"));
//    	testItem.setmFeaturesState(features);
//    	
//    	String jsonString = gson.toJson(testItem);
//    	
//    	System.out.println(jsonString);
//    }

}
