
package com.fx.domain.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "bidRate",
    "askRate"
})
public class FxQuote {

    @JsonProperty("bidRate")
    private long bidRate;
    @JsonProperty("askRate")
    private long askRate;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FxQuote() {
    }

    /**
     * 
     * @param bidRate
     * @param askRate
     */
    public FxQuote(long bidRate, long askRate) {
        super();
        this.bidRate = bidRate;
        this.askRate = askRate;
    }

    @JsonProperty("bidRate")
    public long getBidRate() {
        return bidRate;
    }

    @JsonProperty("bidRate")
    public void setBidRate(long bidRate) {
        this.bidRate = bidRate;
    }

    @JsonProperty("askRate")
    public long getAskRate() {
        return askRate;
    }

    @JsonProperty("askRate")
    public void setAskRate(long askRate) {
        this.askRate = askRate;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(FxQuote.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("bidRate");
        sb.append('=');
        sb.append(this.bidRate);
        sb.append(',');
        sb.append("askRate");
        sb.append('=');
        sb.append(this.askRate);
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
        result = ((result* 31)+((int)(this.askRate^(this.askRate >>> 32))));
        result = ((result* 31)+((int)(this.bidRate^(this.bidRate >>> 32))));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FxQuote) == false) {
            return false;
        }
        FxQuote rhs = ((FxQuote) other);
        return ((this.askRate == rhs.askRate)&&(this.bidRate == rhs.bidRate));
    }

}
