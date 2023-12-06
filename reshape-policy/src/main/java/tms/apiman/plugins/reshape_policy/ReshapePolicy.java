package tms.apiman.plugins.reshape_policy;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.jknack.handlebars.Context;
import com.github.jknack.handlebars.Handlebars;
import com.github.jknack.handlebars.JsonNodeValueResolver;
import com.github.jknack.handlebars.Template;

import io.apiman.common.logging.ApimanLoggerFactory;
import io.apiman.common.logging.IApimanLogger;
import io.apiman.gateway.engine.beans.ApiRequest;
import io.apiman.gateway.engine.beans.ApiResponse;
import io.apiman.gateway.engine.beans.util.QueryMap;
import io.apiman.gateway.engine.components.IBufferFactoryComponent;
import io.apiman.gateway.engine.io.AbstractStream;
import io.apiman.gateway.engine.io.IApimanBuffer;
import io.apiman.gateway.engine.io.IReadWriteStream;
import io.apiman.gateway.engine.policy.IDataPolicy;
import io.apiman.gateway.engine.policy.IPolicyChain;
import io.apiman.gateway.engine.policy.IPolicyContext;
import io.apiman.gateway.engine.policies.AbstractMappedPolicy;
import tms.apiman.plugins.reshape_policy.beans.AddParameterBean;
import tms.apiman.plugins.reshape_policy.beans.ReshapePolicyDefBean;

/**
 * 重装API请求和响应
 * 
 * @author jasony62
 */
public class ReshapePolicy extends AbstractMappedPolicy<ReshapePolicyDefBean> implements IDataPolicy {

  private static final IApimanLogger LOGGER = ApimanLoggerFactory.getLogger(ReshapePolicy.class);

  private static final String CONTENT_LENGTH = "Content-Length";

  private static final String CONTENT_TYPE = "Content-Type";

  @Override
  public Class<ReshapePolicyDefBean> getConfigurationClass() {
    return ReshapePolicyDefBean.class;
  }

