package com.fx.server.grpc;

import com.fx.common.enums.KPILabel;
import com.fx.common.utils.DateUtils;
import com.fx.proto.messaging.TradeMessages;
import com.fx.server.listener.TradesListener;
import io.grpc.stub.StreamObserver;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Disposable;

import java.util.*;

public class SessionTradesProviderService implements StreamObserver<TradeMessages.PortfolioSubscriptionRequestMessage>, Disposable {

    StreamObserver<TradeMessages.PortfolioSubscriptionResponseMessage> outStream;
    TradeMessages.PortfolioSubscriptionRequestMessage portfolioSubscriptionRequestMessage;

    List<TradeMessages.Trade> tradesList = new ArrayList<>();

    private boolean isDisposed = false;

    String currentDate = DateUtils.getCurrentDate();    // TODO : handle refresh current date event

    public SessionTradesProviderService(StreamObserver<TradeMessages.PortfolioSubscriptionResponseMessage> responseObserver) {
        super();
        this.outStream = responseObserver;
    }

//    public io.grpc.stub.StreamObserver<com.fx.proto.messaging.TradeMessages.PortfolioSubscriptionRequestMessage> subscribeTrades(
//            StreamObserver<TradeMessages.PortfolioSubscriptionResponseMessage> responseObserver) {
//
//        AtomicInteger total_trades_count = new AtomicInteger(0);
//
//        TradesListener.getInstance().getTradeSubject().subscribe(tradeObj -> {
//                    TradeMessages.Trade trade = (TradeMessages.Trade) tradeObj;
//                    tradesList.add(trade);
//                    TradeMessages.PortfolioSubscriptionResponseMessage portfolioSubscriptionResponseMessage =
//                            TradeMessages.PortfolioSubscriptionResponseMessage.newBuilder()
//                                    .setBlotterSubscriptionResponse(TradeMessages.BlotterSubscriptionResponse.newBuilder()
//                                            .setSuccess(true)).build();
//                    responseObserver.onNext(portfolioSubscriptionResponseMessage);
//                    System.out.println("published portfolioSubscriptionResponseMessage for responseObserver " + responseObserver.hashCode() + ". total trades : " + total_trades_count.get());
//                },
//                onError -> {
//                    System.out.println("error " + onError);
//                }
//        );
//
//        return new SessionTradesProviderService(outStream);
//
//    }

    @Override
    public void onNext(TradeMessages.PortfolioSubscriptionRequestMessage portfolioSubscriptionRequestMessage) {
        System.out.println("onNext called with portfolioSubscriptionRequestMessage " + portfolioSubscriptionRequestMessage);
        if (isDisposed) {
            outStream.onError(new Throwable("stream disposed!"));
            return;
        }
        this.portfolioSubscriptionRequestMessage = portfolioSubscriptionRequestMessage;

        try {
            if (portfolioSubscriptionRequestMessage.hasBlotterSubscriptionRequest())    {
                onNext(portfolioSubscriptionRequestMessage.getBlotterSubscriptionRequest());
            }
            if (portfolioSubscriptionRequestMessage.hasBlotterFillRequest())    {
                onNext(portfolioSubscriptionRequestMessage.getBlotterFillRequest());
            }
            if (portfolioSubscriptionRequestMessage.hasTradeResolutionRequest())    {
                onNext(portfolioSubscriptionRequestMessage.getTradeResolutionRequest());
            }
        } catch (Exception e)   {
            System.out.println("onNext encountered error. Disposing session and closing the outstream " + e);
            dispose();
            sendReject(e.getMessage());
        }

    }

    private void onNext(TradeMessages.BlotterSubscriptionRequest blotterSubscriptionRequest) {
        System.out.println("processing blotterSubscriptionRequest " + blotterSubscriptionRequest);

        TradesListener.getInstance().getTradeSubject().subscribe(tradeObj -> {
                    TradeMessages.Trade trade = (TradeMessages.Trade) tradeObj;
                    tradesList.add(trade);

                    TradeMessages.PortfolioSubscriptionResponseMessage portfolioSubscriptionResponseMessage =
                            TradeMessages.PortfolioSubscriptionResponseMessage.newBuilder()
                                    .setBlotterSubscriptionResponse(TradeMessages.BlotterSubscriptionResponse.newBuilder()
                                            .setSummaryResponse(TradeMessages.SummaryResponse.newBuilder()
                                                    .addSummary(buildKPIList(tradesList))
                                                    .build())
                                            .setSuccess(true)).build();
                    outStream.onNext(portfolioSubscriptionResponseMessage);
                    System.out.println("published portfolioSubscriptionResponseMessage for responseObserver " + outStream.hashCode() +
                            ". portfolioSubscriptionResponseMessage : " + portfolioSubscriptionResponseMessage);
                },
                onError -> {
                    System.out.println("error " + onError);
                }
        );

    }

