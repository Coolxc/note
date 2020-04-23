package cn.yang.cao.utils;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

//hash工具，默认使用MD5
public class HashUtil {

    public static String hash(String string){
        if (string == null){
            return "";
        }
        MessageDigest hash = null;
        try{
            hash = MessageDigest.getInstance("MD5");
            byte[] bytes = hash.digest(string.getBytes("UTF-8"));
            //将哈希后的字符串转换为十六进制
            String result = "";
            for (byte b : bytes){
                //toHexString将32位的int准换成8位的byte，需要相与一下先将byte转换为int
                String temp = Integer.toHexString(b & 0xff);
                //这里是为了统一格式，如当十六进制字符串为9时就转换成09
                if (temp.length() == 1){
                    temp = "0" + temp;
                }
                result += temp;
            }
            return result;
        }catch (NoSuchAlgorithmException e){
            e.printStackTrace();
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
        }
        return "";
    }

    public static Boolean verify(String string, String target){
        return hash(string).equals(target);
    }
}
