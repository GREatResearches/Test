package cfmts.graph.dfts;

import java.util.ArrayList;
import java.util.LinkedList;
import fmp.featureModel.Feature;

public class Node_DFTS {

	private int id;
	
	boolean visited;
	
	private LinkedList<Feature> activeFeatures = new LinkedList<Feature>();
	
	//transitions related to other nodes
	private LinkedList<Transition_DFTS> transitions = new LinkedList<Transition_DFTS>();
	
	//transitions related to other nodes
	private LinkedList<Transition_DFTS> arrivalTransitions = new LinkedList<Transition_DFTS>();

	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public Node_DFTS(){
		setNotVisited();
	}
	
	public boolean isVisited() {
		return visited;
	}
	
	public void setNotVisited() {
		this.visited = false;
	}
	
	public void setVisited() {
		this.visited = true;
	}

	public LinkedList<Feature> getActiveFeatures() {
		return activeFeatures;
	}

	public ArrayList<String> getStringListOfAtiveFeature() {
		ArrayList<String> listOfActiveFeatures = new ArrayList<>();
		for (Feature feature : activeFeatures) {
			listOfActiveFeatures.add(feature.getName());
		}
		return listOfActiveFeatures;
	}
	
	public void setActiveFeatures(LinkedList<Feature> activeFeatures) {
		this.activeFeatures = activeFeatures;
	}

	public LinkedList<Transition_DFTS> getTransitions() {
		return transitions;
	}

	public void setTransitions(LinkedList<Transition_DFTS> transitions) {
		this.transitions = transitions;
	}
	

	public LinkedList<Transition_DFTS> getArrivalTransitions() {
		return arrivalTransitions;
	}

	public void setArrivalTransitions(LinkedList<Transition_DFTS> transitions) {
		this.arrivalTransitions = transitions;
	}
	
}
