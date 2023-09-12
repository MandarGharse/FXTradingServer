package com.fx.server.grpc;

import com.fx.common.enums.KPILabel;
import com.fx.common.utils.DateUtils;
import com.fx.proto.messaging.TradeMessages;
import com.fx.server.cache.TradesCache;
import com.fx.server.listener.TradesListener;
import io.grpc.stub.StreamObserver;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Disposable;

import java.util.*;
import java.util.stream.Collectors;

public class SessionTradesProviderService implements StreamObserver<TradeMessages.PortfolioSubscriptionRequestMessage>, Disposable {

    StreamObserver<TradeMessages.PortfolioSubscriptionResponseMessage> outStream;
    TradeMessages.BlotterSubscriptionRequest blotterSubscriptionRequest;
    TradeMessages.BlotterFillRequest blotterFillRequest;
    TradeMessages.TradeResolutionRequest tradeResolutionRequest;

    List<TradeMessages.Trade> tradesList = new ArrayList<>();   // list of trades sorted/filtered by user criteria

    private boolean isDisposed = false;

    String currentDate = DateUtils.getCurrentDate();    // TODO : handle refresh current date event

    public SessionTradesProviderService(StreamObserver<TradeMessages.PortfolioSubscriptionResponseMessage> responseObserver) {
        super();
        this.outStream = responseObserver;

        processEvent();
    }

    @Override
    public void onNext(TradeMessages.PortfolioSubscriptionRequestMessage portfolioSubscriptionRequestMessage) {
        System.out.println("onNext called with portfolioSubscriptionRequestMessage " + portfolioSubscriptionRequestMessage);
        if (isDisposed) {
            outStream.onError(new Throwable("stream disposed!"));
            return;
        }

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
        this.blotterSubscriptionRequest = blotterSubscriptionRequest;

        // TODO : only pull based on Criteria/filter
        List<TradeMessages.Trade> tradesListtemp = (List<TradeMessages.Trade>) TradesCache.getInstance().getTradesCache().values();
        tradesList.clear();
        tradesList.addAll(tradesListtemp);

        sendBlotterSubscriptionSnapshot(blotterSubscriptionRequest);
    }

    private void onNext(TradeMessages.BlotterFillRequest blotterFillRequest) {
        System.out.println("processing blotterFillRequest " + blotterFillRequest);
        this.blotterFillRequest = blotterFillRequest;

        sendBlotterFillSnapshot(blotterFillRequest);
    }

    private void onNext(TradeMessages.TradeResolutionRequest tradeResolutionRequest) {
        System.out.println("processing tradeResolutionRequest " + tradeResolutionRequest);
        this.tradeResolutionRequest = tradeResolutionRequest;

        sendTradeResolutionResponse(tradeResolutionRequest);
    }

    private void processEvent() {
        TradesListener.getInstance().getTradeSubject().subscribe(tradeObj -> {
                    TradeMessages.Trade trade = (TradeMessages.Trade) tradeObj;
                    tradesList.add(trade);
                    System.out.println("added to cache. size " + tradesList.size());      // TODO sort/filter the cache

                    TradeMessages.PortfolioSubscriptionResponseMessage portfolioSubscriptionResponseMessage =
                            TradeMessages.PortfolioSubscriptionResponseMessage.newBuilder()
                                    .setBlotterSubscriptionResponse(buildBlotterSubscriptionResponse(blotterSubscriptionRequest))
                                    .build();
                    outStream.onNext(portfolioSubscriptionResponseMessage);
                    System.out.println("published portfolioSubscriptionResponseMessage->BlotterSubscriptionResponse for responseObserver " + outStream.hashCode());
                    //System.out.println("published portfolioSubscriptionResponseMessage->BlotterSubscriptionResponse for responseObserver " + outStream.hashCode() +
                    //        ". portfolioSubscriptionResponseMessage : " + portfolioSubscriptionResponseMessage);

                    portfolioSubscriptionResponseMessage =
                            TradeMessages.PortfolioSubscriptionResponseMessage.newBuilder()
                                    .setBlotterFillResponse(buildBlotterFillResponse(blotterFillRequest))
                                    .build();
                    outStream.onNext(portfolioSubscriptionResponseMessage);
                    System.out.println("published portfolioSubscriptionResponseMessage->BlotterFillResponse for responseObserver " + outStream.hashCode());
                    //System.out.println("published portfolioSubscriptionResponseMessage->BlotterFillResponse for responseObserver " + outStream.hashCode() +
                    //        ". portfolioSubscriptionResponseMessage : " + portfolioSubscriptionResponseMessage);
                },
                onError -> {
                    System.out.println("error " + onError);
                }
        );
    }

