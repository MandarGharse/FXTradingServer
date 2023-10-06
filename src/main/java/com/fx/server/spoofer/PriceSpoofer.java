package com.fx.server.spoofer;

import com.fx.common.utils.DateUtils;
import com.fx.proto.messaging.PricingMessages;
import com.fx.server.listener.PricingListener;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.*;

public class PriceSpoofer {

    static Random random = new Random();
    static String[] ccyPairs = new String[] {"EUR/USD", "USD/JPY", "EUR/JPY", "EUR/GBP", "USD/MXN", "USD/INR", "EUR/NOK", "EUR/MXN", "GBP/USD", "USD/RUB"};

    static String[] dates = new String[] {DateUtils.getCurrentDate(), DateUtils.getPreviousDate(), DateUtils.getNextDate()};

    public void init()  {
        PricingListener.getInstance();   // This will start the price spoofer to simulate incomming prices
    }

    public static PricingMessages.Price spoofPrice() {
        String ccyPair = ccyPairs[random.nextInt(ccyPairs.length)];
        System.out.println("spoofing price for ccyPair " + ccyPair);

        double bidRate = getBidRateByCcyPair(ccyPair).first;
        double askRate = getBidRateByCcyPair(ccyPair).second;
        PricingMessages.Price price = PricingMessages.Price.newBuilder()
                .setId(UUID.randomUUID().toString())    // quoteId
                .setCcyPair(ccyPair)
                // TODO : .setValueDate(dates[random.nextInt(dates.length)])
                .setValueDate("10032023")
                .setBidRate(bidRate)
                .setAskRate(askRate)
                .build();

        return price;
    }

    private static Pair getBidRateByCcyPair(String ccyPair) {
        if ("EUR/USD".equals(ccyPair))  {
            double v = random.nextDouble(1.1012, 1.2500);
            return new Pair(
                    round(v, 4), round(v+0.0003, 4));
        } else if ("USD/JPY".equals(ccyPair))  {
            double v = random.nextDouble(135, 160);
            return new Pair(
                    round(v, 2), round(v+0.0003, 2));
        } else if ("EUR/JPY".equals(ccyPair))  {
            double v = random.nextDouble(150, 160);
            return new Pair(
                    round(v, 2), round(v+0.0003, 2));
        } else if ("EUR/GBP".equals(ccyPair))  {
            double v = random.nextDouble(0.8, 1);
            return new Pair(
                    round(v, 2), round(v+0.0003, 2));
        } else if ("USD/MXN".equals(ccyPair))  {
            double v = random.nextDouble(15, 20);
            return new Pair(
                    round(v, 2), round(v+0.0003, 2));
        } else if ("USD/INR".equals(ccyPair))  {
            double v = random.nextDouble(80, 85);
            return new Pair(
                    round(v, 2), round(v+0.0003, 2));
        } else if ("EUR/NOK".equals(ccyPair))  {
            double v = random.nextDouble(10, 15);
            return new Pair(
                    round(v, 2), round(v+0.0003, 2));
        } else if ("EUR/MXN".equals(ccyPair))  {
            double v = random.nextDouble(15, 20);
            return new Pair(
                    round(v, 2), round(v+0.0003, 2));
        } else if ("GBP/USD".equals(ccyPair))  {
            double v = random.nextDouble(1.155, 1.255);
            return new Pair(
                    round(v, 3), round(v+0.0003, 3));
        } else if ("USD/RUB".equals(ccyPair))  {
            double v = random.nextDouble(90, 105);
            return new Pair(
                    round(v, 2), round(v+0.0003, 2));
        }
        else return new Pair(0.0, 0.0);
    }

    static DecimalFormat df = new DecimalFormat("0.00");
    static double round(double d, int scale)   {
        df.setRoundingMode(RoundingMode.HALF_UP);
        return Double.parseDouble(df.format(d));
    }

}

class Pair  {
    Double first;
    Double second;

    public Pair(Double first, Double second) {
        this.first = first;
        this.second = second;
    }
}
