package fmp.contextModel;

// it represents a Context Feature
public class ContextFeature {

	private String name;
	
	// optional, group
	private ContextType contextType;
	
	private ContextGroup group;
	
	
	public ContextFeature (String name){
		this.name = name;				
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ContextType getContextType() {
		return contextType;
	}

	public void setContextType(ContextType contextType) {
		this.contextType = contextType;
	}

	public ContextGroup getGroup() {
		return group;
	}

	public void setGroup(ContextGroup group) {
		this.group = group;
	}

		
}
