package br.ufc.great;

import java.util.ArrayList;
import java.util.Set;

public class TestCase {
	private int id;
	// TODO - AVISO Os nomes das sequências de teste tem que ser únicos no que diz respeito ao 
	// domínio de valores que os contextos podem assumir.
	// Neste caso ou um valor de contexto é controlado pela anotação ControlContext ou por
	// ControlContextGroup.
    private ArrayList<String> contextState;
    private ArrayList<String> expectedFeatureStatus;
    private ArrayList<String> actualFeatureStatus;
    // TODO - Remover este atributo, porque o relatório do extent que irá armazenar a informação.
    private boolean isPassed;
    private int idTransitionDFTS;
	
	public TestCase() {
	}
	
	// TODO - verificar caso tenham mais features do que deveria.
	public void assertFeatures() {	
		isPassed = true;
		
		for(String feature : expectedFeatureStatus) {
			if ( !actualFeatureStatus.contains(feature) ) {
				isPassed = false;
				break;
			}
		}
	}
	
	public int getId() {
		return id;
	}
	
	public void addContextState(String contextName) {
		if (!this.contextState.contains(contextName)) {
			contextState.add(contextName);
		}
	}
	
	public void removeContextState(String contextName) {
		contextState.remove(contextName);
	}
	
	// TODO - Este método considera o uso apenas com grupos de contextos de valores exclusivos.
	// A ideia é utilizar com contextos que possuem alta quantidade de valores.
	public String getContextGroupValue(String contextGroup) {
		ControlSetup cs = ControlSetup.getInstance();
		Set<String> contexNames = cs.getContextNamesByGroup(contextGroup);
		
		// TODO - possível gargalo de execução se o número de contextos for muito alto.
		for (String contextName : contexNames) {
			if (contextState.contains(contextName)){
				return cs.getContextValue(contextGroup, contextName);
			}
		}
		
		return "UNDEFINED";
	}

	public void setContextState(ArrayList<String> contextState) {
		this.contextState = contextState;
	}
	
	public boolean containsContext(String context) {
		return this.contextState.contains(context);
	}
	
	public void addExpectedFeatures(String featureName) {
		if (!this.expectedFeatureStatus.contains(featureName)){
			this.expectedFeatureStatus.add(featureName);
		}
	}
	
	public void removeExpectedFeatures(String featureName) {	
		this.expectedFeatureStatus.remove(featureName);
	}

	public ArrayList<String> getExpectedFeatureStatus() {
		return expectedFeatureStatus;
	}

	public void setExpectedFeatureStatus(ArrayList<String> expectedFeatureStatus) {
		this.expectedFeatureStatus = expectedFeatureStatus;
	}
	
	public void addActualFeatures(String featureName) {
		if (!this.actualFeatureStatus.contains(featureName)){
			this.actualFeatureStatus.add(featureName);
		}
	}
	
	public void removeActualFeatures(String featureName) {	
		this.actualFeatureStatus.remove(featureName);
	}

	public ArrayList<String> getActualFeatureStatus() {
		return actualFeatureStatus;
	}
	
	public void setActualFeatureStatus(ArrayList<String> actualFeatureStatus) {
		this.actualFeatureStatus = actualFeatureStatus;
	}
	
	// TODO - Remover método.
	public void setId(int id) {
		this.id = id;
	}
	
}