package com.sxonecard.http.serialport;

/**
 * Created by Administrator on 2017-5-23.
 */

import android.util.Log;

import com.google.gson.Gson;
import com.sxonecard.CardApplication;
import com.sxonecard.base.RxBus;
import com.sxonecard.util.ByteUtil;
import com.sxonecard.util.Crc16;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android_serialport_api.SerialPort;

/**
 * 串口操作类
 *
 * @author Jerome
 */
public class SerialPortUtil {
    private static String TAG = "serialPort";
    private static SerialPortUtil portUtil;
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private String path = "/dev/ttyS3";
    private int baudrate = 115200;
    private boolean isStop = false;

    //    第一次接收到数值的记录和标记
    private int[] firstNum = new int[16];
    private boolean isFirst = true;

    public static SerialPortUtil getInstance() {
        if (null == portUtil) {
            portUtil = new SerialPortUtil();
            portUtil.onCreate();
        }
        return portUtil;
    }

    /**
     * 初始化串口信息
     */
    public void onCreate() {
        try {
            mSerialPort = new SerialPort(new File(path), baudrate, 0);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();

            mReadThread = new ReadThread();
            isStop = false;
            mReadThread.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送指令到串口
     *
     * @param cmd
     * @return
     */
    public boolean sendCmds(String cmd) {
        boolean result = true;
        byte[] mBuffer = (cmd).getBytes();
        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBuffer);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean sendBuffer(byte[] mBuffer) {
        boolean result = true;
        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBuffer);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        isStop = true;
        if (mReadThread != null) {
            mReadThread.interrupt();
        }
        if (mSerialPort != null) {
            mSerialPort.close();
        }
    }

    private void onDataReceive(byte[] srcBuffer, int size) {
        if (size == 0) {
            Log.d(TAG, "srcBuffer is null ");
            return;
        }

        byte[] destBuff = new byte[srcBuffer.length];
        System.arraycopy(srcBuffer, 0, destBuff, 0, srcBuffer.length);

        int flag = Integer.valueOf(destBuff[7]);
        Integer.parseInt(
                String.valueOf(Integer.valueOf(destBuff[9])) +
                        String.valueOf(Integer.valueOf(destBuff[8])));
        Log.d(TAG, "Command " + flag);
        switch (flag) {
            case 1:
                getCheckFirst(destBuff);
                break;
            case 2:
                //检测核酸设备是否能正常工作.
                getModuleCheckData(destBuff);
                break;
            case 3:
                //接收核酸检测设备检测数据.
                receiveCheckCardData(destBuff);
                break;
            default:
                break;
        }
    }

    /**
     * 检测核酸设备是否能正常工作.
     *
     * @return
     */
    private byte[] moduleCheck() {
        // TODO: 2017/11/25 第二步：发送设备正常检测
        byte[] buff = new byte[12];
        int index = 0;
        index = ByteUtil.byte_tobuff("CV".getBytes(), buff, index);
        index = ByteUtil.int_tobuff(101, buff, index);

        index = ByteUtil.int_tobuff(0xFF, buff, index);
        index = ByteUtil.int_tobuff(0xFF, buff, index);

        index = ByteUtil.int_tobuff(0x01, buff, index);
        index = ByteUtil.int_tobuff(0x00, buff, index);

        index = ByteUtil.int_tobuff(0x02, buff, index);
        index = ByteUtil.int_tobuff(0x00, buff, index);

        index = ByteUtil.int_tobuff(0x00, buff, index);

        int crc_value = Crc16.crc(buff, index, 0xFFFF, 0xA001);

        buff[index++] = ByteUtil.byte_toL(crc_value);
        buff[index++] = ByteUtil.byte_toH(crc_value);

        return buff;
    }

    public void testConn() {
        // TODO: 2017/11/25 发送第一步的测试链接
        Log.d(TAG, "发送测试链接中...");
        sendBuffer(testConnections());
//        5秒后检查设备标志
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                //设备未检查到，跳转至维护页面
//                if (!CardApplication.getInstance().isDeviceOn()) {
//                    RxBus.get().post("testConn", "0");
//                    //继续请求
//                    testConn();
//                }
//            }
//        }, 5 * 1000);
    }

    private byte[] testConnections() {
        // TODO: 2017/11/25 第一步：发送通信正常测试
        byte[] buff = new byte[12];
        int index = 0;

        index = ByteUtil.byte_tobuff("CV".getBytes(), buff, index);

        index = ByteUtil.int_tobuff(101, buff, index);
        index = ByteUtil.int_tobuff(0xFF, buff, index);
        index = ByteUtil.int_tobuff(0xFF, buff, index);

        index = ByteUtil.int_tobuff(0x00, buff, index);
        index = ByteUtil.int_tobuff(0x00, buff, index);

        index = ByteUtil.int_tobuff(0x01, buff, index);

        index = ByteUtil.int_tobuff(0x00, buff, index);
        index = ByteUtil.int_tobuff(0x00, buff, index);

        int crc_value = Crc16.crc(buff, index, 0xFFFF, 0xA001);
        buff[index++] = ByteUtil.byte_toL(crc_value);
        buff[index++] = ByteUtil.byte_toH(crc_value);

        return buff;
    }

    /**
     * 测试第一步通信是否正常返回的数据
     *
     * @param destBuff
     */
    private void getCheckFirst(byte[] destBuff) {
        Log.i(TAG, Integer.valueOf(destBuff[10]) + "," + Integer.valueOf(destBuff[11]));
        if (0x00 == Integer.valueOf(destBuff[10])) {
            byte[] moduleCheckByte = moduleCheck();
            String model = ByteUtil.bytesToHexString(moduleCheckByte);
            SerialPortUtil.getInstance().sendBuffer(moduleCheckByte);
            Log.d(TAG, "检测核酸设备是否能正常工作" + model);
            Log.d(TAG, "测试链接成功");
        }
    }

    private void getModuleCheckData(byte[] destBuff) {
//        模块异常状态
        String moduleDesc = "";
        String status;

        if (85 == Integer.valueOf(destBuff[11])) {
            //各模块正常.
            status = "1";
            CardApplication.setStatus("1");
            CardApplication.getInstance().setDevice(true);
            RxBus.get().post("checkModule", status);
            //3秒后 向核酸检测设备发送开始检测命令.
          /*  new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Log.i(TAG,"====开始发送检测指令...");
                    sendStartTestCmd();
                }
            }, 3 * 1000);*/
            Log.d(TAG, "模块检测成功");
//            sendStartTestCmd();
        } else {
            if (0xBB == Integer.valueOf(destBuff[11])) {
                moduleDesc = "功能禁止";
            } else if (0xBC == Integer.valueOf(destBuff[11])) {
                moduleDesc = "温度超出正常范围";
            } else if (0xBD == Integer.valueOf(destBuff[11])) {
                moduleDesc = "设备不能正常工作";
            } else if (0xBE == Integer.valueOf(destBuff[11])) {
                moduleDesc = "采集荧光数据异常";
            }
            status = "0";
            CardApplication.setStatus(status);
        }
//        CardApplication.setNote(moduleDesc);
    }

    /**
     * @return
     */
    public byte[] sendStartTestCmd() {
        // TODO: 2017/11/25 第三步：开始发送数据
        Log.d(TAG, "第三步：开始请求数据");
        byte[] buff = new byte[12];
        int index = 0;

        index = ByteUtil.byte_tobuff("CV".getBytes(), buff, index);

        index = ByteUtil.int_tobuff(101, buff, index);
        index = ByteUtil.int_tobuff(0xFF, buff, index);
        index = ByteUtil.int_tobuff(0xFF, buff, index);

        index = ByteUtil.int_tobuff(0x03, buff, index);
        index = ByteUtil.int_tobuff(0x00, buff, index);

        index = ByteUtil.int_tobuff(0x03, buff, index);
        index = ByteUtil.int_tobuff(0x00, buff, index);
        index = ByteUtil.int_tobuff(0x00, buff, index);

        int crc_value = Crc16.crc(buff, index, 0xFFFF, 0xA001);
        buff[index++] = ByteUtil.byte_toL(crc_value);
        buff[index++] = ByteUtil.byte_toH(crc_value);

        sendBuffer(buff);
        return buff;
    }

    /**
     * 接收核酸检测设备发送的试管数据.
     *
     * @param destBuff
     */
    private void receiveCheckCardData(byte[] destBuff) {
        if (0x00 == destBuff[10]) {
            // TODO: 2017/11/24 获取到正常的串口数据处理
            //获取温度值并刷新页面(2,E7 743 /10 -10)
            Log.i(TAG, "温度：" + destBuff[11] + "," + destBuff[12] + "," + (destBuff[11] & 0xFF) + "," + (destBuff[12] & 0xFF));
            String s = Integer.toHexString(destBuff[11] & 0xFF) + Integer.toHexString(destBuff[12] & 0xFF);
            Integer integer = Integer.valueOf(s, 16);
            float v = integer / 10f;
            CardApplication.heSuanTemperature = v;
            RxBus.get().post("temperature", CardApplication.heSuanTemperature + "");

            List<List<Integer>> yAxisValues = CardApplication.yAxisValues;
            Gson gson = new Gson();
//            从12位开始，每四组数据为一个荧光管的光强值，共16组荧光管光强值。
//            每次重新获取，会给16组数据中各添加新的光强值
            for (int i = 0; i < 16; i++) {
                String string = Integer.toHexString(destBuff[13 + i * 4] & 0xFF) +
                        Integer.toHexString(destBuff[14 + i * 4] & 0xFF) +
                        Integer.toHexString(destBuff[15 + i * 4] & 0xFF) +
                        Integer.toHexString(destBuff[16 + i * 4] & 0xFF);
                Integer lex = Integer.valueOf(string, 16);

                if (isFirst) {
                    firstNum[i] = lex;
                }

                if (yAxisValues.size() < i + 1) {
                    List<Integer> line = new ArrayList<>();
                    line.add(lex);
                    yAxisValues.add(line);
                } else {
                    yAxisValues.get(i).add(lex);
                }
            }

            Log.i("yAxisValues", "真实核酸检测数据..." + gson.toJson(yAxisValues));



//            设置统一起点（不显示曲线上的数值）
//            筛选记录每组0位的值，后面数据减去此值(修改每个数组的最后一位值)
            for (int i = 0; i < 16; i++) {
                int size = yAxisValues.get(i).size();
                if (isFirst) {
                    yAxisValues.get(i).set(0, 0);
                }
                if (size > 1) {
                    yAxisValues.get(i).set(size - 1, yAxisValues.get(i).get(size - 1) - firstNum[i]);
                }
            }

            Log.i("yAxisValues", "统一核酸检测数据..." + gson.toJson(yAxisValues));

            //            优化曲线，曲线第十个点之后，如果下降趋势大于5则规定只减2
            if (yAxisValues.get(0).size() > 10) {
                for (int i = 0; i < 16; i++) {
                    int size = yAxisValues.get(i).size();
                    int j = yAxisValues.get(i).get(size - 2) - yAxisValues.get(i).get(size - 1);
                    if (j > 5) {
                        yAxisValues.get(i).set(size - 1, yAxisValues.get(i).get(size - 2) - 2);
                    }
                }
            }
            Log.i("yAxisValues", "优化核酸检测数据..." + gson.toJson(yAxisValues));

            CardApplication.yAxisValues = yAxisValues;
            RxBus.get().post("receData", gson.toJson(yAxisValues));

            isFirst = false;
        } else {
            Log.e(TAG, "receive HeSuan data error:" + destBuff[10]);
        }
    }

    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (!isStop && !isInterrupted()) {
                int size;
                try {
                    if (mInputStream == null) {
                        return;
                    }
                    // 读取.
                    byte[] buffer = new byte[512];
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        // TODO: 2017/11/25 获取到串口的数据
                        onDataReceive(buffer, size);
                    }

                    Thread.sleep(300);
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d(TAG, e.getMessage());
                    closeSerialPort();
                    return;
                }
            }
        }
    }
}