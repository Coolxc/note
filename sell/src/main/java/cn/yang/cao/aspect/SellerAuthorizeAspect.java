package cn.yang.cao.aspect;

import cn.yang.cao.constant.CookieConstant;
import cn.yang.cao.constant.RedisConstant;
import cn.yang.cao.exception.SellAuthorizeException;
import cn.yang.cao.exception.SellException;
import cn.yang.cao.utils.CookieUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

@Aspect
@Component
@Slf4j
public class SellerAuthorizeAspect {

    @Autowired
    private StringRedisTemplate redisTemplate;

    //定义切入点，除了登录和登出其他所有Controller都要拦截
    @Pointcut("execution(public * cn.yang.cao.controller.*.*(..))" +
            "&& !execution(public * cn.yang.cao.controller.SellerUserController.*(..))&& !execution(public * cn.yang.cao.controller.OpenUserController.*(..))")
    public void verify(){};

    @Before("verify()")
    public void doVerify(){
        //获取request对象
        ServletRequestAttributes servletRequestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = servletRequestAttributes.getRequest();

        Cookie cookie = CookieUtil.get(request, CookieConstant.TOKEN);
        if (cookie == null){
            log.warn("【登录校验】Cookie中查不到token");
            throw new SellAuthorizeException();
        }

        //去redis中查询。redis中存储的key是Cookie中的token值，value为用户id
        String value = redisTemplate.opsForValue().get(String.format(RedisConstant.TOKEN_PREFIX, cookie.getValue()));
        if (value == null){
            log.error("【登录校验】 Redis中查不到token");
            throw  new SellAuthorizeException();
        }
    }
}
