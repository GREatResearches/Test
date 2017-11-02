package fmp.example;

import java.util.LinkedList;

import fmp.CFMtoCKS.ExtractorCks;
import fmp.contextEvolModel.CKS;
import fmp.contextModel.AdaptationRuleWithCtxFeature;
import fmp.contextModel.CFM;
import fmp.contextModel.ContextFeature;
import fmp.contextModel.ContextGroup;
import fmp.contextModel.ContextGroupType;
import fmp.contextModel.ContextType;
import fmp.featureModel.Feature;
import fmp.featureModel.FeatureGroup;
import fmp.featureModel.FeatureGroupType;
import fmp.featureModel.FeatureType;

public class GenerateXLS_For_Exp {

	public static void main(String[] args) {
		//genXLSMobiline();
		genXLSSmartHome("D:/testHome.xls");
		//genXMLPhone_Training("D:/test.xls");
	}
	
	public static CKS genXMLPhone_Training(String path){
		Feature application = new Feature();
		application.setName("Application");
		application.setFeatureType(FeatureType.Mandatory);
		
		Feature navigation = new Feature();
		navigation.setName("Navigation");
		navigation.setFeatureType(FeatureType.Group);
		navigation.setFatherFeature(application);
		
		Feature game = new Feature();
		game.setName("Game");
		game.setFeatureType(FeatureType.Group);
		game.setFatherFeature(application);
		
		Feature phoneCall = new Feature();
		phoneCall.setName("PhoneCall");
		phoneCall.setFeatureType(FeatureType.Group);
		phoneCall.setFatherFeature(application);
		
		FeatureGroup appGroup = new FeatureGroup();
		appGroup.append(navigation);
		appGroup.append(game);
		appGroup.append(phoneCall);
		appGroup.setGroupType(FeatureGroupType.Alternative_Group);
	
		Feature sensor = new Feature();
		sensor.setName("Sensor");
		sensor.setFeatureType(FeatureType.Mandatory);		
		
		Feature gps = new Feature();
		gps.setName("GPS");
		gps.setFeatureType(FeatureType.Group);
		gps.setFatherFeature(sensor);
		
		Feature gyroscope = new Feature();
		gyroscope.setName("Gyroscope");
		gyroscope.setFeatureType(FeatureType.Group);
		gyroscope.setFatherFeature(sensor);
		
		FeatureGroup sensorGroup = new FeatureGroup();
		sensorGroup.append(gps);
		sensorGroup.append(gyroscope);
		sensorGroup.setGroupType(FeatureGroupType.OR_Group);
		
		//Context Root
		ContextFeature root = new ContextFeature("Root Context");
		
		// Context Propositions
		ContextFeature office = new ContextFeature("Office");
		office.setContextType(ContextType.Group);
		ContextFeature street = new ContextFeature("Street");
		street.setContextType(ContextType.Group);
		ContextFeature home = new ContextFeature("Home");
		home.setContextType(ContextType.Group);		
		
		//Context Groups
		ContextGroup place = new ContextGroup("Place");
		place.setType(ContextGroupType.XOR);
		place.append(office);
		place.append(street);
		place.append(home);				
		
		// Adaptation Rules
		
		//Adaptation Rule isBtLow => Video off, Image off
		AdaptationRuleWithCtxFeature rule1 = new AdaptationRuleWithCtxFeature();
		LinkedList<ContextFeature> contextTrigger1 = new LinkedList<ContextFeature>();
		contextTrigger1.add(office);
		rule1.setContextRequired(contextTrigger1);
		
		LinkedList<Feature> toDeactiveFeatures1 = new LinkedList<Feature>();
		toDeactiveFeatures1.add(game);
		rule1.setToDeactiveFeatureList(toDeactiveFeatures1);
		
		LinkedList<Feature> toActiveFeatures1 = new LinkedList<Feature>();
		toActiveFeatures1.add(phoneCall);
		rule1.setToDeactiveFeatureList(toActiveFeatures1);
			
		//Adaptation Rule isBtNormal => Video off, Image on
		AdaptationRuleWithCtxFeature rule3 = new AdaptationRuleWithCtxFeature();
		LinkedList<ContextFeature> contextTrigger3 = new LinkedList<ContextFeature>();
		contextTrigger3.add(street);		
		rule3.setContextRequired(contextTrigger3);
		
		LinkedList<Feature> toActiveFeature3 = new LinkedList<Feature>();
		toActiveFeature3.add(navigation);
		rule3.setToActiveFeatureList(toActiveFeature3);
		
		//Adaptation Rule isBtFull => Video on, Image on
		AdaptationRuleWithCtxFeature rule5 = new AdaptationRuleWithCtxFeature();
		LinkedList<ContextFeature> contextTrigger5 = new LinkedList<ContextFeature>();
		contextTrigger5.add(home);		
		rule5.setContextRequired(contextTrigger5);
		
		LinkedList<Feature> toDeactiveFeature5 = new LinkedList<Feature>();
		toDeactiveFeature5.add(gps);				
		rule5.setToDeactiveFeatureList(toDeactiveFeature5);
		
		//Context Model
		CFM ctxModel = new CFM();
		ctxModel.setContextRoot(root);
		ctxModel.getContextGroups().add(place);				
		ctxModel.getAdaptationRules().add(rule1);
		ctxModel.getAdaptationRules().add(rule3);
		ctxModel.getAdaptationRules().add(rule5);				
		
		
		ExtractorCks extractor = new ExtractorCks();
		CKS cks = extractor.getContextKripkeStructureFromContextModel(ctxModel,path);
		return cks;
	}
	
