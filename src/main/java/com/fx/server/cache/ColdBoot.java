package com.fx.server.cache;

import com.fx.proto.messaging.TradeMessages;
import com.fx.server.spoofer.TradeSpoofer;

public class ColdBoot {

    public static int SIZE = 10000;

    public ColdBoot() {

        for (int count=0; count<SIZE; count++) {
            TradeMessages.Trade trade = TradeSpoofer.spoofTrade();
            TradesCache.getInstance().putItem(trade);
        }
        System.out.println("cold boot loaded " + SIZE + " trades");
    }

}
