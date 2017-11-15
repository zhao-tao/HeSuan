package com.sxonecard.utils.data;

/**
 * Created by HeQiang on 2017/9/28.
 */
public class Test {



    public static void main(final String args[]) {
        double[] x = {30000000, 3000000, 300000, 30000, 3000, 300, 30};
        double[] y = {10.43, 13.9, 17.3, 20.59, 24.39, 27.32, 30.32};
        // 对数
        LogRegression logRegression = new LogRegression();
        logRegression.fit(x,y);
        logRegression.print();
        System.out.println(logRegression.predict(30));
// 线性
        LinearRegression linearRegression = new LinearRegression();
        linearRegression.fit(x,y);
        linearRegression.print();
        System.out.println(linearRegression.predict(30));
        linearRegression.fit_matrix(x,y,1);
        linearRegression.print();
        System.out.println(linearRegression.predict(30));
//指数
        ExpRegression expRegression = new ExpRegression();
        expRegression.fit(x,y);
        expRegression.print();
        System.out.println(expRegression.predict(30));
//多项式
        PolyRegression polyRegression = new PolyRegression();
        polyRegression.fit(x,y,3);
        polyRegression.print();
        System.out.println(polyRegression.predict(30));
        polyRegression.fit_matrix(x,y,3);
        polyRegression.print();
        System.out.println(polyRegression.predict(30));

    }
}
