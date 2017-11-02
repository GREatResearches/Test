package cfmts.ui.release;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFileChooser;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import fmp.contextModel.AdaptationRuleWithCtxFeature;
import fmp.contextModel.CFM;
import fmp.contextModel.ContextFeature;
import fmp.contextModel.ContextGroup;
import fmp.contextModel.ContextGroupType;
import fmp.contextModel.ContextType;
import fmp.featureModel.DSPL;
import fmp.featureModel.Feature;
import fmp.featureModel.FeatureGroup;
import fmp.featureModel.FeatureGroupType;
import fmp.featureModel.FeatureType;

// class to test the generation of a JSON file with a CAFM
public class TestJSON_CAFM {

	public static void main(String[] args) {
		// Features
		Feature mobileGuide = new Feature();
		mobileGuide.setName("MobileGuide");
		
		Feature documents = new Feature();
		documents.setName("Files");
		documents.setFeatureType(FeatureType.Mandatory);
		documents.setFatherFeature(mobileGuide);
		
		Feature video = new Feature();
		video.setName("Video");
		video.setFeatureType(FeatureType.Optional);
		video.setFatherFeature(documents);
		
		Feature image = new Feature();
		image.setName("Image");
		image.setFeatureType(FeatureType.Optional);
		image.setFatherFeature(documents);
		
		Feature text = new Feature();
		text.setName("Text");
		text.setFeatureType(FeatureType.Mandatory);
		text.setFatherFeature(documents);
	
		Feature showEvents = new Feature();
		showEvents.setName("ShowEvents");
		showEvents.setFeatureType(FeatureType.Mandatory);
		showEvents.setFatherFeature(mobileGuide);
		
		Feature allEvents = new Feature();
		allEvents.setName("allEvents");
		allEvents.setFeatureType(FeatureType.Group);
		allEvents.setFatherFeature(showEvents);
		
		Feature current = new Feature();
		current.setName("current");
		current.setFeatureType(FeatureType.Group);
		current.setFatherFeature(showEvents);
		
		FeatureGroup eventsGroup = new FeatureGroup();
		eventsGroup.append(allEvents);
		eventsGroup.append(current);
		eventsGroup.setGroupType(FeatureGroupType.OR_Group);
		
		//Context Root
		ContextFeature root = new ContextFeature("Root Context");
		
		// Context Propositions
		ContextFeature isBtFull = new ContextFeature("BtFull");
		isBtFull.setContextType(ContextType.Group);
		ContextFeature isBtNormal = new ContextFeature("BtNormal");
		isBtNormal.setContextType(ContextType.Group);
		ContextFeature isBtLow = new ContextFeature("BtLow");
		isBtLow.setContextType(ContextType.Group);
		ContextFeature slowNetwork = new ContextFeature("slowNetwork");
		slowNetwork.setContextType(ContextType.Optional);		
		
		//Context Groups
		ContextGroup battery = new ContextGroup("Baterry");
		battery.setType(ContextGroupType.XOR);
		battery.append(isBtFull);
		battery.append(isBtNormal);
		battery.append(isBtLow);		
		
		ContextGroup network = new ContextGroup("Network"); // To the interleaving testing
		network.setType(ContextGroupType.NONE);
		network.append(slowNetwork);		
		
		// Adaptation Rules
		
		//Adaptation Rule isBtLow => Video off, Image off
		AdaptationRuleWithCtxFeature rule1 = new AdaptationRuleWithCtxFeature();
		LinkedList<ContextFeature> contextTrigger1 = new LinkedList<ContextFeature>();
		contextTrigger1.add(isBtLow);
		rule1.setContextRequired(contextTrigger1);
		
		LinkedList<Feature> toDeactiveFeatures1 = new LinkedList<Feature>();
		toDeactiveFeatures1.add(image);
		toDeactiveFeatures1.add(video);
		rule1.setToDeactiveFeatureList(toDeactiveFeatures1);
			
		//Adaptation Rule isBtNormal => Video off, Image on
		AdaptationRuleWithCtxFeature rule3 = new AdaptationRuleWithCtxFeature();
		LinkedList<ContextFeature> contextTrigger3 = new LinkedList<ContextFeature>();
		contextTrigger3.add(isBtNormal);		
		rule3.setContextRequired(contextTrigger3);
		
		LinkedList<Feature> toActiveFeature3 = new LinkedList<Feature>();
		toActiveFeature3.add(image);
		rule3.setToActiveFeatureList(toActiveFeature3);
		
		LinkedList<Feature> toDeactiveFeatures3 = new LinkedList<Feature>();
		toDeactiveFeatures3.add(video);
		rule3.setToDeactiveFeatureList(toDeactiveFeatures3);
		
		//Adaptation Rule isBtFull => Video on, Image on
		AdaptationRuleWithCtxFeature rule5 = new AdaptationRuleWithCtxFeature();
		LinkedList<ContextFeature> contextTrigger5 = new LinkedList<ContextFeature>();
		contextTrigger5.add(isBtFull);		
		rule5.setContextRequired(contextTrigger5);
		
		LinkedList<Feature> toActiveFeature5 = new LinkedList<Feature>();
		toActiveFeature5.add(image);
		toActiveFeature5.add(video);
		rule5.setToActiveFeatureList(toActiveFeature5);
		
		//Adaptation Rule SlowNetwork => allEvents off
		AdaptationRuleWithCtxFeature rule6 = new AdaptationRuleWithCtxFeature();
		LinkedList<ContextFeature> contextTrigger6 = new LinkedList<ContextFeature>();
		contextTrigger6.add(slowNetwork);		
		rule6.setContextRequired(contextTrigger6);
		
		LinkedList<Feature> toDeactiveFeature6 = new LinkedList<Feature>();
		toDeactiveFeature6.add(allEvents);		
		rule6.setToDeactiveFeatureList(toDeactiveFeature6);
		
		// COMO é um OR, se ele desativou o outro, automaticamente ele ativa esse
		LinkedList<Feature> toActiveFeature6 = new LinkedList<Feature>();
		//toActiveFeature6.add(current);		
		rule6.setToActiveFeatureList(toActiveFeature6);
		
		//Context Model
		CFM ctxModel = new CFM();
		ctxModel.setContextRoot(root);
			// Context Groups and CoOntext Features
		ctxModel.getContextGroups().add(battery); // the context groups contains the context features
		ctxModel.getContextGroups().add(network);
			// Adaptation Rules
		ctxModel.getAdaptationRules().add(rule1);
		ctxModel.getAdaptationRules().add(rule3);
		ctxModel.getAdaptationRules().add(rule5);
		ctxModel.getAdaptationRules().add(rule6);
		
		//DSPL
		DSPL mobilineDspl = new DSPL();
			//DSPL features
		mobilineDspl.getFeatures().add(text);
		mobilineDspl.getFeatures().add(video);
		mobilineDspl.getFeatures().add(image);
		mobilineDspl.getFeatures().add(documents);
		mobilineDspl.getFeatures().add(allEvents);
		mobilineDspl.getFeatures().add(current);
		mobilineDspl.getFeatures().add(showEvents);
		mobilineDspl.getFeatures().add(mobileGuide);	
			//DSPL Initial Configuration - mandatory features and from group features
		mobilineDspl.getInitialConfiguration().add(documents);
		mobilineDspl.getInitialConfiguration().add(text);
		mobilineDspl.getInitialConfiguration().add(image);
		mobilineDspl.getInitialConfiguration().add(showEvents);
		mobilineDspl.getInitialConfiguration().add(current);
		mobilineDspl.getInitialConfiguration().add(mobileGuide);
			//DSPL Feature Groups
		mobilineDspl.getFeatureGroups().add(eventsGroup);
		
		/* File file = null;
		 JFileChooser chooser = new JFileChooser();
		 chooser.setDialogTitle("Save the Test Sequence");
		 int returnValue = chooser.showSaveDialog( null ) ;
		 if(returnValue == JFileChooser.APPROVE_OPTION) {
		        file = chooser.getSelectedFile() ;
		 }
		 if(file != null)
		 {
			 	String filePath = file.getPath();
			 
				CAFM cafmMobiline = new CAFM();
				cafmMobiline.setName("Mobiline");
				cafmMobiline.setDspl(mobilineDspl);
				cafmMobiline.setCfm(ctxModel);
				
				saveCAFMToJson(cafmMobiline,filePath+".json");		
		 }
		 */
		 File fileToOpen = null;
		 JFileChooser chooserToOpen = new JFileChooser();
		 chooserToOpen.setDialogTitle("Save the Test Sequence");
		 int returnValue2 = chooserToOpen.showOpenDialog( null ) ;
		 if(returnValue2 == JFileChooser.OPEN_DIALOG) {
			 fileToOpen = chooserToOpen.getSelectedFile() ;
		 }
		 if(fileToOpen != null)
		 {
			 	String filePath2 = fileToOpen.getPath();
			 	CAFM cafmMobiline = new CAFM();
			 	cafmMobiline.getDspl().print();
			 	cafmMobiline.getCfm().print();
			 	cafmMobiline = getCAFMFromJSON(filePath2);	
			 	cafmMobiline.getDspl().print();
			 	cafmMobiline.getCfm().print();
		 }
	}
	
	public static void saveCAFMToJson(CAFM cafm, String absoluteFilePath) {
        //Gson gson = new GsonBuilder().create();
        Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
        String jsonString = gson.toJson(cafm);
        try {
            FileWriter fileWriter = new FileWriter(absoluteFilePath);
            fileWriter.write(jsonString);
            System.out.println("File saved successfully");
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
	
	public static CAFM getCAFMFromJSON(String absoluteFilePath) {
		CAFM cafm = new CAFM();
		
		Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();		
		
		try {
			cafm = gson.fromJson(new FileReader(absoluteFilePath), CAFM.class);			
        } catch(FileNotFoundException e) {
            System.out.println(e);
        }
		
		return cafm;
	}
}
