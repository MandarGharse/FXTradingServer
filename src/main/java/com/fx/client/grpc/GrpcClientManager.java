package com.fx.client.grpc;

import com.fx.common.enums.GrpcClientId;
import com.fx.proto.services.PricingServicesGrpc;
import com.fx.proto.services.TradeServicesGrpc;
import com.fx.common.utils.ExecutorWorkerAppPool;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.AbstractAsyncStub;

import java.util.HashMap;
import java.util.Map;

public class GrpcClientManager {
    static final Map<GrpcClientId, GrpcClient> grpcClientMap = new HashMap<>();

    String hostname;
    int port;

    public GrpcClientManager(String hostname, int port) {
        this.hostname = hostname;
        this.port = port;
        System.out.println("GrpcClientManager called with hostname " + hostname + ", port " + port);
    }

    public GrpcClient createGrpcClient(GrpcClientId grpcClientId)   {
        if (grpcClientMap.get(grpcClientId) != null)
            return grpcClientMap.get(grpcClientId);
        SsoAuthClientInterceptor ssoAuthClientInterceptor = new SsoAuthClientInterceptor("", "");
        GrpcClient grpcClient = new GrpcClient(hostname, port, grpcClientId, ssoAuthClientInterceptor);
        grpcClientMap.put(grpcClientId, grpcClient);

        System.out.println("created GrpcClient with hostname " + hostname + ", port " + port + ", grpcClientId " + grpcClientId);
        return grpcClient;
    }

    public static class GrpcClient implements AutoCloseable {

        private ManagedChannel channel = null;
        private AbstractAsyncStub asyncServicesStub;

        public GrpcClient(String host, int port, GrpcClientId grpcClientId, ClientInterceptor... clientInterceptors) {
            init(grpcClientId, ManagedChannelBuilder
                    .forAddress(host, port)
                    .intercept(clientInterceptors)
                    .usePlaintext()
                    .executor(ExecutorWorkerAppPool.getInstance().getExecutor())
                    .build());
        }

        public void init(GrpcClientId grpcClientId, ManagedChannel channel)    {
            this.channel = channel;
            if (grpcClientId == GrpcClientId.TRADES)
                this.asyncServicesStub = TradeServicesGrpc.newStub(channel).withCompression("gzip");
            else if (grpcClientId == GrpcClientId.PRICING)
                this.asyncServicesStub = PricingServicesGrpc.newStub(channel).withCompression("gzip");

            System.out.printf("created ManagedChannel " + channel.hashCode() + ", asyncServicesStub " + asyncServicesStub
                    + ", asyncServicesStub type " + asyncServicesStub.getClass());
        }

        public AbstractAsyncStub getAsyncServicesStub()  {
            return asyncServicesStub;
        }

        public boolean isConnected()    {
            switch (channel.getState(true)) {
                case READY:
                case IDLE:
                    return true;
                case CONNECTING:
                case TRANSIENT_FAILURE:
                case SHUTDOWN:
                default:
                    close();
                    return false;
            }
        }

        public ManagedChannel getChannel()  {
            return channel;
        }

        @Override
        public void close() {
            try {
                System.out.printf("closing ManagedChannel " + channel.hashCode() + ", asyncServicesStub " + asyncServicesStub);
                if (channel != null) {
                    channel.shutdownNow();
                    channel = null;
                    System.out.printf("closed ManagedChannel " + channel.hashCode() + ", asyncServicesStub " + asyncServicesStub);
                }
            } catch (Exception ex)  {
                System.out.printf("Error closing ManagedChannel " + channel.hashCode() + ", asyncServicesStub " + asyncServicesStub);
            }
        }
    }

}
