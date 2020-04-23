package cn.yang.cao.handler;

import cn.yang.cao.VO.ResultVO;
import cn.yang.cao.exception.SellAuthorizeException;
import cn.yang.cao.exception.SellException;
import cn.yang.cao.utils.ResultVOUtil;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class SellExceptionHandler {

    //拦截登录异常
    @ExceptionHandler(SellAuthorizeException.class)
    public String failLogin(){
        return "redirect:/";
    }

    //拦截其他异常
    @ExceptionHandler(SellException.class)
    @ResponseBody
    public ResultVO error(SellException e){
        return ResultVOUtil.error(e.getCode(), e.getMessage());
    }
}
