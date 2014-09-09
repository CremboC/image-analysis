package uk.ac.sanger.mig.xray.trendline.utils.maths;

public interface TrendLine {
    public void setValues(double[] y, double[] x); // y ~ f(x)

    public double predict(double x); // get a predicted y for a given x
}
