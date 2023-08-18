
package com.fx.domain.json;

import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sessionId",
    "startIndex",
    "endIndex",
    "status",
    "rejectText",
    "trades"
})
public class FXTradesResponse {

    @JsonProperty("sessionId")
    private String sessionId;
    @JsonProperty("startIndex")
    private long startIndex;
    @JsonProperty("endIndex")
    private long endIndex;
    @JsonProperty("status")
    private String status;
    @JsonProperty("rejectText")
    private String rejectText;
    @JsonProperty("trades")
    private List<FxTrade> trades = new ArrayList<FxTrade>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public FXTradesResponse() {
    }

    /**
     * 
     * @param startIndex
     * @param endIndex
     * @param trades
     * @param sessionId
     * @param rejectText
     * @param status
     */
    public FXTradesResponse(String sessionId, long startIndex, long endIndex, String status, String rejectText, List<FxTrade> trades) {
        super();
        this.sessionId = sessionId;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
        this.status = status;
        this.rejectText = rejectText;
        this.trades = trades;
    }

    @JsonProperty("sessionId")
    public String getSessionId() {
        return sessionId;
    }

    @JsonProperty("sessionId")
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @JsonProperty("startIndex")
    public long getStartIndex() {
        return startIndex;
    }

    @JsonProperty("startIndex")
    public void setStartIndex(long startIndex) {
        this.startIndex = startIndex;
    }

    @JsonProperty("endIndex")
    public long getEndIndex() {
        return endIndex;
    }

    @JsonProperty("endIndex")
    public void setEndIndex(long endIndex) {
        this.endIndex = endIndex;
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

    @JsonProperty("trades")
    public List<FxTrade> getTrades() {
        return trades;
    }

    @JsonProperty("trades")
    public void setTrades(List<FxTrade> trades) {
        this.trades = trades;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(FXTradesResponse.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("sessionId");
        sb.append('=');
        sb.append(((this.sessionId == null)?"<null>":this.sessionId));
        sb.append(',');
        sb.append("startIndex");
        sb.append('=');
        sb.append(this.startIndex);
        sb.append(',');
        sb.append("endIndex");
        sb.append('=');
        sb.append(this.endIndex);
        sb.append(',');
        sb.append("status");
        sb.append('=');
        sb.append(((this.status == null)?"<null>":this.status));
        sb.append(',');
        sb.append("rejectText");
        sb.append('=');
        sb.append(((this.rejectText == null)?"<null>":this.rejectText));
        sb.append(',');
        sb.append("trades");
        sb.append('=');
        sb.append(((this.trades == null)?"<null>":this.trades));
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
        result = ((result* 31)+((int)(this.startIndex^(this.startIndex >>> 32))));
        result = ((result* 31)+((int)(this.endIndex^(this.endIndex >>> 32))));
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
        if ((other instanceof FXTradesResponse) == false) {
            return false;
        }
        FXTradesResponse rhs = ((FXTradesResponse) other);
        return ((((((this.startIndex == rhs.startIndex)&&(this.endIndex == rhs.endIndex))&&((this.trades == rhs.trades)||((this.trades!= null)&&this.trades.equals(rhs.trades))))&&((this.sessionId == rhs.sessionId)||((this.sessionId!= null)&&this.sessionId.equals(rhs.sessionId))))&&((this.rejectText == rhs.rejectText)||((this.rejectText!= null)&&this.rejectText.equals(rhs.rejectText))))&&((this.status == rhs.status)||((this.status!= null)&&this.status.equals(rhs.status))));
    }

}
