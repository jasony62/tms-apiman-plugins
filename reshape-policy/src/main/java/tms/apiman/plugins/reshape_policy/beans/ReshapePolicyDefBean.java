package tms.apiman.plugins.reshape_policy.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.annotation.Generated;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "addParameters", "stripParameters", "processRequestBody", "requestBodyTemplate",
    "processResponseBody", "responseBodyTemplate" })
public class ReshapePolicyDefBean {
  /**
   * Add Headers
   */
  @JsonProperty("addParameters")
  @JsonDeserialize(as = java.util.LinkedHashSet.class)
  private Set<AddParameterBean> addParameters = new LinkedHashSet<>();
  /**
   * Strip Headers
   * <p>
   * Removes header key and value pairs when pattern matches.
   */
  @JsonProperty("stripParameters")
  @JsonDeserialize(as = java.util.LinkedHashSet.class)
  private Set<StripParameterBean> stripParameters = new LinkedHashSet<>();
  /**
   * 处理请求的消息体
   */
  @JsonProperty("processRequestBody")
  private boolean processRequestBody;
  /**
   * 请求消息体模板
   */
  @JsonProperty("requestBodyTemplate")
  private String requestBodyTemplate;
  /**
   * 处理响应的消息体
   */
  @JsonProperty("processResponseBody")
  private boolean processResponseBody;
  /**
   * 
   */
  @JsonProperty("responseBodyTemplate")
  private String responseBodyTemplate;

  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  // REQUEST
  private Pattern stripRequestKeyRegex;
  private Pattern stripRequestValueRegex;

  /**
   * Add Headers
   * 
   * @return The addParameters
   */
  @JsonProperty("addParameters")
  public Set<AddParameterBean> getAddParameters() {
    return addParameters;
  }

  /**
   * Add Headers
   * 
   * @param addParameters The addParameters
   */
  @JsonProperty("addParameters")
  public void setAddParameters(Set<AddParameterBean> addParameters) {
    this.addParameters = addParameters;
  }

  /**
   * Strip Headers
   * <p>
   * Removes header key and value pairs when pattern matches.
   * 
   * @return The stripParameters
   */
  @JsonProperty("stripParameters")
  public Set<StripParameterBean> getStripHeaders() {
    return stripParameters;
  }

  /**
   * Strip Headers
   * <p>
   * Removes header key and value pairs when pattern matches.
   * 
   * @param stripParameters The stripParameters
   */
  @JsonProperty("stripParameters")
  public void setStripHeaders(Set<StripParameterBean> stripParameters) {
    this.stripParameters = stripParameters;
  }

  @JsonProperty("processRequestBody")
  public boolean isProcessRequestBody() {
    return processRequestBody;
  }

  @JsonProperty("processRequestBody")
  public void setProcessRequestBody(boolean processRequestBody) {
    this.processRequestBody = processRequestBody;
  }

  @JsonProperty("processResponseBody")
  public boolean isProcessResponseBody() {
    return processResponseBody;
  }

  @JsonProperty("processResponseBody")
  public void setProcessResponseBody(boolean processResponseBody) {
    this.processResponseBody = processResponseBody;
  }

  @JsonProperty("requestBodyTemplate")
  public String getRequestBodyTemplate() {
    return requestBodyTemplate;
  }

  @JsonProperty("requestBodyTemplate")
  public void setRequestBodyTemplate(String requestBodyTemplate) {
    this.requestBodyTemplate = requestBodyTemplate;
  }

  @JsonProperty("responseBodyTemplate")
  public String getResponseBodyTemplate() {
    return responseBodyTemplate;
  }

  @JsonProperty("responseBodyTemplate")
  public void setResponseBodyTemplate(String responseBodyTemplate) {
    this.responseBodyTemplate = responseBodyTemplate;
  }

  @Override
  public String toString() {
    return ToStringBuilder.reflectionToString(this);
  }

  @JsonAnyGetter
  public Map<String, Object> getAdditionalProperties() {
    return this.additionalProperties;
  }

  @JsonAnySetter
  public void setAdditionalProperty(String name, Object value) {
    this.additionalProperties.put(name, value);
  }

  @SuppressWarnings("nls")
  private Pattern buildRegex(List<StripParameterBean> itemList) {
    StringBuilder sb = new StringBuilder();
    String divider = "";

    for (StripParameterBean stripHeader : itemList) {
      String pattern = StringUtils.strip(stripHeader.getPattern());
      sb.append(divider);
      sb.append(pattern);
      divider = "|";
    }

    return Pattern.compile(sb.toString(), Pattern.CASE_INSENSITIVE);
  }

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(addParameters).append(stripParameters).append(additionalProperties)
        .toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof ReshapePolicyDefBean) == false) {
      return false;
    }
    ReshapePolicyDefBean rhs = ((ReshapePolicyDefBean) other);
    return new EqualsBuilder().append(addParameters, rhs.addParameters).append(stripParameters, rhs.stripParameters)
        .append(additionalProperties, rhs.additionalProperties).isEquals();
  }

  /**
   * @return the keyRegex
   */
  private Pattern getRequestKeyRegex() {
    if (stripRequestKeyRegex == null) {
      List<StripParameterBean> keys = new ArrayList<>();

      for (StripParameterBean bean : stripParameters) {
        if (bean.getStripType() == StripParameterBean.StripType.KEY) {
          keys.add(bean);
        }
      }

      stripRequestKeyRegex = buildRegex(keys);
    }
    return stripRequestKeyRegex;
  }

  /**
   * @return the keyRegex
   */
  private Pattern getRequestValueRegex() {
    if (stripRequestValueRegex == null) {
      List<StripParameterBean> values = new ArrayList<>();

      for (StripParameterBean bean : stripParameters) {
        if (bean.getStripType() == StripParameterBean.StripType.VALUE) {
          values.add(bean);
        }
      }

      stripRequestValueRegex = buildRegex(values);
    }
    return stripRequestValueRegex;
  }

  public Pattern getKeyRegex() {
    return getRequestKeyRegex();
  }

  public Pattern getValueRegex() {
    return getRequestValueRegex();
  }
}
