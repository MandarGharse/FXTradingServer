package com.fx.client.handlers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fx.client.grpc.GrpcClientManager;
import com.fx.client.service.StompEnhancedMessageSender;
import com.fx.client.websocket.StompPrincipal;
import com.fx.client.websocket.endpoints.SubscriptionEndPoints;
import com.fx.domain.json.FXTradesSubscriptionRequest;
import com.fx.client.grpc.GrpcClientId;
import com.fx.proto.messaging.TradeMessages;
import com.fx.proto.services.TradeServicesGrpc;
import com.fx.common.utils.ObjectMapperUtil;
import com.google.protobuf.InvalidProtocolBufferException;
import com.google.protobuf.util.JsonFormat;
import io.grpc.stub.StreamObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FXTradesRequestHandler {
    @Autowired
    StompEnhancedMessageSender stompEnhancedMessageSender;

    private JsonFormat.Printer jsonPrinter = JsonFormat.printer()
            .includingDefaultValueFields()
            .omittingInsignificantWhitespace();

    public void onFXTradesSubscriptionRequest(FXTradesSubscriptionRequest fxTradesSubscriptionRequest, StompPrincipal stompPrincipal) throws JsonProcessingException {
        System.out.println("processing fxTradesSubscriptionRequest " + fxTradesSubscriptionRequest);

        //ObjectMapperUtil.objectMapper.writeValueAsString(tradesSubscriptionResponseMessage

        // GRPC subscription
        GrpcClientManager.GrpcClient grpcClient = GrpcClientManager.getGrpcClient(GrpcClientId.TRADES);
        System.out.println("retrieved grpc client handle for fxTradesSubscriptionRequest " + fxTradesSubscriptionRequest);
        StreamObserver<TradeMessages.TradesSubscriptionResponseMessage> subscriptionRequestMessageStreamObserver =
                new StreamObserver<>() {
                    @Override
                    public void onNext(TradeMessages.TradesSubscriptionResponseMessage tradesSubscriptionResponseMessage) {
                        System.out.println("onNext:TradesSubscriptionResponseMessage " + tradesSubscriptionResponseMessage);
                        try {
                            String jsonMessageString = jsonPrinter.print(tradesSubscriptionResponseMessage);
                            System.out.println("jsonMessageString : " + jsonMessageString);
                            stompEnhancedMessageSender.sendMessage(stompPrincipal.getName(),
                                    jsonMessageString,
                                    SubscriptionEndPoints.FXTRADES_SUBSCRIPTION_ENDPOINT);
                            System.out.println("message sent to sessionId : " + stompPrincipal.getName());
                        } catch (InvalidProtocolBufferException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        System.out.println("onError:TradesSubscriptionResponseMessage " + throwable.getMessage());
                    }

                    @Override
                    public void onCompleted() {
                        System.out.println("onCompleted:TradesSubscriptionResponseMessage");
                    }
                };

        System.out.println("invoking grpc server for fxTradesSubscriptionRequest " + fxTradesSubscriptionRequest);
        invokeGrpc(grpcClient.getAsyncTradeServicesStub(), subscriptionRequestMessageStreamObserver);
        System.out.println("invoked grpc server for fxTradesSubscriptionRequest " + fxTradesSubscriptionRequest);

//        List<FxTrade> fxTrades = new ArrayList<>();
//        fxTrades.add(new FxTrade("111", "1", "EURUSD", "BUY", "EUR", "1000", "1234.56"));
//        fxTrades.add(new FxTrade("112", "1", "EURUSD", "BUY", "EUR", "2000", "2234.56"));
//        fxTradesSubscriptionResponse.setTrades(fxTrades);

//        stompEnhancedMessageSender.sendMessage(stompPrincipal.getName(),
//                ObjectMapperUtil.objectMapper.writeValueAsString(fxTradesSubscriptionResponse), SubscriptionEndPoints.FXTRADES_SUBSCRIPTION_ENDPOINT);
    }

    protected StreamObserver<TradeMessages.TradesSubscriptionRequestMessage> invokeGrpc(TradeServicesGrpc.TradeServicesStub tradeServicesStub,
                                                                                        StreamObserver<TradeMessages.TradesSubscriptionResponseMessage> subscriptionRequestMessageStreamObserver)  {
        return tradeServicesStub.subscribeTrades(subscriptionRequestMessageStreamObserver);
    }

}
