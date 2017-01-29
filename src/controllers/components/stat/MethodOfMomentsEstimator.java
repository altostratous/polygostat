package controllers.components.stat;

import Jama.Matrix;

/**
 * Created by HP PC on 1/29/2017.
 */
public class MethodOfMomentsEstimator {
    private int degree;
    private double[] polynomialCoefficients;
    private double[] x;
    private double[] y;

    public MethodOfMomentsEstimator(double x[], double y[], int degree) {
        this.degree = degree;
        this.x = x;
        this.y = y;
        Matrix A = momentMatrix(degree);
        Matrix C = sampleMomentMatrix();
        polynomialCoefficients = A.inverse().times(C.transpose()).transpose().getArray()[0];
    }

    private Matrix sampleMomentMatrix() {
        double[] coeffs = new double[degree + 1];
        for (int i = 1; i < degree + 2; i++) {
            coeffs[i - 1] = getSampleNthMoment(i);
        }
        return new Matrix(coeffs,1);
    }

    private Matrix momentMatrix(int degree) {
        double[][] coeffs = new double[degree + 1][degree + 1];
        double minX = getMinX();
        double maxX = getMaxX();
        for (int i = 1; i < degree + 2; i++) {
            for (int j = 0; j < degree + 1; j++) {
                double integralExponent = (degree + i - j) + 1;
                coeffs[i - 1][j] = (Math.pow(maxX, integralExponent) - Math.pow(minX, integralExponent)) / integralExponent;
            }
        }
        return new Matrix(coeffs);
    }

    double getSampleNthMoment(int n){
        double result = 0;
        for (int i = 0; i < n; i++) {
            result += Math.pow(y[i], n);
        }
        return result / y.length;
    }

    public double getMinX() {
        double res = x[0];
        for (int i = 0; i < x.length; i++) {
            if (x[i] < res)
                res = x[i];
        }
        return res;
    }

    public double getMaxX() {
        double res = x[0];
        for (int i = 0; i < x.length; i++) {
            if (x[i] > res)
                res = x[i];
        }
        return res;
    }

    public Number predict(double i) {
        double result = polynomialCoefficients[0];
        for (int j = 1; j < polynomialCoefficients.length; j++) {
            result += polynomialCoefficients[j];
            result *= i;
        }
        return result;
    }
}
