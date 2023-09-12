
package com.fx.domain.json;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sessionId",
    "tradeKeys"
})
public class TradeResolutionRequestMessage {

    @JsonProperty("sessionId")
    private String sessionId;
    @JsonProperty("tradeKeys")
    private List<FxTradeKey> tradeKeys = new ArrayList<FxTradeKey>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public TradeResolutionRequestMessage() {
    }

    /**
     * 
     * @param tradeKeys
     * @param sessionId
     */
    public TradeResolutionRequestMessage(String sessionId, List<FxTradeKey> tradeKeys) {
        super();
        this.sessionId = sessionId;
        this.tradeKeys = tradeKeys;
    }

    @JsonProperty("sessionId")
    public String getSessionId() {
        return sessionId;
    }

    @JsonProperty("sessionId")
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @JsonProperty("tradeKeys")
    public List<FxTradeKey> getTradeKeys() {
        return tradeKeys;
    }

    @JsonProperty("tradeKeys")
    public void setTradeKeys(List<FxTradeKey> tradeKeys) {
        this.tradeKeys = tradeKeys;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TradeResolutionRequestMessage.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("sessionId");
        sb.append('=');
        sb.append(((this.sessionId == null)?"<null>":this.sessionId));
        sb.append(',');
        sb.append("tradeKeys");
        sb.append('=');
        sb.append(((this.tradeKeys == null)?"<null>":this.tradeKeys));
        sb.append(',');
        if (sb.charAt((sb.length()- 1)) == ',') {
            sb.setCharAt((sb.length()- 1), ']');
        } else {
            sb.append(']');
        }
        return sb.toString();
    }

    @Override
    public int hashCode() {
        int result = 1;
        result = ((result* 31)+((this.tradeKeys == null)? 0 :this.tradeKeys.hashCode()));
        result = ((result* 31)+((this.sessionId == null)? 0 :this.sessionId.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TradeResolutionRequestMessage) == false) {
            return false;
        }
        TradeResolutionRequestMessage rhs = ((TradeResolutionRequestMessage) other);
        return (((this.tradeKeys == rhs.tradeKeys)||((this.tradeKeys!= null)&&this.tradeKeys.equals(rhs.tradeKeys)))&&((this.sessionId == rhs.sessionId)||((this.sessionId!= null)&&this.sessionId.equals(rhs.sessionId))));
    }

}
