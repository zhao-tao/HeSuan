package com.sxonecard.ui;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.google.gson.Gson;
import com.sxonecard.CardApplication;
import com.sxonecard.R;
import com.sxonecard.base.BaseFragment;
import com.sxonecard.base.RxBus;
import com.sxonecard.http.bean.FunctionData;
import com.sxonecard.http.serialport.SerialPortUtil;
import com.sxonecard.utils.LineChartManager;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.functions.Action1;

import static com.sxonecard.CardApplication.yAxisValues;

/**
 * Created by Administrator on
 */

public class LineFragment extends BaseFragment {
    @Bind(R.id.chart)
    LineChart chart;
    @Bind(R.id.temperature)
    TextView temperature;
    @Bind(R.id.shiguan_grid)
    GridView shiguanGrid;
    @Bind(R.id.device_status)
    TextView deviceStatus;
    @Bind(R.id.relation_fitting)
    Button relationFitting;
    @Bind(R.id.upmove)
    Button upmove;
    @Bind(R.id.downMove)
    Button downMove;
    //    @Bind(R.id.sample_values)
//    TextView sampleValues;
    @Bind(R.id.concentration_values)
    EditText concentrationValues;
    @Bind(R.id.result_values)
    TextView resultValues;
    @Bind(R.id.sample_values)
    Spinner sampleValues;
    //    @Bind(R.id.function_selected)
//    Spinner functionSelected;
    @Bind(R.id.number_picker)
    NumberPicker numberPicker;


    @Bind(R.id.duibi)
    Button duibi;
    @Bind(R.id.huanyuan)
    Button huanyuan;
    @Bind(R.id.fazhi_guding)
    Button fazhiGuding;
    @Bind(R.id.back)
    Button back;
    @Bind(R.id.start_send_request)
    Button startSendRequest;
    //定义调节荧光值按钮跨度大小的值
    private int minNumber = 1, maxNumber = 200;
    //调节荧光值按钮的步进值（初始值）
    private static int stepNumber = 1;

    //    private static List<List<Integer>> yAxisValues = new ArrayList<List<Integer>>();
    private List<String> shiGuanValuesLst = new ArrayList<>();

    //样本.
    private List<String> data_list;
    private ArrayAdapter<String> arr_adapter;

    //函数选择.
    // private List<String> functionDataList;
    // private ArrayAdapter<String> functionArrAdapter;

    //选择的样本值.（标准，待测，空）
    private int selectedSampleValue = 0;
    //选择的函数类型.
    // private int selectedFunction;
    //输入的浓度值.
    private double selectedConcentration;

    //是否进行对比按钮标识.
    private boolean dbFlag;

    @Override
    public int getLayoutId() {
        return R.layout.mp_line;
    }

    LineChartManager lcm = null;

    @Override
    public void initView() {
        lcm = new LineChartManager(chart);
        //输入浓度.
        //temperature.setText("当前温度\n" + (null == CardApplication.heSuanTemperature ? "0 °C" : CardApplication.heSuanTemperature));
        //deviceStatus.setText("\n\n\n设备状态\n正常");
        initListener();
        initNumber();
        //限制输入固定的某些字符
        concentrationValues.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        //resultValues.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        //样本下拉框.
        sampleSelect();
        //函数下拉框.
        functionSelect();
        initLineChart();
        initShiGuanView();
        registerReceiveDataBus();
        dramLineChart(lcm);
    }

    private void initListener() {
        startSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(LineFragment.super.context, "开始向串口发送检测请求...", Toast.LENGTH_LONG).show();
//                SerialPortUtil.getInstance().sendStartTestCmd();
            }
        });

        //        relationFitting.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Toast.makeText(LineFragment.super.context, "进行关系拟合计算中....", Toast.LENGTH_LONG).show();
