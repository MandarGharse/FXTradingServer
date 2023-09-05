package com.fx.client.handlers;

import com.fx.client.grpc.GrpcClientManager;
import com.fx.client.service.StompEnhancedMessageSender;
import com.fx.client.websocket.StompPrincipal;
import com.fx.client.websocket.endpoints.SubscriptionEndPoints;
import com.fx.domain.json.BlotterFillRequestMessage;
import com.fx.domain.json.BlotterSubscriptionRequestMessage;
import com.fx.common.enums.GrpcClientId;
import com.fx.proto.messaging.TradeMessages;
import com.fx.proto.services.TradeServicesGrpc;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.Message;
import com.google.protobuf.util.JsonFormat;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class PortfolioSubscriptionRequestHandler {
    @Autowired
    StompEnhancedMessageSender stompEnhancedMessageSender;
    @Autowired
    GrpcClientManager grpcClientManagerPortfolio;

    private JsonFormat.Printer jsonPrinter = JsonFormat.printer()
            .includingDefaultValueFields()
            .omittingInsignificantWhitespace();

    private GrpcClientManager.GrpcClient grpcClient;
    private Map<String, StreamObserver<TradeMessages.PortfolioSubscriptionRequestMessage>>
            portfolioSubscriptionRequestMessageStreamObserverMap;

    @Autowired
    public PortfolioSubscriptionRequestHandler() {
        grpcClient = grpcClientManagerPortfolio.getGrpcClient(GrpcClientId.TRADES);  // GRPC subscription
        System.out.println("retrieved grpc client handle " + grpcClient);
        portfolioSubscriptionRequestMessageStreamObserverMap = new ConcurrentHashMap<>();
    }

    public synchronized void onBlotterSubscriptionRequest(BlotterSubscriptionRequestMessage blotterSubscriptionRequestMessage, StompPrincipal stompPrincipal) {
        System.out.println("processing onBlotterSubscriptionRequest " + blotterSubscriptionRequestMessage);

        StreamObserver<TradeMessages.PortfolioSubscriptionRequestMessage> portfolioSubscriptionRequestMessageStreamObserver =
                getOrCreatePortfolioSubscriptionRequestMessageStreamObserver(stompPrincipal);

        TradeMessages.BlotterSubscriptionRequest blotterSubscriptionRequestProto = TradeMessages.BlotterSubscriptionRequest.newBuilder()
                .setSessionKey(blotterSubscriptionRequestMessage.getSessionId())
//                .setSortQuery(blotterSubscriptionRequestMessage.getSortQuery())
//                .setFilterQuery(blotterSubscriptionRequestMessage.getFilterQuery())
                .build();
        TradeMessages.PortfolioSubscriptionRequestMessage portfolioSubscriptionRequestMessage =
                TradeMessages.PortfolioSubscriptionRequestMessage.newBuilder().setBlotterSubscriptionRequest(blotterSubscriptionRequestProto)
                        .build();

        portfolioSubscriptionRequestMessageStreamObserver.onNext(portfolioSubscriptionRequestMessage);
        System.out.println("invoked grpc server for blotterSubscriptionRequestMessage " + blotterSubscriptionRequestMessage);
    }

    public synchronized void onBlotterFillRequest(BlotterFillRequestMessage blotterFillRequestMessage, StompPrincipal stompPrincipal) {
        System.out.println("processing onBlotterFillRequest " + blotterFillRequestMessage);

        StreamObserver<TradeMessages.PortfolioSubscriptionRequestMessage> portfolioSubscriptionRequestMessageStreamObserver =
                getOrCreatePortfolioSubscriptionRequestMessageStreamObserver(stompPrincipal);

        TradeMessages.BlotterFillRequest blotterFillRequestProto = TradeMessages.BlotterFillRequest.newBuilder()
                .setSessionKey(blotterFillRequestMessage.getSessionId())
                .setStartIndex(blotterFillRequestMessage.getStartIndex())
                .setEndIndex(blotterFillRequestMessage.getEndIndex())
                .build();
        TradeMessages.PortfolioSubscriptionRequestMessage portfolioSubscriptionRequestMessage =
                TradeMessages.PortfolioSubscriptionRequestMessage.newBuilder().setBlotterFillRequest(blotterFillRequestProto)
                        .build();

        portfolioSubscriptionRequestMessageStreamObserver.onNext(portfolioSubscriptionRequestMessage);
        System.out.println("invoked grpc server for blotterFillRequestMessage " + blotterFillRequestMessage);

    }

    private StreamObserver<TradeMessages.PortfolioSubscriptionRequestMessage> getOrCreatePortfolioSubscriptionRequestMessageStreamObserver(
            StompPrincipal stompPrincipal) {

        synchronized (portfolioSubscriptionRequestMessageStreamObserverMap) {
            if (portfolioSubscriptionRequestMessageStreamObserverMap.get(stompPrincipal.getName()) != null)
                return portfolioSubscriptionRequestMessageStreamObserverMap.get(stompPrincipal.getName());

            System.out.println("creating new PortfolioSubscriptionRequestMessageStreamObserve for sessionId " + stompPrincipal.getName());

            StreamObserver<TradeMessages.PortfolioSubscriptionResponseMessage> portfolioSubscriptionResponseStreamObserver =
                    new StreamObserver<>() {
                        @Override
                        public void onNext(TradeMessages.PortfolioSubscriptionResponseMessage portfolioSubscriptionResponseMessage) {
                            eventConsumer.accept(portfolioSubscriptionResponseMessage, stompPrincipal.getName());
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            System.out.println("onError:BlotterSubscriptionResponse " + throwable.getMessage());
                        }

                        @Override
                        public void onCompleted() {
                            System.out.println("onCompleted:BlotterSubscriptionResponse");
                        }
                    };

            StreamObserver<TradeMessages.PortfolioSubscriptionRequestMessage> portfolioSubscriptionRequestMessageStreamObserver =
                    subscribePortfolio(stompPrincipal.getName(), grpcClient.getAsyncTradeServicesStub(), portfolioSubscriptionResponseStreamObserver);

            portfolioSubscriptionRequestMessageStreamObserverMap.put(stompPrincipal.getName(), portfolioSubscriptionRequestMessageStreamObserver);
            System.out.println("created new PortfolioSubscriptionRequestMessageStreamObserve for sessionId " + stompPrincipal.getName());

            return portfolioSubscriptionRequestMessageStreamObserver;
        }
    }

    private MyConsumer<Message, String> eventConsumer = (eventMessage, sessionId) -> {
        try {
            System.out.println("eventMessage " + eventMessage + " " + eventMessage.getClass());
            String portfolioSubscriptionResponseJson = jsonPrinter.print(eventMessage);

            if (eventMessage instanceof TradeMessages.PortfolioSubscriptionResponseMessage) {
                if (((TradeMessages.PortfolioSubscriptionResponseMessage) eventMessage).hasBlotterSubscriptionResponse()) {
                    stompEnhancedMessageSender.sendMessage(sessionId,
                            portfolioSubscriptionResponseJson,
                            SubscriptionEndPoints.BLOTTER_SUBSCRIPTION_ENDPOINT);
                }
                if (((TradeMessages.PortfolioSubscriptionResponseMessage) eventMessage).hasBlotterFillResponse()) {
                    stompEnhancedMessageSender.sendMessage(sessionId,
                            portfolioSubscriptionResponseJson,
                            SubscriptionEndPoints.BLOTTER_FILL_ENDPOINT);
                }
                if (((TradeMessages.PortfolioSubscriptionResponseMessage) eventMessage).hasTradeResolutionResponse()) {
                    stompEnhancedMessageSender.sendMessage(sessionId,
                            portfolioSubscriptionResponseJson,
                            SubscriptionEndPoints.TRADE_RESOLUTION_ENDPOINT);
                }
                System.out.println("response message sent to client sessionId : " + sessionId);
            } else
                System.out.println("Invalid type of response received from backend server : " + eventMessage.getClass());
        } catch (InvalidProtocolBufferException e) {
            throw new RuntimeException(e);
        }

    };

    protected synchronized StreamObserver<TradeMessages.PortfolioSubscriptionRequestMessage> subscribePortfolio(
            String sessionId, TradeServicesGrpc.TradeServicesStub tradeServicesStub,
            StreamObserver<TradeMessages.PortfolioSubscriptionResponseMessage> subscriptionRequestMessageStreamObserver)  {
        System.out.println("subscribePortfolio called for sessionId " +sessionId );
        return tradeServicesStub.subscribePortfolio(subscriptionRequestMessageStreamObserver);
    }

}

@FunctionalInterface
interface MyConsumer<T, U> {
    void accept(T t, U u);

    default MyConsumer<T, U> andThen(MyConsumer<? super T, ? super U> after)    {
        Objects.requireNonNull(after);
        return (l, u) -> {
            accept(l, u);
            after.accept(l, u);
        };
    }

}
