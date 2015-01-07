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
 * Helper methods for matrices
 * 
 * @author pi1
 */
public class MatrixHelper {

	/**
	 * Generate the Y array as required to calculate the denominator of an
	 * image: [1 : rows]' * ones(1, cols)
	 * 
	 * @param dX
	 *            cols
	 * @param dY
	 *            rows
	 * @return
	 */
	public static int[] y(int dX, int dY) {
		int[] y = new int[(dX * dY) + dX + dY];

		int val = 1;
		for (int i = 0; i <= dY; i++) {
			for (int j = 0; j <= dX; j++) {
				y[(int) (dX * i + j)] = val;
			}
			val++;
		}

		return y;
	}

	/**
	 * Generate the X array as required to calculate the denominator of an
	 * image: ones(rows, 1) * [1 : cols]
	 * 
	 * @param dX
	 *            cols
	 * @param dY
	 *            rows
	 * @return
	 */
	public static int[] x(int dX, int dY) {
		int[] x = new int[(dX * dY) + dX + dY];

		for (int i = 0; i <= dY; i++) {
			for (int j = 1; j <= dX; j++) {
				x[(int) (dX * i + j)] = j;
			}
		}

		return x;
	}

	/**
	 * Matrix element-wise subtraction. 
	 * @param a
	 * @param deduc
	 * @return
	 */
	public static int[] deductFromEach(int[] a, double deduc) {
		for (int i = 0; i < a.length; i++) {
			a[i] -= deduc;
		}
		return a;
	}
}