    private TradeMessages.SummaryList buildKPIList(List<TradeMessages.Trade> tradesList) {
        TradeMessages.SummaryList.Builder summaryListBuilder = TradeMessages.SummaryList.newBuilder();

        Map<KPILabel, SummaryKPI> summaryKPIMap = new HashMap<>();

        long totalTrades = tradesList.size();
        double totalSumUSD = tradesList.stream().filter(KPILabel.TOTAL_TRADES.getTradePredicate())
                .mapToDouble(value -> value.getUsdAmount()).sum();
        summaryKPIMap.put(KPILabel.TOTAL_TRADES, new SummaryKPI(totalTrades, totalSumUSD));

        long tradeDateTodayCount = tradesList.stream().filter(KPILabel.TRADEDATE_TODAY.getTradePredicate()).count();
        double tradeDateTodaySumUSD = tradesList.stream().filter(KPILabel.TRADEDATE_TODAY.getTradePredicate())
                .mapToDouble(value -> value.getUsdAmount()).sum();
        summaryKPIMap.put(KPILabel.TRADEDATE_TODAY, new SummaryKPI(tradeDateTodayCount, tradeDateTodaySumUSD));

        long valueDateTodayCount = tradesList.stream().filter(KPILabel.VALUEDATE_TODAY.getTradePredicate()).count();
        double valueDateTodaySumUSD = tradesList.stream().filter(KPILabel.VALUEDATE_TODAY.getTradePredicate())
                .mapToDouble(value -> value.getUsdAmount()).sum();
        summaryKPIMap.put(KPILabel.VALUEDATE_TODAY, new SummaryKPI(valueDateTodayCount, valueDateTodaySumUSD));

        long fixingDateTodayCount = tradesList.stream().filter(KPILabel.FIXINGDATE_TODAY.getTradePredicate()).count();
        double fixingDateTodaySumUSD = tradesList.stream().filter(KPILabel.FIXINGDATE_TODAY.getTradePredicate())
                .mapToDouble(value -> value.getUsdAmount()).sum();
        summaryKPIMap.put(KPILabel.FIXINGDATE_TODAY, new SummaryKPI(fixingDateTodayCount, fixingDateTodaySumUSD));

        Arrays.stream(KPILabel.values()).forEach(kpiLabel -> {
            summaryListBuilder.addStatistics(TradeMessages.Statistics.newBuilder()
                    .setKey(kpiLabel.name())
                    .setCount(summaryKPIMap.get(kpiLabel).count)
                    .setVolume(summaryKPIMap.get(kpiLabel).volume).build());

        });

        return summaryListBuilder.build();

    }

    private void onNext(TradeMessages.BlotterFillRequest blotterFillRequest) {
        System.out.println("processing blotterFillRequest " + blotterFillRequest);
    }

    private void onNext(TradeMessages.TradeResolutionRequest tradeResolutionRequest) {
        System.out.println("processing tradeResolutionRequest " + tradeResolutionRequest);
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

    private void sendReject(String message) {
        TradeMessages.PortfolioSubscriptionResponseMessage portfolioSubscriptionResponseMessage =
                TradeMessages.PortfolioSubscriptionResponseMessage.newBuilder()
                        .setBlotterSubscriptionResponse(TradeMessages.BlotterSubscriptionResponse.newBuilder()
                                .setSuccess(false)
                                .setErrorMessage(message).build())
                .build();

        this.outStream.onNext(portfolioSubscriptionResponseMessage);
        this.outStream.onCompleted();
    }

}

class SummaryKPI    {
    long count;
    double volume;

    public SummaryKPI(long count, double volume) {
        this.count = count;
        this.volume = volume;
    }
}
