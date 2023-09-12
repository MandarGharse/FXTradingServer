package com.fx.server.listener;

import com.fx.proto.messaging.TradeMessages;
import com.fx.server.cache.TradesCache;
import com.fx.server.spoofer.TradeSpoofer;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

import java.util.Timer;
import java.util.TimerTask;

public class TradesListener {
    /**
     * Listens for external trade events
     * Write the event to TradesCache
     * Updates the listeners
     */

    static Subject<Object> tradeSubject = PublishSubject.create().toSerialized();

    static TradesListener instance = null;
    boolean spoofTradesEnabled = true;

    private TradesListener()    {}

    public static TradesListener getInstance() {
        if (instance == null){
            instance = new TradesListener();
            instance.init();
        }
        return instance;
    }

    public void init()    {
        System.out.println("instance #" + this.hashCode());

        if (spoofTradesEnabled) {
            TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    TradeMessages.Trade trade = TradeSpoofer.spoofTrade();
                    onMessage(trade);
                }
            };
            Timer timer = new Timer("TradeEvent");
            timer.scheduleAtFixedRate(timerTask, 0, 30 * 1000);
        }
    }

    // process live trade event
    private static void onMessage(TradeMessages.Trade trade) {
        TradesCache.getInstance().putItem(trade);
        tradeSubject.onNext(trade);
    }

    public Subject<Object> getTradeSubject() {
        return tradeSubject;
    }

}
