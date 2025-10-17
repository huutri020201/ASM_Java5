package com.example.asm_java5.service;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class ParamService {
    @Autowired
    HttpServletRequest request;
    public String getString(String name, String defaultValue) {
        String value = request.getParameter(name);
        return value == null ? defaultValue : value;
    }
    public int getInt(String name, int defaultValue) {
        try {
            return Integer.parseInt(request.getParameter(name));
        }catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    public Double getDouble(String name, Double defaultValue) {
        try {
            return Double.parseDouble(request.getParameter(name));
        }catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    public Boolean getBoolean(String name, Boolean defaultValue) {
        try {
            return Boolean.parseBoolean(request.getParameter(name));
        }catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    public Date getDate(String name, String pattern) {
        try {
            String value = request.getParameter(name);
            if (value == null) return null;
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);
            return sdf.parse(value);
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public File save(MultipartFile file, String path) {
        if (file.isEmpty()) return null;
        try {
            File dir = new File(path);
            if (!dir.exists()) dir.mkdirs();
            File saveFile = new File( dir, file.getOriginalFilename());
            file.transferTo(saveFile);
            return saveFile;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
