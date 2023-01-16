# 基于租户的诊所信息管理系统

## 框架与组件

1. spring cloud 微服务框架
2. mysql 数据库
3. nacos 服务注册中心、配置中心
4. redis 缓存数据库
5. elasticsearch 搜索中间件
6. xxl-job 分布式定时任务中间件
7. ...

## 模块

- common 公共组件部分，包括数据缓存、通用业务封装、excel导入导出、常用工具类等
- common-api 定义了一些通用的api
- gateway 微服务网关
- his 业务部分
    - his-core 包含所有诊所信息业务的实现代码模块（可理解为基于his数据库的业务）
    - his-main 提供具体web服务模块，包括前端页面、web控制层、配置等
    - his-schedule 基于xxl-job的任务调度模块
- support 支持服务，包括文件存储、微信公众号、短信发送等服务，其中文件服务暂时只支持单机本地存储，如需要分布式部署support服务，则需要改用分布式文件存储方式
- search 基于elasticsearch的搜索服务模块
- tenant 租户管理（可以把用户认证一块从his提到这里）
- message IM模块

## 数据对象说明

- VO 只用于返回前端展示的数据
- LO 只用于记录日志操作的数据
- DTO 用于方法间传递与调用的其他类型对象
