package com.sxonecard.util.vender;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

public class clsToolBox {

    static short s(byte[] b) {
        return (short) (((b[1] & 0x000000FF) << 8) | (0x000000FF & b[0]));
    }

    static short little_byte2short(byte[] b, int startindex) {
        return (short) (((b[startindex + 1] & 0x000000FF) << 8) | (0x000000FF & b[startindex]));
    }

    static short big_byte2short(byte[] b) {
        return (short) (((b[0] & 0x000000FF) << 8) | (0x000000FF & b[1]));

    }

    static short big_byte2short(byte[] b, int startindex) {
        return (short) (((b[startindex] & 0x000000FF) << 8) | (0x000000FF & b[startindex + 1]));

    }


    /**
     * 基于位移的int转化成byte[]
     *
     * @param number 将要转换的数字
     * @return
     */
    public static byte[] intToByte(int number) {
        byte[] abyte = new byte[4];
        // "&" 与（AND），对两个整型操作数中对应位执行布尔代数，两个位都为1时输出1，否则0。
        abyte[0] = (byte) (0xff & number);
        // ">>"右移位，若为正数则高位补0，若为负数则高位补1
        abyte[1] = (byte) ((0xff00 & number) >> 8);
        abyte[2] = (byte) ((0xff0000 & number) >> 16);
        abyte[3] = (byte) ((0xff000000 & number) >> 24);
        return abyte;
    }

    /**
     * 取得时间字符串，形如：20090831150534
     *
     * @param date 日期
     * @return 返回形如"yyyyMMddHHmmss"的字符串
     */
    public static String getTimeLongString(java.util.Date date) {
        String TemString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TimeZone timezone = TimeZone.getTimeZone("Asia/Shanghai");
        sdf.setTimeZone(timezone);
        TemString = sdf.format(date);
        return TemString;
    }

    /**
     * 基于位移的int转化成byte[]
     *
     * @param number 将要转换的数字
     * @param abyte  目标数组
     * @param index  存储起始索引
     * @return
     */
    public static int intToByte(int number, byte[] abyte, int index) {
        // "&" 与（AND），对两个整型操作数中对应位执行布尔代数，两个位都为1时输出1，否则0。
        abyte[index++] = (byte) (0xff & number);
        // ">>"右移位，若为正数则高位补0，若为负数则高位补1
        abyte[index++] = (byte) ((0xff00 & number) >> 8);
        abyte[index++] = (byte) ((0xff0000 & number) >> 16);
        abyte[index++] = (byte) ((0xff000000 & number) >> 24);
        return index;
    }

    public static int big_int2byte(int number, byte[] abyte, int index) {
        // "&" 与（AND），对两个整型操作数中对应位执行布尔代数，两个位都为1时输出1，否则0。
        abyte[index++] = (byte) ((0xff000000 & number) >> 24);
        // ">>"右移位，若为正数则高位补0，若为负数则高位补1
        abyte[index++] = (byte) ((0xff0000 & number) >> 16);
        abyte[index++] = (byte) ((0xff00 & number) >> 8);
        abyte[index++] = (byte) (0xff & number);
        return index;

    }

    public static int big_short2byte(short number, byte[] abyte, int index) {
        // "&" 与（AND），对两个整型操作数中对应位执行布尔代数，两个位都为1时输出1，否则0。
        abyte[index++] = (byte) ((0xff00 & number) >> 8);

        abyte[index++] = (byte) (0xff & number);
        // ">>"右移位，若为正数则高位补0，若为负数则高位补1

        return index;

    }

    /**
     * @param number
     * @param abyte
     */
    public static int shortToByte(short number, byte[] abyte, int index) {
        // "&" 与（AND），对两个整型操作数中对应位执行布尔代数，两个位都为1时输出1，否则0。
        abyte[index++] = (byte) (0xff & number);
        // ">>"右移位，若为正数则高位补0，若为负数则高位补1
        abyte[index++] = (byte) ((0xff00 & number) >> 8);

        return index;
    }

    /**
     * 基于位移的 byte[]转化成int
     *
     * @param bytes
     * @return number
     */

    public static int little_byte2int(byte[] bytes) {
        int number = bytes[0] & 0xFF;
        // "|="按位或赋值。
        number |= ((bytes[1] << 8) & 0xFF00);
        number |= ((bytes[2] << 16) & 0xFF0000);
        number |= ((bytes[3] << 24) & 0xFF000000);
        return number;
    }


