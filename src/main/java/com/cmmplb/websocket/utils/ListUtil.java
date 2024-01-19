package com.cmmplb.websocket.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author plb
 * @date 2020/6/12 9:58
 */

public class ListUtil {

    /**
     * Obj转List
     * @param obj   需要转换的obj
     * @param clazz 对象字节码
     * @param <T>   集合泛型
     * @return List<T>
     */
    public static <T> List<T> castList(Object obj, Class<T> clazz) {
        List<T> result = new ArrayList<T>();
        if (obj instanceof List<?>) {
            for (Object o : (List<?>) obj) {
                result.add(clazz.cast(o));
            }
            return result;
        }
        return new ArrayList<>();
    }

    /**
     * 提取集合内重复元素
     * @param list 待提取的集合
     * @param <T>  元素泛型
     * @return 重复元素集合
     */
    public static <T> List<T> getRepeatElement(List<T> list) {
        return list.stream()
                // 获得元素出现频率的 Map，键为元素，值为元素出现的次数
                .collect(Collectors.toMap(e -> e, e -> 1, Integer::sum))
                .entrySet().stream()
                // 过滤出元素出现次数大于 1 (重复元素）的 entry
                .filter(e -> e.getValue() > 1)
                // 获得 entry 的键（重复元素）对应的 Stream
                .map(Map.Entry::getKey).collect(Collectors.toList());
    }

    /**
     * 判断两个集合是否相同
     * @param l 集合1
     * @param s 集合2
     * @return boolean
     */
    public static <T> boolean compare(List<T> l, List<T> s, Class<T> t) {
        if (null == l || null == s) {
            return false;
        }
        if (l.size() != s.size()) {
            return false;
        }
        if (t == String.class) {
            List<String> s1 = castList(l, String.class);
            List<String> s2 = castList(s, String.class);
            s1.sort(String::compareTo);
            s2.sort(String::compareTo);
            return s1.toString().equals(s2.toString());
        }
        if (t == Long.class) {
            List<Long> s1 = castList(l, Long.class);
            List<Long> s2 = castList(s, Long.class);
            s1.sort(Long::compareTo);
            s2.sort(Long::compareTo);
            return s1.toString().equals(s2.toString());
        }
        if (t == Integer.class) {
            List<Integer> s1 = castList(l, Integer.class);
            List<Integer> s2 = castList(s, Integer.class);
            s1.sort(Integer::compareTo);
            s2.sort(Integer::compareTo);
            return s1.toString().equals(s2.toString());
        }
        if (t == Byte.class) {
            List<Byte> s1 = castList(l, Byte.class);
            List<Byte> s2 = castList(s, Byte.class);
            s1.sort(Byte::compareTo);
            s2.sort(Byte::compareTo);
            return s1.toString().equals(s2.toString());
        }
        if (t == Short.class) {
            List<Short> s1 = castList(l, Short.class);
            List<Short> s2 = castList(s, Short.class);
            s1.sort(Short::compareTo);
            s2.sort(Short::compareTo);
            return s1.toString().equals(s2.toString());
        }
        if (t == Double.class) {
            List<Double> s1 = castList(l, Double.class);
            List<Double> s2 = castList(s, Double.class);
            s1.sort(Double::compareTo);
            s2.sort(Double::compareTo);
            return s1.toString().equals(s2.toString());
        }
        if (t == Float.class) {
            List<Float> s1 = castList(l, Float.class);
            List<Float> s2 = castList(s, Float.class);
            s1.sort(Float::compareTo);
            s2.sort(Float::compareTo);
            return s1.toString().equals(s2.toString());
        }
        if (t == Boolean.class) {
            List<Boolean> s1 = castList(l, Boolean.class);
            List<Boolean> s2 = castList(s, Boolean.class);
            s1.sort(Boolean::compareTo);
            s2.sort(Boolean::compareTo);
            return s1.toString().equals(s2.toString());
        }
        if (t == Character.class) {
            List<Character> s1 = castList(l, Character.class);
            List<Character> s2 = castList(s, Character.class);
            s1.sort(Character::compareTo);
            s2.sort(Character::compareTo);
            return s1.toString().equals(s2.toString());
        }
        return l.toString().equals(s.toString());
    }

