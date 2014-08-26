package uk.ac.sanger.mig.aligner.helpers;

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
	public static int[] generateY(int dX, int dY) {
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
	public static int[] generateX(int dX, int dY) {
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
