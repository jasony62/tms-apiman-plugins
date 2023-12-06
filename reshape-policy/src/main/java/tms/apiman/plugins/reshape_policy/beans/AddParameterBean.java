/*
 * Copyright 2015 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package tms.apiman.plugins.reshape_policy.beans;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.annotation.JsonValue;
import io.apiman.gateway.engine.beans.ApiRequest;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Header
 * 
 * @author Marc Savy {@literal <msavy@redhat.com>}
 */
@Generated("org.jsonschema2pojo")
@JsonPropertyOrder({ "parameterName", "parameterValue", "valueType", "overwrite" })
@SuppressWarnings("nls")
public class AddParameterBean {

  /**
   * Header Name
   */
  @JsonProperty("parameterName")
  private String parameterName;

  /**
   * Header Value
   */
  @JsonProperty("parameterValue")
  private String parameterValue;

  /**
   * Value Type
   */
  @JsonProperty("valueType")
  private AddParameterBean.ValueType valueType;

  /**
   * Overwrite Existing
   */
  @JsonProperty("overwrite")
  private Boolean overwrite = false;
  @JsonIgnore
  private Map<String, Object> additionalProperties = new HashMap<>();

  /**
   * Header Name
   * <p>
   * 
   * 
   * @return The parameterName
   */
  @JsonProperty("parameterName")
  public String getParameterName() {
    return parameterName;
  }

  /**
   * Header Name
   * <p>
   * 
   * 
   * @param parameterName The parameterName
   */
  @JsonProperty("parameterName")
  public void setParameterName(String parameterName) {
    this.parameterName = parameterName;
  }

  /**
   * Header Value
   * <p>
   * 
   * 
   * @return The parameterValue
   */
  @JsonProperty("parameterValue")
  public String getParameterValue() {
    return parameterValue;
  }

  /**
   * Header Value
   * <p>
   * 
   * 
   * @param parameterValue The parameterValue
   */
  @JsonProperty("parameterValue")
  public void setParameterValue(String parameterValue) {
    this.parameterValue = parameterValue;
  }

  /**
   * Value Type
   * <p>
   * 
   * 
   * @return The valueType
   */
  @JsonProperty("valueType")
  public AddParameterBean.ValueType getValueType() {
    return valueType;
  }

  /**
   * Value Type
   * <p>
   * 
   * 
   * @param valueType The valueType
   */
  @JsonProperty("valueType")
  public void setValueType(AddParameterBean.ValueType valueType) {
    this.valueType = valueType;
  }

  /**
   * Overwrite Existing
   * <p>
   * 
   * 
   * @return The overwrite
   */
  @JsonProperty("overwrite")
  public Boolean getOverwrite() {
    return overwrite;
  }

  /**
   * Overwrite Existing
   * <p>
   * 
   * 
   * @param overwrite The overwrite
   */
  @JsonProperty("overwrite")
  public void setOverwrite(Boolean overwrite) {
    this.overwrite = overwrite;
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

  @Override
  public int hashCode() {
    return new HashCodeBuilder().append(parameterName).append(parameterValue).append(overwrite)
        .append(additionalProperties).toHashCode();
  }

  @Override
  public boolean equals(Object other) {
    if (other == this) {
      return true;
    }
    if ((other instanceof AddParameterBean) == false) {
      return false;
    }
    AddParameterBean rhs = ((AddParameterBean) other);
    return new EqualsBuilder().append(parameterName, rhs.parameterName).append(parameterValue, rhs.parameterValue)
        .append(overwrite, rhs.overwrite)
        .append(additionalProperties, rhs.additionalProperties).isEquals();
  }

  public static enum ValueType {

    STRING("String"), ENV("Env"), SYS("System Properties"), HEADER("Header");

    private final String value;
    private static Map<String, AddParameterBean.ValueType> constants = new HashMap<>();

    static {
      for (AddParameterBean.ValueType c : values()) {
        constants.put(c.value, c);
      }
    }

    private ValueType(String value) {
      this.value = value;
    }

    @JsonValue
    @Override
    public String toString() {
      return this.value;
    }

    @JsonCreator
    public static AddParameterBean.ValueType fromValue(String value) {
      AddParameterBean.ValueType constant = constants.get(value);
      if (constant == null) {
        throw new IllegalArgumentException(value);
      } else {
        return constant;
      }
    }

  }

  public String getResolvedHeaderValue(ApiRequest request) {
    String rVal = null;

    switch (getValueType()) {
      case ENV:
        rVal = System.getenv(parameterValue);
        break;
      case SYS:
        rVal = System.getProperty(parameterValue);
        break;
      case STRING:
        rVal = parameterValue;
        break;
      case HEADER:
        if (request == null) {
          throw new IllegalArgumentException("Invalid access when reading header value.");
        }
        rVal = request.getHeaders().get(parameterValue);
        break;
    }

    return (rVal == null) ? "" : rVal;
  }
}
