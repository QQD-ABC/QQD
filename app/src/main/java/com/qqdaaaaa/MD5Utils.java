package com.qqdaaaaa;

import java.security.MessageDigest;
public class MD5Utils {
    private static String Md5_(String paramString, int paramInt) {
        int i = 0;
        String[] arrayOfString = {"WA", "TH", "FU"};
        if (paramString.length() > 9)
            i = paramString.length();
        while (true) {
            String str1 = String.valueOf(i);
            int j = Integer.valueOf(str1.substring(0, 1)).intValue();
            int k = Integer.valueOf(str1.substring(-1 + str1.length())).intValue();
            int l = j + k;
            String str2 = paramString.substring(0, j) + arrayOfString[0] + paramString.substring(j, j + k) + arrayOfString[1] + paramString.substring(k + j, l + k + j) + arrayOfString[2] + paramString.substring(k + l + j);
            if (paramInt < 3)
                str2 = Md5_(getMD5(str2), paramInt + 1);
            return str2;
//            i = 10;
        }
    }

    private static final String getMD5(String paramString) {
        char[] arrayOfChar1 = {48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 65, 66, 67, 68, 69, 70};
//    public final static String arrayOfChar1(String s) {
//        char arrayOfChar1[] = { '0', '1', '2', '3', '4',
//                '5', '6', '7', '8', '9',
//                'A', 'B', 'C', 'D', 'E', 'F' };
        try {
            byte[] btInput = paramString.getBytes();
            //获得MD5摘要算法的 MessageDigest 对象
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            //使用指定的字节更新摘要
            mdInst.update(btInput);
            //获得密文
            byte[] md = mdInst.digest();
            //把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[k++] = arrayOfChar1[byte0 >>> 4 & 0xf];
                str[k++] = arrayOfChar1[byte0 & 0xf];
            }
            return new String(str);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
//}

//        try {
//            byte[] arrayOfByte1 = paramString.getBytes();
//            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
//            localMessageDigest.update(arrayOfByte1);
//            byte[] arrayOfByte2 = localMessageDigest.digest();
//            int i = arrayOfByte2.length;
//            char[] arrayOfChar2 = new char[i * 2];
//            int j = 0;
//            int k = 0;
//            if (j >= i)
//                return new String(arrayOfChar2);
//            int l = arrayOfByte2[j];
//            int i1 = k + 1;
//            arrayOfChar2[k] = arrayOfChar1[(0xF & l >> 4)];
//            k = i1 + 1;
//            arrayOfChar2[i1] = arrayOfChar1[(l & 0xF)];
//            ++j;
//        } catch (Exception localException) {
//        }
//        return null;
//    }

    public static String getMd5End(String paramString) {
        return Md5_(getMD5(paramString), 0).substring(6, 26);
    }
}
