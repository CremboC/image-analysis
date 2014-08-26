package uk.ac.sanger.mig.aligner.helpers;

public class MatrixHelper {
	
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

	public static int[] generateX(int dX, int dY) {
		int[] x = new int[(dX * dY) + dX + dY];

		for (int i = 0; i <= dY; i++) {
			for (int j = 1; j <= dX; j++) {
				x[(int) (dX * i + j)] = j;
			}
		}
		
		return x;
	}
	
	public static int[] deductFromEach(int[] a, double centroidX) {
		for (int i = 0; i < a.length; i++) {
			a[i] -= centroidX;
		}
		return a;
	}
}
