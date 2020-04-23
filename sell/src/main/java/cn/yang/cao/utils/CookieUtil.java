package cn.yang.cao.utils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

public class CookieUtil {

    public static void set(HttpServletResponse response,
                      String name,
                      String value, Integer MaxAge){
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(MaxAge);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    //获取Cookie
    public static Cookie get(HttpServletRequest request,
                           String name){
        Map<String, Cookie> map = getCookieMap(request);
        if (map.containsKey(name)){
            return map.get(name);
        }
        return null;
    }

    //将Cookies封装成Map
    public static Map<String, Cookie> getCookieMap(HttpServletRequest request){
        Map<String, Cookie> map = new HashMap<>();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                map.put(cookie.getName(), cookie);
            }
        }
        return map;
    }
}
