package br.ufc.great;

import br.ufc.great.annotations.ControlContext;
import br.ufc.great.annotations.ControlContextGroup;

public class ContextManager {
	private double batteryValue;
	private String connectivity;
	
	public ContextManager() {
		batteryValue = 100.0;
		connectivity = "Wifi";
	}
	
	@ControlContextGroup(contextGroupName = "battery")
	public double getBatteryValue() {
		return batteryValue;
	}
	
	public void setConnectivityWifi() {
		this.connectivity = "Wifi";
	}
	
	public void setConnectivity3g() {
		this.connectivity = "3G";
	}
	
	public void setConnectivityNone() {
		this.connectivity = "None";
	}
	
	@ControlContext(contextName = "isConnectivityWifi")
	public boolean isConnectivityWifi() {
		if (connectivity.equals("Wifi")) {
			return true;
		}
		return false;
	}
	
	@ControlContext(contextName = "isConnectivity3g")
	public boolean isConnectivity3g() {
		if (connectivity.equals("3g")) {
			return true;
		}
		return false;
	}
	
	@ControlContext(contextName = "isConnectivityNone")
	public boolean isConnectivityNone() {
		if (connectivity.equals("None")) {
			return true;
		}
		return false;
	}
}
