package com.sxonecard.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.YAxis;
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
    //    试管表格显示的参数值
    private List<String> shiGuanValuesList = new ArrayList<>();
    //是否进行对比按钮标识.
    private boolean dbFlag;
    private CommonAdapter<String> valueAdapter;
    private LineChartManager lcm = null;

    //试管对比的参数
    private List<List<Integer>> abValues;
    //当前点击的试管在表格中的位置
    private int clickPosition;
    //图表空数组
    public List<List<Integer>> nullValues = new ArrayList<List<Integer>>();

    @Override
    public int getLayoutId() {
        return R.layout.mp_line;
    }

    @Override
    public void initView() {
        lcm = new LineChartManager(chart);
        initListener();
//        初始化阈值步进值选择器
        initNumber();
        //函数下拉框.
//        functionSelect();
        initLineChart();
        initShiGuanView();
        registerReceiveDataBus();
        drawLineChart(lcm);
    }

    private void initListener() {
        startSendRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SerialPortUtil.getInstance().sendStartTestCmd();
                startSendRequest.setEnabled(false);
            }
        });

        duibi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LineFragment.super.context, "请选择要对比的试剂", Toast.LENGTH_SHORT).show();
                duibi.setEnabled(false);
                dbFlag = true;
                lcm.showLineChart(xAxisValues, nullValues, labels, colours);
            }
        });

        huanyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!dbFlag) {
                    return;
                }
                dbFlag = false;
                valueAdapter.notifyDataSetChanged();
                duibi.setEnabled(true);
                if (null != abValues) {
                    abValues.clear();
                }

                drawLineChart(lcm);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