//                Log.i("关系拟合函数", selectedFunction+"");
//                Map<String, List<Double>> dataMap =  DataUtil.toFunction(selectedFunction);
//                //绘制拟合函数曲线.
//                drawFunctionChart(lcm, dataMap);
//            }
//        });
        /**
         * 关系拟合
         */
        relationFitting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LineFragment.super.context, RelationFitActivity.class));
            }
        });

        /**
         * 阈值上移
         */
        upmove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                CardApplication.limitLineValue += stepNumber;
                Log.i("up", "--------" + CardApplication.limitLineValue);
                moveLine();
            }
        });
        //长按.
//        upmove.setOnLongClickListener(new View.OnLongClickListener()
//        {
//            @Override
//            public boolean onLongClick(View v) {
//                if(v == upmove){//当按下的是按钮时
//                    CardApplication.limitLineValue += 100;
//                    Log.i("上移按钮长按", "--------" + CardApplication.limitLineValue);
//                    moveLine();
//                }
//                return false;
//            }
//        });

        /**
         * 阈值下移
         */
        downMove.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (CardApplication.limitLineValue > 0 && CardApplication.limitLineValue - stepNumber >= 0) {
                    CardApplication.limitLineValue -= stepNumber;
                } else {
                    CardApplication.limitLineValue = 0;
                }
                Log.i("down", "--------" + CardApplication.limitLineValue);
                moveLine();
            }
        });
        //长按.
//        downMove.setOnLongClickListener(new View.OnLongClickListener()
//        {
//            @Override
//            public boolean onLongClick(View v) {
//                if(v == downMove){//当按下的是按钮时
//                    CardApplication.limitLineValue -= 100;
//                    Log.i("下移按钮长按", "--------" + CardApplication.limitLineValue);
//                    moveLine();
//                }
//                return false;
//            }
//        });
        //lcm.setHightLimitLine(CardApplication.limitLineValue, "荧光阈值", Color.BLACK);

        /**
         * 对比
         */
        duibi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LineFragment.super.context, "开始进行对比...", Toast.LENGTH_LONG).show();
                dbFlag = true;
            }
        });
        /**
         * 还原
         */
        huanyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dramLineChart(lcm);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