  /**
   * 
   */
  protected static JsonNode node(final Object object) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    JsonNode node = mapper.readTree(mapper.writeValueAsString(object));
    return node;
  }

  /**
   * 
   * @param object
   * @return
   * @throws IOException
   */
  protected static Context context(final Object object) throws IOException {
    return Context.newBuilder(node(object))
        .resolver(JsonNodeValueResolver.INSTANCE).build();
  }

  @Override
  protected void doApply(ApiRequest request, IPolicyContext context, ReshapePolicyDefBean config,
      IPolicyChain<ApiRequest> chain) {
    LOGGER.info("进入处理请求的头数据 destination={0}", request.getDestination());
    handleRequestQueryParams(request, config);

    chain.doApply(request);
  }

  /**
   * 处理请求的消息体
   */
  @Override
  public IReadWriteStream<ApiRequest> getRequestDataHandler(final ApiRequest request,
      IPolicyContext context, Object configuration) {
    ReshapePolicyDefBean policyConfiguration = (ReshapePolicyDefBean) configuration;

    LOGGER.info("进入处理请求，配置参数 {0}", policyConfiguration.toString());

    // 指定了不处理请求的消息体
    if (policyConfiguration.isProcessRequestBody() == false)
      return null;

    final String contentType = request.getHeaders().get(CONTENT_TYPE);
    // 目前仅支持改写json数据
    if (contentType.indexOf("application/json") != 0)
      return null;

    final IBufferFactoryComponent bufferFactory = context.getComponent(IBufferFactoryComponent.class);
    final int contentLength = request.getHeaders().containsKey(CONTENT_LENGTH)
        ? Integer.parseInt(request.getHeaders().get(CONTENT_LENGTH))
        : 0;

    return new AbstractStream<ApiRequest>() {

      private IApimanBuffer readBuffer = bufferFactory.createBuffer(contentLength);

      @Override
      public void write(IApimanBuffer chunk) {
        readBuffer.append(chunk.getBytes());
      }

      @Override
      public void end() {
        final String bodyTemplate = policyConfiguration.getRequestBodyTemplate();
        if (bodyTemplate != null && bodyTemplate.length() > 0) {
          /**
           * 指定要转换的模板
           */
          String body = new String(readBuffer.getBytes());
          LOGGER.debug("收到的请求的消息体 {0}", body);
          try {
            /**
             * 解析请求消息体中的json数据
             */
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonBody = objectMapper.readTree(body);
            ObjectNode jsonBodyWrap = objectMapper.createObjectNode();
            jsonBodyWrap.set("body", jsonBody);
            /**
             * 通过模板生成新的请求消息体
             */
            Handlebars handlebars = new Handlebars();
            Template template = handlebars.compileInline(bodyTemplate);
            String newBody = template.apply(context(jsonBodyWrap));
            LOGGER.debug("生成的请求的消息体 {0}", newBody);

            IApimanBuffer writeBuffer = bufferFactory.createBuffer(newBody.length());
            writeBuffer.append(newBody);
            super.write(writeBuffer);
          } catch (JsonMappingException e) {
            e.printStackTrace();
          } catch (JsonProcessingException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
        } else {
          /**
           * 没有指定模板时，被认为要清除消息体
           */
          LOGGER.debug("没有指定请求消息体转换模板");
        }
        super.end();
      }

      @Override
      public ApiRequest getHead() {
        return request;
      }

      @Override
      protected void handleHead(ApiRequest head) {
      }
    };
  }

  @Override
  protected void doApply(ApiResponse response, IPolicyContext context,
      ReshapePolicyDefBean config, IPolicyChain<ApiResponse> chain) {
    LOGGER.info("进入处理响应的头数据");
    super.doApply(response, context, config, chain);
  }

  @Override
  public IReadWriteStream<ApiResponse> getResponseDataHandler(ApiResponse response, IPolicyContext context,
      Object configuration) {
    ReshapePolicyDefBean policyConfiguration = (ReshapePolicyDefBean) configuration;

    LOGGER.info("进入处理响应，配置参数 {0}", policyConfiguration.toString());

    if (policyConfiguration.isProcessResponseBody() == false) {
      LOGGER.debug("没有设置处理响应的消息体");
      return null;
    }

    final String contentType = response.getHeaders().get(CONTENT_TYPE);
    // 目前仅支持改写json数据
    if (contentType.indexOf("application/json") != 0) {
      LOGGER.debug("响应的消息体内容类型是 {0} ，只支持处理application/json", contentType);
      return null;
    }

    final IBufferFactoryComponent bufferFactory = context.getComponent(IBufferFactoryComponent.class);
    final int contentLength = response.getHeaders().containsKey(CONTENT_LENGTH)
        ? Integer.parseInt(response.getHeaders().get(CONTENT_LENGTH))
        : 0;
    LOGGER.debug("响应的消息体长度 {0}", contentLength);
    // 清除消息头，重新计算
    response.getHeaders().remove(CONTENT_LENGTH);

    return new AbstractStream<ApiResponse>() {
      private IApimanBuffer readBuffer = bufferFactory.createBuffer(contentLength);

      @Override
      public void write(IApimanBuffer chunk) {
        readBuffer.append(chunk.getBytes());
      }

      @Override
      public void end() {
        final String bodyTemplate = policyConfiguration.getResponseBodyTemplate();
        if (bodyTemplate != null && bodyTemplate.length() > 0) {
          LOGGER.debug("指定响应消息体模板 {0}", bodyTemplate);
          /**
           * 指定要转换的模板
           */
          String body = new String(readBuffer.getBytes());
          LOGGER.debug("收到响应的消息体 {0}", body);
          try {
            /**
             * 解析请求消息体中的json数据
             */
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonBody = objectMapper.readTree(body);
            ObjectNode jsonBodyWrap = objectMapper.createObjectNode();
            jsonBodyWrap.set("body", jsonBody);
            /**
             * 通过模板生成新的请求消息体
             */
            Handlebars handlebars = new Handlebars();
            Template template = handlebars.compileInline(bodyTemplate);
            String newBody = template.apply(context(jsonBodyWrap));
            LOGGER.debug("生成响应的消息体 {0}", newBody);
            IApimanBuffer writeBuffer = bufferFactory.createBuffer(newBody.length());
            writeBuffer.append(newBody);
            super.write(writeBuffer);
          } catch (JsonMappingException e) {
            e.printStackTrace();
          } catch (JsonProcessingException e) {
            e.printStackTrace();
          } catch (IOException e) {
            e.printStackTrace();
          }
        } else {
          /**
           * 没有指定模板时，被认为要清除消息体
           */
          LOGGER.debug("没有指定响应消息体转换模板");
        }
        super.end();
      }

      @Override
      public ApiResponse getHead() {
        return response;
      }

      @Override
      protected void handleHead(ApiResponse head) {
      }

    };

  }

  /**
   * 处理请求的查询参数
   * 
   * @param request
   * @param config
   */
  private void handleRequestQueryParams(ApiRequest request, ReshapePolicyDefBean config) {
    QueryMap queryParams = request.getQueryParams();
    /**
     * 添加查询参数
     */
    Set<AddParameterBean> addParams = config.getAddParameters();
    for (AddParameterBean addParam : addParams) {
      String name = addParam.getParameterName();
      if (addParam.getOverwrite() || !queryParams.containsKey(name)) {
        String value = addParam.getParameterValue();
        if (queryParams.containsKey(name)) {
          LOGGER.debug("覆盖请求的查询参数 {0}={1}", name, value);
          queryParams.put(name, value);
        } else {
          LOGGER.debug("添加请求的查询参数 {0}={1}", name, value);
          queryParams.add(name, value);
        }
      }
    }
    /**
     * 删除查询参数
     */
    for (Map.Entry<String, String> param : queryParams) {
      if (config.getKeyRegex().matcher(param.getKey()).matches()) {
        LOGGER.debug("删除请求的查询参数 {0}", param.getKey());
        queryParams.remove(param.getKey());
      }
      if (config.getValueRegex().matcher(param.getValue()).matches()) {
        LOGGER.debug("删除请求的查询参数 {0}", param.getKey());
        queryParams.remove(param.getKey());
      }
    }
  }

}
