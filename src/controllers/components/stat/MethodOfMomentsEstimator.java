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
    private double minX, maxX;

    public MethodOfMomentsEstimator(double x[], double y[], int degree, double minX, double maxX) {
        this.degree = degree;
        this.x = x;
        this.y = y;
        sortXAndY(x,y);
        this.minX = minX;
        this.maxX = maxX;
        Matrix A = momentMatrix(degree);
        Matrix C = sampleMomentMatrix();
        polynomialCoefficients = A.inverse().times(C).transpose().getArray()[0];
    }

    private void sortXAndY(double[] x, double[] y) {
        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < x.length - 1; j++) {
                if (x[j] > x[j + 1]){
                    double temp = x[j];
                    x[j] = x[j + 1];
                    x[j+ 1] = temp;
                    temp = y[j];
                    y[j] = y[j + 1];
                    y[j+ 1] = temp;
                }
            }
        }
    }

    private Matrix sampleMomentMatrix() {
        double[] coeffs = new double[degree + 1];
        for (int i = 1; i < degree + 2; i++) {
            coeffs[i - 1] = getSampleNthMoment(i);
        }
        return new Matrix(coeffs,1).transpose();
    }

    private Matrix momentMatrix(int degree) {
        double[][] coeffs = new double[degree + 1][degree + 1];
        double minX = getMinX();
        double maxX = getMaxX();
        for (int i = 1; i < degree + 2; i++) {
            for (int j = 0; j < degree + 1; j++) {
                double exponent = (degree - j) + i;
                double integralExponent = exponent + 1;
                coeffs[i - 1][j] = (Math.pow(maxX, integralExponent) - Math.pow(minX, integralExponent)) / integralExponent;
            }
        }
        return new Matrix(coeffs);
    }

    double getSampleNthMoment(int n){
        double result = 0;
        for (int i = 0; i < y.length; i++) {
            double dx1;
            double dx2;
            if (i == y.length - 1){
                dx2 = 0;
            } else
            {
                dx2 = x[i + 1] - x[i];

            }
//            dx2 = 0;
            if (i == 0)
                dx1 = 0;
            else
                dx1 = x[i] - x[i - 1];
            result += Math.pow(x[i], n) * y[i] * (dx1);
            result += Math.pow(x[i], n) * y[i] * (dx2);
        }
        return result / 2;
    }

    public double getMinX() {
        return minX;
    }

    public double getMaxX() {
        return  maxX;
    }

    public Number predict(double i) {
        double result = polynomialCoefficients[0];
        for (int j = 1; j < polynomialCoefficients.length; j++) {
            result *= i;
            result += polynomialCoefficients[j];
        }
        return result;
    }
}
