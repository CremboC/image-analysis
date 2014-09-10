package uk.ac.sanger.mig.analysis.nodetools.enums;

import com.sun.istack.internal.Nullable;

public enum ReturnType {
	ORIG("Original"),
	LINED("Lined");
	
	private String name;
	
	private ReturnType(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	/**
	 * Finds enum which has names as provided
	 * @param name name to look for, <b>null</b> if not found
	 */
	@Nullable
	public static final ReturnType whereName(String name) {
		
		for (int i = 0; i < values().length; i++) {
			if (values()[i].toString().equals(name)) {
				return values()[i];
			}
		}
		
		return null;
	}
	
	/**
	 * Returns names of all enums
	 */
	public static final String[] names() {
		String[] names = new String[values().length];
		
		for (int i = 0; i < values().length; i++) {
			names[i] = values()[i].toString();
		}
		
		return names;
	}
}