	public static CKS genXLSMobiline(String path){
			// Features
		Feature documents = new Feature();
		documents.setName("Documents");
		documents.setFeatureType(FeatureType.Mandatory);
		
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
		toActiveFeature6.add(current);		
		rule6.setToActiveFeatureList(toActiveFeature6);
		
		//Context Model
		CFM ctxModel = new CFM();
		ctxModel.setContextRoot(root);
		ctxModel.getContextGroups().add(battery);
		ctxModel.getContextGroups().add(network);
		ctxModel.getAdaptationRules().add(rule1);
		ctxModel.getAdaptationRules().add(rule3);
		ctxModel.getAdaptationRules().add(rule5);
		ctxModel.getAdaptationRules().add(rule6);
		
		
		ExtractorCks extractor = new ExtractorCks();
		CKS cks = extractor.getContextKripkeStructureFromContextModel(ctxModel,path);
		return cks;
	}
	
	public static CKS genXLSSmartHome(String path){
		// Features
		Feature security = new Feature();
		security.setName("Security");
		security.setFeatureType(FeatureType.Mandatory);
		
		Feature callPolice = new Feature();
		callPolice.setName("CallPolice");
		callPolice.setFeatureType(FeatureType.Optional);
		callPolice.setFatherFeature(security);
		
		Feature presenceIlusion = new Feature();
		presenceIlusion.setName("PresenceIlusion");
		presenceIlusion.setFeatureType(FeatureType.Optional);
		presenceIlusion.setFatherFeature(security);
		
		Feature alarm = new Feature();
		alarm.setName("Alarm");
		alarm.setFeatureType(FeatureType.Optional);
		alarm.setFatherFeature(security);
	
		Feature temperatureControl = new Feature();
		temperatureControl.setName("TemperatureControl");
		temperatureControl.setFeatureType(FeatureType.Mandatory);		
		
		Feature fromAir = new Feature();
		fromAir.setName("FromAir");
		fromAir.setFeatureType(FeatureType.Group);
		fromAir.setFatherFeature(temperatureControl);
		
		Feature fromWindows = new Feature();
		fromWindows.setName("FromWindows");
		fromWindows.setFeatureType(FeatureType.Group);
		fromWindows.setFatherFeature(temperatureControl);
		
		FeatureGroup temperatureGroup = new FeatureGroup();
		temperatureGroup.append(fromAir);
		temperatureGroup.append(fromWindows);
		temperatureGroup.setGroupType(FeatureGroupType.OR_Group);
		
		ContextFeature root = new ContextFeature("Root Context");
		
		// Context Propositions
		ContextFeature threat = new ContextFeature("Threat");
		threat.setContextType(ContextType.Optional);
		ContextFeature attempt = new ContextFeature("Attempt");
		attempt.setContextType(ContextType.Optional);
		ContextFeature highPower = new ContextFeature("HighPowerConsumption");
		highPower.setContextType(ContextType.Group);
		ContextFeature lowPower = new ContextFeature("LowPowerConsumption");
		lowPower.setContextType(ContextType.Group);		
		
		//Context Groups
		ContextGroup PowerConsumption = new ContextGroup("PowerConsumption");
		PowerConsumption.setType(ContextGroupType.XOR);
		PowerConsumption.append(lowPower);
		PowerConsumption.append(highPower);		
		
		ContextGroup robbery = new ContextGroup("Robbery"); // To the interleaving testing
		robbery.setType(ContextGroupType.NONE);
		robbery.append(threat);
		robbery.append(attempt);
		
		// Adaptation Rules
		
		//Adaptation Rule Threat => PresenceIllustion ON
		AdaptationRuleWithCtxFeature rule1 = new AdaptationRuleWithCtxFeature();
		LinkedList<ContextFeature> contextTrigger1 = new LinkedList<ContextFeature>();
		contextTrigger1.add(threat);
		rule1.setContextRequired(contextTrigger1);
		
		LinkedList<Feature> toActiveFeatures1 = new LinkedList<Feature>();
		toActiveFeatures1.add(presenceIlusion);		
		rule1.setToDeactiveFeatureList(toActiveFeatures1);
			
		//Adaptation Rule Attempt => CallPolice on, FromWindows off, Alarm on
		AdaptationRuleWithCtxFeature rule3 = new AdaptationRuleWithCtxFeature();
		LinkedList<ContextFeature> contextTrigger3 = new LinkedList<ContextFeature>();
		contextTrigger3.add(attempt);		
		rule3.setContextRequired(contextTrigger3);
		
		LinkedList<Feature> toActiveFeature3 = new LinkedList<Feature>();
		toActiveFeature3.add(callPolice);
		toActiveFeature3.add(alarm);
		rule3.setToActiveFeatureList(toActiveFeature3);
		
		//Adaptation Rule High Power => FromWindonws on, FromAir off
		AdaptationRuleWithCtxFeature rule5 = new AdaptationRuleWithCtxFeature();
		LinkedList<ContextFeature> contextTrigger5 = new LinkedList<ContextFeature>();
		contextTrigger5.add(highPower);		
		rule5.setContextRequired(contextTrigger5);
		
		LinkedList<Feature> toActiveFeature5 = new LinkedList<Feature>();
		toActiveFeature5.add(fromWindows);		
		rule5.setToActiveFeatureList(toActiveFeature5);
		
		LinkedList<Feature> toDeactiveFeature5 = new LinkedList<Feature>();
		toDeactiveFeature5.add(fromAir);		
		rule5.setToDeactiveFeatureList(toDeactiveFeature5);
		
		//Adaptation Rule LowPower => FromAir on
		AdaptationRuleWithCtxFeature rule6 = new AdaptationRuleWithCtxFeature();
		LinkedList<ContextFeature> contextTrigger6 = new LinkedList<ContextFeature>();
		contextTrigger6.add(lowPower);		
		rule6.setContextRequired(contextTrigger6);
		
		LinkedList<Feature> toActiveFeature6 = new LinkedList<Feature>();
		toActiveFeature6.add(fromAir);		
		rule6.setToActiveFeatureList(toActiveFeature6);
		
		//Context Model
		CFM ctxModel = new CFM();
		ctxModel.setContextRoot(root);
		ctxModel.getContextGroups().add(PowerConsumption);
		ctxModel.getContextGroups().add(robbery);
		ctxModel.getAdaptationRules().add(rule1);
		ctxModel.getAdaptationRules().add(rule3);
		ctxModel.getAdaptationRules().add(rule5);
		ctxModel.getAdaptationRules().add(rule6);		
		
		ExtractorCks extractor = new ExtractorCks();
		CKS cks = extractor.getContextKripkeStructureFromContextModel(ctxModel,path);
		return cks;
	}
		
		
}
