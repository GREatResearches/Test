package br.ufc.great;

import java.util.Map;

public class ContextDomain {
	
	private Map<String, Map<String, String>> contextGroups;
	
	public ContextDomain(Map<String, Map<String, String>> contexts) {
		this.contextGroups = contexts;
	}
	
	public ContextDomain() {
		
	}

	public Map<String, String> getContextGroup(String contextGroup) {
		return contextGroups.get(contextGroup);
	}
	
	public String getContextGroupValue(String contextGroup, String contextValueName) {
		return contextGroups.get(contextGroup).get(contextValueName);
	}
}