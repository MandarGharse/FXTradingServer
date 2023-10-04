package com.fx.server.grpc;

import com.fx.common.enums.KPILabel;
import com.fx.common.utils.DateUtils;
import com.fx.proto.messaging.PricingMessages;
import com.fx.proto.messaging.TradeMessages;
import com.fx.server.cache.TradesCache;
import com.fx.server.listener.TradesListener;
import io.grpc.stub.StreamObserver;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Disposable;

import java.util.*;
import java.util.stream.Collectors;

public class PricingProviderService implements StreamObserver<PricingMessages.PricingSubscriptionRequestMessage>, Disposable {

    StreamObserver<PricingMessages.PricingSubscriptionResponseMessage> outStream;
    PricingMessages.PricingSubscriptionRequest pricingSubscriptionRequest;

    List<PricingMessages.Price> tradesList = new ArrayList<>();   // list of price tile

    private boolean isDisposed = false;

    String currentDate = DateUtils.getCurrentDate();    // TODO : handle refresh current date event

    public PricingProviderService(StreamObserver<PricingMessages.PricingSubscriptionResponseMessage> responseObserver) {
        super();
        this.outStream = responseObserver;

        //processEvent();
    }

    @Override
    public void onNext(PricingMessages.PricingSubscriptionRequestMessage pricingSubscriptionRequestMessage) {
        System.out.println("onNext called with pricingSubscriptionRequestMessage " + pricingSubscriptionRequestMessage);
        if (isDisposed) {
            outStream.onError(new Throwable("stream disposed!"));
            return;
        }

        try {
            if (pricingSubscriptionRequestMessage.hasPricingSubscriptionRequest())    {
                onNext(pricingSubscriptionRequestMessage.getPricingSubscriptionRequest());
            }
        } catch (Exception e)   {
            System.out.println("onNext encountered error. Disposing session and closing the outstream " + e);
            dispose();
            sendReject(e.getMessage());
        }

    }

    private void onNext(PricingMessages.PricingSubscriptionRequest pricingSubscriptionRequest) {
        System.out.println("processing pricingSubscriptionRequest " + pricingSubscriptionRequest);
        this.pricingSubscriptionRequest = pricingSubscriptionRequest;

        // TODO : only pull based on Criteria/filter
        List<TradeMessages.Trade> tradesListtemp = (List<TradeMessages.Trade>) TradesCache.getInstance().getTradesCache().values();
        List<TradeMessages.Trade> tradesListtemp2 = tradesListtemp.stream().collect(Collectors.toList());


        sendPricingSubscriptionSnapshot(pricingSubscriptionRequest);
    }

    private void sendPricingSubscriptionSnapshot(PricingMessages.PricingSubscriptionRequest pricingSubscriptionRequest) {
        System.out.println("sendPricingSubscriptionSnapshot called");
        PricingMessages.PricingSubscriptionResponseMessage pricingSubscriptionResponseMessage =
                PricingMessages.PricingSubscriptionResponseMessage.newBuilder()
                        .setPricingSubscriptionResponse(buildPricingSubscriptionResponse(pricingSubscriptionRequest)).build();
        System.out.println("pricingSubscriptionResponseMessage built " + pricingSubscriptionResponseMessage);
        outStream.onNext(pricingSubscriptionResponseMessage);
        System.out.println("pricing subscription snapshot sent to responseObserver " + outStream.hashCode());
    }

    private PricingMessages.PricingSubscriptionResponse buildPricingSubscriptionResponse(PricingMessages.PricingSubscriptionRequest pricingSubscriptionRequest)    {
        return PricingMessages.PricingSubscriptionResponse.newBuilder()
                .setSessionKey(pricingSubscriptionRequest.getSessionKey())
                .setId(pricingSubscriptionRequest.getId())
                .setCcyPair(pricingSubscriptionRequest.getCcyPair())
                .setValueDate(pricingSubscriptionRequest.getValueDate())
                .setPrice(PricingMessages.Price.newBuilder().setBidRate(1.1234).setAskRate(1.1244).build())
                .setSuccess(true)
                .build();
    }

//    private void processEvent() {
//        TradesListener.getInstance().getTradeSubject().subscribe(tradeObj -> {
//                    TradeMessages.Trade trade = (TradeMessages.Trade) tradeObj;
//                    tradesList.add(trade);
//                    System.out.println("added to cache. size " + tradesList.size());      // TODO sort/filter the cache
//
//                    if (!blotterSubscriptionRequest.hasSortQuery()) { // apply Default sort : Refactor
//                        System.out.println("sorting...");
//                        tradesList.sort(Comparator.comparing(TradeMessages.Trade::getLastUpdateTime).reversed());
//                        System.out.println("sorted");
//                    } else
//                        System.out.println("TODO : use sort!!!");
//
//                    TradeMessages.PortfolioSubscriptionResponseMessage portfolioSubscriptionResponseMessage =
//                            TradeMessages.PortfolioSubscriptionResponseMessage.newBuilder()
//                                    .setBlotterSubscriptionResponse(buildBlotterSubscriptionResponse(blotterSubscriptionRequest))
//                                    .build();
//                    outStream.onNext(portfolioSubscriptionResponseMessage);
//                    System.out.println("published portfolioSubscriptionResponseMessage->BlotterSubscriptionResponse for responseObserver " + outStream.hashCode());
//                    //System.out.println("published portfolioSubscriptionResponseMessage->BlotterSubscriptionResponse for responseObserver " + outStream.hashCode() +
//                    //        ". portfolioSubscriptionResponseMessage : " + portfolioSubscriptionResponseMessage);
//
//                    if (blotterFillRequest != null) {
//                        portfolioSubscriptionResponseMessage =
//                                TradeMessages.PortfolioSubscriptionResponseMessage.newBuilder()
//                                        .setBlotterFillResponse(buildBlotterFillResponse(blotterFillRequest))
//                                        .build();
//                        outStream.onNext(portfolioSubscriptionResponseMessage);
//                        System.out.println("published portfolioSubscriptionResponseMessage->BlotterFillResponse for responseObserver " + outStream.hashCode());
//                        //System.out.println("published portfolioSubscriptionResponseMessage->BlotterFillResponse for responseObserver " + outStream.hashCode() +
//                        //        ". portfolioSubscriptionResponseMessage : " + portfolioSubscriptionResponseMessage);
//                    }
//                },
//                onError -> {
//                    System.out.println("error " + onError);
//                }
//        );
//    }

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
        PricingMessages.PricingSubscriptionResponseMessage pricingSubscriptionResponseMessage =
                PricingMessages.PricingSubscriptionResponseMessage.newBuilder()
                .setPricingSubscriptionResponse(
                        PricingMessages.PricingSubscriptionResponse.newBuilder()
                                .setSessionKey(pricingSubscriptionRequest.getSessionKey())
                                .setId(pricingSubscriptionRequest.getId())
                                .setCcyPair(pricingSubscriptionRequest.getCcyPair())
                                .setValueDate(pricingSubscriptionRequest.getValueDate())
                                .setSuccess(false).setErrorMessage(message).build())
                        .build();

        this.outStream.onNext(pricingSubscriptionResponseMessage);
        this.outStream.onCompleted();
    }

}

