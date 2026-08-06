# shiro-weixin-extension

[English](./README.md) | [简体中文](./README.zh-CN.md)

## 目录

- [1. Project Overview](#1-project-overview)
- [2. Features & Status](#2-features--status)
- [3. Requirements & Compatibility](#3-requirements--compatibility)
- [4. Architecture & Modules](#4-architecture--modules)
- [5. Installation](#5-installation)
- [6. Quick Start](#6-quick-start)
- [7. Configuration](#7-configuration)
- [8. Core Usage / API](#8-core-usage--api)
- [9. Testing & Build](#9-testing--build)
- [10. Versioning & Branches](#10-versioning--branches)
- [11. Contributing & License](#11-contributing--license)

## 1. Project Overview

`shiro-weixin-extension` 是 Apache Shiro 的微信认证扩展，将微信公众号（MP）OAuth 2.0 登录与微信小程序（Ma）登录接入 Shiro 的 Realm 与过滤器链。应用只需携带微信 `code` / `jscode` 发起登录请求，即可完成认证并将用户作为普通 Shiro Subject 处理。

该模块基于 easy4j 的 `shiro-biz` 模块（`AbstractTrustableAuthenticatingFilter`、`AbstractAuthorizingRealm`），并与 easy4j 的 `shiro-jwt-extension` 模块配合用于签发 JWT。微信协议本身委托给 WxJava（`weixin-java-mp`、`weixin-java-miniapp`）。

它不是：

- 不是 Spring Boot 自动配置 Starter —— Realm 与过滤器 Bean 需由应用自行装配；
- 不是微信 SDK —— 所有微信 API 调用均通过 WxJava 的 `WxMpService`、`WxMaService` 完成。

典型场景：

| 场景 | 本模块提供的组件 |
|:---|:---|
| 公众号网站 OAuth 2.0 `code` 登录 | `WxMpAuthenticatingFilter` + `WxMpAuthorizingRealm` |
| 小程序 `jscode` 登录 | `WxMaAuthenticatingFilter` + `WxMaAuthorizingRealm` |
| 登录成功签发 JWT | `WxAuthenticationSuccessHandler`（通过 `JwtPayloadRepository` 写入 JWT） |
| MP / Ma 统一主体类型 | `ShiroWeiXinPrincipal` |

## 2. Features & Status

项目状态：`1.0.x.*` 预发布开发线（快照版本）；在首个正式 Release 标签之前，公开 API 仍在稳定过程中。

| 能力 | 状态 | 说明 |
|:---|:---|:---|
| 公众号登录过滤器 | 稳定 | 提取 `code` / `token` / `state` 请求参数并构建 `WxMpAuthenticationToken` |
| 公众号授权 Realm | 稳定 | 通过 `WxMpService` 用 OAuth 2.0 `code` 换取 access token 与用户信息，再从配置的 Repository 加载 `AuthenticationInfo` |
| 小程序登录过滤器 | 稳定 | 处理 `jscode`、`sessionKey`、`signature`、`rawData`、`encryptedData`、`iv`、`unionid`、`openid`、`token` 参数 |
| 小程序授权 Realm | 稳定 | 基于 `WxMaService` 完成认证 |
| 登录成功处理器 | 稳定 | `WxAuthenticationSuccessHandler` 输出登录结果并支持过期校验（`checkExpiry`） |
| 登录请求模型 | 稳定 | `WxMpLoginRequest` / `WxMaLoginRequest` 承载 code、openid、unionid、userInfo 等微信字段 |
| 主体类型 | 稳定 | `ShiroWeiXinPrincipal` 继承自 `ShiroPrincipal` |
| 微信异常体系 | 稳定 | `WxAuthenticationException` 与 `WxJsCode*` 异常族（`WxJsCodeNotFoundException`、`WxJsCodeExpiredException`、`WxJsCodeInvalidException`、`WxJsCodeIncorrectException`、`URIUnpermittedException`） |

## 3. Requirements & Compatibility

| 要求 | 版本 |
|:---|:---|
| JDK | 8+ |
| Maven | 3.6+ |
| Apache Shiro | 1.13.0 |
| Spring Framework | 5.3.x |
| WxJava | 4.3.4.B（`weixin-java-mp`、`weixin-java-miniapp`） |
| easy4j 兄弟模块 | `shiro-biz`、`shiro-jwt-extension`（同一 `1.0.x.*` 版本线） |

版本线：

| 分支 | JDK | 版本模式 | 说明 |
|:---|:---|:---|:---|
| `feature/1.0.x` | 8 | `1.0.x.*` | 当前开发线；Spring 5.x / Servlet API 时代 |
| `feature/2.0.x` | 17 | `2.0.x.*` | 下一条版本线 |
| `feature/3.0.x` | 21 | `3.0.x.*` | 未来版本线 |

## 4. Architecture & Modules

```
WeChat MP / MiniApp client
        |
        v
+---------------------------------+
| Shiro filter chain              |
| WxMp / WxMaAuthenticatingFilter |
+---------------------------------+
        |
        v
 WxMpAuthenticationToken / WxMaAuthenticationToken
        |
        v
 WxMp / WxMaAuthorizingRealm
 (code/jscode -> accessToken -> userInfo)
        |
        v
 ShiroWeiXinPrincipal -> Subject (JWT via shiro-jwt-extension)
```

本工程为单 jar 模块，包结构位于 `org.apache.shiro.spring.boot.weixin`：

| 包 | 职责 |
|:---|:---|
| `authc` | `WxMpAuthenticatingFilter`、`WxMaAuthenticatingFilter`、`WxAuthenticationSuccessHandler`、登录请求模型 |
| `realm` | `WxMpAuthorizingRealm`、`WxMaAuthorizingRealm` |
| `token` | `WxMpAuthenticationToken`、`WxMaAuthenticationToken` |
| `exception` | `WxAuthenticationException` 与 `WxJsCode*` 异常族 |
| 根包 | `ShiroWeiXinPrincipal` |

## 5. Installation

制品发布到 easy4j 私有仓库与 GitHub Releases，暂未发布 Maven Central。

Maven：

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>shiro-weixin-extension</artifactId>
    <version>1.0.x.20260630-SNAPSHOT</version>
</dependency>
```

Gradle：

```groovy
implementation 'io.github.easy4j:shiro-weixin-extension:1.0.x.20260630-SNAPSHOT'
```

## 6. Quick Start

将公众号 Realm 与登录过滤器装配进 Shiro `SecurityManager` 配置（`WxMpService` 实例来自 WxJava）：

```java
import org.apache.shiro.spring.boot.weixin.authc.WxMpAuthenticatingFilter;
import org.apache.shiro.spring.boot.weixin.realm.WxMpAuthorizingRealm;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;

// 1. 构建 WxJava 公众号服务（config storage 由应用配置）
WxMpService wxMpService = new WxMpServiceImpl();
// wxMpService.setWxMpConfigStorage(...);

// 2. 公众号 Realm：用 OAuth2 code 换取微信用户信息
WxMpAuthorizingRealm wxMpRealm = new WxMpAuthorizingRealm(wxMpService);

// 3. 公众号登录过滤器
WxMpAuthenticatingFilter wxMpFilter = new WxMpAuthenticatingFilter();
wxMpFilter.setLoginUrl("/wxmp/login");
wxMpFilter.setCodeParameter("code");   // 默认 "code"
wxMpFilter.setTokenParameter("token"); // 默认 "token"

// 4. 将 Realm 与过滤器注册到 Shiro SecurityManager
// securityManager.setRealms(Arrays.asList(wxMpRealm, ...));
// shiroFilterFactoryBean.getFilters().put("wxmp", wxMpFilter);
// shiroFilterFactoryBean.setFilterChainDefinitionMap(...);
```

预期结果：携带 `?code=xxx` 的 `POST /wxmp/login` 请求被 `WxMpAuthenticatingFilter` 拦截；Realm 从微信换取 access token 与用户信息（`openid`、`unionid`），从配置的 Repository 加载 `AuthenticationInfo`，请求以已认证的 Shiro Subject 继续执行。

## 7. Configuration

过滤器通过 JavaBean 属性暴露请求参数名：

| 过滤器 | 属性 | 默认值 | 说明 |
|:---|:---|:---|:---|
| `WxMpAuthenticatingFilter` | `codeParameter` | `code` | OAuth 2.0 授权码参数 |
| `WxMpAuthenticatingFilter` | `stateParameter` | `state` | OAuth 2.0 state 参数 |
| `WxMpAuthenticatingFilter` | `tokenParameter` | `token` | 已有 token 参数 |
| `WxMaAuthenticatingFilter` | `jscodeParameter` | `jscode` | 小程序登录凭证 |
| `WxMaAuthenticatingFilter` | `sessionKeyParameter` | `sessionKey` | 微信下发的会话密钥 |
| `WxMaAuthenticatingFilter` | `signatureParameter` | `signature` | `rawData` 的签名 |
| `WxMaAuthenticatingFilter` | `rawDataParameter` | `rawData` | 客户端原始用户数据 |
| `WxMaAuthenticatingFilter` | `encryptedDataParameter` | `encryptedData` | 加密的用户数据 |
| `WxMaAuthenticatingFilter` | `ivParameter` | `iv` | 初始化向量 |
| `WxMaAuthenticatingFilter` | `unionidParameter` | `unionid` | 用户 Union ID 参数 |
| `WxMaAuthenticatingFilter` | `openidParameter` | `openid` | 用户 Open ID 参数 |
| `WxMaAuthenticatingFilter` | `tokenParameter` | `token` | 已有 token 参数 |

`WxAuthenticationSuccessHandler` 支持 `checkExpiry`（boolean）与可设置的 `JwtPayloadRepository`，用于 JWT 载荷的持久化。

## 8. Core Usage / API

小程序登录流程（`WxMaAuthenticatingFilter` 与 `WxMaLoginRequest`）：

```java
import org.apache.shiro.spring.boot.weixin.authc.WxMaAuthenticatingFilter;
import org.apache.shiro.spring.boot.weixin.authc.WxMaLoginRequest;
import org.apache.shiro.spring.boot.weixin.token.WxMaAuthenticationToken;

// 过滤器从请求中读取 jscode 并包装成 token
WxMaLoginRequest loginRequest = new WxMaLoginRequest();
loginRequest.setJscode("wx-jscode-from-client");
loginRequest.setUnionid("o-unionid");

WxMaAuthenticationToken token = new WxMaAuthenticationToken(loginRequest, "127.0.0.1");
// subject.login(token) -> WxMaAuthorizingRealm 完成用户解析
```

异常按类型区分，便于应用处理不同登录失败：

```java
import org.apache.shiro.spring.boot.weixin.exception.WxJsCodeNotFoundException;
import org.apache.shiro.spring.boot.weixin.exception.WxJsCodeExpiredException;
```

## 9. Testing & Build

构建与测试：

```bash
./mvnw clean verify
```

- 构建配置了 JaCoCo Maven 插件：覆盖率报告生成于 `target/site/jacoco/index.html`，并配置了 BUNDLE 行覆盖率 90% 的校验规则（`haltOnFailure=false`，即只报告不阻断构建）；
- `central` Maven Profile（`./mvnw -Pcentral deploy`）附加 GPG 签名、源码包与 Javadoc 包用于发布；
- 当前仓库本模块暂无单元测试，覆盖率以 JaCoCo 报告为准。

## 10. Versioning & Branches

维护三条并行版本线：

| 分支 | JDK | 版本模式 |
|:---|:---|:---|
| `feature/1.0.x` | 8 | `1.0.x.*` |
| `feature/2.0.x` | 17 | `2.0.x.*` |
| `feature/3.0.x` | 21 | `3.0.x.*` |

维护策略：`1.0.x` 为当前活跃开发线（当前快照 `1.0.x.20260630-SNAPSHOT`）；`2.0.x` 与 `3.0.x` 为面向更新 JDK 的前向移植线。快照按需构建，正式 Release 通过 GitHub Releases 分发。

## 11. Contributing & License

- Fork 仓库并提交 Pull Request；`1.0.x` 版本线保持 JDK 8 兼容；
- Bug 反馈与功能建议通过 GitHub Issues 跟踪；
- 基于 [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0) 开源。
