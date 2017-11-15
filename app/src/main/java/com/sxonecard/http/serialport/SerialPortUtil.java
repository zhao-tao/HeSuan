package com.sxonecard.http.serialport;

/**
 * Created by Administrator on 2017-5-23.
 */

import android.os.Handler;
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
                //测试链接.
                Log.d(TAG, "链接成功");
                byte[] moduleCheckByte = moduleCheck();
                String model = ByteUtil.bytesToHexString(moduleCheckByte);
                SerialPortUtil.getInstance().sendBuffer(moduleCheckByte);
                Log.d(TAG, "检测核酸设备是否能正常工作" + model);
                break;
            case 2:
                //检测核酸设备是否能正常工作.
                getModuleCheckData(destBuff);
                Log.d(TAG, "模块检测成功");
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

    /**
     * 测试连接
     */
    public void testConn() {
        Log.d(TAG, "发送测试链接中...");
        sendBuffer(testConnections());
        //5秒后检查设备标志
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //设备未检查到，跳转至维护页面
                if (!CardApplication.getInstance().isDeviceOn()) {
                    RxBus.get().post("testConn", "0");
                    //继续请求
                    testConn();
                }
            }
        }, 5 * 1000);
    }

    private byte[] testConnections() {
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

    private void getModuleCheckData(byte[] destBuff) {
        String moduleDesc = "";
        String status;

        if (0x55 == Integer.valueOf(destBuff[10]) || 0xFF == Integer.valueOf(destBuff[10])) {
            //各模块正常.
            status = "1";
            moduleDesc = "各模块正常";
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
        //CardApplication.setNote(moduleDesc);
    }

    public byte[] sendStartTestCmd() {
        byte[] buff = new byte[12];
        int index = 0;

        index = ByteUtil.byte_tobuff("CV".getBytes(), buff, index);

        index = ByteUtil.int_tobuff(101, buff, index);
        index = ByteUtil.int_tobuff(0xFF, buff, index);
        index = ByteUtil.int_tobuff(0xFF, buff, index);

        index = ByteUtil.int_tobuff(0x02, buff, index);
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
            //获取温度值并刷新页面
            CardApplication.heSuanTemperature = Integer.valueOf(destBuff[11]);
            RxBus.get().post("temperature", CardApplication.heSuanTemperature + "");

            List<List<Integer>> yAxisValues = CardApplication.yAxisValues;
            Gson gson = new Gson();
//            从12位开始，每四组数据为一个荧光管的光强值，共16组荧光管光强值。
//            每次重新获取，会给16组数据中各添加新的光强值。
            for (int i = 0; i < 16; i++) {
                int a = Integer.valueOf(destBuff[12 + i * 4]);
                int b = Integer.valueOf(destBuff[13 + i * 4]);
                int c = Integer.valueOf(destBuff[14 + i * 4]);
                int d = Integer.valueOf(destBuff[15 + i * 4]);
                Integer e = a + b + c + d;

                if (yAxisValues.size() < i + 1) {
                    List<Integer> line = new ArrayList<>();
                    line.add(e);
                    yAxisValues.add(line);
                } else {
                    yAxisValues.get(i).add(e);
                }
            }
            CardApplication.yAxisValues = yAxisValues;
            RxBus.get().post("receData", gson.toJson(yAxisValues));
            Log.i("yAxisValues", "接收核酸检测数据..." + gson.toJson(yAxisValues));
        } else {
            Log.e(TAG, "receive HeSuan data error:" + destBuff[10]);
        }
//        String byteString = ByteUtil.bytesToHexString(destBuff);
//        String balance = byteString.substring(11 * 2, 15 * 2);
//        String cardNumber = byteString.substring(19 * 2, 23 * 2);
//        String cardNumDate = byteString.substring(27 * 2, 31 * 2);
//        String cardType = byteString.substring(31 * 2, 33 * 2);
//        String cardStatus = byteString.substring(33 * 2, 34 * 2);
//        int value = 0;
//        try {
//            value = Integer.parseInt(balance, 16);
//        } catch (Exception e) {
//
//        }
//        RechargeCardBean cardBean = new RechargeCardBean(cardNumber,
//                value, cardNumDate, cardType, cardStatus);
//        Gson gson = new Gson();
//        RxBus.get().post("checkCard", gson.toJson(cardBean));
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