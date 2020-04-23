package cn.yang.cao.utils;

import java.util.Random;

public class KeyUtil {
    //生成随机的唯一主键key。必须加同步关键字，防止多线程获得同一个kry
    public static synchronized String genPriKey(){
        Random random = new Random();
        Integer number = random.nextInt(99999) + 10000;
        return System.currentTimeMillis() + String.valueOf(number);
    }
}
