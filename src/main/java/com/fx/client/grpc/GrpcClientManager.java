package com.fx.client.grpc;

import com.fx.proto.services.TradeServicesGrpc;
import com.fx.common.utils.ExecutorWorkerAppPool;
import io.grpc.ClientInterceptor;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.HashMap;
import java.util.Map;

public class GrpcClientManager {

    static final Map<GrpcClientId, GrpcClient> grpcClientMap = new HashMap<>();

    public GrpcClientManager(GrpcClientId grpcClientId, String hostname, int port) {
        System.out.println("GrpcClientManager called with grpcClientId " + grpcClientId + " hostname " + hostname + ", port " + port);
        SsoAuthClientInterceptor ssoAuthClientInterceptor = new SsoAuthClientInterceptor("", "");
        GrpcClient grpcClient = new GrpcClient(hostname, port, ssoAuthClientInterceptor);
        grpcClientMap.put(grpcClientId, grpcClient);
    }

    public static GrpcClient getGrpcClient(GrpcClientId grpcClientId)   {
        return grpcClientMap.get(grpcClientId);
    }

    public static class GrpcClient implements AutoCloseable {

        private ManagedChannel channel = null;
        private TradeServicesGrpc.TradeServicesStub asyncTradeServicesStub;

        public GrpcClient(String host, int port, ClientInterceptor... clientInterceptors) {
            init(ManagedChannelBuilder
                    .forAddress(host, port)
                    .intercept(clientInterceptors)
                    .usePlaintext()
                    .executor(ExecutorWorkerAppPool.getInstance().getExecutor())
                    .build());
        }

        public void init(ManagedChannel channel)    {
            this.channel = channel;
            this.asyncTradeServicesStub = TradeServicesGrpc.newStub(channel).withCompression("gzip");
            System.out.printf("created ManagedChannel " + channel.hashCode() + ", asyncTradeServicesStub " + asyncTradeServicesStub);
        }

        public TradeServicesGrpc.TradeServicesStub getAsyncTradeServicesStub()  {
            return asyncTradeServicesStub;
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
                System.out.printf("closing ManagedChannel " + channel.hashCode() + ", asyncTradeServicesStub " + asyncTradeServicesStub);
                if (channel != null) {
                    channel.shutdownNow();
                    channel = null;
                    System.out.printf("closed ManagedChannel " + channel.hashCode() + ", asyncTradeServicesStub " + asyncTradeServicesStub);
                }
            } catch (Exception ex)  {
                System.out.printf("Error closing ManagedChannel " + channel.hashCode() + ", asyncTradeServicesStub " + asyncTradeServicesStub);
            }
        }
    }

}
