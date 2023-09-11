package com.fx.server.cache;

import com.fx.proto.messaging.TradeMessages;
import com.google.common.collect.ImmutableMap;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class TradesCache {
    Map<TradeMessages.TradeKeyVersion, TradeMessages.Trade> tradesCache = new ConcurrentHashMap<>();
    static final TradesCache instance = new TradesCache();

    private TradesCache()   {
    }

    public static TradesCache getInstance()   {
        return instance;
    }

    public synchronized Map<TradeMessages.TradeKeyVersion, TradeMessages.Trade> getTradesCache() {
        return ImmutableMap.copyOf(tradesCache);
    }

    public void putItem(TradeMessages.Trade trade) {
        if (Objects.isNull(trade.getTradeKeyVersion()))    {
            System.out.println("Failed to write to TradesCache. Invalid key " + trade.getTradeKeyVersion() + " for trade " + trade);
            return;
        }
        tradesCache.put(trade.getTradeKeyVersion(), trade);
        System.out.println("cache size after adding item " + tradesCache.size());
    }

    public TradeMessages.Trade getItem(TradeMessages.TradeKeyVersion tradeKeyVersion)   {
        return tradesCache.get(tradeKeyVersion);
    }

}
