package org.example.ordermanagement.util;

import java.util.LinkedHashMap;
import java.util.Map;

public class ResponseUtil {

    public static Map<String, Object> success(Object data) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("code", "SUCCESS");
        res.put("data", data);
        return res;
    }

    public static Map<String, Object> error(String code, String message) {
        Map<String, Object> res = new LinkedHashMap<>();
        res.put("code", code);
        res.put("message", message);
        return res;
    }
}