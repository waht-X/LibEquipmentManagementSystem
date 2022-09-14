package com.example.lib_equipment_management_system.Exception;

import javax.servlet.http.Cookie;

public class NoSuchCookieNameException extends Exception{

    private Cookie []cookies;

    private String msg;

    public NoSuchCookieNameException() {
        super();
    }

    public NoSuchCookieNameException(String s) {
        super(s);
    }

    public NoSuchCookieNameException(Cookie []cookies, String s) {
        this.cookies = cookies;
        StringBuilder sb = new StringBuilder();
        sb.append("\n");
        for (Cookie cookie : cookies) {
            sb.append(String.format("cookie=>[name: %s, value: %s]\n", cookie.getName(), cookie.getValue()));
        }
        String msg = (String.format("message: %s\ncookies: ", s, sb.toString()));
    }

    @Override
    public String getMessage() {
        return msg;
    }

    public Cookie[] getCookies() {
        return cookies;
    }
}
