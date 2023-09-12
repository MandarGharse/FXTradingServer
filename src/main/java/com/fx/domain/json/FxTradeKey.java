
package com.fx.domain.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "tradeKey",
    "tradeVersion"
})
public class FxTradeKey {

    @JsonProperty("tradeKey")
    private String tradeKey;
    @JsonProperty("tradeVersion")
    private String tradeVersion;

    /**
     * No args constructor for use in serialization
     * 
     */
    public FxTradeKey() {
    }

    /**
     * 
     * @param tradeKey
     * @param tradeVersion
     */
    public FxTradeKey(String tradeKey, String tradeVersion) {
        super();
        this.tradeKey = tradeKey;
        this.tradeVersion = tradeVersion;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(FxTradeKey.class.getName()).append('@').append(Integer.toHexString(System.identityHashCode(this))).append('[');
        sb.append("tradeKey");
        sb.append('=');
        sb.append(((this.tradeKey == null)?"<null>":this.tradeKey));
        sb.append(',');
        sb.append("tradeVersion");
        sb.append('=');
        sb.append(((this.tradeVersion == null)?"<null>":this.tradeVersion));
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
        result = ((result* 31)+((this.tradeKey == null)? 0 :this.tradeKey.hashCode()));
        result = ((result* 31)+((this.tradeVersion == null)? 0 :this.tradeVersion.hashCode()));
        return result;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }
        if ((other instanceof FxTradeKey) == false) {
            return false;
        }
        FxTradeKey rhs = ((FxTradeKey) other);
        return (((this.tradeKey == rhs.tradeKey)||((this.tradeKey!= null)&&this.tradeKey.equals(rhs.tradeKey)))&&((this.tradeVersion == rhs.tradeVersion)||((this.tradeVersion!= null)&&this.tradeVersion.equals(rhs.tradeVersion))));
    }

}
