package top.dabaibai.demo.biz.stream;

import lombok.extern.slf4j.Slf4j;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/4/12 14:33
 */
@Slf4j
public final class StopException extends RuntimeException {

    public static final StopException INSTANCE = new StopException();

    @Override
    public synchronized Throwable fillInStackTrace() {
        return this;
    }
}
