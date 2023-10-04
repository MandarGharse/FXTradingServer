package com.fx.server.grpc;

import com.fx.proto.messaging.PricingMessages;
import com.fx.proto.messaging.TradeMessages;
import com.fx.proto.services.PricingServicesGrpc;
import com.fx.proto.services.TradeServicesGrpc;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Component;

@Component
public class GrpcPricingService extends PricingServicesGrpc.PricingServicesImplBase {

    @Override
    public StreamObserver<PricingMessages.PricingSubscriptionRequestMessage> subscribePricing
            (StreamObserver<PricingMessages.PricingSubscriptionResponseMessage> responseObserver) {
        System.out.println("subscribePricing called. responseObserver " + responseObserver.hashCode());
        return new PricingProviderService(responseObserver);
    }

}
