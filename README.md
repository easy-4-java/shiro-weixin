# shiro-weixin-extension

[English](./README.md) | [简体中文](./README.zh-CN.md)

[![Java](https://img.shields.io/badge/Java-8-orange)](https://github.com/easy-4-java/shiro-weixin-extension) [![License](https://img.shields.io/badge/license-Apache%202.0-green)](https://www.apache.org/licenses/LICENSE-2.0.txt)

shiro-weixin-extension is an Apache Shiro extension that bridges WeChat authentication into a Shiro security chain

## Table of Contents

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

`shiro-weixin-extension` is an Apache Shiro extension that bridges WeChat authentication into a Shiro security chain. It plugs WeChat Official Account (MP) OAuth 2.0 login and WeChat Mini Program (Ma) login into Shiro realms and filters, so that an application can authenticate users with a WeChat `code` / `jscode` and then treat them as normal Shiro subjects.

It builds on the easy4j `shiro-biz` module (`AbstractTrustableAuthenticatingFilter`, `AbstractAuthorizingRealm`) and pairs with the easy4j `shiro-jwt-spring` module for JWT-based session issuance. The WeChat protocol itself is delegated to WxJava (`weixin-java-mp`, `weixin-java-miniapp`).

What it is **not**:

- Not a Spring Boot auto-configuration starter — the realm/filter beans must be wired by the application.
- Not a WeChat SDK — all WeChat API calls go through WxJava services (`WxMpService`, `WxMaService`).

Typical scenarios:

| Scenario | What this module contributes |
|:---|:---|
| MP website login with OAuth 2.0 `code` | `WxMpAuthenticatingFilter` + `WxMpAuthorizingRealm` |
| Mini Program login with `jscode` | `WxMaAuthenticatingFilter` + `WxMaAuthorizingRealm` |
| JWT-issued login result | `WxAuthenticationSuccessHandler` (writes a JWT through `JwtPayloadRepository`) |
| Unified principal across MP/Ma | `ShiroWeiXinPrincipal` |

## 2. Features & Status

Project status: pre-release development line (`1.0.x.*` snapshots); public API is still stabilizing until the first tagged release.

| Capability | Status | Notes |
|:---|:---|:---|
| MP login filter | Stable | Extracts `code` / `token` / `state` request parameters and builds a `WxMpAuthenticationToken` |
| MP authorizing realm | Stable | Exchanges the OAuth 2.0 `code` for an access token and user info via `WxMpService`, then loads the `AuthenticationInfo` from the configured repository |
| Mini Program login filter | Stable | Handles `jscode`, `sessionKey`, `signature`, `rawData`, `encryptedData`, `iv`, `unionid`, `openid`, `token` parameters |
| Mini Program authorizing realm | Stable | Authenticates against `WxMaService` |
| Login success handler | Stable | `WxAuthenticationSuccessHandler` issues the login result and supports expiry checking (`checkExpiry`) |
| Login request models | Stable | `WxMpLoginRequest` / `WxMaLoginRequest` carry WeChat fields (code, openid, unionid, userInfo, ...) |
| Principal type | Stable | `ShiroWeiXinPrincipal` extends `ShiroPrincipal` |
| WeChat error exceptions | Stable | `WxAuthenticationException` plus a `WxJsCode*` hierarchy (`WxJsCodeNotFoundException`, `WxJsCodeExpiredException`, `WxJsCodeInvalidException`, `WxJsCodeIncorrectException`, `URIUnpermittedException`) |

## 3. Requirements & Compatibility

| Requirement | Version |
|:---|:---|
| JDK | 8+ |
| Maven | 3.6+ |
| Apache Shiro | 1.13.0 |
| Spring Framework | 5.3.x |
| WxJava | 4.3.4.B (`weixin-java-mp`, `weixin-java-miniapp`) |
| easy4j sibling modules | `shiro-biz`, `shiro-jwt-spring` (same `1.0.x.*` line) |

Version lines:

| Branch | JDK | Version pattern | Notes |
|:---|:---|:---|:---|
| `feature/1.0.x` | 8 | `1.0.x.*` | Current line; Spring 5.x / Servlet API era |
| `feature/2.0.x` | 17 | `2.0.x.*` | Next line |
| `feature/3.0.x` | 21 | `3.0.x.*` | Future line |

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
 ShiroWeiXinPrincipal -> Subject (JWT via shiro-jwt-spring)
```

The project is a single jar module. Package layout under `org.apache.shiro.spring.boot.weixin`:

| Package | Responsibility |
|:---|:---|
| `authc` | `WxMpAuthenticatingFilter`, `WxMaAuthenticatingFilter`, `WxAuthenticationSuccessHandler`, login request models |
| `realm` | `WxMpAuthorizingRealm`, `WxMaAuthorizingRealm` |
| `token` | `WxMpAuthenticationToken`, `WxMaAuthenticationToken` |
| `exception` | `WxAuthenticationException` and the `WxJsCode*` hierarchy |
| root | `ShiroWeiXinPrincipal` |

## 5. Installation

Artifacts are published to the easy4j private repository and GitHub Releases; the project is not yet on Maven Central.

Maven:

```xml
<dependency>
    <groupId>io.github.easy4j</groupId>
    <artifactId>shiro-weixin-extension</artifactId>
    <version>1.0.x.20260630-SNAPSHOT</version>
</dependency>
```

Gradle:

```groovy
implementation 'io.github.easy4j:shiro-weixin-extension:1.0.x.20260630-SNAPSHOT'
```

## 6. Quick Start

Wire the MP realm and login filter into a Shiro `SecurityManager` configuration (the `WxMpService` instance comes from WxJava):

```java
import org.apache.shiro.spring.boot.weixin.authc.WxMpAuthenticatingFilter;
import org.apache.shiro.spring.boot.weixin.realm.WxMpAuthorizingRealm;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.impl.WxMpServiceImpl;

// 1. Build the WxJava MP service (config storage set by the application)
WxMpService wxMpService = new WxMpServiceImpl();
// wxMpService.setWxMpConfigStorage(...);

// 2. WeChat MP realm: exchanges the OAuth2 code for WeChat user info
WxMpAuthorizingRealm wxMpRealm = new WxMpAuthorizingRealm(wxMpService);

// 3. WeChat MP login filter
WxMpAuthenticatingFilter wxMpFilter = new WxMpAuthenticatingFilter();
wxMpFilter.setLoginUrl("/wxmp/login");
wxMpFilter.setCodeParameter("code");   // default is "code"
wxMpFilter.setTokenParameter("token"); // default is "token"

// 4. Register the realm and filter with the Shiro SecurityManager
// securityManager.setRealms(Arrays.asList(wxMpRealm, ...));
// shiroFilterFactoryBean.getFilters().put("wxmp", wxMpFilter);
// shiroFilterFactoryBean.setFilterChainDefinitionMap(...);
```

Expected result: a `POST /wxmp/login` request carrying `?code=xxx` is intercepted by `WxMpAuthenticatingFilter`; the realm resolves the access token and user info (`openid`, `unionid`) from WeChat, loads the `AuthenticationInfo` from the configured repository, and the request proceeds as an authenticated Shiro subject.

## 7. Configuration

The filters expose their request parameter names as JavaBean properties:

| Filter | Property | Default | Description |
|:---|:---|:---|:---|
| `WxMpAuthenticatingFilter` | `codeParameter` | `code` | OAuth 2.0 authorization code parameter |
| `WxMpAuthenticatingFilter` | `stateParameter` | `state` | OAuth 2.0 state parameter |
| `WxMpAuthenticatingFilter` | `tokenParameter` | `token` | Existing token parameter |
| `WxMaAuthenticatingFilter` | `jscodeParameter` | `jscode` | Mini Program login code |
| `WxMaAuthenticatingFilter` | `sessionKeyParameter` | `sessionKey` | Session key from WeChat |
| `WxMaAuthenticatingFilter` | `signatureParameter` | `signature` | Signature of `rawData` |
| `WxMaAuthenticatingFilter` | `rawDataParameter` | `rawData` | Raw user data from the client |
| `WxMaAuthenticatingFilter` | `encryptedDataParameter` | `encryptedData` | Encrypted user data |
| `WxMaAuthenticatingFilter` | `ivParameter` | `iv` | Initialization vector |
| `WxMaAuthenticatingFilter` | `unionidParameter` | `unionid` | Union ID parameter |
| `WxMaAuthenticatingFilter` | `openidParameter` | `openid` | Open ID parameter |
| `WxMaAuthenticatingFilter` | `tokenParameter` | `token` | Existing token parameter |

`WxAuthenticationSuccessHandler` supports `checkExpiry` (boolean) and a settable `JwtPayloadRepository` for JWT payload persistence.

## 8. Core Usage / API

Mini Program login flow with `WxMaAuthenticatingFilter` and `WxMaLoginRequest`:

```java
import org.apache.shiro.spring.boot.weixin.authc.WxMaAuthenticatingFilter;
import org.apache.shiro.spring.boot.weixin.authc.WxMaLoginRequest;
import org.apache.shiro.spring.boot.weixin.token.WxMaAuthenticationToken;

// The filter reads the jscode from the request and wraps it into a token
WxMaLoginRequest loginRequest = new WxMaLoginRequest();
loginRequest.setJscode("wx-jscode-from-client");
loginRequest.setUnionid("o-unionid");

WxMaAuthenticationToken token = new WxMaAuthenticationToken(loginRequest, "127.0.0.1");
// subject.login(token) -> WxMaAuthorizingRealm resolves the user
```

Exception handling is typed so the application can distinguish login failures:

```java
import org.apache.shiro.spring.boot.weixin.exception.WxJsCodeNotFoundException;
import org.apache.shiro.spring.boot.weixin.exception.WxJsCodeExpiredException;
```

## 9. Testing & Build

Build and run tests:

```bash
./mvnw clean verify
```

- The build is configured with the JaCoCo Maven plugin: a coverage report is generated at `target/site/jacoco/index.html` and a rule checks the bundle line coverage against a 90% minimum (`haltOnFailure=false`, so the check reports but does not fail the build).
- The `central` Maven profile (`./mvnw -Pcentral deploy`) attaches GPG signatures, sources and Javadoc jars for publishing.
- The repository currently ships no unit tests for this module; coverage is tracked via the JaCoCo report.

## 10. Versioning & Branches

Three parallel version lines are maintained:

| Branch | JDK | Version pattern |
|:---|:---|:---|
| `feature/1.0.x` | 8 | `1.0.x.*` |
| `feature/2.0.x` | 17 | `2.0.x.*` |
| `feature/3.0.x` | 21 | `3.0.x.*` |

Maintenance policy: the `1.0.x` line is the actively developed line (current snapshot `1.0.x.20260630-SNAPSHOT`); `2.0.x` and `3.0.x` are forward porting lines targeting newer JDKs. Snapshots are built on demand; tagged releases are distributed via GitHub Releases.

## 11. Contributing & License

- Fork the repository and open a pull request; keep the `1.0.x` line compatible with JDK 8.
- Bug reports and feature requests are tracked via GitHub Issues.
- Licensed under the [Apache License, Version 2.0](https://www.apache.org/licenses/LICENSE-2.0).
