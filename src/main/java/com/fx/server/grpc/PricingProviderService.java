package com.fx.server.grpc;

import com.fx.common.utils.DateUtils;
import com.fx.proto.messaging.PricingMessages;
import com.fx.proto.messaging.TradeMessages;
import com.fx.server.cache.PricingCache;
import com.fx.server.listener.PricingListener;
import io.grpc.stub.StreamObserver;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Disposable;

import java.util.*;

public class PricingProviderService implements StreamObserver<PricingMessages.PricingSubscriptionRequestMessage>, Disposable {

    StreamObserver<PricingMessages.PricingSubscriptionResponseMessage> outStream;
    PricingMessages.PricingSubscriptionRequest pricingSubscriptionRequest;

    private boolean isDisposed = false;

    String currentDate = DateUtils.getCurrentDate();    // TODO : handle refresh current date event

    public PricingProviderService(StreamObserver<PricingMessages.PricingSubscriptionResponseMessage> responseObserver) {
        super();
        this.outStream = responseObserver;

        processEvent();
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

        PricingMessages.Price price = PricingCache.getInstance().getItem(
                pricingSubscriptionRequest.getCcyPair(), pricingSubscriptionRequest.getValueDate());
        if (price != null)
            sendPricingSubscriptionSnapshot(pricingSubscriptionRequest, price);
    }

    private void sendPricingSubscriptionSnapshot(PricingMessages.PricingSubscriptionRequest pricingSubscriptionRequest, PricingMessages.Price price) {
        System.out.println("sendPricingSubscriptionSnapshot called");
        PricingMessages.PricingSubscriptionResponseMessage pricingSubscriptionResponseMessage =
                PricingMessages.PricingSubscriptionResponseMessage.newBuilder()
                        .setPricingSubscriptionResponse(buildPricingSubscriptionResponse(pricingSubscriptionRequest, price)).build();
        System.out.println("pricingSubscriptionResponseMessage built " + pricingSubscriptionResponseMessage);
        outStream.onNext(pricingSubscriptionResponseMessage);
        System.out.println("pricing subscription snapshot sent to responseObserver " + outStream.hashCode());
    }

    private PricingMessages.PricingSubscriptionResponse buildPricingSubscriptionResponse(PricingMessages.PricingSubscriptionRequest pricingSubscriptionRequest,
                                                                                         PricingMessages.Price price)    {
        return PricingMessages.PricingSubscriptionResponse.newBuilder()
                .setSessionKey(pricingSubscriptionRequest.getSessionKey())
                .setId(pricingSubscriptionRequest.getId())
                .setCcyPair(pricingSubscriptionRequest.getCcyPair())
                .setValueDate(pricingSubscriptionRequest.getValueDate())
                .setPrice(PricingMessages.Price.newBuilder().setBidRate(price.getBidRate()).setAskRate(price.getAskRate()).build())
                .setSuccess(true)
                .build();
    }

    private void processEvent() {
        PricingListener.getInstance().getPricingSubject().subscribe(priceObj -> {
                    System.out.println("received price : " + priceObj);
                    System.out.println("for pricingSubscriptionRequest : " + pricingSubscriptionRequest);
                    PricingMessages.Price price = (PricingMessages.Price) priceObj;

                    // TODO : Check if user subscribed for ccyPair, valueDate before sending
                    if (pricingSubscriptionRequest.getCcyPair().equals(price.getCcyPair()) &&
                    pricingSubscriptionRequest.getValueDate().equals(price.getValueDate())) {
                        PricingMessages.PricingSubscriptionResponseMessage pricingSubscriptionResponseMessage =
                                PricingMessages.PricingSubscriptionResponseMessage.newBuilder()
                                        .setPricingSubscriptionResponse(
                                                PricingMessages.PricingSubscriptionResponse.newBuilder()
                                                        .setSessionKey(pricingSubscriptionRequest.getSessionKey())
                                                        .setId(pricingSubscriptionRequest.getId())
                                                        .setCcyPair(pricingSubscriptionRequest.getCcyPair())
                                                        .setValueDate(pricingSubscriptionRequest.getValueDate())
                                                        .setPrice(PricingMessages.Price.newBuilder()
                                                                .setBidRate(price.getBidRate())
                                                                .setAskRate(price.getAskRate()).build())
                                                        .setSuccess(true).build())
                                        .build();

                        outStream.onNext(pricingSubscriptionResponseMessage);
                        System.out.println("published pricingSubscriptionResponseMessage-> for responseObserver " + outStream.hashCode());
                    }
                },
                onError -> {
                    System.out.println("error " + onError);
                }
        );
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

