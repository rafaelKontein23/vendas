package br.com.visaogrupo.tudofarmarep.Utils.bagunçaHome;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Criptho {

    public static final int NORMAL_MODE = 0;
    public static final int BASE64_MODE = 1;
    private static final String TAG = "Criptho";

    private byte[] mKey = {};
    private byte[] mIV = {};

    public Criptho(byte[] key, byte[] iv) {
        this.mKey = key;
        this.mIV = iv;
    }

    public String encode(String value, int mode) throws Exception {
        Cipher cipher;
        byte[] bytes;

        cipher = this.getCipher(Cipher.ENCRYPT_MODE);
        bytes = cipher.doFinal(value.getBytes(StandardCharsets.UTF_8));

        if (mode == Criptho.BASE64_MODE)
            bytes = Base64.encodeToString(bytes, Base64.DEFAULT).getBytes(StandardCharsets.UTF_8);

        return new String(bytes, 0, bytes.length);
    }

    public String encodeBitmap(Bitmap bitmap, int mode) throws Exception {

        byte[] bytes = Support.convertBitmapToArrayByte(bitmap);

        Log.i(TAG, "encodeBitmap Trace bytes: " + bytes);

        if (mode == Criptho.BASE64_MODE && bytes != null)
            bytes = Base64.encodeToString(bytes, Base64.DEFAULT).getBytes(StandardCharsets.UTF_8);

        return new String(bytes, 0, bytes.length);
    }



    public String decode(String value, int mode) throws Exception {
        Cipher cipher;
        byte[] bytes;

        cipher = this.getCipher(Cipher.DECRYPT_MODE);

        if (mode == Criptho.BASE64_MODE)
            bytes = cipher.doFinal(Base64.decode(value.getBytes(), Base64.DEFAULT));
        else
            bytes = cipher.doFinal(value.getBytes());

        return new String(bytes, 0, bytes.length);
    }

    private Cipher getCipher(int mode) throws Exception {

        SecretKeySpec skeySpec = new SecretKeySpec(mKey, "AES");
        IvParameterSpec ivSpec = new IvParameterSpec(mIV);

        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS7Padding");
        cipher.init(mode, skeySpec, ivSpec);
        return cipher;
    }
}