//                startActivity(new Intent(LineFragment.super.getContext(), HeSuanActivity.class));
            }
        });
    }

    List<String> labels = new ArrayList<String>();
    List<Integer> colours = new ArrayList<Integer>();
    List<Integer> xAxisValues = new ArrayList<Integer>();

    private void dramLineChart(LineChartManager lcm) {
        labels.add("A1");
        colours.add(Color.BLACK);
        labels.add("B2");
        colours.add(Color.BLUE);
        labels.add("C3");
        colours.add(Color.CYAN);
        labels.add("D4");
        colours.add(Color.DKGRAY);
        labels.add("E5");
        colours.add(Color.GRAY);
        labels.add("F6");
        colours.add(Color.GREEN);
        labels.add("G7");
        colours.add(Color.LTGRAY);
        labels.add("H8");
        colours.add(Color.MAGENTA);
        labels.add("A2");
        colours.add(Color.RED);
        labels.add("B2");
        colours.add(Color.TRANSPARENT);
        labels.add("C2");
        colours.add(Color.YELLOW);
        labels.add("D2");
        colours.add(Color.BLUE);
        labels.add("E2");
        colours.add(Color.CYAN);
        labels.add("F2");
        colours.add(Color.DKGRAY);
        labels.add("G2");
        colours.add(Color.DKGRAY);
        labels.add("H2");
        colours.add(Color.GRAY);

//        LineChartManager lcm = new LineChartManager(chart);

        for (int i = 0; i <= 60; i++) {
            xAxisValues.add(i);
        }
//        虚拟数据
//        List<List<Integer>> yAxisValues = new ArrayList<List<Integer>>();
//        for (int i = 0; i < 16; i++) {
//            List<Integer> line = new ArrayList<Integer>();
//            for (int j = 0; j <= 60; j++) {
//                int n = (int) (Math.random() * 9999);
//                line.add(n);
//            }
//            yAxisValues.add(line);
//        }
        lcm.showLineChart(xAxisValues, yAxisValues, labels, colours);
    }

    private void functionSelect() {
//        functionDataList = new ArrayList<String>();
//        functionDataList.add("对数函数");
//        functionDataList.add("线性函数");
//        functionDataList.add("指数函数");
//        functionDataList.add("多项式函数");
        //适配器
//        functionArrAdapter = new ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_item, functionDataList);
        //设置样式
        //functionArrAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
//        functionSelected.setAdapter(functionArrAdapter);
//        functionSelected.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            // parent： 为控件Spinner   view：显示文字的TextView   position：下拉选项的位置从0开始
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                //获取Spinner控件的适配器
//                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
//                selectedFunction = position;
//                Log.i("====函数下拉框", position + "");
//                Log.i("====函数下拉框", adapter.getItem(position));
//            }
//
//            //没有选中时的处理
//            public void onNothingSelected(AdapterView<?> parent) {
//            }
//        });
    }

    private void sampleSelect() {
        data_list = new ArrayList<String>();
        data_list.add("标准");
        data_list.add("待测");
        data_list.add("空");
        //适配器
        arr_adapter = new ArrayAdapter<String>(this.context, android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        sampleValues.setAdapter(arr_adapter);
        sampleValues.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            // parent： 为控件Spinner   view：显示文字的TextView   position：下拉选项的位置从0开始
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //获取Spinner控件的适配器
                ArrayAdapter<String> adapter = (ArrayAdapter<String>) parent.getAdapter();
                Log.i("====下拉框", adapter.getItem(position));
                selectedSampleValue = position;
            }

            //没有选中时的处理
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }



    /*private void drawFunctionChart(LineChartManager lcm, Map<String, List<Double>> dataMap) {

        List<Double> doubleX = dataMap.get("x");
        List<Integer> intX = new ArrayList<Integer>();
        for (double d : doubleX) {
            intX.add((int) d);
        }
        List<Double> doubleY = dataMap.get("y");
        List<Integer> intY = new ArrayList<Integer>();
        for (double dy : doubleY) {
            intY.add((int) dy);
        }
        lcm.showSingerLineChart(intX, intY, "拟合图", Color.BLACK);
        chart.getAxisLeft().removeAllLimitLines();
        chart.invalidate();//refresh.
    }*/

    /**
     * 初始化数字选择器.
     */
    private void initNumber() {
        // 禁止点击后打开键盘
        numberPicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        numberPicker.setMaxValue(maxNumber);
        numberPicker.setMinValue(minNumber);
        numberPicker.setValue(minNumber);
//        String[] numberStr = {"1","5","10","20","50","100","500","1000"};
//        numberPicker.setDisplayedValues(numberStr);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                Log.i("number-picker", oldVal + "," + newVal);
                stepNumber = newVal;
            }
        });
    }

    /**
     * 移动荧光线阈值
     */
    private void moveLine() {
        LimitLine hightLimit = new LimitLine(CardApplication.limitLineValue, "荧光阈值");
        hightLimit.setLineWidth(2f);
        hightLimit.setTextSize(10f);
        hightLimit.setLineColor(Color.RED);
        hightLimit.setTextColor(Color.RED);
        chart.getAxisLeft().removeAllLimitLines();
        chart.getAxisLeft().addLimitLine(hightLimit);
        chart.invalidate();
    }

    private void initLineChart() {
        chart.setDrawGridBackground(true); // 是否显示表格颜色
        chart.setGridBackgroundColor(Color.WHITE & 0x70FFFFFF); // 表格的的颜色，在这里是是给颜色设置一个透明度
        chart.setGridBackgroundColor(Color.WHITE);
        chart.setTouchEnabled(false); // 设置是否可以触摸
        chart.setDragEnabled(false);// 是否可以拖拽
        chart.setScaleEnabled(false);// 是否可以缩放
        chart.setPinchZoom(false);//X、Y轴同时缩放
        chart.setScaleEnabled(true);//设置推动
        chart.setHighlightPerTapEnabled(true);//双击高亮显示
        chart.setDescription(null);//设置图表描述信息
    }

    /**
     * 设置底部的十六个试管显示数据
     */
    private void initShiGuanView() {
        // List<FunctionData> testData = new ArrayList<FunctionData>(16);
        FunctionData funData = null;
        for (int i = 0; i < 16; i++) {
            funData = new FunctionData();
            CardApplication.shiGuanData.add(funData);
        }
        shiGuanValuesLst = Arrays.asList("*", "A", "B", "C", "D", "E", "F", "G", "H",
                "1", "阴性", "阴性", "阴性", "阴性", "阴性", "阴性", "阴性", "阴性", "2", "阴性", "阴性",
                "阴性", "阴性", "阴性", "阴性", "阴性", "阴性");

        Log.i("LineAivity", shiGuanValuesLst.size() + "============");
        shiguanGrid.setAdapter(new CommonAdapter<String>(getContext(), R.layout.test_tube_16, shiGuanValuesLst) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
                viewHolder.setText(R.id.number, item);
            }
        });
        shiguanGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if ((position >= 10 && position <= 17) || (position >= 19 && position <= 26)) {
                    int index;
                    if (position >= 19) {
                        index = position - 11;
                    } else {
                        index = position - 10;
                    }
                    if (dbFlag) {
                        //触发对比按钮后画单个试管曲线.
                        // yAxisValues.
                        //dramLineChart(lcm);
                    } else {
                        Log.i("输入浓度", concentrationValues.getText().toString());
                        selectedConcentration = Double.parseDouble(concentrationValues.getText().toString());
                        concentrationValues.setText("0");//获取浓度后重置为0.

                        String testTubeDesc = shiGuanValuesLst.get(position);
                        Toast.makeText(LineFragment.super.context, "保存试管" + index + "浓度:" + selectedConcentration,
                                Toast.LENGTH_LONG).show();
                        FunctionData funData = new FunctionData();
                        funData.setTestNumber(index);
                        funData.setTestSample(selectedSampleValue);
                        funData.setTestConcentration(selectedConcentration);
                        funData.setSetValue(true);
                        CardApplication.shiGuanData.set(index, funData);
                        Log.i("试管浓度保存完成", index + "");
                    }
                }
            }
        });
    }

    private void registerReceiveDataBus() {
        Observable<String> receiveDataObservable = RxBus.get().register("receData", String.class);
        receiveDataObservable.subscribe(new Action1<String>() {
            @Override
            public void call(String data) {
                if (null != data && !"".equals(data)) {
                    if (null != yAxisValues && yAxisValues.size() != 0) {
                        final Gson gson = new Gson();
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Handler handler = new Handler(Looper.getMainLooper());
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        lcm.showLineChart(xAxisValues, yAxisValues, labels, colours);
//                                        Toast.makeText(context, gson.toJson(yAxisValues), Toast.LENGTH_LONG).show();
                                    }
                                });
                            }
                        }).start();

//                        Gson gson = new Gson();
//                        Toast.makeText(context, gson.toJson(yAxisValues), Toast.LENGTH_LONG).show();
                    }
//                    yAxisValues = gson.fromJson(data, List.class);
//                    lcm.showLineChart(xAxisValues, yAxisValues, labels, colours);
                }
            }
        });

        Observable<String> temperatureObservable = RxBus.get().register("temperature", String.class);
        temperatureObservable.subscribe(new Action1<String>() {
            @Override
            public void call(final String data) {
                if (null != data && !"".equals(data)) {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Handler handler = new Handler(Looper.getMainLooper());
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    String s = "当前温度" + "\n" + data + "度";
                                    temperature.setText(s);
                                }
                            });
                        }
                    }).start();
                }
            }
        });
    }

    @Override
    public void loadData() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
