package cfmts.ts.elements;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import fmp.featureModel.Feature;

public class TestCase {
	    
	    private int id;
	    private int idTransitionDFTS;
	    private Map<String, Boolean> contextState;
	    private Map<String, Boolean> expectedFeatureStatus;
	    private Map<String, Boolean> actualFeatureStatus;	    
	    
		public TestCase(int id, HashMap<String, Boolean> contextState, HashMap<String, Boolean> expectedFeatureStatus) {
			super();
			this.id = id;
			this.contextState = contextState;
			this.expectedFeatureStatus = expectedFeatureStatus;			
		}
		
		public void addContext(String context, Boolean status) {
			contextState.put(context, status);
		}
		
		public void removeContext(String context) {
			contextState.remove(context);
		}
		
		public void addStatusExpectedFeature(String feature, Boolean status) {
			expectedFeatureStatus.put(feature, status);
		}
		
		public void removeStatusExpectedFeature(String feature) {
			expectedFeatureStatus.remove(feature);
		}
		
		public void addStatusActualFeature(String feature, Boolean status) {
			actualFeatureStatus.put(feature, status);
		}
		
		public void removeStatusActualFeature(String feature) {
			actualFeatureStatus.remove(feature);
		}

		public int getId() {
			return id;
		}
		
		public int getIdTransitionDFTS() {
			return idTransitionDFTS;
		}
		
		public void setIdTransitionDFTS(int idTransitionsDFTS) {
			this.idTransitionDFTS = idTransitionsDFTS;
		}

		public Map<String, Boolean> getContextState() {
			return contextState;
		}

		public void setContextState(HashMap<String, Boolean> contextState) {
			this.contextState = contextState;
		}

		public Map<String, Boolean> getExpectedFeatures() {
			return expectedFeatureStatus;
		}

	
		public LinkedList<String> getExpectedFeaturesStringList() {
			LinkedList<String> expectedFeaturesStringList = new LinkedList<String>();
			for (Entry entry : expectedFeatureStatus.entrySet()) {
				if((boolean)entry.getValue()) //if feature status is true
					expectedFeaturesStringList.add((String)entry.getKey());
			}
			return expectedFeaturesStringList;
		}
		
		public LinkedList<String> getContextStringList() {
			LinkedList<String> contextStringList = new LinkedList<String>();
			for (Entry entry : contextState.entrySet()) {
				if((boolean)entry.getValue()) //if context is true
					contextStringList.add((String)entry.getKey());
			}
			return contextStringList;
		}
		
		public void setExpectedFeatures(HashMap<String, Boolean> expectedFeatureStatus) {
			this.expectedFeatureStatus = expectedFeatureStatus;
		}
		
		public Map<String, Boolean> getActualFeatures() {
			return actualFeatureStatus;
		}
		
		public void setActualFeatures(HashMap<String, Boolean> actualFeatureStatus) {
			this.actualFeatureStatus = actualFeatureStatus;
		}
}
