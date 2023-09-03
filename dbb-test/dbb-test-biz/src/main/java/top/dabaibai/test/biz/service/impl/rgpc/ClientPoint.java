package top.dabaibai.test.biz.service.impl.rgpc;

import lombok.extern.slf4j.Slf4j;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.stereotype.Service;
import top.dabaibai.grpc.api.HelloServiceGrpc;
import top.dabaibai.grpc.api.HelloServiceProto;

/**
 * @description:
 * @author: 白剑民
 * @dateTime: 2023/9/3 13:32
 */
@Slf4j
@Service
public class ClientPoint {

    @GrpcClient("local-grpc-server")
    private HelloServiceGrpc.HelloServiceBlockingStub stub;

    public String sayHello(String name) {
        return stub.sayHello(HelloServiceProto.HelloRequest.newBuilder().setName(name).build()).getResult();
    }

}
