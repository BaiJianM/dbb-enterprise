package com.gientech.iot.log.core.function;


import com.alibaba.fastjson.JSON;
import com.gientech.iot.log.annotations.LogDiff;
import com.gientech.iot.log.annotations.LogFunction;
import com.gientech.iot.log.core.pojo.DiffFieldVO;
import com.gientech.iot.log.core.pojo.DiffVO;
import com.gientech.iot.log.core.properties.LogProperties;
import com.gientech.iot.log.core.context.LogContext;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringSubstitutor;

import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @description: 自定义函数对象差异
 * @author: 王强
 * @dateTime: 2022-09-02 16:26:45
 */
@Slf4j
@LogFunction
public class CustomFunctionObjectDiff {

    public static final String DEFAULT_DIFF_MSG_FORMAT = "编辑前【${_beforeMsg}】, 编辑后【${_afterMsg}】";
    public static final String DEFAULT_ADD_MSG_FORMAT = "【${_afterMsg}】";
    public static final String DEFAULT_MSG_FORMAT = "%s: %s";
    public static final String DEFAULT_MSG_SEPARATOR = ", ";

    /**
     * 对象属性变更时的描述
     */
    private static String DIFF_MSG_FORMAT;
    /**
     * 新增对象属性时的描述
     */
    private static String ADD_MSG_FORMAT;
    /**
     * 对象属性键值对映射描述，第一个属性为键，第二个属性为值
     */
    private static String MSG_FORMAT;
    /**
     * 多个对象属性键值对转字符串的分隔符
     */
    private static String MSG_SEPARATOR;

