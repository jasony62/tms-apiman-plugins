`apiman`是一个 api 管理平台，虽然提供了多个扩展功能的插件，但是这些插件都是为了控制 api 的执行，并不会改变 api 本身，也就是说，`apiman`实现的主要 api 的代理功能。但是，进行系统对接时往往需要进行 api 的适配，例如：修改查询参数，修改请求或响应的消息体等。为了满足 api 的适配需求，实现了插件`reshape-policy`.

# 改写查询参数

`apiman`不支持在`API Endpoint`中设置查询参数，默认只会添加请求中指定的查询参数。

`reshape-policy`支持添加、修改和删除查询参数。

# 改写请求或响应的消息体

`reshape-policy`支持改写请求或响应的消息体。

实现的原理是在策略中配置目标消息体的模板，然后用请求或响应携带的数据填充模板生成数据。

模板使用`handlerbars`编写。目前，仅支持改写`json`数据；仅支持请求或响应的消息体中获取数据。

消息体模板

```json
{ "prop1": "{{body.name}}" }
```

收到的消息体

```json
{ "name": "apiman" }
```

生成的消息体

```json
{ "prop1": "apiman" }
```

**注意**：当设置消息体模板时，请求或响应中的`Content-Length`头会被去掉（只能去掉，无法修改），`apiman`会添加`transfer-encoding: chunked`头。
