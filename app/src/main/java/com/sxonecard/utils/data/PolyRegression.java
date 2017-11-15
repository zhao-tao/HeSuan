package com.sxonecard.utils.data;

/**
 * Created by HeQiang on 2017/9/28.
 */
public class PolyRegression extends BaseRegression {
    @Override
    public double transform(double x) {
        return (x-minValue)*scale;
    }
}
