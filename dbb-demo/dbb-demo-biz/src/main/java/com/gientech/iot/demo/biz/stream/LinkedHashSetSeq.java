package com.gientech.iot.demo.biz.stream;

import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashSet;
import java.util.function.Consumer;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/4/14 09:53
 */
@Slf4j
public class LinkedHashSetSeq<T> extends LinkedHashSet<T> implements Seq<T> {

    private static final long serialVersionUID = 6349972414805429295L;

    @Override
    public void consume(Consumer<T> consumer) {
        super.forEach(consumer);
    }
}
