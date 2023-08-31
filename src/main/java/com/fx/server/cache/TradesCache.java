package com.fx.server.cache;

import com.fx.proto.messaging.TradeMessages;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class TradesCache {
    Map<String, TradeMessages.Trade> tradesCache = new ConcurrentHashMap<>();
    static final TradesCache instance = new TradesCache();

    private TradesCache()   {
    }

    public static TradesCache getInstance()   {
        return instance;
    }

    public void putItem(TradeMessages.Trade trade) {
        if (Objects.isNull(trade.getTradeKeyVersion()))    {
            System.out.println("Failed to write to TradesCache. Invalid key " + trade.getTradeKeyVersion() + " for trade " + trade);
            return;
        }
        tradesCache.put(trade.getTradeKeyVersion().getTradeKey(), trade);
        System.out.println("cache size after adding item " + tradesCache.size());
    }

    public TradeMessages.Trade getItem(String tradeKey)   {
        return tradesCache.get(tradeKey);
    }

}
