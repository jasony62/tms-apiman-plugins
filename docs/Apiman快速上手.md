在浏览器中打开

> http://localhost:9501/apimanui

使用`admin/admin123!`登录。

# 添加策略

进入`Administration/Manage Plugins`，选择`Available Plugins`，执行`Simple Header Policy Plugin`行的`install`。

成功后在`Installed Plugins`页出现`Simple Header Policy Plugin`。

# 新建组织

打开`Organizations/New Organization`

| 字段              | 值    |
| ----------------- | ----- |
| Organization Name | org01 |

执行`Create Organization`

# 新建 API

打开`APIs/New API`

| 字段            | 值    |
| --------------- | ----- |
| API Name        | api01 |
| Initial Version | 1.0   |

执行`Create API`

进入`Implementation`

| 字段         | 值                            |
| ------------ | ----------------------------- |
| API Endpoint | 需要被调用的外部 API 的 URL。 |

**注意**：`API Endpoint`不支持指定 URL 查询参数。

执行`Save`

进入`Plans`

勾选`Make this API public`

执行`Save`

进入`Policies`

执行`Add Policy`（示例在消息头中添加认证 token）

`Policy Type`选择`Simple Header Policy`

执行`+ Header`

| 字段               | 值            |
| ------------------ | ------------- |
| Header Name        | Authorization |
| Header Value       | token 的值    |
| Value Type         | String        |
| Apply To           | Request       |
| Overwrite Existing | true          |

执行`Add Policy`

执行`Publish`

进入`Endpint`

复制`Managed Endpoint`

执行

> http://host.docker.internal:9502/org01/api01/1.0
