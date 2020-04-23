package cn.yang.cao.utils;

import cn.yang.cao.enums.CodeEnum;

public class EnumUtil {
    //入参和返回值只能是继承CodeEnum接口的子类
    public static <T extends CodeEnum> T getByCode(Integer code, Class<T> enumClass){
        for (T each : enumClass.getEnumConstants()){
            if (each.getCode().equals(code)){
                return each;
            }
        }
        return null;
    }
}
