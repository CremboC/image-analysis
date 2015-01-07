/*
 * Copyright (c) 2014-2015 Genome Research Ltd.
 * 
 * Author: Mouse Informatics Group <team110g@sanger.ac.uk>
 * This file is part of Aligner.
 * 
 * Aligner is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Affero General Public License as published by the Free
 * Software Foundation; either version 3 of the License, or (at your option ) any
 * later version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */

package uk.ac.sanger.mig.aligner.helpers;

/**
 * Contains final logic to find the angle
 * 
 * @author pi1
 */
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
				cos2thetamin = (denom.a() - denom.c()) / Math.sqrt(denom.denom());
		
		thetamin = Math.atan2(sin2thetamin, cos2thetamin) / 2;
		
		return thetamin;
	}
	
}
