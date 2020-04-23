package cn.yang.cao.controller;

import cn.yang.cao.constant.CookieConstant;
import cn.yang.cao.constant.RedisConstant;
import cn.yang.cao.dataobject.SellerInfo;
import cn.yang.cao.enums.ResultEnum;
import cn.yang.cao.exception.SellException;
import cn.yang.cao.form.SellerForm;
import cn.yang.cao.form.SellerRegisterForm;
import cn.yang.cao.service.SellerService;
import cn.yang.cao.utils.CookieUtil;
import cn.yang.cao.utils.HashUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Controller
@RequestMapping("/seller")
@Slf4j
public class SellerUserController {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private SellerService sellerService;

    @PostMapping("/login")
    public String login(@Valid SellerForm sellerForm,
                        Map<String, Object> map,
                        HttpServletResponse response){
        SellerInfo sellerInfo = sellerService.findSellerInfoByPhone(sellerForm.getPhone());
        //如果用户不存在或密码与数据库中不同则跳转到登录界面
        if (sellerInfo == null || !HashUtil.verify(sellerForm.getPassword(),sellerInfo.getPassword())){
            map.put("msg", ResultEnum.LOG_IN_FAIL.getMessage());
            map.put("url", "/");
            return "/common/error";
        }
        //设置token至redis
        String token = UUID.randomUUID().toString();
        Integer expire = RedisConstant.EXPIRE;
        //向redis里存入数据和设置缓存时间。redis中的值存的是用户id
        redisTemplate.opsForValue().set(String.format(RedisConstant.TOKEN_PREFIX, token), sellerInfo.getId(), expire, TimeUnit.SECONDS);
        //存入cookie
        CookieUtil.set(response, CookieConstant.TOKEN, token, expire);
        return "redirect:/seller/order/list";
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

    @PostMapping("sellerRegister")
    public String register(@Valid SellerRegisterForm sellerRegisterForm,
                           BindingResult bindingResult,
                           Map<String, Object> map){
        SellerInfo sellerInfo = new SellerInfo();
        sellerInfo.setUsername(sellerRegisterForm.getUsername());
        sellerInfo.setPassword(sellerRegisterForm.getPassword());
        sellerInfo.setPhone(sellerRegisterForm.getPhone());
        try{
            sellerService.create(sellerInfo);
        }catch (SellException e){
            map.put("msg", e.getMessage());
            map.put("url", "/seller/sellerRegister");
            return "/common/error";
        }
        map.put("msg", "注册成功");
        map.put("url", "/");
        return "/common/success";
    }
}
