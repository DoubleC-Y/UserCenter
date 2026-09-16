# Fount

基于 [Ant Design Pro v6](https://github.com/ant-design/ant-design-pro) 的**精简版**脚手架：保留 Pro 的布局、权限、请求、mock、ProTable 等核心能力，去掉了示例页、国际化、图表与仓库级工具链。

- 上游版本：`v6.0.3`（`master`，commit `adfd440`）
- 技术栈：`@umijs/max` 4.x + `antd` 6.x + `@ant-design/pro-components` 3.x + React 19 + TypeScript

## 快速开始

```bash
npm install        # 需要 Node >= 22
npm run start      # http://localhost:8000 ，接口走 mock
```

演示账号：

| 账号 | 密码 | 权限 |
| --- | --- | --- |
| `admin` | `ant.design` | 可访问 `/admin`（`access: canAdmin`） |
| `user` | `ant.design` | 普通用户，访问 `/admin` 显示 403 |

## 常用命令

| 命令 | 说明 |
| --- | --- |
| `npm run start` | 本地开发，启用 mock |
| `npm run start:no-mock` | 本地开发，关闭 mock（配合 proxy 联调） |
| `npm run build` | 构建产物到 `dist` |
| `npm run preview` | 预览构建产物 |
| `npm run tsc` | TypeScript 类型检查（依赖 `src/.umi` 生成类型，若刚清过缓存先跑 `max setup`） |
| `npm run biome` | 代码格式化 + lint 自动修复 |
| `npm run openapi` | 按 `config/oneapi.json` 生成 service 与类型 |

## 目录结构

```text
config/
  config.ts          # umi 配置入口（antd/layout/request/access/mock/proxy）
  routes.ts          # 路由表（精简后：登录 / 欢迎 / 管理页 / 查询表格 / 404）
  defaultSettings.ts # ProLayout 外观设置
  proxy.ts           # 分环境代理
mock/                # 本地接口 mock（dev 生效）
src/
  app.tsx            # 运行时配置：getInitialState / layout / request
  access.ts          # 权限定义（canAdmin、canUser）
  requestErrorConfig.ts # 统一错误处理
  components/        # Footer / RightContent(AvatarDropdown、DocLink) / HeaderDropdown / ErrorBoundary / OfflineBanner
  pages/
    user/login/      # 登录页（LoginForm）
    Welcome.tsx      # 欢迎页
    Admin.tsx        # 仅 admin 可见的二级页
    table-list/      # ProTable 增删改查示例
    exception/404/   # 404 页（403 由 ProLayout 内置渲染）
  services/          # 接口定义（可用 npm run openapi 生成）
```

## 与完整版的差异

已删除：`dashboard`、`form`、`list`、`profile`、`result`、`account`、`chatbot`、`register` 等示例页；`src/locales` 及 `useIntl`/`FormattedMessage` 调用（文案改为中文直写）；`@ant-design/plots`、`d3`、`topojson-client`、`@ant-design/x*`、`highlight.js`、`china-division` 等依赖；`tailwindcss`；测试体系（`vitest`、`tests`、`*.test.tsx`）；提交钩子（`husky`、`commitlint`、`lint-staged`）；仓库级文件（`.github`、`docs`、`cloudflare-worker`、CI/覆盖率/诊断配置、上游 README）。

二次清理（无引用的死代码）：`src/global.tsx`、`src/global.style.ts`、`src/utils/format.ts`、未被路由引用的 `exception/403`、`exception/500` 页；`public/CNAME`、`public/pro_icon.svg`、`public/icons/`；`config.ts` 中的 `@root` 别名、`commitHash` 计算与 `define` 版本号注入（配套的 `__APP_VERSION__` 等声明一并移除）；`request-record` 插件、markdown loader 规则与 `mock` 精细化配置。

已调整：`package.json` 名称改为 `fount`，浏览器标题与布局标题改为 `Fount`；关闭模板自带的 Google Analytics；`request.baseURL` 不再指向上游演示接口；页脚改为简洁版权行，不再展示上游仓库与框架版本。

保留：ProLayout 混排布局 + 主题抽屉、登录/退出、路由与按钮级权限、统一请求与错误处理、mock、ProTable 示例、OpenAPI 代码生成、biome（lint/format）。

## 接真实后端

1. 在 `config/proxy.ts` 中配置 `dev` 代理，或把 `src/app.tsx` 里的 `baseURL` 指向你的服务；
2. 用 `npm run start:no-mock` 启动（或删掉 `mock` 目录）；
3. 接口类型与请求函数可用 `npm run openapi` 从 OpenAPI 文档生成。

## 起始仓库说明

当前 `.git` 指向上游（浅克隆，depth 1）。作为自己的项目起点时：

```bash
Remove-Item -Recurse -Force .git   # 或 rm -rf .git
git init && git add -A && git commit -m "chore: init fount"
```

如需恢复被精简掉的示例页与国际化（上游全量代码在 HEAD 中）：`git checkout -- .`。
