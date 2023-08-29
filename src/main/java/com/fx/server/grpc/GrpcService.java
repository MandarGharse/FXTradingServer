package com.fx.server.grpc;

import com.fx.proto.services.TradeServicesGrpc;
import org.springframework.stereotype.Component;

@Component
public class GrpcService extends TradeServicesGrpc.TradeServicesImplBase {

    @Override
    public io.grpc.stub.StreamObserver<com.fx.proto.messaging.TradeMessages.TradesSubscriptionRequestMessage> subscribeTrades(
            io.grpc.stub.StreamObserver<com.fx.proto.messaging.TradeMessages.TradesSubscriptionResponseMessage> responseObserver) {
        System.out.println("subscribe trades called. responseObserver " + responseObserver.hashCode());

        SessionTradesProviderService sessionTradesProviderService = new SessionTradesProviderService(responseObserver);
        sessionTradesProviderService.subscribeTrades(responseObserver);

        return sessionTradesProviderService;
    }

}
