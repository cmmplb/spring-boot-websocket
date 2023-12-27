package com.cmmplb.websocket.utils;

import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author penglibo
 * @date 2022-03-04 14:33:16
 * @since jdk 1.8
 */

public class MapObjectUtil {

    /**
     * 实体对象转成Map
     * @param obj 实体对象
     * @return map
     */
    public static <T> Map<String, Object> object2Map(T obj) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (obj == null) {
            return map;
        }
        Class<?> clazz = obj.getClass();
        Field[] fields = clazz.getDeclaredFields();
        try {
            for (Field field : fields) {
                field.setAccessible(true);
                map.put(field.getName(), field.get(obj));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * Map转成实体对象
     * @param map   map实体对象包含属性
     * @param clazz 实体对象类型
     * @return 实体
     */
    public static <T> T map2Object(Map<String, Object> map, Class<T> clazz) {
        if (map == null) {
            return null;
        }
        T obj = null;
        try {
            obj = clazz.newInstance();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                int mod = field.getModifiers();
                if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                    continue;
                }
                field.setAccessible(true);
                String filedTypeName = field.getType().getName();
                if ("java.util.date".equalsIgnoreCase(filedTypeName)) {
                    String dateTimestamp = String.valueOf(map.get(field.getName()));
                    if ("null".equalsIgnoreCase(dateTimestamp)) {
                        field.set(obj, null);
                    } else {
                        field.set(obj, new Date(Long.parseLong(dateTimestamp)));
                    }
                } else {
                    field.set(obj, map.get(field.getName()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    /**
     * url参数转换成map
     * @param param url=> a=1&b=2
     * @return map
     */
    public static Map<String, String> urlParams2Map(String param) {
        Map<String, String> map = new HashMap<>(16);
        if (StringUtils.isBlank(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (String s : params) {
            String[] p = s.split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     * 将map转换成url
     * @param map map
     * @return urlParams
     */

    public static String map2UrlParams(Map<String, String> map) {
        if (map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            sb.append(entry.getKey());
            sb.append("=");
            sb.append(entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        return s.endsWith("&") ? StringUtils.substringBeforeLast(s, "&") : s;
    }
}
