package com.qqdhelper.net;

import android.content.Context;
import android.util.Base64;

import com.qqdhelper.util.MD5Utils;
import com.qqdhelper.util.RSAUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class z {
    public static String c(String paramString) {
        return MD5Utils.getMd5End(paramString);
    }

    private void deleteSoFile(String paramString) {
        new File(paramString).delete();
    }

    private InputStream getInputStream(Context paramContext, String paramString)
            throws IOException {
        return paramContext.getResources().getAssets().open(paramString);
    }

    private FileOutputStream getOutputStream(String paramString)
            throws FileNotFoundException {
        return new FileOutputStream(paramString);
    }

    public static String getRSA(Context paramContext, String paramString) {
        byte[] arrayOfByte1;
        byte[] arrayOfByte2;
        try {
            arrayOfByte2 = RSAUtils.encryptData(paramString.getBytes(), RSAUtils.loadPublicKey(RSAUtils.loadFile(paramContext, "rsa_public_key.pem")));
            arrayOfByte1 = arrayOfByte2;
//            return Base64.encodeToString(arrayOfByte1, 0);
        } catch (Exception localException) {
            localException.printStackTrace();
            arrayOfByte1 = null;
        }
        return Base64.encodeToString(arrayOfByte1, 0);
    }

    private String getSoPath(Context paramContext) {
        return paramContext.getFilesDir() + "/temp";
    }

    public static List<NameValuePair> getSortParams(Context paramContext, HashMap<String, String> paramHashMap1, HashMap<String, String> paramHashMap2) {
        Object[] arrayOfObject2 = new Object[0];
        ArrayList localArrayList = null;
        int k = 0;
        int l = 0;
        paramHashMap2.putAll(paramHashMap1);
        Object[] arrayOfObject1 = paramHashMap2.keySet().toArray();
        Arrays.sort(arrayOfObject1);
        StringBuilder localStringBuilder = new StringBuilder();
        int i = arrayOfObject1.length;

        for (int m = 0; m < arrayOfObject1.length; m++) {
            String a = paramHashMap2.get((arrayOfObject1[m]).toString());
            localStringBuilder.append(a);
        }

        paramHashMap2.put("zz", getRSA(paramContext, c(localStringBuilder.toString())));
        arrayOfObject2 = paramHashMap2.keySet().toArray();
        Arrays.sort(arrayOfObject2);
        localArrayList = new ArrayList();

        for (int m = 0; m < arrayOfObject2.length; m++) {
            String a = paramHashMap2.get((arrayOfObject2[m]).toString());
//            localStringBuilder.append(a);
            BasicNameValuePair localBasicNameValuePair = new BasicNameValuePair((String) arrayOfObject2[m], (String) a);
            localArrayList.add(localBasicNameValuePair);
        }

        return localArrayList;
//        int j = 0;
//        if (j >= i) {
//            paramHashMap2.put("zz", getRSA(paramContext, c(localStringBuilder.toString())));
//            arrayOfObject2 = paramHashMap2.keySet().toArray();
//            Arrays.sort(arrayOfObject2);
//            localArrayList = new ArrayList();
//            k = arrayOfObject2.length;
//            l = 0;
//        }
//        while (true) {
//            while (l >= k) {
//                String a = paramHashMap2.get((arrayOfObject1[j]).toString());
//                localStringBuilder.append(a);
//                ++j;
//                return localArrayList;
//            }
//            Object localObject = arrayOfObject2[l];
//            paramHashMap2.get(localObject);
//            BasicNameValuePair localBasicNameValuePair = new BasicNameValuePair((String) localObject, (String) paramHashMap2.get(localObject));
//            localArrayList.add(localBasicNameValuePair);
//            ++l;
//        }
    }

    private void loadLibrary(String paramString) {
        System.load(paramString);
    }


}
