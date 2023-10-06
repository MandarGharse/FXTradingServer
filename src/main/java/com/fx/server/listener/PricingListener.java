package com.fx.server.listener;

import com.fx.proto.messaging.PricingMessages;
import com.fx.server.cache.PricingCache;
import com.fx.server.spoofer.PriceSpoofer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.util.Timer;
import java.util.TimerTask;
public class PricingListener {
    /**
     * Listens for external pricing events
     * Write the event to PricingCache
     * Updates the listeners
     */

    static Subject<Object> priceSubject = PublishSubject.create().toSerialized();

    static PricingListener instance = null;
    boolean spoofPricingEnabled = true;

    private PricingListener()    {}

    public static PricingListener getInstance() {
        if (instance == null){
            instance = new PricingListener();
            instance.init();
        }
        return instance;
    }

    public void init()    {
        System.out.println("instance #" + this.hashCode());

        if (spoofPricingEnabled) {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    PricingMessages.Price price = PriceSpoofer.spoofPrice();
                    onMessage(price);
                }
            };
            Timer timer = new Timer("PriceEvent");
            timer.scheduleAtFixedRate(timerTask, 0, 1 * 1000);
        }
    }

    // process live trade event
    private static void onMessage(PricingMessages.Price price) {
        PricingCache.getInstance().putItem(price.getCcyPair(), price.getValueDate(), price);
        priceSubject.onNext(price);
    }

    public Subject<Object> getPricingSubject() {
        return priceSubject;
    }

}
