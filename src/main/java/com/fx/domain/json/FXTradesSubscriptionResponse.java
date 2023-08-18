
package com.fx.domain.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sessionId",
    "type",
    "status",
    "rejectText"
})
public class FXTradesSubscriptionResponse {

    @JsonProperty("sessionId")
    private String sessionId;
    @JsonProperty("type")
    private String type;
    @JsonProperty("status")
    private String status;
    @JsonProperty("rejectText")
    private String rejectText;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FXTradesSubscriptionResponse() {
    }

    /**
     * 
     * @param sessionId
     * @param type
     * @param rejectText
     * @param status
     */
    public FXTradesSubscriptionResponse(String sessionId, String type, String status, String rejectText) {
        super();
        this.sessionId = sessionId;
        this.type = type;
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

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
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
        sb.append(FXTradesSubscriptionResponse.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("sessionId");
        sb.append('=');
        sb.append(((this.sessionId == null)?"<null>":this.sessionId));
        sb.append(',');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
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
        result = ((result* 31)+((this.sessionId == null)? 0 :this.sessionId.hashCode()));
        result = ((result* 31)+((this.type == null)? 0 :this.type.hashCode()));
        result = ((result* 31)+((this.rejectText == null)? 0 :this.rejectText.hashCode()));
        result = ((result* 31)+((this.status == null)? 0 :this.status.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FXTradesSubscriptionResponse) == false) {
            return false;
        }
        FXTradesSubscriptionResponse rhs = ((FXTradesSubscriptionResponse) other);
        return (((((this.sessionId == rhs.sessionId)||((this.sessionId!= null)&&this.sessionId.equals(rhs.sessionId)))&&((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type))))&&((this.rejectText == rhs.rejectText)||((this.rejectText!= null)&&this.rejectText.equals(rhs.rejectText))))&&((this.status == rhs.status)||((this.status!= null)&&this.status.equals(rhs.status))));
    }

}