    public CustomFunctionObjectDiff(LogProperties properties) {
        DIFF_MSG_FORMAT = properties.getDiffMsgFormat().equals(DEFAULT_DIFF_MSG_FORMAT)
                ? DEFAULT_DIFF_MSG_FORMAT
                : new String(properties.getDiffMsgFormat().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

        ADD_MSG_FORMAT = properties.getAddMsgFormat().equals(DEFAULT_ADD_MSG_FORMAT)
                ? DEFAULT_ADD_MSG_FORMAT
                : new String(properties.getAddMsgFormat().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

        MSG_FORMAT = properties.getMsgFormat().equals(DEFAULT_MSG_FORMAT)
                ? DEFAULT_MSG_FORMAT
                : new String(properties.getMsgFormat().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);

        MSG_SEPARATOR = properties.getMsgSeparator().equals(DEFAULT_MSG_SEPARATOR)
                ? DEFAULT_MSG_SEPARATOR
                : new String(properties.getMsgSeparator().getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
    }

    /**
     * @param oldObject 原对象
     * @param newObject 新对象
     * @description: 计算并记录对象变更的属性
     * @author: 王强
     * @date: 2022-09-02 16:26:50
     * @return: @return {@code String }
     * @version: 1.0
     */
    @LogFunction("_DIFF")
    public static String diff(Object oldObject, Object newObject) {
        StringBuilder msg = new StringBuilder();
        // 若包含null对象，直接返回
        if (oldObject == null || newObject == null) {
            log.warn("计算对象属性差异时检测到空对象类型，原对象：[{}]，新对象：[{}]", oldObject, newObject);
            return msg.toString();
        }
        DiffVO diffVO = getDiff(oldObject, newObject);
        List<String> beforeList = new ArrayList<>();
        List<String> afterList = new ArrayList<>();
        diffVO.getDiffFieldVOList().forEach(df -> {
            String fieldName = StringUtils.isBlank(df.getFieldAlias()) ? df.getFieldName() : df.getFieldAlias();
            beforeList.add(String.format(MSG_FORMAT, fieldName, df.getOldValue()));
            afterList.add(String.format(MSG_FORMAT, fieldName, df.getNewValue()));
        });
        if (beforeList.size() > 0 && afterList.size() > 0) {
            Map<String, String> msgMap = new LinkedHashMap<>();
            msgMap.put("_beforeMsg", String.join(MSG_SEPARATOR, beforeList));
            msgMap.put("_afterMsg", String.join(MSG_SEPARATOR, afterList));
            StringSubstitutor sub = new StringSubstitutor(msgMap);
            msg.append(sub.replace(DIFF_MSG_FORMAT));
            LogContext.addDiffVO(diffVO);
        } else {
            log.debug("操作日志: 编辑前后数据相同, 不处理");
        }
        return msg.toString();
    }

    /**
     * @param newObject 新对象
     * @description: 计算并记录新增的属性
     * @author: 王强
     * @date: 2022-09-02 16:26:53
     * @return: @return {@code String }
     * @version: 1.0
     */
    @LogFunction("_ADD")
    public static String add(Object newObject) throws InstantiationException, IllegalAccessException {
        StringBuilder msg = new StringBuilder();
        // 若包含null对象，直接返回
        if (newObject == null) {
            log.warn("计算对象属性新增时检测到空对象类型 null");
            return msg.toString();
        }
        Object oldObject = newObject.getClass().newInstance();
        DiffVO diffVO = getDiff(oldObject, newObject);
        List<String> addMsgList = diffVO.getDiffFieldVOList().stream().map(item -> {
            String fieldName = StringUtils.isBlank(item.getFieldAlias()) ? item.getFieldName() : item.getFieldAlias();
            return String.format(MSG_FORMAT, fieldName, item.getNewValue());
        }).collect(Collectors.toList());
        Map<String, String> msgMap = new LinkedHashMap<>();
        if (addMsgList.size() > 0) {
            msgMap.put("_afterMsg", String.join(MSG_SEPARATOR, addMsgList));
            StringSubstitutor sub = new StringSubstitutor(msgMap);
            msg.append(sub.replace(ADD_MSG_FORMAT));
            LogContext.addDiffVO(diffVO);
        } else {
            log.debug("操作日志: 编辑前后数据相同, 不处理");
        }
        return msg.toString();
    }

    /**
     * @param delObject 待删除对象
     * @description: 记录删除的对象信息
     * @author: 白剑民
     * @date: 2023-05-06 16:22:45
     * @return: java.lang.String
     * @version: 1.0
     */
    @LogFunction("_DEL")
    public static String del(Object delObject) {
        // 若包含null对象，直接返回
        if (delObject == null) {
            log.warn("计算对象删除时检测到空对象类型 null");
            return "";
        }
        return JSON.toJSONString(delObject);
    }

    /**
     * @param oldObject 旧对象
     * @param newObject 新对象
     * @description: 获取差异值对象
     * @author: 王强
     * @date: 2022-09-02 18:20:24
     * @return: @return {@code DiffVO }
     * @version: 1.0
     */
    public static DiffVO getDiff(Object oldObject, Object newObject) {
        Class<?> oClass = oldObject.getClass();
        Class<?> nClass = newObject.getClass();
        // 获取新旧对象类名
        String oldClassName = oClass.getName();
        String newClassName = nClass.getName();
        // 获取类上的注解
        LogDiff oldClassLogDiff = oClass.getDeclaredAnnotation(LogDiff.class);
        LogDiff newClassLogDiff = nClass.getDeclaredAnnotation(LogDiff.class);
        // 获取类上注解的别名
        String oldClassAlias = oldClassLogDiff != null && StringUtils.isNotBlank(oldClassLogDiff.alias())
                ? oldClassLogDiff.alias() : null;
        String newClassAlias = newClassLogDiff != null && StringUtils.isNotBlank(newClassLogDiff.alias())
                ? newClassLogDiff.alias() : null;
        log.debug("旧对象类名：[{}]，旧对象别名 [{}]，新对象类名：[{}]，新对象别名：[{}]",
                oldClassName, oldClassAlias, newClassName, newClassAlias);
        // 新旧对象变更的属性键值集合
        Map<String, Object> oldValueMap = new LinkedHashMap<>();
        Map<String, Object> newValueMap = new LinkedHashMap<>();
        // 初始化差异值VO
        DiffVO diffVO = new DiffVO();
        // 设置旧对象的class类名，别名
        diffVO.setOldClassName(oldClassName);
        diffVO.setOldClassAlias(oldClassAlias);
        // 设置新对象的class类名，别名
        diffVO.setNewClassName(newClassName);
        diffVO.setNewClassAlias(newClassAlias);
        // 定义对象属性变更列表
        List<DiffFieldVO> diffFieldVOList = new ArrayList<>();
        diffVO.setDiffFieldVOList(diffFieldVOList);

        // 获取类的字段
        Field[] fields = getAllFields(oClass);
        // 遍历字段加了@LogDiff注解的字段
        for (Field oldField : fields) {
            LogDiff oldObjectLogDiff = oldField.getDeclaredAnnotation(LogDiff.class);
            // 若没有@LogDiff注解，跳过
            if (oldObjectLogDiff == null) {
                continue;
            }
            try {
                String fieldName = oldField.getName();
                // 在新对象中寻找同名字段，若找不到则跳过本次循环
                Field newField = getFieldByName(nClass, oldField.getName());
                // 获取对象该字段的别名（旧对象和新对象属性名一致，一般不存在字段属性名变更但别名不变的情况）
                String fieldAlias = oldObjectLogDiff.alias();
                // 关闭字段安全检查
                oldField.setAccessible(true);
                newField.setAccessible(true);
                // 获取旧对象字段值
                Object oldValue = oldField.get(oldObject);
                // 获取新对象字段值
                Object newValue = newField.get(newObject);
                // 若新旧值不同，则将数据放入Map
                if (newValue != null && !newValue.equals(oldValue)) {
                    if (log.isDebugEnabled()) {
                        log.debug("对象字段属性 [{}] 发生变更，旧值：[{}]，新值：[{}]", oldField.getName(), oldValue, newValue);
                        oldValueMap.put(fieldName, oldValue);
                        newValueMap.put(fieldName, newValue);
                    }
                    // 初始化对象变更属性值
                    DiffFieldVO diffFieldVO = new DiffFieldVO();
                    diffFieldVO.setFieldName(fieldName);
                    diffFieldVO.setFieldAlias(fieldAlias);
                    diffFieldVO.setOldValue(oldValue);
                    diffFieldVO.setNewValue(newValue);
                    // 存入变更对象列表
                    diffFieldVOList.add(diffFieldVO);
                }
            } catch (IllegalAccessException e) {
                log.error("遍历@LogDiff注解字段出错，错误信息：{}", e.getMessage());
            }
        }
        if (log.isDebugEnabled()) {
            log.debug("旧对象字段属性键值映射 [{}]", oldValueMap);
            log.debug("新对象字段属性键值映射 [{}]", newValueMap);
        }
        return diffVO;
    }

    /**
     * @param clazz clazz
     * @description: 获取本类和父类所有字段
     * @author: 王强
     * @date: 2022-11-06 15:55:30
     * @return: Field[]
     * @version: 1.0
     */
    public static Field[] getAllFields(Class<?> clazz) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Field[] fields = new Field[fieldList.size()];
        return fieldList.toArray(fields);
    }


    /**
     * @param clazz     clazz
     * @param fieldName 字段名称
     * @description: 根据class和字段名称获取字段信息
     * @author: 王强
     * @date: 2022-11-06 15:55:30
     * @return: Field
     * @version: 1.0
     */
    public static Field getFieldByName(Class<?> clazz, String fieldName) {
        List<Field> fieldList = new ArrayList<>();
        while (clazz != null) {
            fieldList.addAll(new ArrayList<>(Arrays.asList(clazz.getDeclaredFields())));
            clazz = clazz.getSuperclass();
        }
        Map<String, Field> fieldMap = fieldList.stream().collect(Collectors.toMap(Field::getName, Function.identity()));
        return fieldMap.get(fieldName);
    }
}
