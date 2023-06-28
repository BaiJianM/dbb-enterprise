package com.gientech.iot.core.utils;

import com.gientech.iot.core.pojo.vo.BaseTreeVO;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @description: 树结构转换 (目标类需继承BaseTreeVO, 若未指定根节点ID, 则默认根节点ID为0)
 * @author: 王强
 * @dateTime: 2022-07-28 20:50:57
 */
public class TreeUtils {

    /**
     * @param source 源数据列表
     * @description: 将源数据列表转为树结构列表
     * @author: 王强
     * @date: 2022-07-28 21:24:41
     * @return: java.util.List<V>
     * @version: 1.0
     */
    public static <V extends BaseTreeVO> List<V> listToTree(List<V> source) {
        return listToTree(new ArrayList<>(), source);
    }

    /**
     * @param rootIdList 根节点主键id列表
     * @param source     源数据列表
     * @description: 将源数据列表转为树结构列表并按传入的rootIdList作为根节点展示
     * @author: 王强
     * @date: 2022-07-28 20:51:14
     * @return: java.util.List<V>
     * @version: 1.0
     */
    public static <V extends BaseTreeVO> List<V> listToTree(List<Long> rootIdList, List<V> source) {
        // 如果根节点id不为空，则调用展示根节点方法
        return listToTreeByRootId(rootIdList, source);
    }

    /**
     * @param source         源数据列表
     * @param targetSupplier 目标转换方法
     * @description: 将源数据列表转为树结构列表，并且按提供的目标转换方法进行属性赋值转换
     * @author: 王强
     * @date: 2022-07-28 21:24:41
     * @return: java.util.List<V>
     * @version: 1.0
     */
    public static <E, V extends BaseTreeVO> List<V> listToTree(List<E> source, Supplier<V> targetSupplier) {
        // 实体对象转换VO
        List<V> vs = (List<V>) BeanConvertUtils.convertCollection(source, targetSupplier, (s, t) -> {
        }).orElse(new ArrayList<>());
        return listToTree(vs);
    }

    /**
     * @param source         源数据列表
     * @param targetSupplier 目标转换方法
     * @param callBack       回调方法（用于属性转换时的特殊转换）
     * @description: 将源数据列表转为树结构列表，并且按提供的目标转换方法进行属性赋值转换
     * @author: 王强
     * @date: 2022-07-28 21:24:41
     * @return: java.util.List<V>
     * @version: 1.0
     */
    public static <E, V extends BaseTreeVO> List<V> listToTree(List<E> source, Supplier<V> targetSupplier, BiConsumer<E, V> callBack) {
        // 实体对象转换VO
        List<V> vs = (List<V>) BeanConvertUtils.convertCollection(source, targetSupplier, callBack).orElse(new ArrayList<>());
        return listToTree(vs);
    }

    /**
     * @param rootIdList     根节点主键id列表
     * @param source         源数据列表
     * @param targetSupplier 目标转换方法
     * @param callBack       回调方法（用于属性转换时的特殊转换）
     * @description: 将源数据列表转为树结构列表，并且按提供的目标转换方法进行属性赋值转换，
     * 并按传入的rootIdList作为根节点列出
     * @author: 王强
     * @date: 2022-07-28 21:24:41
     * @return: java.util.List<V>
     * @version: 1.0
     */
    public static <E, V extends BaseTreeVO> List<V> listToTree(List<Long> rootIdList, List<E> source, Supplier<V> targetSupplier, BiConsumer<E, V> callBack) {
        // 实体对象转换VO
        List<V> vs = (List<V>) BeanConvertUtils.convertCollection(source, targetSupplier, callBack).orElse(new ArrayList<>());
        return listToTree(rootIdList, vs);
    }

    /**
     * @param rootIdList 根节点主键id列表
     * @param source     源数据列表
     * @description: 将源数据列表转为树结构列表，传入的rootIdList不作为根节点展示
     * @author: 王强
     * @date: 2022-07-28 20:51:14
     * @return: java.util.List<V>
     * @version: 1.0
     */
    public static <V extends BaseTreeVO> List<V> listToTreeWithoutRoot(List<Long> rootIdList, List<V> source) {
        // 不展示根节点
        return listToTreeByRootId(rootIdList, source, false);
    }

