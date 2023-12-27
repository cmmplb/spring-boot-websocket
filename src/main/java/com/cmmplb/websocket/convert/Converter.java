package com.cmmplb.websocket.convert;

import java.util.List;

/**
 * @author penglibo
 * @date 2022-08-04 11:33:12
 * @since jdk 1.8
 * 封装通用转换工具类
 */
public interface Converter<E, V> {

    /**
     * 转换对象
     * @param t t
     * @return v
     */
    V convert(E t);

    /**
     * 转换集合
     * @param list list<e>
     * @return list<v>
     */
    List<V> convertList(List<E> list);

}
