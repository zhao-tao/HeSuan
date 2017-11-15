package com.sxonecard.utils.data;

import com.sxonecard.CardApplication;
import com.sxonecard.http.bean.FunctionData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017-10-17.
 */

public class DataUtil {

    public static Map<String, List<Double>> toFunction(int type){
        List<List<Integer>> yAxisValues = CardApplication.yAxisValues;
       // List<FunctionData> fd_list = CardApplication.fd_list;
        List<FunctionData> fd_list = CardApplication.shiGuanData;
        int limitLineValue = CardApplication.limitLineValue;

        Map<String, List<Double>> result_map = new HashMap<String, List<Double>>();

        List<Double> x = new ArrayList<Double>();
        List<Double> y = new ArrayList<Double>();

        double line_y = 0.0;

        int i = 0;
        for(FunctionData data : fd_list){

            if(data.isSetValue()){

                if(data.getTestSample() == 0){

                    List<Integer> ylist = yAxisValues.get(i);
                    Integer temp = ylist.get(30);

                    if(temp > 0){
                        data.setTestResult("阳性");
                    }else{
                        data.setTestResult("阴性");
                    }

                    for(int j = 0; j < ylist.size(); j++){
                        int k = j == ylist.size()-1? j : j + 1;

                        Integer value_j = ylist.get(j);
                        Integer value_k = ylist.get(k);

                        if(value_j < limitLineValue && value_k > limitLineValue){
                            double pren = ( j + k ) / 2.0;

                            x.add(data.getTestConcentration());
                            y.add(pren);
                            break;
                        }
                    }

                }else if(data.getTestSample() == 1){
                    List<Integer> ylist = yAxisValues.get(i);
                    for(int j = 0; j < ylist.size(); j++){
                        int k = j == ylist.size()-1? j : j + 1;

                        Integer value_j = ylist.get(j);
                        Integer value_k = ylist.get(k);

                        if(value_j < limitLineValue && value_k > limitLineValue){
                            line_y =  ( j + k ) / 2.0;

//                            x.add(data.getTestConcentration());
//                            y.add(pren);

                            break;
                        }
                    }
                }
            }
            i++;
        }

        double[] zu_x = new double[x.size()];
        for(int a=0;a<x.size();a++){
            zu_x[a]=x.get(a);
        }

        double[] zu_y = new double[y.size()];
        for(int b=0;b<y.size();b++){
            zu_y[b]=y.get(b);
        }

        if(0 == type){ // 对数
            LogRegression logRegression = new LogRegression();
            logRegression.fit(zu_x,zu_y);
            logRegression.print();
            double line_x = logRegression.predict(line_y);
            y.add(line_y);
            x.add(line_x);
        }else if(1 == type){ // 线性
            LinearRegression linearRegression = new LinearRegression();
            linearRegression.fit(zu_x,zu_y);
            linearRegression.print();
//            System.out.println(linearRegression.predict(30));
//            linearRegression.fit_matrix(x,y,1);
//            linearRegression.print();
//            System.out.println(linearRegression.predict(30));
            double line_x = linearRegression.predict(line_y);
            y.add(line_y);
            x.add(line_x);

        }else if(2 == type){ // 指数
            ExpRegression expRegression = new ExpRegression();
            expRegression.fit(zu_x,zu_y);
            expRegression.print();
            //System.out.println(expRegression.predict(30));
            double line_x = expRegression.predict(line_y);
            y.add(line_y);
            x.add(line_x);
        }else if(3 == type){ // 多项式
            PolyRegression polyRegression = new PolyRegression();
            polyRegression.fit(zu_x,zu_y,3);
            polyRegression.print();
//            System.out.println(polyRegression.predict(30));
//            polyRegression.fit_matrix(x,y,3);
//            polyRegression.print();
//            System.out.println(polyRegression.predict(30));
            double line_x = polyRegression.predict(line_y);
            y.add(line_y);
            x.add(line_x);

        }

        result_map.put("x", x);
        result_map.put("y", y);

        return result_map;

    }
}
