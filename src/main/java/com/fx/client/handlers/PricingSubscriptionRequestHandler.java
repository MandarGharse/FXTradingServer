package com.fx.client.handlers;

import com.fx.client.grpc.GrpcClientManager;
import com.fx.client.service.StompEnhancedMessageSender;
import com.fx.client.websocket.StompPrincipal;
import com.fx.client.websocket.endpoints.SubscriptionEndPoints;
import com.fx.common.enums.GrpcClientId;
import com.fx.proto.messaging.PricingMessages;
import com.fx.proto.services.PricingServicesGrpc;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import com.fx.domain.json.PricingSubscriptionRequestMessage;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class PricingSubscriptionRequestHandler {
    @Autowired
    StompEnhancedMessageSender stompEnhancedMessageSender;

    private JsonFormat.Printer jsonPrinter = JsonFormat.printer()
            .includingDefaultValueFields()
            .omittingInsignificantWhitespace();

    private GrpcClientManager.GrpcClient grpcClient;
    private Map<String, StreamObserver<PricingMessages.PricingSubscriptionRequestMessage>>
            pricingSubscriptionRequestMessageStreamObserverMap;

    @Autowired
    public PricingSubscriptionRequestHandler(GrpcClientManager grpcClientManager) {
        grpcClient = grpcClientManager.createGrpcClient(GrpcClientId.PRICING);  // GRPC subscription
        System.out.println("retrieved grpc client handle " + grpcClient);
        pricingSubscriptionRequestMessageStreamObserverMap = new ConcurrentHashMap<>();
    }

    public synchronized void onPricingSubscriptionRequest(PricingSubscriptionRequestMessage pricingSubscriptionRequestMessage, StompPrincipal stompPrincipal) {
        System.out.println("processing onPricingSubscriptionRequest " + pricingSubscriptionRequestMessage);

        StreamObserver<PricingMessages.PricingSubscriptionRequestMessage> pricingSubscriptionRequestMessageStreamObserver =
                getOrCreatePricingSubscriptionRequestMessageStreamObserver(stompPrincipal, pricingSubscriptionRequestMessage.getId());

        PricingMessages.PricingSubscriptionRequest pricingSubscriptionRequestProto = PricingMessages.PricingSubscriptionRequest.newBuilder()
                .setSessionKey(pricingSubscriptionRequestMessage.getSessionId())
                .setId(pricingSubscriptionRequestMessage.getId())
                .setCcyPair(pricingSubscriptionRequestMessage.getCcyPair())
                .setValueDate(pricingSubscriptionRequestMessage.getValueDate())
                .build();

        PricingMessages.PricingSubscriptionRequestMessage pricingSubscriptionRequestMessageProto =
                PricingMessages.PricingSubscriptionRequestMessage.newBuilder().setPricingSubscriptionRequest(pricingSubscriptionRequestProto)
                        .build();

        pricingSubscriptionRequestMessageStreamObserver.onNext(pricingSubscriptionRequestMessageProto);
        System.out.println("invoked grpc server for pricingSubscriptionRequestMessageProto " + pricingSubscriptionRequestMessageProto);
    }

    private StreamObserver<PricingMessages.PricingSubscriptionRequestMessage> getOrCreatePricingSubscriptionRequestMessageStreamObserver(
            StompPrincipal stompPrincipal, String id) {

        synchronized (pricingSubscriptionRequestMessageStreamObserverMap) {
            if ((pricingSubscriptionRequestMessageStreamObserverMap.get(stompPrincipal.getName()+id) != null))
                return (pricingSubscriptionRequestMessageStreamObserverMap.get(stompPrincipal.getName()+id));

            System.out.println("creating new PricingSubscriptionRequestMessageStreamObserve for sessionId " + stompPrincipal.getName());

            StreamObserver<PricingMessages.PricingSubscriptionResponseMessage> pricingSubscriptionResponseStreamObserver =
                    new StreamObserver<>() {
                        @Override
                        public void onNext(PricingMessages.PricingSubscriptionResponseMessage pricingSubscriptionResponseMessage) {
                            System.out.println("received pricingSubscriptionResponseMessage >>> " + pricingSubscriptionResponseMessage);
                            eventConsumer.accept(pricingSubscriptionResponseMessage, stompPrincipal.getName());
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            System.out.println("onError:PricingSubscriptionResponse " + throwable.getMessage());
                        }

                        @Override
                        public void onCompleted() {
                            System.out.println("onCompleted:PricingSubscriptionResponse");
                        }
                    };

            StreamObserver<PricingMessages.PricingSubscriptionRequestMessage> pricingSubscriptionRequestMessageStreamObserver =
                    subscribePricing(stompPrincipal.getName(),
                            (PricingServicesGrpc.PricingServicesStub) grpcClient.getAsyncServicesStub(),
                            pricingSubscriptionResponseStreamObserver);

            pricingSubscriptionRequestMessageStreamObserverMap.put(stompPrincipal.getName()+id, pricingSubscriptionRequestMessageStreamObserver);
            System.out.println("created new pricingSubscriptionRequestMessageStreamObserverMap for sessionId, id " + stompPrincipal.getName() + " " + id);

            return pricingSubscriptionRequestMessageStreamObserver;
        }
    }

    private MyPricingConsumer<Message, String> eventConsumer = (eventMessage, sessionId) -> {
        try {
            System.out.println("eventMessage " + eventMessage + " " + eventMessage.getClass());
            String pricingSubscriptionResponseJson = jsonPrinter.print(eventMessage);

            if (eventMessage instanceof PricingMessages.PricingSubscriptionResponseMessage) {
                if (((PricingMessages.PricingSubscriptionResponseMessage) eventMessage).hasPricingSubscriptionResponse()) {
                    stompEnhancedMessageSender.sendMessage(sessionId,
                            pricingSubscriptionResponseJson,
                            SubscriptionEndPoints.PRICING_SUBSCRIPTION_ENDPOINT);
                }
                System.out.println("response message sent to client sessionId : " + sessionId);
            } else
                System.out.println("Invalid type of response received from backend server : " + eventMessage.getClass());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }

    };
    protected synchronized StreamObserver<PricingMessages.PricingSubscriptionRequestMessage> subscribePricing(
            String sessionId, PricingServicesGrpc.PricingServicesStub pricingServicesStub,
            StreamObserver<PricingMessages.PricingSubscriptionResponseMessage> subscriptionRequestMessageStreamObserver)  {
        System.out.println("subscribePrice called for sessionId " +sessionId );
        return pricingServicesStub.subscribePricing(subscriptionRequestMessageStreamObserver);
    }

}

@FunctionalInterface
interface MyPricingConsumer<T, U> {
    void accept(T t, U u);

    default MyPricingConsumer<T, U> andThen(MyPricingConsumer<? super T, ? super U> after)    {
        Objects.requireNonNull(after);
        return (l, u) -> {
            accept(l, u);
            after.accept(l, u);
        };
    }

}

