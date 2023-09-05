package com.fx.server.grpc;

import com.fx.proto.messaging.TradeMessages;
import com.fx.proto.services.TradeServicesGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Component;

@Component
public class GrpcService extends TradeServicesGrpc.TradeServicesImplBase {

    @Override
    public StreamObserver<TradeMessages.PortfolioSubscriptionRequestMessage> subscribePortfolio
            (StreamObserver<TradeMessages.PortfolioSubscriptionResponseMessage> responseObserver) {
        System.out.println("subscribePortfolio called. responseObserver " + responseObserver.hashCode());
        return new SessionTradesProviderService(responseObserver);
    }

}