    private void sendTradeResolutionResponse(TradeMessages.TradeResolutionRequest tradeResolutionRequest) {
        TradeMessages.PortfolioSubscriptionResponseMessage portfolioSubscriptionResponseMessage =
                TradeMessages.PortfolioSubscriptionResponseMessage.newBuilder()
                        .setTradeResolutionResponse(buildTradeResolutionResponse(tradeResolutionRequest)).build();
        outStream.onNext(portfolioSubscriptionResponseMessage);
        System.out.println("trade resolution snapshot sent to responseObserver " + outStream.hashCode());
    }

    private TradeMessages.TradeResolutionResponse buildTradeResolutionResponse(TradeMessages.TradeResolutionRequest tradeResolutionRequest) {
        List<TradeMessages.Trade> resolvedTradesList = new ArrayList<>();
        tradeResolutionRequest.getTradeKeysList().forEach(tradeKey -> {
            TradeMessages.Trade trade = TradesCache.getInstance().getItem(tradeKey);
            resolvedTradesList.add(trade);
        });
        TradeMessages.TradeResolutionResponse tradeResolutionResponse = TradeMessages.TradeResolutionResponse.newBuilder()
                .setSessionKey(tradeResolutionRequest.getSessionKey())
                .addAllTrades(resolvedTradesList)
                .build();

        return tradeResolutionResponse;
    }

    private void sendBlotterSubscriptionSnapshot(TradeMessages.BlotterSubscriptionRequest blotterSubscriptionRequest) {
        TradeMessages.PortfolioSubscriptionResponseMessage portfolioSubscriptionResponseMessage =
                TradeMessages.PortfolioSubscriptionResponseMessage.newBuilder()
                        .setBlotterSubscriptionResponse(buildBlotterSubscriptionResponse(blotterSubscriptionRequest)).build();
        outStream.onNext(portfolioSubscriptionResponseMessage);
        System.out.println("blotter subscription snapshot sent to responseObserver " + outStream.hashCode());
    }

    private TradeMessages.BlotterSubscriptionResponse buildBlotterSubscriptionResponse(TradeMessages.BlotterSubscriptionRequest blotterSubscriptionRequest)    {
        return TradeMessages.BlotterSubscriptionResponse.newBuilder()
                .setSessionKey(blotterSubscriptionRequest.getSessionKey())
                .setTotalTradeCount(tradesList.size())
                .setTotalTradeVolume(tradesList.stream().mapToDouble(value -> value.getUsdAmount()).sum())
                .setSummaryResponse(TradeMessages.SummaryResponse.newBuilder()
                        .addSummary(buildKPIList(tradesList))
                        .build())
                .setSuccess(true)
                .build();
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

    private void sendBlotterFillSnapshot(TradeMessages.BlotterFillRequest blotterFillRequest) {
        TradeMessages.PortfolioSubscriptionResponseMessage portfolioSubscriptionResponseMessage =
                TradeMessages.PortfolioSubscriptionResponseMessage.newBuilder()
                        .setBlotterFillResponse(buildBlotterFillResponse(blotterFillRequest))
                        .build();
        outStream.onNext(portfolioSubscriptionResponseMessage);
        System.out.println("blotter fill snapshot sent to responseObserver " + outStream.hashCode());
    }

    private TradeMessages.BlotterFillResponse buildBlotterFillResponse(TradeMessages.BlotterFillRequest blotterFillRequest)    {
        return TradeMessages.BlotterFillResponse.newBuilder()
                .setSessionKey(blotterFillRequest.getSessionKey())
                .setStartIndex(blotterFillRequest.getStartIndex())
                .setEndIndex(blotterFillRequest.getEndIndex())
                .addAllTradesData(getTradesSublist(blotterFillRequest.getStartIndex(), blotterFillRequest.getEndIndex()))
                .build();
    }

    private Iterable<? extends TradeMessages.TradeKeyVersion> getTradesSublist(long startIndex, long endIndex) {
        System.out.println("getTradesSublist called with " + startIndex + ", " + endIndex +". tradesList.size() : " + tradesList.size());
        if (startIndex > tradesList.size() || tradesList.size() == 0)
            return new ArrayList<>();
        if (endIndex > tradesList.size())
            endIndex = tradesList.size();
        List<TradeMessages.TradeKeyVersion> collect = tradesList.stream().map(trade -> trade.getTradeKeyVersion()).collect(Collectors.toList());
        System.out.println("sublist returning data between startIndex:" + startIndex + ", endIndex:" + endIndex);
        return collect.subList(Long.valueOf(startIndex).intValue(), Long.valueOf(endIndex).intValue());
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("onError! " + throwable);
        System.out.println(throwable.getMessage());
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
