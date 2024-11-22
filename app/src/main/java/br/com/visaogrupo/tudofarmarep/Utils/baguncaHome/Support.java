package br.com.visaogrupo.tudofarmarep.Utils.baguncaHome;
import android.graphics.Bitmap;
import java.io.ByteArrayOutputStream;


public abstract class Support {
    public static final byte[] KEY_CRIPTY = "oSENHOR3oMeUp4sT0rN4DaMeF4Lt@ra!".getBytes();
    public static final byte[] IV_CRIPTY = "?S4LM0S_23:V3R_1".getBytes();

    public static final Criptho CRIPTHO = new Criptho(KEY_CRIPTY, IV_CRIPTY);

    public static byte[] convertBitmapToArrayByte(Bitmap bitmap) {
        if(bitmap != null) {
            Bitmap bmp = bitmap;
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();

            return byteArray;
        } else {
            return null;
        }
    }

}


