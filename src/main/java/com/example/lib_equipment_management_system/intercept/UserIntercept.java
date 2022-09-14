package com.example.lib_equipment_management_system.intercept;

import com.example.lib_equipment_management_system.enums.CookieEnum;
import com.example.lib_equipment_management_system.repository.UserRepository;
import com.example.lib_equipment_management_system.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
public class UserIntercept implements HandlerInterceptor {

    @Resource
    UserRepository userRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (request.getMethod().equals("OPTIONS")) {
            //遇到预请求放行
            return true;
        }
        if (request.getCookies() == null) {
            log.info("cookies is null, request method: [{}]", request.getMethod());
            return false;
        }
        log.debug("intercept user[cookies.length={}]", request.getCookies().length);
        for (Cookie cookie : request.getCookies()) {
//            log.debug("cookie name: " + cookie.getName());
            if (cookie.getName().equals(CookieEnum.USER_ID.toString())) {
                //判断令牌是否正确
//                log.debug("cookie[USER_ID={}]", cookie.getValue());
                String account = RSAUtils.decryptString(cookie.getValue());
                log.info("user[{}] come", account);
                if (userRepository.existsByAccount(account)) {
                    log.info("user[{}] exist", account);
                    return true;
                }
                log.info("user[{}] not exist", account);
                return false;
            }
        }
        log.debug("intercept one");
        return false;
    }
}
