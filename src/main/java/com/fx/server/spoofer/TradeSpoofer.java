package com.fx.server.spoofer;

import com.fx.proto.messaging.TradeMessages;

import java.util.Random;

public class TradeSpoofer {

    static String[] ccyPairs = new String[] {"EUR/USD", "USD/JPY", "EUR/JPY", "EUR/GBP", "USD/MXN", "USD/INR", "EUR/NOK"};
    static String[] buySells = new String[] {"Buy", "Sell"};

    public static TradeMessages.Trade spoofTrade() {
        Random random = new Random();
        String ccyPair = ccyPairs[random.nextInt(ccyPairs.length)];
        String[] dealtCcys = ccyPair.split("/");
        String buySell = buySells[random.nextInt(buySells.length)];

        TradeMessages.Trade trade = TradeMessages.Trade.newBuilder()
                .setTradeKey("TradeKey" + random.nextInt())
                .setTradeVersion(1)
                .setCcyPair(ccyPair)
                .setBuySell(buySell)
                .setDealtCcy(dealtCcys[random.nextInt(dealtCcys.length)])
                .setDealtAmount(random.nextDouble(1000000.00))
                .setCounterAmount(random.nextDouble(2000000.00))
                .build();

        return trade;
    }

}
