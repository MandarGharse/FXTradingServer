package com.fx.common.enums;

import com.fx.common.utils.DateUtils;
import com.fx.proto.messaging.TradeMessages;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public enum KPILabel {

    TOTAL_TRADES (trade1 -> trade1.getValueDate().compareTo(DateUtils.getCurrentDate()) >= 0 ? true : false),
    TRADEDATE_TODAY (trade1 -> trade1.getTradeDate().equals(DateUtils.getCurrentDate())),
    VALUEDATE_TODAY (trade1 -> trade1.getValueDate().equals(DateUtils.getCurrentDate())),
    FIXINGDATE_TODAY (trade1 -> trade1.getFixingDate().equals(DateUtils.getCurrentDate()));

    Predicate <TradeMessages.Trade> tradePredicate;
    KPILabel(Predicate<TradeMessages.Trade> tradePredicate) {
        this.tradePredicate = tradePredicate;
    }

    public Predicate<TradeMessages.Trade> getTradePredicate() {
        return tradePredicate;
    }

    public static void main(String[] args) {
        String td = "20230831";
        String vd = "20230830";
        TradeMessages.Trade trade1 = TradeMessages.Trade.newBuilder()
                .setTradeKeyVersion(TradeMessages.TradeKeyVersion.newBuilder().setTradeKey("Key1").build())
                .setTradeDate(DateUtils.getCurrentDate()).setValueDate(DateUtils.getPreviousDate()).build();
        TradeMessages.Trade trade2 = TradeMessages.Trade.newBuilder()
                .setTradeKeyVersion(TradeMessages.TradeKeyVersion.newBuilder().setTradeKey("Key2").build())
                .setTradeDate(DateUtils.getCurrentDate()).setValueDate(DateUtils.getCurrentDate()).build();
        TradeMessages.Trade trade3 = TradeMessages.Trade.newBuilder()
                .setTradeKeyVersion(TradeMessages.TradeKeyVersion.newBuilder().setTradeKey("Key3").build())
                .setTradeDate(DateUtils.getCurrentDate()).setFixingDate(DateUtils.getCurrentDate()).setValueDate(DateUtils.getNextDate()).build();
        List<TradeMessages.Trade> trades = new ArrayList<>();
        trades.add(trade1);
        trades.add(trade2);
        trades.add(trade3);

        trades.stream().filter(KPILabel.TOTAL_TRADES.getTradePredicate()).forEach(trade -> System.out.println("Total trades " + trade));
        trades.stream().filter(KPILabel.TRADEDATE_TODAY.getTradePredicate()).forEach(trade -> System.out.println("Todays trade date " + trade));
        trades.stream().filter(KPILabel.VALUEDATE_TODAY.getTradePredicate()).forEach(trade -> System.out.println("Todays value date " + trade));
        trades.stream().filter(KPILabel.FIXINGDATE_TODAY.getTradePredicate()).forEach(trade -> System.out.println("Todays fixing date " + trade));
    }

}
