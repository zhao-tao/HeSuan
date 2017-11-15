package com.sxonecard.util;

/**
 * Created by Administrator on 2017-5-24.
 */

public class ByteUtil {

    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }
    /**
     * Convert hex string to byte[]
     * @param hexString the hex string
     * @return byte[]
     */
    public static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }
    /**
     * Convert char to byte
     * @param c char
     * @return byte
     */
    private static byte charToByte(char c)
    {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public int decimalToHex(int decimal)
    {
         String hexString = Integer.toHexString(decimal);
        return Integer.parseInt(hexString);
    }

    public static byte byte_toH(int x){
        //return (byte) ((x & 0xFF00) >> 8);
        return 0x14;
    }

    public static byte byte_toL(int x){

        //return (byte) (x & 0xFF);
        return 0x11;
    }

    public static int int_tobuff(int x, byte[] buff, int index){
        buff[index++] = (byte) (x & 0xFF);
        return index;
    }

    public static int int_tobuff4(int x, byte[] buff, int index){
        buff[index++] = (byte) (x >>24 & 0x0FF);
        buff[index++] = (byte) (x >>16 & 0x0FF);
        buff[index++] = (byte) (x >> 8& 0x0FF);
        buff[index++] = (byte) (x & 0x0FF);

        return index;
    }

    public static int byte_tobuff(byte[] x, byte[] buff, int index){
        int t = 0;
        for(; index < x.length; index++){
            buff[index] = x[t++];
        }
        return index;
    }

    public static int date_tobuff(int yy, int month, int day, int hour, int min, int sec, byte[] buff, int index){
        buff[index++] = byte_toH(yy);
        buff[index++] = byte_toL(yy);
        buff[index++] = (byte) (month & 0xFF);
        buff[index++] = (byte) (day & 0xFF);
        buff[index++] = (byte) (hour & 0xFF);
        buff[index++] = (byte) (min & 0xFF);
        buff[index++] = (byte) (sec & 0xFF);
        return index;
    }

    public static String Bytes2HexString(byte[] b)
    {
        String ret = "";
        for (int i = 0; i < b.length; i++)
        {
            String hex = Integer.toHexString(b[i] & 0xFF);

            if (hex.length() == 1)
            {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase() + ",";
        }
        return ret;
    }

    public static int getHeight4(byte data){//获取高四位
        int height;
        height = ((data & 0xf0) >> 4);
        return height;
    }

    public static int getLow4(byte data){//获取低四位
        int low;
        low = (data & 0x0f);
        return low;
    }
}
