package com.fx.client.handlers;

import com.fx.client.grpc.GrpcClientManager;
import com.fx.client.service.StompEnhancedMessageSender;
import com.fx.client.websocket.StompPrincipal;
import com.fx.client.websocket.endpoints.SubscriptionEndPoints;
import com.fx.domain.json.BlotterSubscriptionRequestMessage;
import com.fx.common.enums.GrpcClientId;
import com.fx.proto.messaging.TradeMessages;
import com.fx.proto.services.TradeServicesGrpc;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PortfolioSubscriptionRequestHandler {
    @Autowired
    StompEnhancedMessageSender stompEnhancedMessageSender;

    private JsonFormat.Printer jsonPrinter = JsonFormat.printer()
            .includingDefaultValueFields()
            .omittingInsignificantWhitespace();

    public void onBlotterSubscriptionRequest(BlotterSubscriptionRequestMessage blotterSubscriptionRequestMessage, StompPrincipal stompPrincipal) {
        System.out.println("processing onBlotterSubscriptionRequest " + blotterSubscriptionRequestMessage);

        // GRPC subscription
        GrpcClientManager.GrpcClient grpcClient = GrpcClientManager.getGrpcClient(GrpcClientId.TRADES);
        System.out.println("retrieved grpc client handle for blotterSubscriptionRequestMessage " + blotterSubscriptionRequestMessage);

        StreamObserver<TradeMessages.PortfolioSubscriptionResponseMessage> portfolioSubscriptionResponseStreamObserver =
                new StreamObserver<>() {
                    @Override
                    public void onNext(TradeMessages.PortfolioSubscriptionResponseMessage portfolioSubscriptionResponseMessage) {
                        System.out.println("onNext:portfolioSubscriptionResponseMessage " + portfolioSubscriptionResponseMessage);
                        try {
                            String portfolioSubscriptionResponseJson = jsonPrinter.print(portfolioSubscriptionResponseMessage);
                            System.out.println("converted portfolioSubscriptionResponseMessage to json : " + portfolioSubscriptionResponseMessage);
                            stompEnhancedMessageSender.sendMessage(stompPrincipal.getName(),
                                    portfolioSubscriptionResponseJson,
                                    SubscriptionEndPoints.BLOTTER_SUBSCRIPTION_ENDPOINT);
                            System.out.println("message sent to sessionId : " + stompPrincipal.getName());
                        } catch (InvalidProtocolBufferException e) {
                            throw new RuntimeException(e);
                        }
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

        System.out.println("invoking grpc server for portfolioSubscriptionResponseStreamObserver " + portfolioSubscriptionResponseStreamObserver);

        StreamObserver<TradeMessages.PortfolioSubscriptionRequestMessage> portfolioSubscriptionRequestMessageStreamObserver =
                invokeGrpc(grpcClient.getAsyncTradeServicesStub(), portfolioSubscriptionResponseStreamObserver);

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

//    public void onFXTradesSubscriptionRequest(TradesSubscriptionRequestMessage tradesSubscriptionRequestMessage, StompPrincipal stompPrincipal) throws JsonProcessingException {
//        System.out.println("processing tradesSubscriptionRequestMessage " + tradesSubscriptionRequestMessage);
//
//        // GRPC subscription
//        GrpcClientManager.GrpcClient grpcClient = GrpcClientManager.getGrpcClient(GrpcClientId.TRADES);
//        System.out.println("retrieved grpc client handle for tradesSubscriptionRequestMessage " + tradesSubscriptionRequestMessage);
//        StreamObserver<TradeMessages.TradesSubscriptionResponseMessage> subscriptionRequestMessageStreamObserver =
//                new StreamObserver<>() {
//                    @Override
//                    public void onNext(TradeMessages.TradesSubscriptionResponseMessage tradesSubscriptionResponseMessage) {
//                        System.out.println("onNext:TradesSubscriptionResponseMessage " + tradesSubscriptionResponseMessage);
//                        try {
//                            String jsonMessageString = jsonPrinter.print(tradesSubscriptionResponseMessage);
//                            System.out.println("jsonMessageString : " + jsonMessageString);
//                            stompEnhancedMessageSender.sendMessage(stompPrincipal.getName(),
//                                    jsonMessageString,
//                                    SubscriptionEndPoints.FXTRADES_SUBSCRIPTION_ENDPOINT);
//                            System.out.println("message sent to sessionId : " + stompPrincipal.getName());
//                        } catch (InvalidProtocolBufferException e) {
//                            throw new RuntimeException(e);
//                        }
//                    }
//
//                    @Override
//                    public void onError(Throwable throwable) {
//                        System.out.println("onError:TradesSubscriptionResponseMessage " + throwable.getMessage());
//                    }
//
//                    @Override
//                    public void onCompleted() {
//                        System.out.println("onCompleted:TradesSubscriptionResponseMessage");
//                    }
//                };
//
//        System.out.println("invoking grpc server for tradesSubscriptionRequestMessage " + tradesSubscriptionRequestMessage);
//        invokeGrpc(grpcClient.getAsyncTradeServicesStub(), subscriptionRequestMessageStreamObserver);
//        System.out.println("invoked grpc server for tradesSubscriptionRequestMessage " + tradesSubscriptionRequestMessage);
//
//    }

    protected StreamObserver<TradeMessages.PortfolioSubscriptionRequestMessage> invokeGrpc(TradeServicesGrpc.TradeServicesStub tradeServicesStub,
                                                                                        StreamObserver<TradeMessages.PortfolioSubscriptionResponseMessage> subscriptionRequestMessageStreamObserver)  {
        return tradeServicesStub.subscribePortfolio(subscriptionRequestMessageStreamObserver);
    }

}
