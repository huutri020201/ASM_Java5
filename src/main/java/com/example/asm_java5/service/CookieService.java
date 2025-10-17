package com.example.asm_java5.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CookieService {
    @Autowired
    HttpServletRequest request;
    @Autowired
    HttpServletResponse response;
    public Cookie get(String name) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie c : cookies)
                if (c.getName().equalsIgnoreCase(name))
                    return c;
        return null;
    }
    public String getValue(String name) {
        Cookie c = get(name);
        return (c != null) ? c.getValue() : null;
    }
    public Cookie add(String name, String value,int hours) {
        Cookie c = new Cookie(name, value);
        c.setMaxAge(hours*60*60);
        c.setPath("/");
        response.addCookie(c);
        return c;
    }
    public void remove(String name) {
        Cookie c = new Cookie(name, "");
        c.setMaxAge(0);
        c.setPath("/");
        response.addCookie(c);
    }

}
