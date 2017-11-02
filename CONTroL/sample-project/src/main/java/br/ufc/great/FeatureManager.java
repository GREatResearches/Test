package br.ufc.great;

import java.util.HashMap;
import java.util.Map;

import br.ufc.great.annotations.ControlFeature;
import br.ufc.great.annotations.ControlSystemStatus;

public class FeatureManager {
	
	private Map<String, Boolean> featuresState;
	private ContextManager contextManager;
	
	public FeatureManager() {
		featuresState = new HashMap<String, Boolean>();
		contextManager = new ContextManager();
		
		featuresState.put("Video", true);
		featuresState.put("PDF", true);
	}
	
	public void verifyFeatures() {
		verifyPDF();
		verifyVideo();
	}
	
	@ControlSystemStatus
	public void updateFeatures() {
		System.out.println("All features updated.");
	}
	
	//TODO - Considerar no exemplo método que retorna qualquer feature?
	// Nesse caso o valor passado como parâmetro tem que ser capturado para inferir
	// a feature interceptada.
	public boolean getFeatureState(String feature) {
		return featuresState.get(feature);
	}
	
	// Ou um método por feature deve ser considerado?
	@ControlFeature(feature="Video")
	public boolean verifyVideo() {
		if (contextManager.getBatteryValue() < 50.0) {
			featuresState.put("Video", false);
		} else if (contextManager.getBatteryValue() >= 50.0) {
			featuresState.put("Video", true);
		}
		
		return featuresState.get("Video");
	}
	
	@ControlFeature(feature="PDF")
	public boolean verifyPDF() {
		if (contextManager.getBatteryValue() < 10.0) {
			featuresState.put("Video", false);
		} else if (contextManager.getBatteryValue() >= 10.0) {
			featuresState.put("Video", true);
		}
		
		return featuresState.get("Video");
	}
}
