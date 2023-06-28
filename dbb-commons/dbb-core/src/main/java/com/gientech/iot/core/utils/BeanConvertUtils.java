package com.gientech.iot.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

/**
 * @description: BeanUtils增强工具类
 * @author: 白剑民
 * @dateTime: 2022/7/25 22:22
 */
@Slf4j
public class BeanConvertUtils extends BeanUtils {
    /**
     * @param source         转换源对象
     * @param targetSupplier 目标对象函数表达式
     * @description: 将源对象按提供的目标对象函数表达式进行转换
     * @author: 白剑民
     * @date: 2022-07-25 22:47:46
     * @return: java.util.Optional<T>
     * @version: 1.0
     */
    public static <S, T> Optional<T> convert(S source, Supplier<T> targetSupplier) {
        return convert(source, targetSupplier, null);
    }

    /**
     * @param source         转换源对象
     * @param targetSupplier 目标对象函数表达式
     * @param callBack       特殊回调方法（S，转换源对象类型；T，目标对象类型）
     * @description: 将源对象按提供的目标对象函数表达式进行转换，同时提供指定的特殊回调方法调用
     * @author: 白剑民
     * @date: 2022-07-25 22:37:34
     * @return: java.util.Optional<T>
     * @version: 1.0
     */
    public static <S, T> Optional<T> convert(S source, Supplier<T> targetSupplier, BiConsumer<S, T> callBack) {
        // 如果提供的转换源对象为null或未提供目标对象函数表达式，则返回空Optional
        if (null == source || null == targetSupplier) {
            return Optional.empty();
        }
        // 调用表达式方法取出转换的目标对象
        T target = targetSupplier.get();
        // 使用Spring的BeanUtils工具类的内置属性copy方法进行拷贝
        copyProperties(source, target);
        // 如果存在指定特殊回调，则进行函数调用
        if (callBack != null) {
            callBack.accept(source, target);
        }
        return Optional.of(target);
    }

    /**
     * @param sources        转换源对象列表
     * @param targetSupplier 目标对象函数表达式
     * @description: 将源对象列表按提供的目标对象函数表达式进行转换
     * @author: 白剑民
     * @date: 2022-07-25 22:48:14
     * @return: java.util.Optional<java.util.Collection < T>>
     * @version: 1.0
     */
    public static <S, T> Optional<Collection<T>> convertCollection(Collection<S> sources, Supplier<T> targetSupplier) {
        return convertCollection(sources, targetSupplier, null);
    }

    /**
     * @param sources        转换源对象列表
     * @param targetSupplier 目标对象函数表达式
     * @param callBack       特殊回调方法（S，转换源对象类型；T，目标对象类型）
     * @description: 将源对象列表按提供的目标对象函数表达式进行转换，同时提供指定的特殊回调方法调用
     * @author: 白剑民
     * @date: 2022-07-25 22:42:33
     * @return: java.util.Optional<java.util.Collection < T>>
     * @version: 1.0
     */
    public static <S, T> Optional<Collection<T>> convertCollection(Collection<S> sources,
                                                                   Supplier<T> targetSupplier,
                                                                   BiConsumer<S, T> callBack) {
        Collection<T> c = null;
        // 判断源对象列表类型
        if (sources instanceof List) {
            // 调用表达式方法取出转换的目标对象列表并转换ArrayList
            c = new ArrayList<>(sources.size());
        } else if (sources instanceof Set) {
            // 调用表达式方法取出转换的目标对象列表并转换HashSet
            c = new HashSet<>(sources.size());
        }
        // 如果提供的转换源对象列表为null或列表内无元素或提供目标对象函数表达式，则返回空Optional
        if (null == sources || sources.size() == 0 || null == targetSupplier || null == c) {
            return Optional.empty();
        }
        // 遍历列表元素
        for (S source : sources) {
            // 取出列表对象元素
            T target = targetSupplier.get();
            // 使用Spring的BeanUtils工具类的内置属性copy方法进行拷贝
            copyProperties(source, target);
            // 如果存在指定特殊回调，则进行函数调用
            if (callBack != null) {
                callBack.accept(source, target);
            }
            c.add(target);
        }
        return Optional.of(c);
    }
}
