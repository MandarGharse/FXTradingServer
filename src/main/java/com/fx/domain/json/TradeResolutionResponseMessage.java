
package com.fx.domain.json;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sessionId",
    "trades",
    "status",
    "rejectText"
})
public class TradeResolutionResponseMessage {

    @JsonProperty("sessionId")
    private String sessionId;
    @JsonProperty("trades")
    private List<FxTrade__1> trades = new ArrayList<FxTrade__1>();
    @JsonProperty("status")
    private String status;
    @JsonProperty("rejectText")
    private String rejectText;

    /**
     * No args constructor for use in serialization
     * 
     */
    public TradeResolutionResponseMessage() {
    }

    /**
     * 
     * @param trades
     * @param sessionId
     * @param rejectText
     * @param status
     */
    public TradeResolutionResponseMessage(String sessionId, List<FxTrade__1> trades, String status, String rejectText) {
        super();
        this.sessionId = sessionId;
        this.trades = trades;
        this.status = status;
        this.rejectText = rejectText;
    }

    @JsonProperty("sessionId")
    public String getSessionId() {
        return sessionId;
    }

    @JsonProperty("sessionId")
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @JsonProperty("trades")
    public List<FxTrade__1> getTrades() {
        return trades;
    }

    @JsonProperty("trades")
    public void setTrades(List<FxTrade__1> trades) {
        this.trades = trades;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("rejectText")
    public String getRejectText() {
        return rejectText;
    }

    @JsonProperty("rejectText")
    public void setRejectText(String rejectText) {
        this.rejectText = rejectText;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(TradeResolutionResponseMessage.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("sessionId");
        sb.append('=');
        sb.append(((this.sessionId == null)?"<null>":this.sessionId));
        sb.append(',');
        sb.append("trades");
        sb.append('=');
        sb.append(((this.trades == null)?"<null>":this.trades));
        sb.append(',');
        sb.append("status");
        sb.append('=');
        sb.append(((this.status == null)?"<null>":this.status));
        sb.append(',');
        sb.append("rejectText");
        sb.append('=');
        sb.append(((this.rejectText == null)?"<null>":this.rejectText));
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
        result = ((result* 31)+((this.trades == null)? 0 :this.trades.hashCode()));
        result = ((result* 31)+((this.sessionId == null)? 0 :this.sessionId.hashCode()));
        result = ((result* 31)+((this.rejectText == null)? 0 :this.rejectText.hashCode()));
        result = ((result* 31)+((this.status == null)? 0 :this.status.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof TradeResolutionResponseMessage) == false) {
            return false;
        }
        TradeResolutionResponseMessage rhs = ((TradeResolutionResponseMessage) other);
        return (((((this.trades == rhs.trades)||((this.trades!= null)&&this.trades.equals(rhs.trades)))&&((this.sessionId == rhs.sessionId)||((this.sessionId!= null)&&this.sessionId.equals(rhs.sessionId))))&&((this.rejectText == rhs.rejectText)||((this.rejectText!= null)&&this.rejectText.equals(rhs.rejectText))))&&((this.status == rhs.status)||((this.status!= null)&&this.status.equals(rhs.status))));
    }

}
