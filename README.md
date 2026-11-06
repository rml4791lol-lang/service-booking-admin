# 服务预约管理后台（源码）

一套面向**预约制服务**的运营管理后台源码：Java（Spring Boot）后端 + Vue 3 / Element Plus 管理前端。

适用于课程培训、会员服务、场馆预约、上门服务、活动报名等「先预约、后履约、按次核销」的业务场景。

> **重要声明**
> - 本仓库**仅包含源代码**，不包含任何生产数据、用户数据、手机号、密钥、证书、数据库备份或服务器配置。
> - 配置文件中的口令、盐值、密钥均为占位符（通过环境变量注入），示例值不可用于生产。
> - 代码中的品牌字样已全部替换为通用名称。

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
- 客户与受益对象（学员/会员）管理、计次账户
- 服务项目 / 课程 / 班期 / 场次排期
- 预约、取消、签到核销
- 订单、退款审批、退款任务与对账
- 计次卡券：卡券模板、按手机号发放、批量导入、撤回
- 活动与报名、区域/网点维护
- 服务人员（教练/讲师）与请假审批
- 财务流水、报表、导出
- 定时任务、操作日志、内容安全预留

## 许可证

**PolyForm Noncommercial License 1.0.0** —— 仅限非商业用途（个人学习、研究、教育、公益等），**禁止任何商业使用**。详见 [LICENSE](./LICENSE)。

如需商业授权，请联系版权所有者。

## 免责

本软件按"现状"提供，不附带任何担保。使用者自行承担使用风险。
