package top.dabaibai.demo.biz.service.impl.grpc;

import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.server.service.GrpcService;
import top.dabaibai.grpc.api.HelloServiceGrpc;
import top.dabaibai.grpc.api.HelloServiceProto;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/9/3 13:29
 */
@Slf4j
@GrpcService
public class ServerPoint extends HelloServiceGrpc.HelloServiceImplBase {

    @Override
    public void sayHello(HelloServiceProto.HelloRequest request,
                         StreamObserver<HelloServiceProto.HelloResponse> responseObserver) {
        log.info("收到客户端消息:{}", request.getName());
        HelloServiceProto.HelloResponse response = HelloServiceProto.HelloResponse.newBuilder()
                .setResult("Hello " + request.getName()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
