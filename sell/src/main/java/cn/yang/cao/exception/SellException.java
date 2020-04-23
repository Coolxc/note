package cn.yang.cao.exception;

import cn.yang.cao.enums.ResultEnum;
import lombok.Getter;

@Getter
public class SellException extends RuntimeException {
    private Integer code;

    public SellException(ResultEnum resultEnum) {
        super((resultEnum.getMessage()));
        this.code = resultEnum.getCode();
    }

    public SellException(Integer code, String msg) {
        super(msg);
        this.code = code;
    }
}
