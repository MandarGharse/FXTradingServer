package com.fx.server.spoofer;

import com.fx.common.utils.DateUtils;
import com.fx.proto.messaging.TradeMessages;
import com.fx.server.listener.TradesListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Random;

public class TradeSpoofer {

    static String[] ccyPairs = new String[] {"EUR/USD", "USD/JPY", "EUR/JPY", "EUR/GBP", "USD/MXN", "USD/INR", "EUR/NOK"};
    static String[] buySells = new String[] {"Buy", "Sell"};

    static String[] dates = new String[] {DateUtils.getCurrentDate(), DateUtils.getPreviousDate(), DateUtils.getNextDate()};

    public void init()  {
        TradesListener.getInstance();   // This will start the trade spoofer to simulate incomming trades
    }

    public static TradeMessages.Trade spoofTrade() {
        Random random = new Random();
        String ccyPair = ccyPairs[random.nextInt(ccyPairs.length)];
        String[] dealtCcys = ccyPair.split("/");
        String buySell = buySells[random.nextInt(buySells.length)];

        TradeMessages.Trade trade = TradeMessages.Trade.newBuilder()
                .setTradeKeyVersion(TradeMessages.TradeKeyVersion.newBuilder()
                        .setTradeKey("TradeKey" + random.nextInt())
                        .setTradeVersion(1).build())
                .setCcyPair(ccyPair)
                .setBuySell(buySell)
                .setDealtCcy(dealtCcys[random.nextInt(dealtCcys.length)])
                .setDealtAmount(round(random.nextDouble(1000000.00), 2))
                .setCounterAmount(round(random.nextDouble(2000000.00), 2))
                .setTradeDate(dates[random.nextInt(dates.length)])
                .setValueDate(dates[random.nextInt(dates.length)])
                .setFixingDate(dates[random.nextInt(dates.length)])
                .setUsdAmount(round(random.nextDouble(5000000.00), 2))
                .setCreateTime(System.currentTimeMillis())
                .setLastUpdateTime(System.currentTimeMillis())
                .build();

        return trade;
    }

    static DecimalFormat df = new DecimalFormat("0.00");
    static double round(double d, int scale)   {
        df.setRoundingMode(RoundingMode.HALF_UP);
        return Double.parseDouble(df.format(d));
    }

}
