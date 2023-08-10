
package com.fx.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sessionId",
    "startIndex",
    "endIndex"
})
public class FXTradesRequest {

    @JsonProperty("sessionId")
    private String sessionId;
    @JsonProperty("startIndex")
    private long startIndex;
    @JsonProperty("endIndex")
    private long endIndex;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FXTradesRequest() {
    }

    /**
     * 
     * @param startIndex
     * @param endIndex
     * @param sessionId
     */
    public FXTradesRequest(String sessionId, long startIndex, long endIndex) {
        super();
        this.sessionId = sessionId;
        this.startIndex = startIndex;
        this.endIndex = endIndex;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(FXTradesRequest.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
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
        result = ((result* 31)+((this.sessionId == null)? 0 :this.sessionId.hashCode()));
        result = ((result* 31)+((int)(this.endIndex^(this.endIndex >>> 32))));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FXTradesRequest) == false) {
            return false;
        }
        FXTradesRequest rhs = ((FXTradesRequest) other);
        return (((this.startIndex == rhs.startIndex)&&((this.sessionId == rhs.sessionId)||((this.sessionId!= null)&&this.sessionId.equals(rhs.sessionId))))&&(this.endIndex == rhs.endIndex));
    }

}
