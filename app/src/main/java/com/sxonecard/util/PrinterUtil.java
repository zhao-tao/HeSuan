package com.sxonecard.util;

/**
 * Created by Administrator on 2017-5-23.
 */

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.gson.Gson;
import com.sxonecard.http.bean.GsonData;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import android_serialport_api.SerialPort;

/**
 * 串口操作类
 *
 * @author Jerome
 *
 */
public class PrinterUtil {
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private String path = "/dev/ttyS1";
    private int baudrate = 9600;
    private static PrinterUtil portUtil;


    public static PrinterUtil getInstance() {
        if (null == portUtil) {
            portUtil = new PrinterUtil();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void send(){
        try {
            PRINT_ALIGN_CENTER();
            sendCmds("123456");
            PRINT_LR();

                /*居中*/
            PRINT_ALIGN_CENTER();

                /*标题*/

            PRINT_LR();
                /*正常字体*/
            PRINT_NORMAL_FONT();

            PRINT_LR();

            sendCmds("请依号码顺序等候\r\n本卷限当日使用隔日作废\r\n");

                /*放大字体*/
            PRINT_DOUBLE_FONT();
            sendCmds("等候号码：123456");

            PRINT_NORMAL_FONT();
            PRINT_LR();

                /*左对齐*/
            PRINT_ALIGN_LEFT();

                /*正常字体*/
            PRINT_NORMAL_FONT();

            PRINT_LR();

            //sprintf(str,"终 端 号：%06d",ptr->VendId);
            sendCmds("终 端 号：45678");
            PRINT_LR();
            sendCmds("支付方式：现金");


            PRINT_LR();
            sendCmds("99566544654");
            PRINT_LR();
            PRINT_LR();
            PRINT_LR();
            PRINT_LR();
            PRINT_LR();

            PRINT_CUT_PAPER();
//            while (true){
//
//                Thread.sleep(3000);
//            }
        } catch (Exception e){
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
        try {
            byte[] mBuffer = cmd.getBytes("GBK");
            mOutputStream.write(mBuffer);
        } catch (Exception e) {
            e.printStackTrace();
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

    private void PRINT_CLR_BUFFER() throws IOException {
        sendBuffer(new byte[]{0x1B,0x40});
    }

    private void PRINT_DOUBLE_FONT() throws IOException {
        sendBuffer(new byte[]{0x1B,0x21,0x30});
    }

    private void PRINT_NORMAL_FONT() throws IOException {
        sendBuffer(new byte[]{0x1B,0x21,0x00});
    }

    private void PRINT_ALIGN_LEFT() throws IOException {
        sendBuffer(new byte[]{0x1B,0x61,0x30});
    }

    private void PRINT_BARCODE_WIDTH() throws IOException {
        sendBuffer(new byte[]{0x1D,0x77,0x02});
    }

    private void PRINT_BARCODE_HEIGHT() throws IOException {
        sendBuffer(new byte[]{0x1d,0x68,0x60});//x1D\x68\x60
    }

    private void PRINT_BARCODE_WRT_ASC() throws IOException {
        sendBuffer(new byte[]{0x1D,0x48,0x02});
    }

    private void PRINT_BARCODE_DATA_CODE128() throws IOException {
        sendBuffer(new byte[]{0x1D,0x6B,0x48});
    }

    private void PRINT_CUT_PAPER() throws IOException {
        sendBuffer(new byte[]{0x1B,0x69});
    }

    private void PRINT_LR() throws IOException {
        sendBuffer(new byte[]{0xD,0xA});
    }

    private void PRINT_ALIGN_CENTER() throws IOException {
        sendBuffer(new byte[]{0x1B,0x61,0x31});
    }


}