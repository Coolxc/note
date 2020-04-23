package cn.yang.cao.controller;

import cn.yang.cao.VO.ResultVO;
import cn.yang.cao.constant.CookieConstant;
import cn.yang.cao.constant.RedisConstant;
import cn.yang.cao.dataobject.OpenUser;
import cn.yang.cao.dataobject.SellerInfo;
import cn.yang.cao.enums.ResultEnum;
import cn.yang.cao.exception.SellException;
import cn.yang.cao.form.OrderForm;
import cn.yang.cao.form.SellerForm;
import cn.yang.cao.form.UserForm;
import cn.yang.cao.form.UserRegisterForm;
import cn.yang.cao.service.OpenUserService;
import cn.yang.cao.utils.CookieUtil;
import cn.yang.cao.utils.HashUtil;
import cn.yang.cao.utils.ResultVOUtil;
import cn.yang.cao.utils.UserForm2User;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.apache.bcel.classfile.Module;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/user")
@Slf4j
public class OpenUserController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    OpenUserService userService;

    @PostMapping("/create")
    public ResultVO<OpenUser> create(@Valid UserRegisterForm userForm, BindingResult bindingResult){
        if (bindingResult.hasErrors()){
            log.error("注册参数错误：orderForm = {}",userForm);
            throw new SellException(ResultEnum.PARAM_ERROR.getCode(),
                    bindingResult.getFieldError().getDefaultMessage());
        }
        OpenUser user = UserForm2User.convert(userForm);
        userService.create(user);
        return ResultVOUtil.success(user);
    }

    @GetMapping("delete")
    public ResultVO<Map<String, String>> delete(@RequestParam("phone") String phone){
        OpenUser result = userService.delete(phone);
        Map<String,String> map = new HashMap<>();
        map.put("UserName:{}", result.getUsername());
        map.put("Id:{}", result.getId());
        return ResultVOUtil.success(map);
    }

    @PostMapping("/login")
    public String login(@Valid UserForm userForm,
                        Map<String, Object> map,
                        HttpServletResponse response){
        OpenUser openUser = userService.findOpenUser(userForm.getPhone());
        //如果用户不存在或密码与数据库中不同则跳转到登录界面
        if (openUser == null || !HashUtil.verify(userForm.getPassword(),openUser.getPassword())){
            map.put("msg", ResultEnum.LOG_IN_FAIL.getMessage());
            map.put("url", "/");
            return "/common/error";
        }
        //设置token至redis
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;
        //向redis里存入数据和设置缓存时间。redis中的值存的是用户id
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token), openUser.getId(), expire, TimeUnit.SECONDS);
        //存入cookie
        CookieUtil.set(response, CookieConstant.TOKEN, token, expire);
        return "redirect:/buyer/product/list";
    }

    @GetMapping("/logout")
    public String logout(HttpServletResponse response,
                         HttpServletRequest request,
                         Map<String, Object> map){
        //1. 获取Cookie
        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);

        //2. 清除redis中的Cookie值
        redisTemplate.opsForValue().getOperations().delete(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));

        //3. 清除Cookie
        CookieUtil.set(response, CookieConstant.TOKEN,null, 0);
        map.put("msg", "登出成功");
        map.put("url", "/");
        return "/common/success";
    }

}