    /**
     * 基于位移的 byte[]转化成int
     *
     * @param bytes
     * @param index
     * @return
     */
    public static int little_byte2int(byte[] bytes, int index) {
        int number = bytes[index + 0] & 0xFF;
        // "|="按位或赋值。
        number |= ((bytes[index + 1] << 8) & 0xFF00);
        number |= ((bytes[index + 2] << 16) & 0xFF0000);
        number |= ((bytes[index + 3] << 24) & 0xFF000000);
        return number;
    }

    public static Calendar ParseDateString(String datestring) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");

        java.util.Date date;
        Calendar c = Calendar.getInstance();
        try {
            date = sdf.parse(datestring);
            c.setTimeInMillis(date.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return c;
    }


    /**
     * 基于位移的 byte[]转化成int
     *
     * @param bytes
     * @return int  number
     */

    public static int big_byte2int(byte[] bytes) {
        int number = bytes[3] & 0xFF;
        // "|="按位或赋值。
        number |= ((bytes[2] << 8) & 0xFF00);
        number |= ((bytes[1] << 16) & 0xFF0000);
        number |= ((bytes[0] << 24) & 0xFF000000);
        return number;
    }

    public static int big_byte2int(byte[] bytes, int index) {
        int number = bytes[index + 3] & 0xFF;
        // "|="按位或赋值。
        number |= ((bytes[index + 2] << 8) & 0xFF00);
        number |= ((bytes[index + 1] << 16) & 0xFF0000);
        number |= ((bytes[index] << 24) & 0xFF000000);
        return number;
    }

    public static int ParseInt(String str_sid) {
        try {
            return Integer.parseInt(str_sid);
        } catch (Exception e) {
            return 0;
        }

    }

    /**
     * 将一个asc数据流转化成一个字符串
     *
     * @param b
     * @param start_index
     * @return
     */
    public static String AscByte2StringTerminalByNull(byte[] b, int start_index) {
        if (b == null) {
            return "";
        }
        int end_index = start_index;
        for (; end_index < b.length; end_index++) {
            if (b[end_index] == 0) {
                break;
            }
        }


        try {
            return new String(b, start_index, end_index - start_index, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }

    /**
     * 将一个asc数据流转化成一个字符串
     *
     * @param b
     * @param start_index
     * @return
     */
    public static String AscByte2StringByEndIndex(byte[] b, int start_index, int end_index) {
        if (b == null) {
            return "";
        }

        try {
            return new String(b, start_index, end_index - start_index, "GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return "";
    }

    public static void printX(byte x) {
        System.out.print(String.format("%02X,", x));
    }


    public static void printX(byte[] b, int len) {
        int i;
        for (i = 0; i < len; i++) {
            printX(b[i]);
        }
        System.out.println();
    }

    public static String bytesToHexString(byte[] b, int start, int len) {
        StringBuilder sb = new StringBuilder();

        for (int i = start; i < len + start; i++) {
            sb.append(String.format("%02X", b[i]));
        }

        return sb.toString();
    }


    /**
     * 取得日期时间字符串，形如：2009-8-31 16:11:03
     *
     * @param 无
     * @return 日期时间字符串，形如：2009-08-31 16:11:03
     */
    public static String getDateTimeString() {
        String TemString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //TimeZone timezone = TimeZone.getTimeZone("Asia/Shanghai");
        //sdf.setTimeZone(timezone);
        TemString = sdf.format(new java.util.Date(System.currentTimeMillis()));
        return TemString;
    }

    /**
     * 取得日期时间字符串，形如：2009-8-31 16:11:03
     *
     * @param date java.util.Date，指定的日期
     * @return 日期时间字符串，形如：2009-08-31 16:11:03
     */
    public static String getDateTimeString(java.util.Date date) {
        String TemString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //TimeZone timezone = TimeZone.getTimeZone("Asia/Shanghai");
        //sdf.setTimeZone(timezone);
        TemString = sdf.format(date);
        return TemString;
    }

    /**
     * 获取0~999999之间的一个整数
     *
     * @return 一个不超过999999的整数
     */
    public static int getRandomNumber() {
        return (int) (Math.random() * 1000000);
    }

    /**
     * 取得时间字符串，形如：20090831150534
     *
     * @param 无
     * @return 返回形如"yyyyMMddHHmmss"的字符串
     */
    public static String getTimeString(java.util.Date date) {
        String TemString = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        TimeZone timezone = TimeZone.getTimeZone("Asia/Shanghai");
        sdf.setTimeZone(timezone);
        TemString = sdf.format(date);
        return TemString;
    }

    /**
     * 取得时间字符串，形如：20090831150534
     *
     * @param 无
     * @return 返回形如"yyyyMMddHHmmss"的字符串
     */
    public static String getTimeString() {
        return getTimeString(new java.util.Date());
    }

    /**
     * 获取交易流水号
     *
     * @param SellerId
     * @return 返回交易流水号 形如：090903104822009XXXX，其中009时售货机编号，最后为4位随机码
     */
    public static String MakeTradeID() {
        return getTimeString() + String.format("%06d", getRandomNumber());
    }

    /**
     * 生成sign MD5 加密 toUpperCase
     *
     * @param map
     * @param paternerKey
     * @return
     */
    public static String generateSign(Map<String, String> map, String paternerKey) {
        Map<String, String> tmap = MapUtil.order(map);
        if (tmap.containsKey("Md5Code")) {
            tmap.remove("Md5Code");
        }
        String str = MapUtil.mapJoin(tmap, false, false);
        System.out.println(str + "&key=" + paternerKey);
        return getMd5(str + "&key=" + paternerKey).toUpperCase();
    }


    /**
     * 获取一个字符串的MD5码
     *
     * @param plainText 源字符串
     * @return 返回字符串的MD5码
     * @throws InvalidParameterException
     * @author Administrator
     */
    public static String getMd5(String plainText) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(plainText.getBytes());
            byte b[] = md.digest();
            int i;
            StringBuffer buf = new StringBuffer("");
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            String TemStr = buf.toString().toUpperCase();
            //System.out.println("md5code of "+plainText+":"+TemStr);
            return TemStr;
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }


    /**
     * 获取外置SD卡路径
     *
     * @return 应该就一条记录或空
     */
    public static ArrayList<String> getExtSDCardPath() {
        ArrayList<String> lResult = new ArrayList<String>();
        try {
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                if (line.contains("external_sd")) {
                    String[] arr = line.split(" ");
                    String path = arr[1];
                    File file = new File(path);
                    if (file.isDirectory()) {
                        lResult.add(path);
                    }
                }
            }
            isr.close();
        } catch (Exception e) {
        }
        return lResult;
    }


    /**
     * @功能: 10进制串转为BCD码
     * @参数: 10进制串
     * @结果: BCD码
     */
    public static byte[] str2Bcd(String asc) {
        int len = asc.length();
        int mod = len % 2;
        if (mod != 0) {
            asc = "0" + asc;
            len = asc.length();
        }
        byte abt[] = new byte[len];
        if (len >= 2) {
            len = len / 2;
        }
        byte bbt[] = new byte[len];
        abt = asc.getBytes();
        int j, k;
        for (int p = 0; p < asc.length() / 2; p++) {
            if ((abt[2 * p] >= '0') && (abt[2 * p] <= '9')) {
                j = abt[2 * p] - '0';
            } else if ((abt[2 * p] >= 'a') && (abt[2 * p] <= 'z')) {
                j = abt[2 * p] - 'a' + 0x0a;
            } else {
                j = abt[2 * p] - 'A' + 0x0a;
            }
            if ((abt[2 * p + 1] >= '0') && (abt[2 * p + 1] <= '9')) {
                k = abt[2 * p + 1] - '0';
            } else if ((abt[2 * p + 1] >= 'a') && (abt[2 * p + 1] <= 'z')) {
                k = abt[2 * p + 1] - 'a' + 0x0a;
            } else {
                k = abt[2 * p + 1] - 'A' + 0x0a;
            }
            int a = (j << 4) + k;
            byte b = (byte) a;
            bbt[p] = b;
        }
        return bbt;
    }

    /* CRC校验方法
    * @param buff，待校验数据
    * @param initial,初始值，一般取0xFFFF或0x0
    * @param type,校验字,一般取0x1021
    * @return
    */
    public static String crc(byte[] buff, int initial, int type) {
        int crc_reg = initial;
        int current = 0;
        for (int i = 0; i < buff.length; i++) {
            current = ((0xFF & buff[i]) << 8) & 0xFFFF;
            for (int j = 0; j < 8; j++) {
                short temp = (short) (crc_reg ^ current);
                if (temp < 0)
                    crc_reg = (crc_reg << 1) ^ type;
                else
                    crc_reg = (crc_reg << 1) & 0xFFFF;
                current = (current << 1) & 0xFFFF;
            }
        }
        return String.format("%04x", 0xFFFF & crc_reg).toUpperCase();
//		return Integer.toHexString(0xFFFF & crc_reg).toUpperCase();
    }
}
