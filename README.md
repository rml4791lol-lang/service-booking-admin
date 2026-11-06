# 洛小童管理后台（源码）

一套社区少儿体育培训管理后台的重建源码：Java（Spring Boot）后端 + Vue 3 / Element Plus 管理前端。

> **重要声明**
> - 本仓库**仅包含源代码**，不包含任何生产数据、用户数据、手机号、密钥、证书、数据库备份或服务器配置。
> - 配置文件中的口令、盐值、密钥均为占位符（通过环境变量注入），示例值不可用于生产。
> - 项目中的业务标识（如公司名）仅用于说明代码用途。

## 目录结构

```
reconstruction/          后端（Maven 多模块）
├── pom.xml              父 POM
├── like-admin/          管理后台接口服务（Spring Boot 2.x / MyBatis-Plus / Sa-Token）
└── like-common/         公共模块（实体、Mapper、工具、支付/存储插件）
admin-web/               管理前端（Vue 3 + TypeScript + Element Plus + Vite）
```

## 构建

后端（JDK 8+，Maven 3.9+）：

```bash
cd reconstruction
mvn -pl like-admin -am package -DskipTests
```

前端（Node 18+）：

```bash
cd admin-web
npm install
npm run build
```

运行时需要的数据库表结构、Redis、对象存储等不在本仓库内，请按实际环境自行准备；所有敏感配置通过环境变量注入（参考各 `application*.yml` 中的 `${...}` 占位）。

## 主要能力（代码层面）

- 管理员 / 角色 / 菜单权限（按钮级权限点）
- 学员与家长管理、学员课时账户
- 课程 / 班级 / 课节 / 核销
- 订单、退款审批、退款任务
- 次数券：券商品、按手机号发券、批量发券、撤回
- 活动与报名、社区与区域维护
- 教练与请假审批、财务流水与报表
- 定时任务、操作日志

## 许可证

**仅限个人学习、研究或非商业演示使用，禁止任何商业用途。** 详见 [LICENSE](./LICENSE)。

## 免责

本软件按"现状"提供，不附带任何担保。使用者自行承担使用风险。
