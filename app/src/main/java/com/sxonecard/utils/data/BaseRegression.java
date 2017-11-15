package com.sxonecard.utils.data;

import org.ujmp.core.Matrix;

/**
 * Created by HeQiang on 2017/9/28.
 */
public abstract class BaseRegression {
    protected double[] weight;
    protected double accuracy = 10E-8;
    protected double alpha = 0.01;
    protected double error;


    protected double r2;
    protected double minValue = Double.MAX_VALUE;
    protected double maxValue = Double.MIN_VALUE;
    protected double scale;
    protected int maxIter = 10000000;

    /**
     * 梯度下降迭代计算
     *
     * @param x
     * @param y
     * @param rank
     */
    public void fit(double[] x, double[] y, int rank) {
        if (x.length != y.length || rank <= 0)
            return;
        defaultScale(x);
        weight = new double[rank + 1];
        for (int iter = 0; iter < maxIter; iter++) {
            double[] theta = new double[weight.length];
            for (int i = 0; i < x.length; i++) {
                double grad = (y[i] - hypothesis(x[i]));
                for (int j = 0; j < weight.length; j++)
                    theta[j] += grad * Math.pow(transform(x[i]), j);
            }
            for (int j = 0; j < weight.length; j++)
                weight[j] += alpha * theta[j] / x.length;
            double current = loss(x, y);
            if (Math.abs(error - current) < accuracy)
                break;
            error = current;
        }
        compute_r2(x, y);
    }


    /**
     * 损失函数
     *
     * @param x
     * @param y
     * @return
     */
    public double loss(double[] x, double[] y) {
        double error = 0;
        for (int i = 0; i < x.length; i++) {
            error += 0.5 * Math.pow((y[i] - hypothesis(x[i])), 2);
        }
        return error;
    }

    public double predict(double x) {
        return hypothesis(x);
    }

    public double hypothesis(double x) {
        double value = 0;
        double realX = transform(x);
        for (int i = 0; i < weight.length; i++) {
            value += weight[i] * Math.pow(realX, i);
        }
        return value;
    }

    private void compute_r2(double[] x, double[] y) {
        double[] y_pred = new double[y.length];
        for (int i = 0; i < x.length; i++) {
            y_pred[i] = predict(x[i]);
        }
        double y_mean = 0;
        for (int i = 0; i < y.length; i++) {
            y_mean += y[i];
        }
        y_mean /= y.length;
        double v1 = 0;
        double v2 = 0;
        for (int i = 0; i < y.length; i++) {
            v1 += (y[i] - y_pred[i]) * (y[i] - y_pred[i]);
            v2 += (y[i] - y_mean) * (y[i] - y_mean);
        }

        r2 = 1 - v1 / v2;
    }

    /**
     * 自变量变换函数
     *
     * @param x
     * @return
     */
    public abstract double transform(double x);

    /**
     * 计算缩放比例，自变量缩放到[0,1]区间
     *
     * @param x
     */
    private void defaultScale(double[] x) {
        if (scale != 0)
            return;
        for (int i = 0; i < x.length; i++) {
            if (x[i] < minValue)
                minValue = x[i];
            if (x[i] > maxValue)
                maxValue = x[i];
        }
        computeScale();
    }

    protected void computeScale(){
        if (maxValue != minValue)
            scale = 1 / (maxValue - minValue);
        else
            scale = 1;
    }
    /**
     * 使用矩阵方式计算
     *
     * @param x
     * @param y
     * @param rank
     */
    public void fit_matrix(double[] x, double[] y, int rank) {
        rank = rank + 1;
        defaultScale(x);
        Matrix mx = Matrix.Factory.zeros(x.length,rank);
        Matrix my = Matrix.Factory.zeros(x.length,1);
        for (int i = 0; i < x.length; i++) {
            double px = transform(x[i]);
            for (int j = 0; j < rank; j++)
                mx.setAsDouble(Math.pow(px, j),i, j);
            my.setAsDouble(y[i],i, 0);
        }
        Matrix tx = (mx.transpose().mtimes(mx)).pinv().mtimes(mx.transpose()).mtimes(my);
        weight = new double[rank];
        for (int i = 0; i < rank; i++)
            weight[i] = tx.getAsDouble(i, 0);
        compute_r2(x, y);
    }

    public double getR2() {
        return r2;
    }

    public void print() {
        for (int i = weight.length; i > 0; i--)
            System.out.print(weight[i - 1] + " ");
        System.out.println("r2=" + r2);
    }

    public void fit(double[] x, double[] y) {
        fit(x, y, 1);
    }

    public void fit_matrix(double[] x, double[] y) {
        fit_matrix(x, y, 1);
    }
}

