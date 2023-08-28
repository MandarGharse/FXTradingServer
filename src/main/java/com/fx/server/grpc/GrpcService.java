package com.fx.server.grpc;

import com.fx.proto.services.TradeServicesGrpc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GrpcService extends TradeServicesGrpc.TradeServicesImplBase {

    @Autowired
    TradesProviderService tradesProviderService;

    @Override
    public io.grpc.stub.StreamObserver<com.fx.proto.messaging.TradeMessages.TradesSubscriptionRequestMessage> subscribeTrades(
            io.grpc.stub.StreamObserver<com.fx.proto.messaging.TradeMessages.TradesSubscriptionResponseMessage> responseObserver) {
        System.out.println("subscribe trades called. responseObserver " + responseObserver.hashCode());

        tradesProviderService.initialize(responseObserver);
        tradesProviderService.subscribeTrades(responseObserver);

        return tradesProviderService;
    }

}
