package com.gientech.iot.demo.biz.stream;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.function.Consumer;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/4/12 15:15
 */
@Slf4j
public class ArraySeq<T> extends ArrayList<T> implements Seq<T> {

    private static final long serialVersionUID = -7847114483773409503L;

    @Override
    public void consume(Consumer<T> consumer) {
        super.forEach(consumer);
    }
}
