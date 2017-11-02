package br.ufc.great;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class TestClass {

    public static void main(String[] args) {
    	FeatureManager feature = new FeatureManager();
    	
    	feature.verifyFeatures();
    	feature.updateFeatures();
    	
    	feature.verifyFeatures();
    	feature.updateFeatures();
    	
    	feature.verifyFeatures();
    	feature.updateFeatures();
    	
    	feature.verifyFeatures();
    	feature.updateFeatures();
    	
    	feature.verifyFeatures();
    	feature.updateFeatures();
    	
    	feature.verifyFeatures();
    	feature.updateFeatures();
    	
//    	debug();
    }
    
    public static void debug() {
////    	ControlSetup controlSetup = new ControlSetup();
////    	controlSetup.setTestSequencePath("sequence_path");
////    	controlSetup.setTestReportPath("report_path");
////    	ArrayList<String> features = new ArrayList();
////    	features.add("1");
////    	features.add("2");
////    	controlSetup.setFeaturesAvaliable(features);
////    	
////    	HashMap<String, HashMap<String, String>> contexts = new HashMap<String, HashMap<String, String>>();
////    	HashMap<String, String> group1 = new HashMap<String, String>();
////    	group1.put("key1", "value1");
////    	group1.put("key2", "value2");
////    	HashMap<String, String> group2 = new HashMap<String, String>();
////    	group2.put("key1", "value1");
////    	group2.put("key2", "value2");
////    	contexts.put("context1", group1);
////    	contexts.put("context2", group2);
////    	
//////     	ContextDomain contextDomain = new ContextDomain(contexts);
////     	
////     	controlSetup.setContextDomain(contexts);
////     	
////     	Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
////     	String json = gson.toJson(controlSetup);
////     	System.out.println(json);
//////     	
////     	ControlSetup controlSetup2;
////		try {
////			controlSetup2 = gson.fromJson(new FileReader("C:\\Users\\Erick\\Documents\\control_setup.json"), ControlSetup.class);
////			System.out.println(controlSetup2.getContextGroupValue("batery", "isBatteryFull"));
////		} catch (JsonSyntaxException | JsonIOException | FileNotFoundException e) {
////			// TODO Auto-generated catch block
////			e.printStackTrace();
////		}
//////     	
//     	
//     	
//     	
//     	
//     	
     	ArrayList<ArrayList<TestCase>> sequences = new ArrayList<ArrayList<TestCase>>();
     	
     	ArrayList<TestCase> testCases = new ArrayList<TestCase>();
     	ArrayList<TestCase> testCases2 = new ArrayList<TestCase>();
     	
     	TestCase testCase1 = new TestCase();
     	TestCase testCase2 = new TestCase();
     	
     	int id = 1;
     	HashMap<String, String> contextState = new HashMap<String, String>();
     	contextState.put("context1", "value1");
     	contextState.put("context2", "value2");
     	HashMap<String, Boolean> expectedFeatureStatus = new HashMap<String, Boolean>();
     	expectedFeatureStatus.put("feature1", true);
     	expectedFeatureStatus.put("feature2", true);
     	HashMap<String, Boolean> actualFeatureStatus = new HashMap<String, Boolean>();
     	actualFeatureStatus.put("feature1", false);
     	actualFeatureStatus.put("feature2", false);
     	
//     	testCase1.setId(id);
//     	testCase1.setContextState(contextState);
//     	testCase1.setExpectedFeatureStatus(expectedFeatureStatus);
//     	testCase1.setActualFeatureStatus(actualFeatureStatus);
//     	
//     	testCase2.setId(2);
//     	testCase2.setContextState(contextState);
//     	testCase2.setExpectedFeatureStatus(expectedFeatureStatus);
//     	testCase2.setActualFeatureStatus(actualFeatureStatus);
     	
     	testCases.add(testCase1);
     	testCases.add(testCase2);
     	
     	testCases2.add(testCase1);
     	testCases2.add(testCase2);
     	
     	sequences.add(testCases);
     	sequences.add(testCases2);
     	
     	TestSequences testSequences = new TestSequences();
//     	testSequences.setCurrentPosTestCase(0);
//     	testSequences.setCurrentPosTestSequence(0);
//     	testSequences.setCurrentTestCase(null);
//     	testSequences.setCurrentTestSequence(null);
//     	testSequences.setFinished(false);
     	testSequences.setTestSequences(sequences);
     		
     	Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
//     	Gson gson = new Gson();
//     	String json = gson.toJson(sequences);
     	String json2 = gson.toJson(testSequences);
     	
//     	System.out.println(json);
     	System.out.println(json2);
     	
////     	TestSequence testSequences2 = 
    }
}