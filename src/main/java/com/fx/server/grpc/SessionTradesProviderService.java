package com.fx.server.grpc;

import com.fx.proto.messaging.TradeMessages;
import com.fx.server.listener.TradesListener;
import io.grpc.stub.StreamObserver;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Disposable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SessionTradesProviderService implements StreamObserver<TradeMessages.TradesSubscriptionRequestMessage>, Disposable {

    StreamObserver<TradeMessages.TradesSubscriptionResponseMessage> outStream;
    TradeMessages.TradesSubscriptionRequestMessage tradesSubscriptionRequestMessage;

    List<TradeMessages.Trade> tradesList = new ArrayList<>();

    private boolean isDisposed = false;

    public SessionTradesProviderService(StreamObserver<TradeMessages.TradesSubscriptionResponseMessage> responseObserver) {
        super();
        this.outStream = responseObserver;
    }

    public io.grpc.stub.StreamObserver<com.fx.proto.messaging.TradeMessages.TradesSubscriptionRequestMessage> subscribeTrades(
            io.grpc.stub.StreamObserver<com.fx.proto.messaging.TradeMessages.TradesSubscriptionResponseMessage> responseObserver) {

        AtomicInteger total_trades_count = new AtomicInteger(0);

        TradesListener.getInstance().getTradeSubject().subscribe(tradeObj -> {
                    TradeMessages.Trade trade = (TradeMessages.Trade) tradeObj;
                    tradesList.add(trade);
                    TradeMessages.TradesSubscriptionResponseMessage tradesSubscriptionResponseMessage =
                            TradeMessages.TradesSubscriptionResponseMessage.newBuilder()
                                    //.setSessionKey(tradesSubscriptionRequestMessage.getSessionKey())
                                    .setSummary(TradeMessages.TradeSummary.newBuilder()
                                            .setTotalTrades(total_trades_count.incrementAndGet())
                                            .setTradesTradedateToday(10)
                                            .setTradesValuedateToday(30).build())
                                    .addTrades(trade).build();
                    responseObserver.onNext(tradesSubscriptionResponseMessage);
                    System.out.println("published trade summary for responseObserver " + responseObserver.hashCode() + ". total trades : " + total_trades_count.get());
                },
                onError -> {
                    System.out.println("error " + onError);
                }
        );

        return new SessionTradesProviderService(outStream);

    }

    @Override
    public void onNext(TradeMessages.TradesSubscriptionRequestMessage tradesSubscriptionRequestMessage) {
        System.out.println("onNext called with tradesSubscriptionRequestMessage " + tradesSubscriptionRequestMessage);
        if (isDisposed) {
            outStream.onError(new Throwable("stream disposed!"));
            return;
        }
        this.tradesSubscriptionRequestMessage = tradesSubscriptionRequestMessage;

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
