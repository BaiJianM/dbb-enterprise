package com.gientech.iot.gateway.filter.log;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.LinkedList;
import java.util.List;

/**
 * @description: decorator记录器服务器http请求
 * @author: 王强
 * @dateTime: 2023-06-05 10:25:13
 */
@Slf4j
public class RecorderServerHttpRequestDecorator extends ServerHttpRequestDecorator {
    private final List<DataBuffer> dataBuffers = new LinkedList<>();
    private boolean bufferCached = false;
    private Mono<Void> progress = null;

    public RecorderServerHttpRequestDecorator(ServerHttpRequest delegate) {
        super(delegate);
    }

    @NonNull
    @Override
    public Flux<DataBuffer> getBody() {
        synchronized (dataBuffers) {
            if (bufferCached) {
                return copy();
            }
            if (progress == null) {
                progress = cache();
            }
            return progress.thenMany(Flux.defer(this::copy));
        }
    }

    private Flux<DataBuffer> copy() {
        return Flux.fromIterable(dataBuffers)
                .map(buf -> buf.factory().wrap(buf.asByteBuffer()));
    }

    private Mono<Void> cache() {
        return super.getBody()
                .map(dataBuffers::add)
                .then(Mono.defer(() -> {
                    bufferCached = true;
                    progress = null;
                    return Mono.empty();
                }));
    }
}

