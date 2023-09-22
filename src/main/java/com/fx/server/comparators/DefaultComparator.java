package com.fx.server.comparators;

import com.fx.proto.messaging.TradeMessages;

import java.util.Comparator;

public class DefaultComparator implements Comparator {

    @Override
    public int compare(Object o1, Object o2) {
        return Long.compare(((TradeMessages.Trade)o2).getLastUpdateTime(),
                ((TradeMessages.Trade)o2).getLastUpdateTime());
    }

}
