apiman 自带了部分需要安装的插件，这些插件默认从 maven 仓库中下载（apiman.properties 中设置）。如果 apiman 的运行环境不能连接公网，就需要先把插件的 war 文件复制到`apiman-manager`和`apiman-gateway`的`.m2`的目录中，直接从本地加载。

# 手工安装插件

下载指定的 apiman 插件，例如：apiman-plugins-simple-header-policy。

```shell
wget https://repo.maven.apache.org/maven2/io/apiman/plugins/apiman-plugins-simple-header-policy/3.1.3.Final/apiman-plugins-simple-header-policy-3.1.3.Final.war
```

`docker-compose.yml`文件中对应的内容，需要通过`docker-compose.override.yml`指定具体值。

```yaml
volumes:
  # apiman插件包
  # - ../apiman-policies:/opt/jboss/.m2/repository/io/apiman/plugins:ro
  # 自定义插件包
  # - ../dist-policies:/opt/jboss/.m2/repository/tms/apiman/plugins:ro
```

```yaml
volumes:
  # apiman插件包
  # - ../apiman-policies:/home/apiman/.m2/repository/io/apiman/plugins:ro
  # 自定义插件包
  # - ../dist-policies:/home/apiman/.m2/repository/tms/apiman/plugins:ro
```

# apiman 自带的可安装插件

```json
{
  "id": "apiman-plugin-registry",
  "name": "Official apiman Plugins",
  "description": "This plugin registry lists all of the official apiman plugins.  No third party plugins are included in this registry.",
  "version": "3.1.3.Final",
  "repository": {
    "name": "Maven Central",
    "url": "https://repo1.maven.org/maven2/"
  },
  "plugins": [
    {
      "groupId": "io.apiman.plugins",
      "artifactId": "apiman-plugins-apikey-policy",
      "version": "3.1.3.Final",
      "name": "API Key Policy Plugin",
      "description": "This plugin provides a policy that can help pass the API Key through to the back-end service."
    },
    {
      "groupId": "io.apiman.plugins",
      "artifactId": "apiman-plugins-cors-policy",
      "version": "3.1.3.Final",
      "name": "CORS Policy Plugin",
      "description": "This plugin implements CORS (Cross-origin resource sharing): A method of controlling access to resources outside of an originating domain."
    },
    {
      "groupId": "io.apiman.plugins",
      "artifactId": "apiman-plugins-header-allow-deny-policy",
      "version": "3.1.3.Final",
      "name": "Header Allow/Deny Policy Plugin",
      "description": "Provides a policy that permits or denies requests matching certain HTTP headers."
    },
    {
      "groupId": "io.apiman.plugins",
      "artifactId": "apiman-plugins-http-security-policy",
      "version": "3.1.3.Final",
      "name": "HTTP Security Policy Plugin",
      "description": "Provides a policy which allows security-related HTTP headers to be set, which can help mitigate a range of common security vulnerabilities."
    },
    {
      "groupId": "io.apiman.plugins",
      "artifactId": "apiman-plugins-jsonp-policy",
      "version": "3.1.3.Final",
      "name": "JSONP Policy Plugin",
      "description": "A plugin that contributes a policy that turns a standard RESTful endpoint into a JSONP compatible endpoint."
    },
    {
      "groupId": "io.apiman.plugins",
      "artifactId": "apiman-plugins-keycloak-oauth-policy",
      "version": "3.1.3.Final",
      "name": "Keycloak OAuth Policy Plugin",
      "description": "This plugin offers an OAuth2 policy which leverages the Keycloak authentication platform as the identity and access provider."
    },
    {
      "groupId": "io.apiman.plugins",
      "artifactId": "apiman-plugins-simple-header-policy",
      "version": "3.1.3.Final",
      "name": "Simple Header Policy Plugin",
      "description": "Offers a simple policy that allows request headers to be added or stripped from the HTTP request (outgoing) or HTTP response (incoming)."
    },
    {
      "groupId": "io.apiman.plugins",
      "artifactId": "apiman-plugins-transformation-policy",
      "version": "3.1.3.Final",
      "name": "XML<->JSON Transformation Policy Plugin",
      "description": "This plugin provides a very simple policy which can transform the request and/or response payload between XML and JSON."
    },
    {
      "groupId": "io.apiman.plugins",
      "artifactId": "apiman-plugins-log-policy",
      "version": "3.1.3.Final",
      "name": "Log Headers Policy",
      "description": "A policy that logs the headers to std out.  Useful to analyse inbound HTTP traffic to the gateway when added as the first policy in the chain or to analyse outbound HTTP traffic from the gateway when added as the last policy in the chain."
    },
    {
      "groupId": "io.apiman.plugins",
      "artifactId": "apiman-plugins-circuit-breaker-policy",
      "version": "3.1.3.Final",
      "name": "Circuit Breaker Policy",
      "description": "This plugin has policies for performing circuit breaker functionality."
    }
  ]
}
```

参看：https://www.apiman.io/apiman-docs/user-guide/latest/gateway/policies/authorization-policy.html
