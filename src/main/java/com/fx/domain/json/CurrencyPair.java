
package com.fx.domain.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "ccypair",
    "ccycode"
})
public class CurrencyPair {

    @JsonProperty("ccypair")
    private String ccypair;
    @JsonProperty("ccycode")
    private String ccycode;

    /**
     * No args constructor for use in serialization
     * 
     */
    public CurrencyPair() {
    }

    /**
     * 
     * @param ccypair
     * @param ccycode
     */
    public CurrencyPair(String ccypair, String ccycode) {
        super();
        this.ccypair = ccypair;
        this.ccycode = ccycode;
    }

    @JsonProperty("ccypair")
    public String getCcypair() {
        return ccypair;
    }

    @JsonProperty("ccypair")
    public void setCcypair(String ccypair) {
        this.ccypair = ccypair;
    }

    @JsonProperty("ccycode")
    public String getCcycode() {
        return ccycode;
    }

    @JsonProperty("ccycode")
    public void setCcycode(String ccycode) {
        this.ccycode = ccycode;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(CurrencyPair.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("ccypair");
        sb.append('=');
        sb.append(((this.ccypair == null)?"<null>":this.ccypair));
        sb.append(',');
        sb.append("ccycode");
        sb.append('=');
        sb.append(((this.ccycode == null)?"<null>":this.ccycode));
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
        result = ((result* 31)+((this.ccypair == null)? 0 :this.ccypair.hashCode()));
        result = ((result* 31)+((this.ccycode == null)? 0 :this.ccycode.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CurrencyPair) == false) {
            return false;
        }
        CurrencyPair rhs = ((CurrencyPair) other);
        return (((this.ccypair == rhs.ccypair)||((this.ccypair!= null)&&this.ccypair.equals(rhs.ccypair)))&&((this.ccycode == rhs.ccycode)||((this.ccycode!= null)&&this.ccycode.equals(rhs.ccycode))));
    }

}
