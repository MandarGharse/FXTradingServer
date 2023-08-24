package com.fx.server.grpc;

import com.fx.common.ApplicationConfiguration;
import com.fx.grpc.EnableCompressionInterceptor;
import io.grpc.Server;
import io.grpc.ServerInterceptors;
import io.grpc.netty.NettyServerBuilder;
import io.netty.channel.DefaultEventLoopGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.fx.common.utils.ExecutorWorkerAppPool;

import java.io.IOException;

@Component
public class GrpcServer {

    private Server server;

    @Autowired
    ApplicationConfiguration config;

    @Autowired
    GrpcService grpcService;

    public void startServer() throws IOException {
        io.netty.channel.EventLoopGroup epollEventLoopGroupBoss = new DefaultEventLoopGroup();
        io.netty.channel.EventLoopGroup epollEventLoopGroupWorker = new DefaultEventLoopGroup();

        this.server = NettyServerBuilder
                .forPort(config.grpcPort)
                //.bossEventLoopGroup(epollEventLoopGroupBoss)
                //.workerEventLoopGroup(epollEventLoopGroupWorker)
                //.channelType(nioSocketChannel)
                .executor(ExecutorWorkerAppPool.getInstance().getExecutor())
                .addService(ServerInterceptors.intercept(grpcService, new EnableCompressionInterceptor()))
                .build().start();
    }

}
