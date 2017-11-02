package br.ufc.great.aspects;

public abstract class RunnableWithReturn implements Runnable {
	private Object returnValue;
	
	public Object getReturnValue() {
		return this.returnValue;
	}
	
	public void setReturnValue(Object returnValue) {
		this.returnValue = returnValue;
	}
	
	public void changeReturnValue(String newValue, String type) {
		switch (type) {
			case "int":
				returnValue = Integer.valueOf(newValue);
				break;
			case "double":
				returnValue = Double.valueOf(newValue);
				break;
		}
	}
}
