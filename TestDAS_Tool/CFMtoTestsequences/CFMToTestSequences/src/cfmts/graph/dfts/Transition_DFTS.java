package cfmts.graph.dfts;

import fmp.contextEvolModel.ContextState;

public class Transition_DFTS {
	// it represents a transitions ctx -> new system state

	private int id;
	
	private ContextState ctxState;
	
	private Node_DFTS newState;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public ContextState getCtxState() {
		return ctxState;
	}

	public void setCtxState(ContextState ctxState) {
		this.ctxState = ctxState;
	}

	public Node_DFTS getNewState() {
		return newState;
	}

	public void setNewState(Node_DFTS newState) {
		this.newState = newState;
		newState.getArrivalTransitions().add(this);
	}
}