    /**
     * 合并集合并去重
     * @param l 集合1
     * @param s 集合2
     * @return res
     */
    public static <T> List<T> mergeDuplicate(List<T> l, List<T> s) {
        return Stream.of(l, s).flatMap(Collection::stream).distinct().collect(Collectors.toList());
    }

    /**
     * 获取集合第一个元素
     * @param list 集合
     * @param <T>  t
     * @return t
     */
    public static <T> T getFirst(List<T> list) {
        return list != null && !list.isEmpty() ? list.get(0) : null;
    }

    /**
     * 获取集合最后一个元素
     * @param list l
     * @param <T>  t
     * @return t
     */
    public static <T> T getLast(List<T> list) {
        return list != null && !list.isEmpty() ? list.get(list.size() - 1) : null;
    }

    /**
     * 字符串转集合
     * @param str 字符串
     * @return List
     * @throws IOException i
     */
    public static List<String> str2list(String str) throws IOException {
        List<String> list = new ArrayList<>();
        if ((str != null) && (!"".equals(str))) {
            StringReader fr = new StringReader(str);
            BufferedReader br = new BufferedReader(fr);
            String aline;
            while ((aline = br.readLine()) != null) {
                list.add(aline);
            }
        }
        return list;
    }

    public static void main(String[] args) throws ParseException {
        Date startDate = parseToDate("2020-04-01" + " 00:00:00", "yyyy-MM-dd HH:mm:ss");
        System.out.println(startDate);
        parseToDate("2020-04-01", "yyyy-MM-dd");
        System.out.println(startDate);
    }

    public static Date parseToDate(String dateStr, String pattern) throws ParseException {
        return new SimpleDateFormat(pattern).parse(dateStr);
    }

    /**
     * 集合转字符串
     * @param iterable 集合
     * @return 字符串: element,ele2
     */
    public static <T> String list2str(Iterable<T> iterable){
        StringBuilder str = new StringBuilder();
        if (null == iterable) {
            return null;
        }
        for (T t : iterable) {
            str.append(t.toString()).append(",");
        }
        return str.substring(0, str.length() - 1);
    }

    /**
     * list分页
     * @param list    数据集
     * @param current 当前页
     * @param size    每页条数
     * @param <T>     指定数据集类型
     * @return 分页结果数据
     */
    public static <T> List<T> startPage(List<T> list, int current, int size) {
        if (list == null) {
            return new ArrayList<>();
        }
        if (list.size() == 0) {
            return null;
        }
        // 记录总数
        int count = list.size();
        // 页数
        int pageCount;
        if (count % size == 0) {
            pageCount = count / size;
        } else {
            pageCount = count / size + 1;
        }
        // 开始索引
        int fromIndex;
        // 结束索引
        int toIndex;
        if (current != pageCount) {
            fromIndex = (current - 1) * size;
            toIndex = fromIndex + size;
        } else {
            fromIndex = (current - 1) * size;
            toIndex = count;
        }
        if (fromIndex >= count) {
            return null;
        }
        if (toIndex > count) {
            toIndex = count - 1;
        }
        return list.subList(fromIndex, toIndex);
    }

    /**
     * 获取总页数
     * @param list 数据集
     * @param size 每页条数
     * @param <T>  指定数据集类型
     * @return 总页数
     */
    public static <T> Integer getTotalPage(List<T> list, int size) {
        if (list == null) {
            return 0;
        }
        if (list.size() == 0) {
            return 0;
        }
        // 记录总数
        int count = list.size();
        // 页数
        int pageCount;
        if (count % size == 0) {
            pageCount = count / size;
        } else {
            pageCount = count / size + 1;
        }
        return pageCount;
    }

}













