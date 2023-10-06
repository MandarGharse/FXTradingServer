package com.fx.server.cache;

import com.fx.proto.messaging.PricingMessages;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class PricingCache {
    Map<String, PricingMessages.Price> pricingCache = new ConcurrentHashMap<>();     // Key=ccyPair+valueDate
    static final PricingCache instance = new PricingCache();

    private PricingCache()   {
    }

    public static PricingCache getInstance()   {
        return instance;
    }

    public void putItem(String ccyPair, String valueDate, PricingMessages.Price price) {
        if (Objects.isNull(ccyPair) || Objects.isNull(valueDate))    {
            System.out.println("Failed to write to PricingCache. Invalid key ccyPair:" + ccyPair + " valueDate:" + valueDate);
            return;
        }
        pricingCache.put(ccyPair+valueDate, price);
        System.out.println("pricing cache size after adding item " + pricingCache.size());
    }

    public PricingMessages.Price getItem(String ccyPair, String valueDate)   {
        return pricingCache.get(ccyPair+valueDate);
    }

}