//                startActivity(new Intent(LineFragment.super.getContext(), HeSuanActivity.class));
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
    }

    List<String> labels = new ArrayList<String>();
    List<Integer> colours = new ArrayList<Integer>();
    List<Integer> xAxisValues = new ArrayList<Integer>();

    private void drawLineChart(LineChartManager lcm) {
        colours.add(Color.BLACK);
        labels.add("A1");
        colours.add(Color.BLUE);
        labels.add("B1");
        colours.add(Color.CYAN);
        labels.add("C1");
        colours.add(Color.DKGRAY);
        labels.add("D1");
        colours.add(Color.GRAY);
        labels.add("E1");
        colours.add(Color.GREEN);
        labels.add("F1");
        colours.add(Color.LTGRAY);
        labels.add("G1");
        colours.add(Color.MAGENTA);
        labels.add("H1");
        colours.add(Color.RED);
        labels.add("A2");
        colours.add(Color.TRANSPARENT);
        labels.add("B2");
        colours.add(Color.YELLOW);
        labels.add("C2");
        colours.add(Color.BLUE);
        labels.add("D2");
        colours.add(Color.CYAN);
        labels.add("E2");
        colours.add(Color.DKGRAY);
        labels.add("F2");
        colours.add(Color.DKGRAY);
        labels.add("G2");
        colours.add(Color.GRAY);
        labels.add("H2");

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

    /**
     * 初始化图表参数
     */
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

        chart.getAxisLeft();
    }

    /**
     * 设置底部的十六个试管显示数据
     */
    private void initShiGuanView() {
        FunctionData funData;
        for (int i = 0; i < 16; i++) {
            funData = new FunctionData();
            CardApplication.shiGuanData.add(funData);
        }

        shiGuanValuesList = Arrays.asList("*", "A", "B", "C", "D", "E", "F", "G", "H",
                "1", "空", "空", "空", "空", "空", "空", "空", "空", "2", "空", "空",
                "空", "空", "空", "空", "空", "空");

        valueAdapter = new CommonAdapter<String>(getContext(), R.layout.test_tube_16, shiGuanValuesList) {
            @Override
            protected void convert(ViewHolder viewHolder, String item, int position) {
//                非对比更改文字，对比更改文字颜色
                TextView view = viewHolder.getView(R.id.number);
                if (!dbFlag) {
                    viewHolder.setText(R.id.number, item);
                    view.setTextColor(Color.BLACK);
                } else {
                    if (position == clickPosition) {

                        if (view.getCurrentTextColor() == Color.BLACK) {
                            view.setTextColor(Color.RED);
                        } else {
                            view.setTextColor(Color.BLACK);
                        }
                    }
                }
            }
        };

        shiguanGrid.setAdapter(valueAdapter);

        shiguanGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if ((position >= 10 && position <= 17) || (position >= 19 && position <= 26)) {
                    clickPosition = position;
                    int index;
                    if (position >= 19) {
                        index = position - 11;
                    } else {
                        index = position - 10;
                    }
                    if (dbFlag) {
                        //触发对比按钮后画单个试管曲线.表格中添加背景色标注(再次点击取消显示)
                        if (CardApplication.shiGuanData.get(index).getTestSample() == 0) {
                            Toast.makeText(context, "空值无法加入对比！", Toast.LENGTH_SHORT).show();
                        } else {
                            if (null == abValues) {
                                abValues = new ArrayList<List<Integer>>();
                            }
                            if (abValues.contains(yAxisValues.get(index))) {
                                abValues.remove(yAxisValues.get(index));
                            } else {
                                abValues.add(yAxisValues.get(index));
                            }
                            valueAdapter.notifyDataSetChanged();
                            if (abValues.size() > 0) {
                                lcm.showLineChart(xAxisValues, abValues, labels, colours);
                            } else {
                                lcm.showLineChart(xAxisValues, nullValues, labels, colours);
                            }
                        }
                    } else {
                        showSetDialog(index);
                    }
                }
            }
        });
    }

    /**
     * 只展示部分曲线显示(0~15之内任意组)
     */
    private void showLessLine(int[] index) {

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
                                    }
                                });
                            }
                        }).start();
                    }
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

    int yourChoice;

    /**
     * 设置试管参数的对话框（在开始接收数据之前）
     * 入参:试管的编号
     */
    private void showSetDialog(final int index) {
        final String[] items = {"空", "标准", "待测"};
        final EditText editText = new EditText(context);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText.setHint("请在此处输入试剂浓度值");
        Double testConcentration = CardApplication.shiGuanData.get(index).getTestConcentration();
        if (0.0 != testConcentration) {
            editText.setText(testConcentration.toString());
        }

        yourChoice = CardApplication.shiGuanData.get(index).getTestSample();
        AlertDialog.Builder setDialog = new AlertDialog.Builder(context);
        setDialog.setTitle(indexToTable(index) + "试管参数设置：");

        setDialog.setSingleChoiceItems(items, yourChoice, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                yourChoice = which;
            }
        });
        setDialog.setView(editText);

        setDialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//               1 将设置的值保存到全局参数，2 将当前的值显示在表格中
                if (yourChoice != 0) {
                    CardApplication.shiGuanData.get(index).setTestSample(yourChoice);
                    if (TextUtils.isEmpty(editText.getText())) {
                        CardApplication.shiGuanData.get(index).setTestConcentration(0);
                        shiGuanValuesList.set(indexForValue(index), items[yourChoice]);
                    } else {
                        CardApplication.shiGuanData.get(index).setTestConcentration(Double.valueOf(editText.getText().toString()));
                        shiGuanValuesList.set(indexForValue(index), items[yourChoice] + " " + editText.getText());
//                      测试完成后换行添加显示结果 + "\n" + "阴性"
                    }
                } else {
                    shiGuanValuesList.set(indexForValue(index), "空");
                    CardApplication.shiGuanData.get(index).setTestSample(0);
                    CardApplication.shiGuanData.get(index).setTestConcentration(0);
                }
                valueAdapter.notifyDataSetChanged();
            }
        });
        setDialog.show();
    }

    /**
     * 试管编号转换为表格坐标(试管编号从0开始)
     *
     * @param index
     */
    private String indexToTable(int index) {
        String[] s = {"A", "B", "C", "D", "E", "F", "G", "H"};
        String num = null;
        if (index < 8) {
            num = s[index] + 1;
        } else {
            num = s[index - 8] + 2;
        }
        return num;
    }

    /**
     * 试管编号对应表格的编号
     *
     * @param index
     */
    private int indexForValue(int index) {
        if (index < 8) {
            index = index + 10;
        } else {
            index = index + 11;
        }
        return index;
    }
}
