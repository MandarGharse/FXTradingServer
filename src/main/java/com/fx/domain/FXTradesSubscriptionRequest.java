
package com.fx.domain;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "type"
})
public class FXTradesSubscriptionRequest {

    @JsonProperty("id")
    private String id;
    @JsonProperty("type")
    private String type;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FXTradesSubscriptionRequest() {
    }

    /**
     * 
     * @param id
     * @param type
     */
    public FXTradesSubscriptionRequest(String id, String type) {
        super();
        this.id = id;
        this.type = type;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(FXTradesSubscriptionRequest.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("id");
        sb.append('=');
        sb.append(((this.id == null)?"<null>":this.id));
        sb.append(',');
        sb.append("type");
        sb.append('=');
        sb.append(((this.type == null)?"<null>":this.type));
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
        result = ((result* 31)+((this.type == null)? 0 :this.type.hashCode()));
        result = ((result* 31)+((this.id == null)? 0 :this.id.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FXTradesSubscriptionRequest) == false) {
            return false;
        }
        FXTradesSubscriptionRequest rhs = ((FXTradesSubscriptionRequest) other);
        return (((this.type == rhs.type)||((this.type!= null)&&this.type.equals(rhs.type)))&&((this.id == rhs.id)||((this.id!= null)&&this.id.equals(rhs.id))));
    }

}
