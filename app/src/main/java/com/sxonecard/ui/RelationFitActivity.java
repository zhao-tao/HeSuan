package com.sxonecard.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.sxonecard.R;
import com.sxonecard.utils.LineChartManager;
import com.sxonecard.utils.data.DataUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * 拟合曲线界面.
 */

public class RelationFitActivity extends FragmentActivity {

    @Bind(R.id.fit_chart)
    LineChart fitChart;
    @Bind(R.id.index)
    Button index;
    @Bind(R.id.logarithm_fun)
    Button logarithmFun;
    @Bind(R.id.line_fun)
    Button lineFun;
    @Bind(R.id.save_btn)
    Button saveBtn;
    @Bind(R.id.export_btn)
    Button exportBtn;
    @Bind(R.id.back_btn)
    Button backBtn;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.relation_fit);
        ButterKnife.bind(this);

        final LineChartManager lcm = new LineChartManager(fitChart);
        dramLineChart(lcm);

        //指数函数.
        indexFun(lcm);
        log_fun(lcm);
        line_fun(lcm);

        goBackListener();
        exportListener();
        saveListener();
    }

    private void line_fun(final LineChartManager lcm) {
        lineFun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, List<Double>> dataMap =  DataUtil.toFunction(1);
                drawFunctionChart(lcm, dataMap);
            }
        });
    }

    /**
     * 对数拟合.
     */
    private void log_fun(final LineChartManager lcm) {
        logarithmFun.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, List<Double>> dataMap =  DataUtil.toFunction(0);
                drawFunctionChart(lcm, dataMap);
            }
        });
    }

    /**
     * 对数函数拟合.
     */
    private void indexFun(final LineChartManager lcm) {
        index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, List<Double>> dataMap =  DataUtil.toFunction(2);
                drawFunctionChart(lcm, dataMap);
            }
        });
    }

    private void drawFunctionChart(LineChartManager lcm, Map<String, List<Double>> dataMap)
    {
        List<Double> doubleX = dataMap.get("x");
        List<Integer> intX = new ArrayList<Integer>();
        for(double d : doubleX)
        {
            intX.add((int)d);
        }
        List<Double> doubleY = dataMap.get("y");
        List<Integer> intY = new ArrayList<Integer>();
        for(double dy : doubleY)
        {
            intY.add((int)dy);
        }
        lcm.showSingerLineChart(intX, intY, "拟合图", Color.BLACK);
        fitChart.getAxisLeft().removeAllLimitLines();
        fitChart.invalidate();//refresh.
    }

    private void saveListener() {
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "暂未开放", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void exportListener() {
        exportBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "暂未开放", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void goBackListener() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), LineFragment.class));
            }
        });
    }

    private void dramLineChart(LineChartManager lcm) {
        List<String> labels = new ArrayList<String>();
        List<Integer> colours = new ArrayList<Integer>();
        labels.add("A1");
        colours.add(Color.BLACK);

        List<Integer> xAxisValues = new ArrayList<Integer>();
        for (int i = 0; i < 60; i++) {
            xAxisValues.add(i);
        }
        List<List<Integer>> yAxisValues = new ArrayList<List<Integer>>();
        for (int i = 0; i < 1; i++) {
            List<Integer> line = new ArrayList<Integer>();
            for (int j = 0; j < 60; j++) {
                int n = (int) (Math.random() * 9999);
                line.add(n);
            }
            yAxisValues.add(line);
        }
        lcm.showLineChart(xAxisValues, yAxisValues, labels, colours);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
