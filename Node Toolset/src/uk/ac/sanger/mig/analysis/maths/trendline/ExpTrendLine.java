package uk.ac.sanger.mig.analysis.maths.trendline;

public class ExpTrendLine extends OLSTrendLine {
    @Override
    protected double[] xVector(double x) {
        return new double[]{1, x};
    }

    @Override
    protected boolean logY() {
        return true;
    }
}