    /**
     * @param rootIdList     根节点主键id列表
     * @param source         源数据列表
     * @param targetSupplier 目标转换方法
     * @param callBack       回调方法（用于属性转换时的特殊转换）
     * @description: 将源数据列表转为树结构列表，并且按提供的目标转换方法进行属性赋值转换，传入的rootIdList不作为根节点展示
     * @author: 王强
     * @date: 2022-07-28 21:24:41
     * @return: java.util.List<V>
     * @version: 1.0
     */
    public static <E, V extends BaseTreeVO> List<V> listToTreeWithoutRoot(List<Long> rootIdList, List<E> source, Supplier<V> targetSupplier,
                                                                          BiConsumer<E, V> callBack) {
        // 实体对象转换VO
        List<V> vs = (List<V>) BeanConvertUtils.convertCollection(source, targetSupplier, callBack).orElse(new ArrayList<>());
        return listToTreeWithoutRoot(rootIdList, vs);
    }

    /**
     * @param rootIdList 根节点主键id列表
     * @param source     源数据列表
     * @description: 带根节点判断的树结构转换方法
     * @author: 王强
     * @date: 2022-07-28 20:51:20
     * @return: java.util.List<V>
     * @version: 1.0
     */
    private static <V extends BaseTreeVO> List<V> listToTreeByRootId(List<Long> rootIdList, List<V> source) {
        // 默认展示根节点
        return listToTreeByRootId(rootIdList, source, true);
    }

    /**
     * @param rootIdList 根节点主键id列表
     * @param source     源数据列表
     * @param showParent 树结构列表中是否显示父级节点
     * @description: 带根节点判断的树结构转换方法
     * @author: 王强
     * @date: 2022-07-28 20:51:20
     * @return: java.util.List<V>
     * @version: 1.0
     */
    private static <V extends BaseTreeVO> List<V> listToTreeByRootId(List<Long> rootIdList, List<V> source, Boolean showParent) {
        return source.stream()
                // 过滤出根节点
                .filter(item -> {
                    // 在传入的根节点id列表不为空的时候才执行showParent的判断
                    if (rootIdList != null && rootIdList.size() > 0) {
                        if (showParent) {
                            // 如果显示根节点则判断主键id==根节点id
                            return rootIdList.contains(item.getId());
                        } else {
                            // 如果不显示根节点则判断父级id==根节点id
                            return rootIdList.contains(item.getParentId());
                        }
                    } else {
                        // 如果传入的根节点id为空，默认为0
                        return 0 == item.getParentId();
                    }
                })
                // 对每一个根节点进行递归赋值children处理
                .peek(root -> {
                    // 取出根节点的主键id
                    Object rootId = root.getId();
                    // 去除该根节点下的所有子节点
                    List<V> subList = source.stream()
                            // 过滤出根节点下的子节点
                            .filter(data -> rootId.equals(data.getParentId()))
                            // 对每一个子节点进行递归赋值children处理
                            .peek(data -> {
                                // 递归对该子节点的children进行赋值
                                data.setChildren(getTree(data.getId(), source));
                            }).collect(Collectors.toList());
                    // 对该根节点的children进行赋值
                    root.setChildren(subList);
                }).collect(Collectors.toList());
    }

    /**
     * @param parentId    父级id
     * @param allDataList 所有数据列表
     * @description: 获取树结构的递归方法
     * @author: 王强
     * @date: 2022-07-28 20:51:31
     * @return: java.util.List<V>
     * @version: 1.0
     */
    private static <V extends BaseTreeVO> List<V> getTree(Long parentId, List<V> allDataList) {
        return allDataList.stream()
                // 找出根节点下的子节点
                .filter(item -> parentId.equals(item.getParentId()))
                // 对每一个子节点进行递归赋值children处理
                .peek(data -> {
                    // 递归对该子节点的children进行赋值
                    data.setChildren(getTree(data.getId(), allDataList));
                })
                .collect(Collectors.toList());
    }
}
