`apiman`是一个开源的 Java 开发的 API 管理系统，支持通过插件（policy）的方式扩展功能。

本仓库包含了`apiman`插件。

| 插件           | 功能说明                                            |
| -------------- | --------------------------------------------------- |
| reshape-policy | 修改 api 调用参数和返回结果，包括查询参数和消息体。 |

# 运行 APIMAN

进入 apiman-docker 目录

复制`env.template`文件，新建`.env`文件

执行

> docker-compose -p tms-apiman -f docker-compose.setup.yml up

执行

> docker-compose -p tms-apiman -f docker-compose.yml up -d postgres elasticsearch keycloak

执行

> docker-compose -p tms-apiman -f docker-compose.yml up -d apiman-manager apiman-gateway

注：文件来源于`apiman-docker-compose-3.1.3.Final`。修改为各个服务通过端口直接进入（原来是通过域名区分）。`postgres`和`elasticsearch`服务的数据在本地磁盘持久化。

注：目前仅支持 elasticsearch7.x，8 版本运行报错。

# 安装 apiman 自带扩展插件

进入`Administration/Manage Plugins`，选择`Available Plugins`，执行`Add Custom Plugin`。

| 字段        | 值                                  |
| ----------- | ----------------------------------- |
| Group Id    | io.apiman.plugins                   |
| Artifact Id | apiman-plugins-simple-header-policy |
| Version     | 3.1.3.Final                         |

执行`Add Plugin`。成功后在`Installed Plugins`页出现`HTTP Header Policy Plugin`。

参考：[手工安装插件](apiman-policies/README.md)

# 安装自定义插件

进入`reshape-police`

执行

> mvn clean package

打包生成的`war`文件会复制到`dist-polices`中。`apiman-manager`和`apiman-gateway`服务要挂在目录`dist-polices`。

在浏览器中打开

> http://localhost:9501/apimanui

使用`admin/admin123!`登录。进入`Administration/Manage Plugins`，选择`Available Plugins`，执行`Add Custom Plugin`

| 字段        | 值                 |
| ----------- | ------------------ |
| Group Id    | tms.apiman.plugins |
| Artifact Id | reshape-policy     |
| Version     | 1.0-SNAPSHOT       |

执行`Add Plugin`。成功后在`Installed Plugins`页出现`Reshape Policy Plugin`。

# 参考

https://www.apiman.io
