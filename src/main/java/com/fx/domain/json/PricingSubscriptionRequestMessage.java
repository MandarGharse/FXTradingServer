
package com.fx.domain.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sessionId",
    "id",
    "ccyPair",
    "valueDate"
})
public class PricingSubscriptionRequestMessage {

    @JsonProperty("sessionId")
    private String sessionId;
    @JsonProperty("id")
    private String id;
    @JsonProperty("ccyPair")
    private String ccyPair;
    @JsonProperty("valueDate")
    private String valueDate;

    /**
     * No args constructor for use in serialization
     * 
     */
    public PricingSubscriptionRequestMessage() {
    }

    /**
     * 
     * @param ccyPair
     * @param sessionId
     * @param id
     * @param valueDate
     */
    public PricingSubscriptionRequestMessage(String sessionId, String id, String ccyPair, String valueDate) {
        super();
        this.sessionId = sessionId;
        this.id = id;
        this.ccyPair = ccyPair;
        this.valueDate = valueDate;
    }

    @JsonProperty("sessionId")
    public String getSessionId() {
        return sessionId;
    }

    @JsonProperty("sessionId")
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("ccyPair")
    public String getCcyPair() {
        return ccyPair;
    }

    @JsonProperty("ccyPair")
    public void setCcyPair(String ccyPair) {
        this.ccyPair = ccyPair;
    }

    @JsonProperty("valueDate")
    public String getValueDate() {
        return valueDate;
    }

    @JsonProperty("valueDate")
    public void setValueDate(String valueDate) {
        this.valueDate = valueDate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PricingSubscriptionRequestMessage.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("sessionId");
        sb.append('=');
        sb.append(((this.sessionId == null)?"<null>":this.sessionId));
        sb.append(',');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("ccyPair");
        sb.append('=');
        sb.append(((this.ccyPair == null)?"<null>":this.ccyPair));
        sb.append(',');
        sb.append("valueDate");
        sb.append('=');
        sb.append(((this.valueDate == null)?"<null>":this.valueDate));
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
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        result = ((result* 31)+((this.valueDate == null)? 0 :this.valueDate.hashCode()));
        result = ((result* 31)+((this.ccyPair == null)? 0 :this.ccyPair.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof PricingSubscriptionRequestMessage) == false) {
            return false;
        }
        PricingSubscriptionRequestMessage rhs = ((PricingSubscriptionRequestMessage) other);
        return (((((this.sessionId == rhs.sessionId)||((this.sessionId!= null)&&this.sessionId.equals(rhs.sessionId)))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))))&&((this.valueDate == rhs.valueDate)||((this.valueDate!= null)&&this.valueDate.equals(rhs.valueDate))))&&((this.ccyPair == rhs.ccyPair)||((this.ccyPair!= null)&&this.ccyPair.equals(rhs.ccyPair))));
    }

}
