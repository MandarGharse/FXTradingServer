
package com.fx.domain.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "tradeKey",
    "tradeVersion",
    "ccyPair",
    "buySell",
    "dealtCcy",
    "dealtAmount",
    "counterAmount"
})
public class FxTrade__1 {

    @JsonProperty("tradeKey")
    private String tradeKey;
    @JsonProperty("tradeVersion")
    private String tradeVersion;
    @JsonProperty("ccyPair")
    private String ccyPair;
    @JsonProperty("buySell")
    private String buySell;
    @JsonProperty("dealtCcy")
    private String dealtCcy;
    @JsonProperty("dealtAmount")
    private String dealtAmount;
    @JsonProperty("counterAmount")
    private String counterAmount;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FxTrade__1() {
    }

    /**
     * 
     * @param counterAmount
     * @param buySell
     * @param ccyPair
     * @param dealtCcy
     * @param tradeKey
     * @param tradeVersion
     * @param dealtAmount
     */
    public FxTrade__1(String tradeKey, String tradeVersion, String ccyPair, String buySell, String dealtCcy, String dealtAmount, String counterAmount) {
        super();
        this.tradeKey = tradeKey;
        this.tradeVersion = tradeVersion;
        this.ccyPair = ccyPair;
        this.buySell = buySell;
        this.dealtCcy = dealtCcy;
        this.dealtAmount = dealtAmount;
        this.counterAmount = counterAmount;
    }

    @JsonProperty("tradeKey")
    public String getTradeKey() {
        return tradeKey;
    }

    @JsonProperty("tradeKey")
    public void setTradeKey(String tradeKey) {
        this.tradeKey = tradeKey;
    }

    @JsonProperty("tradeVersion")
    public String getTradeVersion() {
        return tradeVersion;
    }

    @JsonProperty("tradeVersion")
    public void setTradeVersion(String tradeVersion) {
        this.tradeVersion = tradeVersion;
    }

    @JsonProperty("ccyPair")
    public String getCcyPair() {
        return ccyPair;
    }

    @JsonProperty("ccyPair")
    public void setCcyPair(String ccyPair) {
        this.ccyPair = ccyPair;
    }

    @JsonProperty("buySell")
    public String getBuySell() {
        return buySell;
    }

    @JsonProperty("buySell")
    public void setBuySell(String buySell) {
        this.buySell = buySell;
    }

    @JsonProperty("dealtCcy")
    public String getDealtCcy() {
        return dealtCcy;
    }

    @JsonProperty("dealtCcy")
    public void setDealtCcy(String dealtCcy) {
        this.dealtCcy = dealtCcy;
    }

    @JsonProperty("dealtAmount")
    public String getDealtAmount() {
        return dealtAmount;
    }

    @JsonProperty("dealtAmount")
    public void setDealtAmount(String dealtAmount) {
        this.dealtAmount = dealtAmount;
    }

    @JsonProperty("counterAmount")
    public String getCounterAmount() {
        return counterAmount;
    }

    @JsonProperty("counterAmount")
    public void setCounterAmount(String counterAmount) {
        this.counterAmount = counterAmount;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(FxTrade__1 .class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("tradeKey");
        sb.append('=');
        sb.append(((this.tradeKey == null)?"<null>":this.tradeKey));
        sb.append(',');
        sb.append("tradeVersion");
        sb.append('=');
        sb.append(((this.tradeVersion == null)?"<null>":this.tradeVersion));
        sb.append(',');
        sb.append("ccyPair");
        sb.append('=');
        sb.append(((this.ccyPair == null)?"<null>":this.ccyPair));
        sb.append(',');
        sb.append("buySell");
        sb.append('=');
        sb.append(((this.buySell == null)?"<null>":this.buySell));
        sb.append(',');
        sb.append("dealtCcy");
        sb.append('=');
        sb.append(((this.dealtCcy == null)?"<null>":this.dealtCcy));
        sb.append(',');
        sb.append("dealtAmount");
        sb.append('=');
        sb.append(((this.dealtAmount == null)?"<null>":this.dealtAmount));
        sb.append(',');
        sb.append("counterAmount");
        sb.append('=');
        sb.append(((this.counterAmount == null)?"<null>":this.counterAmount));
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
        result = ((result* 31)+((this.counterAmount == null)? 0 :this.counterAmount.hashCode()));
        result = ((result* 31)+((this.buySell == null)? 0 :this.buySell.hashCode()));
        result = ((result* 31)+((this.ccyPair == null)? 0 :this.ccyPair.hashCode()));
        result = ((result* 31)+((this.dealtCcy == null)? 0 :this.dealtCcy.hashCode()));
        result = ((result* 31)+((this.tradeKey == null)? 0 :this.tradeKey.hashCode()));
        result = ((result* 31)+((this.tradeVersion == null)? 0 :this.tradeVersion.hashCode()));
        result = ((result* 31)+((this.dealtAmount == null)? 0 :this.dealtAmount.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FxTrade__1) == false) {
            return false;
        }
        FxTrade__1 rhs = ((FxTrade__1) other);
        return ((((((((this.counterAmount == rhs.counterAmount)||((this.counterAmount!= null)&&this.counterAmount.equals(rhs.counterAmount)))&&((this.buySell == rhs.buySell)||((this.buySell!= null)&&this.buySell.equals(rhs.buySell))))&&((this.ccyPair == rhs.ccyPair)||((this.ccyPair!= null)&&this.ccyPair.equals(rhs.ccyPair))))&&((this.dealtCcy == rhs.dealtCcy)||((this.dealtCcy!= null)&&this.dealtCcy.equals(rhs.dealtCcy))))&&((this.tradeKey == rhs.tradeKey)||((this.tradeKey!= null)&&this.tradeKey.equals(rhs.tradeKey))))&&((this.tradeVersion == rhs.tradeVersion)||((this.tradeVersion!= null)&&this.tradeVersion.equals(rhs.tradeVersion))))&&((this.dealtAmount == rhs.dealtAmount)||((this.dealtAmount!= null)&&this.dealtAmount.equals(rhs.dealtAmount))));
    }

}
