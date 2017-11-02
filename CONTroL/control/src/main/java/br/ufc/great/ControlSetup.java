package br.ufc.great;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ControlSetup {
	private static ControlSetup controlSetupInstance = null;
	
	private String testSequencePath;
	private String testReportPath;
	private ArrayList<String> featuresAvaliable;
//	private ContextDomain contextDomain;
	
	private HashMap<String, HashMap<String, String>> contextDomain;
	
	public ControlSetup() {
		
	}
	
	public static ControlSetup getInstance() {
        if (controlSetupInstance == null) {
            controlSetupInstance = ControlUtils.initializeControlSetup();
        }

        return controlSetupInstance;
    }

	public String getTestSequencePath() {
		return testSequencePath;
	}

	public void setTestSequencePath(String testSequencePath) {
		this.testSequencePath = testSequencePath;
	}

	public String getTestReportPath() {
		return testReportPath;
	}

	public void setTestReportPath(String testReportPath) {
		this.testReportPath = testReportPath;
	}

	public ArrayList<String> getFeaturesAvaliable() {
		return featuresAvaliable;
	}

	public void setFeaturesAvaliable(ArrayList<String> featuresAvaliable) {
		this.featuresAvaliable = featuresAvaliable;
	}

//	public ContextDomain getContextDomain() {
//		return contextDomain;
//	}
//
//	public void setContextDomain(ContextDomain contextDomain) {
//		this.contextDomain = contextDomain;
//	}
	
	public Map<String, String> getContextGroup(String contextGroup) {
		return contextDomain.get(contextGroup);
	}
	
	public Set<String> getContextNamesByGroup(String contextGroup) {
		return contextDomain.get(contextGroup).keySet();
	}
	
	public String getContextValue(String contextGroup, String contextName) {
		return contextDomain.get(contextGroup).get(contextName);
	}
	
	public void setContextDomain(HashMap<String, HashMap<String, String>> contexts) {
		this.contextDomain = contexts;
	}
	
}