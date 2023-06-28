package top.dabaibai.gateway.filter.log;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.reactivestreams.Publisher;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.http.server.reactive.ServerHttpResponseDecorator;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @description: 记录服务器http响应装饰
 * @author: 白剑民
 * @dateTime: 2023-06-05 10:25:06
 */
@Slf4j
public class RecorderServerHttpResponseDecorator extends ServerHttpResponseDecorator {

    RecorderServerHttpResponseDecorator(ServerHttpResponse delegate) {
        super(delegate);
    }

    /**
     * 基于netty,我这里需要显示的释放一次dataBuffer,但是slice出来的byte是不需要释放的,
     * 与下层共享一个字符串缓冲池,gateway过滤器使用的是nettyWrite类,会发生response数据多次才能返回完全。
     * 在 ServerHttpResponseDecorator 之后会释放掉另外一个refCount.
     */
    @NonNull
    @Override
    public Mono<Void> writeWith(@NonNull Publisher<? extends DataBuffer> body) {
        DataBufferFactory bufferFactory = this.bufferFactory();
        if (body instanceof Flux) {
            Flux<? extends DataBuffer> fluxBody = (Flux<? extends DataBuffer>) body;
            Publisher<? extends DataBuffer> re = fluxBody.map(dataBuffer -> {
                // probably should reuse buffers
                byte[] content = new byte[dataBuffer.readableByteCount()];
                // 数据读入数组
                dataBuffer.read(content);
                // 释放掉内存
                DataBufferUtils.release(dataBuffer);
                // 记录返回值
                String response = new String(content, StandardCharsets.UTF_8);
                byte[] uppedContent = new String(content, StandardCharsets.UTF_8).getBytes();
                return bufferFactory.wrap(uppedContent);
            });
            return super.writeWith(re);
        }
        return super.writeWith(body);
    }

    @NonNull
    @Override
    public Mono<Void> writeAndFlushWith(@NonNull Publisher<? extends Publisher<? extends DataBuffer>> body) {
        return writeWith(Flux.from(body).flatMapSequential(p -> p));
    }
}


