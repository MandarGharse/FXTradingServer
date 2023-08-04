
package com.fx.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "CurrencyPairs"
})
public class CurrencyPairs {

    @JsonProperty("CurrencyPairs")
    private List<CurrencyPair> currencyPairs = new ArrayList<CurrencyPair>();
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     * 
     */
    public CurrencyPairs() {
    }

    /**
     * 
     * @param currencyPairs
     */
    public CurrencyPairs(List<CurrencyPair> currencyPairs) {
        super();
        this.currencyPairs = currencyPairs;
    }

    @JsonProperty("CurrencyPairs")
    public List<CurrencyPair> getCurrencyPairs() {
        return currencyPairs;
    }

    @JsonProperty("CurrencyPairs")
    public void setCurrencyPairs(List<CurrencyPair> currencyPairs) {
        this.currencyPairs = currencyPairs;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(CurrencyPairs.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("currencyPairs");
        sb.append('=');
        sb.append(((this.currencyPairs == null)?"<null>":this.currencyPairs));
        sb.append(',');
        sb.append("additionalProperties");
        sb.append('=');
        sb.append(((this.additionalProperties == null)?"<null>":this.additionalProperties));
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
        result = ((result* 31)+((this.currencyPairs == null)? 0 :this.currencyPairs.hashCode()));
        result = ((result* 31)+((this.additionalProperties == null)? 0 :this.additionalProperties.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof CurrencyPairs) == false) {
            return false;
        }
        CurrencyPairs rhs = ((CurrencyPairs) other);
        return (((this.currencyPairs == rhs.currencyPairs)||((this.currencyPairs!= null)&&this.currencyPairs.equals(rhs.currencyPairs)))&&((this.additionalProperties == rhs.additionalProperties)||((this.additionalProperties!= null)&&this.additionalProperties.equals(rhs.additionalProperties))));
    }

}
