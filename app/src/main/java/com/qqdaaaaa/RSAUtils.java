package com.qqdaaaaa;

import android.content.Context;
import android.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

public class RSAUtils {
    public static byte[] encryptData(byte[] paramArrayOfByte, PublicKey paramPublicKey) {
        Cipher localCipher;
        try {
            localCipher = Cipher.getInstance("RSA/None/PKCS1Padding");
            localCipher.init(1, paramPublicKey);
            byte[] arrayOfByte = localCipher.doFinal(paramArrayOfByte);
            return arrayOfByte;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return null;
    }

    public static InputStream loadFile(Context paramContext, String paramString) {
        InputStream localInputStream;
        try {
            localInputStream = paramContext.getAssets().open("rsa_public_key.pem");
            return localInputStream;
        } catch (IOException localIOException) {
            localIOException.printStackTrace();
        }
        return null;
    }

    public static PublicKey loadPublicKey(InputStream paramInputStream)
            throws Exception {
        PublicKey localPublicKey;
        try {
            localPublicKey = loadPublicKey(readKey(paramInputStream));
            return localPublicKey;
        } catch (IOException localIOException) {
            throw new Exception("公钥数据流读取错误");
        } catch (NullPointerException localNullPointerException) {
            throw new Exception("公钥输入流为空");
        }
    }

    public static PublicKey loadPublicKey(String paramString)
            throws Exception {
        byte[] arrayOfByte;
        try {
            arrayOfByte = Base64.decode(paramString, 0);
            RSAPublicKey localRSAPublicKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(arrayOfByte));
            return localRSAPublicKey;
        } catch (NoSuchAlgorithmException localNoSuchAlgorithmException) {
            throw new Exception("无此算法");
        } catch (InvalidKeySpecException localInvalidKeySpecException) {
            throw new Exception("公钥非法");
        } catch (NullPointerException localNullPointerException) {
            throw new Exception("公钥数据为空");
        }
    }

    private static String readKey(InputStream paramInputStream)
            throws IOException {
        BufferedReader localBufferedReader = new BufferedReader(new InputStreamReader(paramInputStream));
        StringBuilder localStringBuilder = new StringBuilder();
        while (true) {
            String str;
            do {
                str = localBufferedReader.readLine();
                if (str == null)
                    return localStringBuilder.toString();
            }
            while (str.charAt(0) == '-');
            localStringBuilder.append(str);
            localStringBuilder.append('\r');
        }
    }
}
