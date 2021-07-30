package sg.just4fun.tgasdk.web;

import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class DesEncryptUtils {

    private static List<String>outStrList=new ArrayList<>();
    private static String outStr="";
    private static int num;

    //RSA公钥加密
    public static String encrypt(String str, String publicKey) throws Exception{
            Log.e("googlePayWay","publicKey="+publicKey);
            //base64编码的公钥
            byte[] decoded = base64DecodeToBytes(publicKey);
        PublicKey pubKey =  KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            Log.e("googlePayWay","RSAPublicKey="+pubKey);
            //RSA加密
            Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            outStrList.clear();
            if (str.length()>100){
                Log.e("googlePayWay","大于100="+str);
                if (str.length()%100>0){
                    num=str.length()/100+1;
                }else {
                    num=str.length()/100;
                }
                for (int a=1;a<num+1;a++){
                    if (a==num){
                        if (str.length()%100>0){
                            String substring = str.substring((a-1)*100, 100 * (a-1)+str.length()%100);
                            String outStr = getString(cipher, substring);
                            outStrList.add(outStr);
                        }else {
                            String substring = str.substring((a-1)*100, 100 * a);
                            String outStr = getString(cipher, substring);
                            outStrList.add(outStr);
                        }
                    }else {
                        if (a==1){
                            String substring = str.substring(0, 100);
                            String outStr = getString(cipher, substring);
                            outStrList.add(outStr);
                        }else {
                            String substring = str.substring((a-1)*100, 100 * a);
                            String outStr = getString(cipher, substring);
                            outStrList.add(outStr);
                        }
                    }
                }
                outStr="";
                for (int a=0;a<outStrList.size();a++){
                    outStr+=outStrList.get(a)+",";
                }
                Log.e("googlePayWay","outStr="+outStr);
                return outStr;
            }else {
                Log.e("googlePayWay","小于100="+outStr);
                String outStr = getString(cipher, str);
                return outStr;
            }

    }

    private static String getString(Cipher cipher, String substring) throws UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
        byte[] bytes = substring.getBytes("UTF-8");
        byte[] bytes1 = cipher.doFinal(bytes);
        return base64EncodeFromBytes(bytes1);
    }

    public static String base64EncodeFromBytes(byte[] textBytes) {

        return Base64.getEncoder().encodeToString(textBytes);
    }

    public static byte[] base64DecodeToBytes(String base64code) {
        return Base64.getDecoder().decode(base64code.replace(" ", "+"));//事实上会变成空格的编码除了加号还有&。但base64编码中没有&
    }


}
