/*
 * Copyright (c) 2014-2015 Genome Research Ltd.
 * 
 * Author: Mouse Informatics Group <team110g@sanger.ac.uk>
 * This file is part of Node Toolset.
 * 
 * Node Toolset is free software: you can redistribute it and/or modify it under
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

package uk.ac.sanger.mig.analysis.maths.trendline;

import java.util.Arrays;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.apache.commons.math3.stat.regression.OLSMultipleLinearRegression;

public abstract class OLSTrendLine implements TrendLine {

    RealMatrix coef = null; // will hold prediction coefs once we get values

    protected abstract double[] xVector(double x); // create vector of values from x

    protected abstract boolean logY(); // set true to predict log of y (note: y must be positive)

    @Override
    public void setValues(double[] y, double[] x) {
        if (x.length != y.length) {
            throw new IllegalArgumentException(String.format("The numbers of y and x values must be equal (%d != %d)", y.length, x.length));
        }

        double[][] xData = new double[x.length][];

        for (int i = 0; i < x.length; i++) {
            // the implementation determines how to produce a vector of predictors from a single x
            xData[i] = xVector(x[i]);
        }

        if (logY()) { // in some models we are predicting ln y, so we replace each y with ln y
            y = Arrays.copyOf(y, y.length); // user might not be finished with the array we were given
            for (int i = 0; i < x.length; i++) {
                y[i] = Math.log(y[i]);
            }
        }

        OLSMultipleLinearRegression ols = new OLSMultipleLinearRegression();
        ols.setNoIntercept(true); // let the implementation include a constant in xVector if desired
        ols.newSampleData(y, xData); // provide the data to the model

        coef = MatrixUtils.createColumnRealMatrix(ols.estimateRegressionParameters()); // get our coefs
    }

    @Override
    public double predict(double x) {
        double yhat = coef.preMultiply(xVector(x))[0]; // apply coefs to xVector
        if (logY()) yhat = (Math.exp(yhat)); // if we predicted ln y, we still need to get y

        return yhat;
    }
    
    public RealMatrix coef() {
    	return coef;
    }
    
    public void setCoef(RealMatrix coef) {
    	this.coef = coef;
    }
}
