package com.fx.providers;

import com.fx.domain.json.CurrencyPair;
import com.fx.domain.json.CurrencyPairs;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static com.fx.websocket.endpoints.SubscriptionEndPoints.CURRENCYPAIR_ENDPOINT;

@RestController
public class CurrencyPairListProvider   {

    private static final String LOGIN_ID = "loginid";
    private static List<CurrencyPair> currencyPairList = new ArrayList<CurrencyPair>();

    CurrencyPairListProvider()  {
        currencyPairList.add(new CurrencyPair());
        currencyPairList.add(new CurrencyPair());
        currencyPairList.add(new CurrencyPair());
        currencyPairList.add(new CurrencyPair());
        currencyPairList.add(new CurrencyPair());
    }

    @RequestMapping(value = "/gatewaywebsocket"+CURRENCYPAIR_ENDPOINT, method = RequestMethod.GET)
    public CurrencyPairs handleRequest(@RequestHeader(LOGIN_ID) String userId)  {

        CurrencyPairs currencyPairs = new CurrencyPairs(currencyPairList);
        return currencyPairs;
    }

}
