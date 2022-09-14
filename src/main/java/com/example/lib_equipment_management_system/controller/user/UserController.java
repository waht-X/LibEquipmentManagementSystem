package com.example.lib_equipment_management_system.controller.user;

import com.example.lib_equipment_management_system.Exception.NoSuchCookieNameException;
import com.example.lib_equipment_management_system.entity.EquipmentUserEntity;
import com.example.lib_equipment_management_system.enums.CookieEnum;
import com.example.lib_equipment_management_system.enums.ErrorCodeEnum;
import com.example.lib_equipment_management_system.enums.UserLevelEnum;
import com.example.lib_equipment_management_system.repository.UserRepository;
import com.example.lib_equipment_management_system.response.ResponseVO;
import com.example.lib_equipment_management_system.service.UserService;
import com.example.lib_equipment_management_system.utils.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {

    @Resource
    UserService userService;

    @Resource
    UserRepository userRepository;

    @PostMapping("login")
    public ResponseVO login(@RequestBody EquipmentUserEntity user) {
        ErrorCodeEnum errorCode = userService.login(user);
        if (errorCode == ErrorCodeEnum.CORRECT) {
            EquipmentUserEntity equipmentUserEntity = (EquipmentUserEntity) userService.userInfo(user.getAccount()).getData();
            HashMap<String, Object> hashMap = new HashMap<>(1);
            hashMap.put("userInfo", equipmentUserEntity);
            return new ResponseVO(true, hashMap, errorCode.getDesc(), errorCode.getCode());
        } else {
            return new ResponseVO(false, null, errorCode.getDesc(), errorCode.getCode());
        }
    }

    @PostMapping("register")
    public ResponseVO register(@RequestBody EquipmentUserEntity user) {
        ErrorCodeEnum errorCode = userService.register(user);
        if (errorCode == ErrorCodeEnum.CORRECT) {
            return new ResponseVO(true, null, errorCode.getDesc(), errorCode.getCode());
        } else {
            return new ResponseVO(false, null, errorCode.getDesc(), errorCode.getCode());
        }
    }

    @RequestMapping("getUserName")
    public ResponseVO getUserName(int id) {
        EquipmentUserEntity equipmentUserEntity = userRepository.findById(id).orElse(null);
        if (equipmentUserEntity != null) {
            return new ResponseVO(true, equipmentUserEntity.getUsername(), ErrorCodeEnum.CORRECT.getDesc(), ErrorCodeEnum.CORRECT.getCode());
        }
        return new ResponseVO(false, null, ErrorCodeEnum.USER_NOT_EXIST.getDesc(), ErrorCodeEnum.USER_NOT_EXIST.getCode());
    }

    @RequestMapping("isHandler")
    public ResponseVO isHandler(HttpServletRequest request) {
        try {
            String account = getAccount(request);
            EquipmentUserEntity user = (EquipmentUserEntity)userService.userInfo(account).getData();
            if (UserLevelEnum.isHandler(user.getLevel())) {
                return new ResponseVO(true, null, ErrorCodeEnum.CORRECT.getDesc(), ErrorCodeEnum.CORRECT.getCode());
            }
        } catch (NoSuchCookieNameException e) {
            log.warn("解析用户cookie失败，拦截器未成功拦截", e);
        }
        return new ResponseVO(false, null, ErrorCodeEnum.PERMISSION_DENY.getDesc(), ErrorCodeEnum.PERMISSION_DENY.getCode());
    }

    @RequestMapping("isSuperHandler")
    public ResponseVO isSuperHandler(HttpServletRequest request) {
        try {
            String account = getAccount(request);
            EquipmentUserEntity byAccount = userRepository.findByAccount(account);
            if (UserLevelEnum.isSuperHandler(byAccount.getLevel())) {
                return new ResponseVO(true, null, ErrorCodeEnum.CORRECT.getDesc(), ErrorCodeEnum.CORRECT.getCode());
            }
        } catch (NoSuchCookieNameException e) {
            log.warn("解析用户cookie失败，拦截器未成功拦截", e);
        }
        return new ResponseVO(false, null, ErrorCodeEnum.PERMISSION_DENY.getDesc(), ErrorCodeEnum.PERMISSION_DENY.getCode());
    }

    @RequestMapping("getUsersInfo")
    public ResponseVO getUsersInfo(@Nullable String account, int size, int index) {
        log.debug("获取用户数据， [account={}, size= {}, index={}]", account, size, index);
        if (account != null) {
            ErrorCodeEnum data =  userService.userInfo(account);
            if (data.getData() == null || data != ErrorCodeEnum.CORRECT) {
                return new ResponseVO(false, null, data.getDesc(), data.getCode());
            }
            HashMap<String, Object> hashMap = new HashMap<>(1);
            List<Object> list = new ArrayList<>(1);
            list.add(data.getData());
            hashMap.put("users", list);
            hashMap.put("totalPage", 1);
            return new ResponseVO(true, hashMap, data.getDesc(), data.getCode());
        } else {
            ErrorCodeEnum allUsersInfo = userService.getAllUsersInfo(size, index);
            if (allUsersInfo != ErrorCodeEnum.CORRECT) {
                return new ResponseVO(false, null, allUsersInfo.getDesc(), allUsersInfo.getCode());
            } else {
                HashMap<String, Object> hashMap = new HashMap<>(4);
                Page page = (Page) allUsersInfo.getData();
                hashMap.put("users", page.getContent());
                hashMap.put("totalPage", page.getTotalPages());
                return new ResponseVO(true, hashMap, allUsersInfo.getDesc(), allUsersInfo.getCode());
            }
        }
    }

    @PostMapping
    public ResponseVO modifyUserInfo(EquipmentUserEntity user) {
        return null;
    }

    @RequestMapping("modifyUserLevel")
    public ResponseVO modifyUserLevel(int model, String account) {
        log.debug("modify user level[model={}, account={}]", model, account);
        ErrorCodeEnum errorCode = userService.modifyUserLevel(model, account);
        if (errorCode == ErrorCodeEnum.CORRECT) {
            return new ResponseVO(true, null, errorCode.getDesc(), errorCode.getCode());
        }  else {
            return new ResponseVO(false, null, errorCode.getDesc(), errorCode.getCode());
        }
    }

    @RequestMapping("deleteUser")
    public ResponseVO deleteUser(String account) {
        ErrorCodeEnum errorCode = userService.deleteUser(account);
        if (errorCode == ErrorCodeEnum.CORRECT) {
            return new ResponseVO(true, null, errorCode.getDesc(), errorCode.getCode());
        }
        return new ResponseVO(false, null, errorCode.getDesc(), errorCode.getCode());
    }


    private String getAccount(HttpServletRequest request) throws NoSuchCookieNameException {
        for (Cookie cookie : request.getCookies()) {
            if (cookie.getName().equals(CookieEnum.USER_ID.toString())) {
                log.debug("cookie[{}={}]",CookieEnum.USER_ID, cookie.getValue());
                log.debug("cookies => {}", getCookies(request.getCookies()));
                return RSAUtils.decryptString(cookie.getValue());
            }
        }
        throw new NoSuchCookieNameException();
    }

    private String getCookies(Cookie []cookies) {
        StringBuilder sb = new StringBuilder();
        for (Cookie cookie : cookies) {
            sb.append(String.format("[name: %s, value: %s]\n", cookie.getName(), cookie.getValue()));
        }
        return sb.toString();
    }
}
