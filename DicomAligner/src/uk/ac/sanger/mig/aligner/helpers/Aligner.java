package uk.ac.sanger.mig.aligner.helpers;

public class Aligner {
	
	private Denominator denom;
	private Double thetamin = null;

	public Aligner(Denominator denom) {
		this.denom = denom;
	}
	
	public double thetamin() {
		if (thetamin != null)
			return thetamin;
		
		double sin2thetamin = denom.b() / Math.sqrt(denom.denom()),
				cos2thetamin = (denom.a() - denom.c()) / Math.sqrt(denom.denom()),
		
		thetamin = Math.atan2(sin2thetamin, cos2thetamin) / 2;
		
		return thetamin;
	}
	
}
