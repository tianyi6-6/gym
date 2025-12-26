# 健身俱乐部管理系统

## 项目简介
这是一个基于前后端分离架构的私人健身房管理系统，支持管理员、教练、用户三种角色。

## 技术栈
- **后端**: Spring Boot 2 + MyBatis
- **前端**: Vue2 + ElementUI + Axios
- **数据库**: MySQL 5.7/8.0

## 环境要求
- JDK 1.8
- MySQL 5.7 或 8.0
- Node.js 16+
- Maven 3.8+

## 项目结构
```
gym/
├── backend/          # 后端Spring Boot项目
├── frontend/         # 前端Vue项目
└── database/         # 数据库脚本
```

## 功能模块

### 管理员功能
- 登录、个人信息、修改密码
- 课程管理（关联教练）
- 课程订单管理
- 系统公告管理
- 教练预约管理
- 健身器材管理
- 器材预约管理
- 健身菜谱管理（富文本）
- 健身经验管理
- 数据统计（ECharts图表）
- 管理员/教练/学员信息管理

### 教练功能
- 登录、个人信息、修改密码
- 我的预约（查看用户预约）
- 我的经验（发布健身经验）
- 查看平台信息（教练、课程、器材、菜谱、经验）

### 用户功能
- 登录、个人信息、修改密码
- 用户充值（模拟充值）
- 我的预约（预约教练）
- 我的经验（发布健身经验）
- 购买健身课程
- 查看平台信息

## 快速开始

### 1. 数据库配置
1. 创建MySQL数据库（MySQL 5.7或8.0）
2. 执行 `database/gym.sql` 创建数据库和表
3. 修改 `backend/src/main/resources/application.yml` 中的数据库连接信息

### 2. 后端启动
```bash
cd backend
mvn clean install
mvn spring-boot:run
```
后端服务将在 `http://localhost:8080` 启动

### 3. 前端启动
```bash
cd frontend
npm install
npm run serve
```
前端服务将在 `http://localhost:8081` 启动

### 4. 密码说明
数据库中的密码使用明文存储，默认密码都是 `123456`

**注意**：此项目为开发/测试版本，使用明文密码。生产环境请使用密码加密。

## 默认账号
- **管理员**：admin / 123456
- **教练**：zhang / 123456
- **用户**：zhangsan / 123456

## 项目结构说明

### 后端结构
```
backend/
├── src/main/java/com/gym/
│   ├── entity/          # 实体类
│   ├── mapper/          # MyBatis Mapper接口
│   ├── service/         # 业务逻辑层
│   ├── controller/      # 控制器层
│   ├── config/          # 配置类
│   ├── common/          # 通用类（Result、JwtUtil等）
│   └── util/            # 工具类
└── src/main/resources/
    ├── mapper/           # MyBatis XML映射文件
    └── application.yml # 配置文件
```

### 前端结构
```
frontend/
├── src/
│   ├── api/             # API接口定义
│   ├── assets/           # 静态资源
│   ├── components/       # 组件
│   ├── layouts/          # 布局组件
│   ├── router/           # 路由配置
│   ├── store/            # Vuex状态管理
│   └── views/            # 页面组件
│       ├── admin/        # 管理员页面
│       ├── coach/        # 教练页面
│       └── user/         # 用户页面
```

## 主要功能

### 管理员功能
- ✅ 课程管理（增删改查，关联教练）
- ✅ 订单管理（查看所有订单）
- ✅ 教练管理（增删改查）
- ✅ 用户管理（增删改查）
- ✅ 器材管理（增删改查）
- ✅ 菜谱管理（支持富文本）
- ✅ 经验管理（管理用户和教练发布的经验）
- ✅ 公告管理（系统公告）
- ✅ 数据统计（ECharts图表展示）

### 教练功能
- ✅ 查看我的预约
- ✅ 发布/管理健身经验
- ✅ 查看平台信息

### 用户功能
- ✅ 浏览和购买课程
- ✅ 账户充值（模拟充值）
- ✅ 预约教练
- ✅ 发布/管理健身经验
- ✅ 查看平台信息（课程、器材、菜谱、经验等）

## 技术特点
- 前后端分离架构
- JWT Token认证
- RESTful API设计
- 多角色权限控制
- ECharts数据可视化
- ElementUI现代化UI
- MyBatis数据库操作

## 注意事项
1. 确保MySQL数据库已启动
2. 确保JDK版本为1.8
3. 确保Node.js版本为16+
4. 首次运行需要执行数据库脚本
5. 后端和前端需要同时启动才能正常使用

