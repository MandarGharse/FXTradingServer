package com.fx.server.grpc;

import com.fx.proto.messaging.TradeMessages;
import io.grpc.stub.StreamObserver;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Disposable;
import org.springframework.stereotype.Component;

public class TradesProviderService implements StreamObserver<TradeMessages.TradesSubscriptionRequestMessage>, Disposable {

    StreamObserver<TradeMessages.TradesSubscriptionResponseMessage> outStream;
    TradeMessages.TradesSubscriptionRequestMessage tradesSubscriptionRequestMessage;

    private boolean isDisposed = false;

    public TradesProviderService(StreamObserver<TradeMessages.TradesSubscriptionResponseMessage> responseObserver) {
        super();
        this.outStream = responseObserver;
    }

    public io.grpc.stub.StreamObserver<com.fx.proto.messaging.TradeMessages.TradesSubscriptionRequestMessage> subscribeTrades(
            io.grpc.stub.StreamObserver<com.fx.proto.messaging.TradeMessages.TradesSubscriptionResponseMessage> responseObserver) {
        System.out.println("subscribe trades called. responseObserver " + responseObserver.hashCode());
        int total_trades_count = 0;
        while (true) {
            TradeMessages.TradesSubscriptionResponseMessage tradesSubscriptionResponseMessage =
                    TradeMessages.TradesSubscriptionResponseMessage.newBuilder()
                            //.setSessionKey(tradesSubscriptionRequestMessage.getSessionKey())
                            .setSummary(TradeMessages.TradeSummary.newBuilder()
                                    .setTotalTrades(++total_trades_count)
                                    .setTradesTradedateToday(10)
                                    .setTradesValuedateToday(30).build()).build();
            responseObserver.onNext(tradesSubscriptionResponseMessage);
            System.out.println("published trade summary # " + total_trades_count);

            try {
                Thread.sleep(30*1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void onNext(TradeMessages.TradesSubscriptionRequestMessage tradesSubscriptionRequestMessage) {
        System.out.println("onNext called with tradesSubscriptionRequestMessage " + tradesSubscriptionRequestMessage);
        if (isDisposed)   {
            outStream.onError(new Throwable("stream disposed!"));
            return;
        }
        this.tradesSubscriptionRequestMessage = tradesSubscriptionRequestMessage;
//        try {
//            tradesSubscriptionRequestMessage
//        }
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("onError!");
        dispose();
    }

    @Override
    public void onCompleted() {
        System.out.println("onCompleted!");
        dispose();
    }

    @Override
    public void dispose() {
        isDisposed = true;
    }

}
