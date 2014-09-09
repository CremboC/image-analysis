package uk.ac.sanger.mig.xray.trendline.utils;

import com.sun.istack.internal.Nullable;

public enum Fitting {

	EXP("Exponential"),
	LOG("Logarithmic"),
	POLY("Polynomial"),
	POWER("Power");	
	
	private String name;
	
	private Fitting(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Finds Fitting which has names as provided
	 * @param name name to look for, <b>null</b> if not found
	 */
	@Nullable
	public static final Fitting whereName(String name) {
		
		for (int i = 0; i < values().length; i++) {
			if (values()[i].toString().equals(name)) {
				return values()[i];
			}
		}
		
		return null;
	}
	
	/**
	 * Returns names of all fittings
	 */
	public static final String[] names() {
		String[] names = new String[values().length];
		
		for (int i = 0; i < values().length; i++) {
			names[i] = values()[i].toString();
		}
		
		return names;
	}
	
}
